package com.capstone.trashcan.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    init {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Add text change listener to validate password length
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

        hint = "Masukkan password Anda"
        typeface = Typeface.DEFAULT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        typeface = Typeface.DEFAULT
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}