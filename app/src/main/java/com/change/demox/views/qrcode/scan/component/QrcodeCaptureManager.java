package com.change.demox.views.qrcode.scan.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Window;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.change.demox.views.webview.webcache.component.utils.LogUtils;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.InactivityTimer;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QrcodeCaptureManager extends CaptureManager {

    public static final String isFromStampPage = "stamp";
    public static final String WHICHPAGEFROM = "WHICHPAGEFROM";

    private static final String TAG = QrcodeCaptureManager.class.getSimpleName();
    private static int cameraPermissionReqCode = 250;
    private Activity activity;
    private DecoratedBarcodeView barcodeView;
    private int orientationLock = -1;
    private static final String SAVED_ORIENTATION_LOCK = "SAVED_ORIENTATION_LOCK";
    private boolean returnBarcodeImagePath = false;
    private boolean destroyed = false;
    private static final long DELAY_BEEP = 150L;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private Handler handler;
    private String mwhichPage = null;
    private BarcodeCallback callback = new BarcodeCallback() {
        public void barcodeResult(final BarcodeResult result) {
            if (mwhichPage != null && TextUtils.equals(mwhichPage, isFromStampPage)) {
                if (!result.getResult().getText().contains("stamp")) {
                    return;
                }
            }
            QrcodeCaptureManager.this.barcodeView.pause();
            QrcodeCaptureManager.this.beepManager.playBeepSoundAndVibrate();
            QrcodeCaptureManager.this.handler.postDelayed(new Runnable() {
                public void run() {
                    QrcodeCaptureManager.this.returnResult(result);
                }
            }, 150L);
        }

        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };
    private final CameraPreview.StateListener stateListener = new CameraPreview.StateListener() {
        public void previewSized() {
        }

        public void previewStarted() {
        }

        public void previewStopped() {
        }

        public void cameraError(Exception error) {
            QrcodeCaptureManager.this.displayFrameworkBugMessageAndExit();
        }
    };
    private boolean askedPermission = false;


    public QrcodeCaptureManager(Activity activity, DecoratedBarcodeView barcodeView) {
        super(activity, barcodeView);
        this.activity = activity;
        this.barcodeView = barcodeView;
        barcodeView.getBarcodeView().addStateListener(this.stateListener);
        this.handler = new Handler();
        this.inactivityTimer = new InactivityTimer(activity, new Runnable() {
            public void run() {
                Log.d(QrcodeCaptureManager.TAG, "Finishing due to inactivity");
                QrcodeCaptureManager.this.finish();
            }
        });
        this.beepManager = new BeepManager(activity);
    }

    public void initializeFromIntent(Intent intent, Bundle savedInstanceState) {
        Window window = this.activity.getWindow();
        window.addFlags(128);
        if (savedInstanceState != null) {
            this.orientationLock = savedInstanceState.getInt("SAVED_ORIENTATION_LOCK", -1);
        }

        if (intent != null) {
            if (this.orientationLock == -1) {
                boolean orientationLocked = intent.getBooleanExtra("SCAN_ORIENTATION_LOCKED", true);
                if (orientationLocked) {
                    this.lockOrientation();
                }
            }

            if ("com.google.zxing.client.android.SCAN".equals(intent.getAction())) {
                this.barcodeView.initializeFromIntent(intent);
            }

            if (!intent.getBooleanExtra("BEEP_ENABLED", true)) {
                this.beepManager.setBeepEnabled(false);
                this.beepManager.updatePrefs();
            }

            if (intent.hasExtra("TIMEOUT")) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        QrcodeCaptureManager.this.returnResultTimeout();
                    }
                };
                this.handler.postDelayed(runnable, intent.getLongExtra("TIMEOUT", 0L));
            }

            if (intent.getBooleanExtra("BARCODE_IMAGE_ENABLED", false)) {
                this.returnBarcodeImagePath = true;
            }

            if (intent.getStringExtra(WHICHPAGEFROM) != null) {
                mwhichPage = intent.getStringExtra(WHICHPAGEFROM);
            }

        }

    }

    @SuppressLint("WrongConstant")
    protected void lockOrientation() {
        if (this.orientationLock == -1) {
            Display display = this.activity.getWindowManager().getDefaultDisplay();
            int rotation = display.getRotation();
            int baseOrientation = this.activity.getResources().getConfiguration().orientation;
            int orientation = 0;
            if (baseOrientation == 2) {
                if (rotation != 0 && rotation != 1) {
                    orientation = 8;
                } else {
                    orientation = 0;
                }
            } else if (baseOrientation == 1) {
                if (rotation != 0 && rotation != 3) {
                    orientation = 9;
                } else {
                    orientation = 1;
                }
            }

            this.orientationLock = orientation;
        }

        this.activity.setRequestedOrientation(this.orientationLock);
    }

    public void decode() {
        this.barcodeView.decodeSingle(this.callback);
    }

    public void onResume() {
        if (Build.VERSION.SDK_INT >= 23) {
            this.openCameraWithPermission();
        } else {
            this.barcodeView.resume();
        }

        this.beepManager.updatePrefs();
        this.inactivityTimer.start();
    }

    @TargetApi(23)
    private void openCameraWithPermission() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.CAMERA") == 0) {
            this.barcodeView.resume();
        } else if (!this.askedPermission) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.CAMERA"}, cameraPermissionReqCode);
            this.askedPermission = true;
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == cameraPermissionReqCode) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                this.barcodeView.resume();
            } else {
                this.displayFrameworkBugMessageAndExit();
            }
        }

    }

    public void onPause() {
        this.barcodeView.pause();
        this.inactivityTimer.cancel();
        this.beepManager.close();
    }

    public void onDestroy() {
        this.destroyed = true;
        this.inactivityTimer.cancel();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("SAVED_ORIENTATION_LOCK", this.orientationLock);
    }

    @SuppressLint("WrongConstant")
    public static Intent resultIntent(BarcodeResult rawResult, String barcodeImagePath) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.addFlags(524288);
        intent.putExtra("SCAN_RESULT", rawResult.toString());
        intent.putExtra("SCAN_RESULT_FORMAT", rawResult.getBarcodeFormat().toString());
        byte[] rawBytes = rawResult.getRawBytes();
        if (rawBytes != null && rawBytes.length > 0) {
            intent.putExtra("SCAN_RESULT_BYTES", rawBytes);
        }

        Map<ResultMetadataType, ?> metadata = rawResult.getResultMetadata();
        if (metadata != null) {
            if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
                intent.putExtra("SCAN_RESULT_UPC_EAN_EXTENSION", metadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
            }

            Number orientation = (Number) metadata.get(ResultMetadataType.ORIENTATION);
            if (orientation != null) {
                intent.putExtra("SCAN_RESULT_ORIENTATION", orientation.intValue());
            }

            String ecLevel = (String) metadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
            if (ecLevel != null) {
                intent.putExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL", ecLevel);
            }

            Iterable<byte[]> byteSegments = (Iterable) metadata.get(ResultMetadataType.BYTE_SEGMENTS);
            if (byteSegments != null) {
                int i = 0;

                for (Iterator var9 = byteSegments.iterator(); var9.hasNext(); ++i) {
                    byte[] byteSegment = (byte[]) var9.next();
                    intent.putExtra("SCAN_RESULT_BYTE_SEGMENTS_" + i, byteSegment);
                }
            }
        }

        if (barcodeImagePath != null) {
            intent.putExtra("SCAN_RESULT_IMAGE_PATH", barcodeImagePath);
        }

        return intent;
    }

    private String getBarcodeImagePath(BarcodeResult rawResult) {
        String barcodeImagePath = null;
        if (this.returnBarcodeImagePath) {
            Bitmap bmp = rawResult.getBitmap();

            try {
                File bitmapFile = File.createTempFile("barcodeimage", ".jpg", this.activity.getCacheDir());
                FileOutputStream outputStream = new FileOutputStream(bitmapFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                barcodeImagePath = bitmapFile.getAbsolutePath();
            } catch (IOException var6) {
                Log.w(TAG, "Unable to create temporary file and store bitmap! " + var6);
            }
        }

        return barcodeImagePath;
    }

    private void finish() {
        this.activity.finish();
    }

    protected void returnResultTimeout() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("TIMEOUT", true);
        this.activity.setResult(0, intent);
        this.finish();
    }

    protected void returnResult(BarcodeResult rawResult) {
//        if (mwhichPage != null && TextUtils.equals(mwhichPage,isFromStampPage)){
//            if (rawResult.getResult().getText().contains("stamp")){
//                Intent intent = resultIntent(rawResult, this.getBarcodeImagePath(rawResult));
//                this.activity.setResult(-1, intent);
//                this.finish();
//            } else {
//                this.barcodeView.resume();
//            }
//        } else {
//            Intent intent = resultIntent(rawResult, this.getBarcodeImagePath(rawResult));
//            this.activity.setResult(-1, intent);
//            this.finish();
//        }
        LogUtils.d("扫描结果：" + rawResult.getText());
    }

    protected void displayFrameworkBugMessageAndExit() {
        if (!this.activity.isFinishing() && !this.destroyed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle(this.activity.getString(com.google.zxing.client.android.R.string.zxing_app_name));
            builder.setMessage(this.activity.getString(com.google.zxing.client.android.R.string.zxing_msg_camera_framework_bug));
            builder.setPositiveButton(com.google.zxing.client.android.R.string.zxing_button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    QrcodeCaptureManager.this.finish();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    QrcodeCaptureManager.this.finish();
                }
            });
            builder.show();
        }
    }

    public static int getCameraPermissionReqCode() {
        return cameraPermissionReqCode;
    }

    public static void setCameraPermissionReqCode(int cameraPermissionReqCode) {
        cameraPermissionReqCode = cameraPermissionReqCode;
    }
}
