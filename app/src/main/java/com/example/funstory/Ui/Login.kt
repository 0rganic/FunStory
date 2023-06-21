package com.example.funstory.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.funstory.Ui.viewModel.LoginViewModel
import com.example.funstory.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.visibility = View.INVISIBLE

        loginPageActivity()
        registerPage()

    }

    private fun registerPage() {
        binding.signUp.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun loginPageActivity(){
            binding.btnLogin.setOnClickListener {
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()
                when {
                    email.isEmpty() -> binding.edtEmail.error = "Email can't be empty"
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> binding.edtEmail.error = "Invalid Email"
                    password.isEmpty() -> binding.edtPassword.error = "Password can't be empty "
                    else -> {
                        binding.loading.visibility = View.VISIBLE
                        login(email,password)
                    }
                }
            }
        }



    private fun login(email: String, password: String) {
        lifecycleScope.launch {
            try {
                loginViewModel.login(email, password).collect { result ->
                    if (result.isSuccess) {
                        Toast.makeText( this@Login, "Login Success",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, MainActivity::class.java )
                        startActivity(intent)
                    } else {
                        val errorMessage = result.exceptionOrNull()?.message ?: "Unknown Error"
                        Toast.makeText(this@Login, "Login Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (exception: Exception) {
                Toast.makeText(this@Login, "Login Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.loading.visibility = View.INVISIBLE
            }
        }
    }
}