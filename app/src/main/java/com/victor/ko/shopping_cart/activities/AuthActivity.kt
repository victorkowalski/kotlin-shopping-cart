package com.victor.ko.shopping_cart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.victor.ko.shopping_cart.BuildConfig
import com.victor.ko.shopping_cart.R
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
/*
        if (checkToken()) {
            this.goToMainActivity()
            return
        }
*/
        if (BuildConfig.DEBUG) {
            continue_button.isEnabled = true
            phoneTextEdit.setText(R.string.test_phone_number)
        }
    }
}