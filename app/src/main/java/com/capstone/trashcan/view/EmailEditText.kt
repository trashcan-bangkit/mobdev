package com.capstone.trashcan.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    companion object {
        private val EMAIL_PATTERN: Pattern = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        )
    }

    var isValid: Boolean = false
        private set

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isValid = EMAIL_PATTERN.matcher(s).matches()
                if (!isValid) {
                    setError("Format email salah", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        hint = "Masukkan email Anda"
        typeface = Typeface.DEFAULT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        typeface = Typeface.DEFAULT
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}