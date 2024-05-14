package com.myapp.application.newsreader.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class CommonUtils {
    companion object {
        fun Context.showToastMessage(message: String, duration: Int = Toast.LENGTH_LONG) {
            Toast.makeText(this, message, duration).show()
        }

        fun View.showSnackBar(messageResId: Int, duration: Int = Snackbar.LENGTH_LONG) {
            Snackbar.make(this, messageResId, duration).show()
        }
    }
}