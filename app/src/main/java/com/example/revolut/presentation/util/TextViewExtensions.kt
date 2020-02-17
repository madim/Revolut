package com.example.revolut.presentation.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

internal inline fun TextView.afterTextChanged(crossinline body: (text: CharSequence) -> Unit): TextWatcher {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable?) = body(s.toString())
    }
    addTextChangedListener(watcher)
    return watcher
}
