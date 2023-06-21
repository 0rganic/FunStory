package com.example.funstory.Ui

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class PasswordInput : AppCompatEditText {

    private val MIN_PASSWORD_LENGTH = 8
    private val ERROR_COLOR = Color.RED

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize()
    }

    private fun initialize() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val passwordLength = s?.length ?: 0
                if (passwordLength < MIN_PASSWORD_LENGTH) {
                    showError()
                } else {
                    hideError()
                }
            }
        })
    }

    private fun showError() {
        // Set the error message or display error indication as per your UI design
        error = "Password must be at least 8 characters long"
        setTextColor(ERROR_COLOR)
    }

    private fun hideError() {
        error = null
        val textColor = android.R.color.black
        setTextColor(ContextCompat.getColor(context, textColor))
    }

}