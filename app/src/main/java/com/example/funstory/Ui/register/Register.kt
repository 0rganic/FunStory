package com.example.funstory.Ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.funstory.Ui.login.Login
import com.example.funstory.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerActivityPage()
        loginPage()
    }

    private fun registerActivityPage() {
        binding.btnSignup.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            when{
                name.isEmpty() -> binding.edRegisterName.error = "Name can't be empty"
                email.isEmpty() -> binding.edRegisterEmail.error = "Email can't be empty"
                password.isEmpty() -> binding.edRegisterPassword.error = "Password can't be empty"
                else -> {
                    binding.loading.visibility = View.VISIBLE
                    register(name, email, password)
                }
            }
        }
    }


    private fun register(name : String, email: String, password: String) {
        lifecycleScope.launch{
            try {
                registerViewModel.register(name, email, password).collect{result ->
                    if (result.isSuccess) {
                        Toast.makeText(this@Register, "Register Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Register, Login::class.java)
                        startActivity(intent)
                    } else {
                        val errorMessage = result.exceptionOrNull()?.message ?: "Unknown Error"
                        Toast.makeText(this@Register, "Register Failed = $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (exception: Exception ) {
                Toast.makeText(this@Register, "Register Failed = ${exception.message}", Toast.LENGTH_SHORT).show()
            }
            finally {
                binding.loading.visibility = View.INVISIBLE
            }
        }
    }

    private fun loginPage() {
        binding.signIn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}