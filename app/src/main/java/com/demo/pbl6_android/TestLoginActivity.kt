package com.demo.pbl6_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.pbl6_android.databinding.ActivityTestLoginBinding

/**
 * Activity tạm thời để test màn hình Login
 * Xóa file này sau khi đã tích hợp authentication flow hoàn chỉnh
 */
class TestLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
    }

    private fun setupViews() {
        binding.btnTestLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        
        binding.btnGoToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}

