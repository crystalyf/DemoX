package com.change.demox

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.pdf.PdfShowActivity
import com.change.demox.themecolor.waytwo.DynamicThemeColorActivity
import com.change.demox.ucrop.UcropActivity
import com.change.demox.views.ViewActivity
import com.change.demox.views.tutorial.TutorialActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        adaptFirebaseDynamicLinks()
        initClickListener()
    }

    private fun initClickListener() {
        btn_dynamic_theme.setOnClickListener(this)
        btn_lecenses.setOnClickListener(this)
        btn_ucrop.setOnClickListener(this)
        btn_views.setOnClickListener(this)
        btn_pdf_online.setOnClickListener(this)
        btn_tutorial.setOnClickListener(this)
    }

    /**
     * FirebaseDynamicLinksのリスニング
     *
     */
    private fun adaptFirebaseDynamicLinks() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(this.intent!!)
                .addOnSuccessListener(this@MainActivity) { pendingDynamicLinkData ->
                    getInvitationKey(pendingDynamicLinkData)
                }
                .addOnFailureListener(this) { e ->
                    Timber.e(e, "FirebaseDynamicLinks.getDynamicLink failed.")
                }
    }

    /**
     * 从dynamicLink中的deepLink里拿到邀请key
     *
     * @param pendingDynamicLinkData DeepLink Data
     */
    fun getInvitationKey(pendingDynamicLinkData: PendingDynamicLinkData?) {
        var deepLink: Uri? = null
        if (pendingDynamicLinkData != null) {
            deepLink = pendingDynamicLinkData.link
        }
        if (deepLink != null) {
            val invitationKeyValue = deepLink.getQueryParameter("invitationKey")
            if (!TextUtils.isEmpty(invitationKeyValue)) {
                // 取得邀请key的值
                Toast.makeText(this, invitationKeyValue.toString(), Toast.LENGTH_LONG).show()
            } else {

                Timber.w("Invite group, cannot get invitation key from DeepLink = $deepLink")
            }

        } else {
            //リンクが見つかりません
            Timber.w("Invite group, DeepLink is null")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_dynamic_theme -> {
                val intent = Intent(this, DynamicThemeColorActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_lecenses -> {
                val intent2 = Intent(this, OssLicensesMenuActivity::class.java)
                startActivity(intent2)
            }
            R.id.btn_ucrop -> {
                val intent3 = Intent(this, UcropActivity::class.java)
                startActivity(intent3)
            }
            R.id.btn_views -> {
                val intent3 = Intent(this, ViewActivity::class.java)
                startActivity(intent3)
            }
            R.id.btn_pdf_online -> {
                val intent = Intent(this, PdfShowActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_tutorial -> {
                val intent = Intent(this, TutorialActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }
}