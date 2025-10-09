package com.demo.pbl6_android.utils

import android.content.Context
import android.content.Intent
import com.demo.pbl6_android.LoginActivity

object NavigationHelper {
    
    fun navigateToLogin(context: Context, clearStack: Boolean = false) {
        val intent = Intent(context, LoginActivity::class.java)
        if (clearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}

