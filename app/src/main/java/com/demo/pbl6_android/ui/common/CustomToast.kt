package com.demo.pbl6_android.ui.common

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.demo.pbl6_android.R

object CustomToast {
    
    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.custom_toast, null)
        
        val textView: TextView = layout.findViewById(R.id.tv_toast_message)
        textView.text = message
        
        val toast = Toast(context)
        toast.duration = duration
        toast.view = layout
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}

