package com.victor.ko.scart.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.victor.ko.scart.app.SCartApplication

fun toast(text: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(SCartApplication.context, text, length).show()
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

/*
fun EditText.setErrorId(@IdRes errorId: Int?) {
    this.error = if (errorId == null) null else context.getString(errorId)
}*/