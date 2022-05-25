package com.osenov.notes.utils

import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment


fun Fragment.closeKeyBoard() {
    requireActivity().currentFocus?.let { view ->
        val imm = getSystemService(view.context, InputMethodManager::class.java) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}