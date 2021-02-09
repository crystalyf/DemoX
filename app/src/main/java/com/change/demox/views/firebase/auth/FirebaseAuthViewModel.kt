package com.change.demox.views.firebase.auth

import android.app.Activity
import android.util.Log
import com.change.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 *
 * "apple.com":  苹果认证登陆
 * "twitter.com" :  Twitter认证登陆
 *
 */

class FirebaseAuthViewModel : BaseViewModel() {
    // Initialize Firebase Auth
    var auth: FirebaseAuth = Firebase.auth
    private val TAG = "Login"
    private var authProvider: OAuthProvider? = null

    /**
     * Firebase Twiter 认证
     *
     * @param context
     */
    fun doTwitterAuthentication(context: Activity) {
        authProvider = OAuthProvider.newBuilder("twitter.com").build()
        auth.startActivityForSignInWithProvider(context, authProvider!!)
                .addOnSuccessListener { authResult ->
                    var token: String? = ""
                    if (authResult.user != null) {
                        // User is signed in
                        val task = authResult.user?.getIdToken(false)
                        if (task?.isSuccessful!!) {
                            token = task.result?.token
                        }
                    }
                    Log.d(TAG, "activitySignIn:onSuccess:${authResult.user}")
                    //authResult: zza,zzc,zzc
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "activitySignIn:onFailure", e)
                }
    }

    /**
     * Firebase Apple 认证
     *
     * @param context
     */
    fun doAppleAuthentication(context: Activity) {
        authProvider = OAuthProvider.newBuilder("apple.com").build()
        auth.startActivityForSignInWithProvider(context, authProvider!!)
                .addOnSuccessListener { authResult ->
                    Log.d(TAG, "activitySignIn:onSuccess:${authResult.user}")
                    //authResult: zza,zzc,zzc
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "activitySignIn:onFailure", e)
                }
    }

    /**
     * 更新トークン
     *
     */
    fun refreshTwitterAuthentication() {
        var token: String? = ""
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            val task = user.getIdToken(true)
            token = if (task.isSuccessful) {
                task.result?.token
            } else {
                "token_refresh_failed"
            }
            Log.w("token:", token.toString())
        }
    }

}