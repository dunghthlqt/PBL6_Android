package com.demo.pbl6_android

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.LoginRequest
import com.demo.pbl6_android.data.auth.AuthManager
import com.demo.pbl6_android.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authManager: AuthManager
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        authManager = AuthManager.getInstance(this)
        
        setupViews()
        setupClickListeners()
    }

    private fun setupViews() {
        // Set initial password visibility
        binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun setupClickListeners() {
        binding.apply {
            // Back button
            btnBack.setOnClickListener {
                finish()
            }
            
            // Toggle password visibility
            ivPasswordToggle.setOnClickListener {
                togglePasswordVisibility()
            }
            
            // Login button
            btnLogin.setOnClickListener {
                handleLogin()
            }
            
            // Forgot password
            tvForgotPassword.setOnClickListener {
                showToast("Chức năng quên mật khẩu đang phát triển")
            }
            
            // Google login
            btnGoogleLogin.setOnClickListener {
                showToast("Đăng nhập với Google đang phát triển")
            }
            
            // Facebook login
            btnFacebookLogin.setOnClickListener {
                showToast("Đăng nhập với Facebook đang phát triển")
            }
            
            // Register link
            tvRegisterLink.setOnClickListener {
                showToast("Chức năng đăng ký đang phát triển")
            }
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        
        binding.apply {
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivPasswordToggle.setImageResource(R.drawable.ic_eye_open)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivPasswordToggle.setImageResource(R.drawable.ic_eye_closed)
            }
            
            // Move cursor to end
            etPassword.setSelection(etPassword.text?.length ?: 0)
        }
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        
        // Validate inputs
        if (email.isEmpty()) {
            binding.etEmail.error = "Vui lòng nhập email"
            binding.etEmail.requestFocus()
            return
        }
        
        if (!isValidEmail(email)) {
            binding.etEmail.error = "Email không hợp lệ"
            binding.etEmail.requestFocus()
            return
        }
        
        if (password.isEmpty()) {
            binding.etPassword.error = "Vui lòng nhập mật khẩu"
            binding.etPassword.requestFocus()
            return
        }
        
        if (password.length < 6) {
            binding.etPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            binding.etPassword.requestFocus()
            return
        }
        
        // Call login API
        callLoginApi(email, password)
    }
    
    private fun callLoginApi(email: String, password: String) {
        showLoading(true)
        
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(email = email, password = password)
                val response = RetrofitClient.apiService.login(loginRequest)
                
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    
                    if (loginResponse?.success == true && loginResponse.data != null) {
                        // Save auth data
                        val data = loginResponse.data
                        authManager.saveAuthData(
                            token = data.token,
                            refreshToken = data.refreshToken,
                            userId = data.id,
                            username = data.username,
                            roles = data.roles
                        )
                        
                        showLoading(false)
                        showToast(data.message)
                        navigateToMain()
                    } else {
                        showLoading(false)
                        val errorMessage = loginResponse?.error ?: "Đăng nhập thất bại"
                        showToast(errorMessage)
                    }
                } else {
                    showLoading(false)
                    val errorMessage = when (response.code()) {
                        401 -> "Email hoặc mật khẩu không đúng"
                        404 -> "Không tìm thấy tài khoản"
                        500 -> "Lỗi server, vui lòng thử lại sau"
                        else -> "Đăng nhập thất bại (${response.code()})"
                    }
                    showToast(errorMessage)
                }
            } catch (e: Exception) {
                showLoading(false)
                val errorMessage = when {
                    e is java.net.UnknownHostException -> "Không có kết nối internet"
                    e is java.net.SocketTimeoutException -> "Timeout - Server phản hồi quá lâu"
                    else -> "Lỗi: ${e.message}"
                }
                showToast(errorMessage)
                e.printStackTrace()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                btnLogin.isEnabled = false
                btnLogin.alpha = 0.5f
            } else {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true
                btnLogin.alpha = 1f
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

