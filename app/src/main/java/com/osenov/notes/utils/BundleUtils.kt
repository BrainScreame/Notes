package com.osenov.notes.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.osenov.notes.data.model.Note
import kotlin.properties.ReadOnlyProperty

fun noteArgs(key: String): ReadOnlyProperty<Fragment, Note?> {
    return ReadOnlyProperty { thisRef, _ ->
        val args : Bundle? = thisRef.arguments
        args?.getParcelable(key)
    }
}