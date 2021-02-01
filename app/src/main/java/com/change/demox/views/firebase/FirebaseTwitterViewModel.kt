package com.change.demox.views.firebase

import android.app.Activity
import android.util.Log
import com.change.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FirebaseTwitterViewModel : BaseViewModel() {
    // Initialize Firebase Auth
    var auth: FirebaseAuth = Firebase.auth
    private val TAG = "Login"
    private val twitterAuthProvider = OAuthProvider.newBuilder("twitter.com")
        .build()

    fun doTwitterAuthentication(context: Activity) {
        auth.startActivityForSignInWithProvider(
            context,
            twitterAuthProvider
        )
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
            val task =user.getIdToken(true)
            token = if (task.isSuccessful) {
                task.result?.token
            } else {
                "token_refresh_failed"
            }
            Log.w("token:", token.toString())
        }
    }

}