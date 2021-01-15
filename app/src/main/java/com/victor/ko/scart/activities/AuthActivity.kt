package com.victor.ko.scart.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.victor.ko.shopping_cart.BuildConfig
import com.victor.ko.shopping_cart.R
import com.victor.ko.scart.network.ApiAdapter
import com.victor.ko.shopping_cart.databinding.ActivityAuthBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var bnd: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(bnd.root)

/*
        if (checkToken()) {
            this.goToMainActivity()
            return
        }
*/
        if (BuildConfig.DEBUG) {
            bnd.continueButton.isEnabled = true
            bnd.phoneTextEdit.setText(R.string.test_phone_number)
        }

        bnd.continueButton.setOnClickListener { requirePin() }
    }

    private fun requirePin() {
        bnd.continueButton.visibility = View.GONE
        bnd.sendAgainButton.isEnabled = false
        countDownTimer.cancel()
        countDownTimer.start()

//***************
        CoroutineScope(Dispatchers.Main).launch() {
            // Try catch block to handle exceptions when calling the API.
            try {
                val response = ApiAdapter.apiClient.getPin("${bnd.phoneTextEdit.text} test", "1")
                // Check if response was successful
                if (response.isSuccessful && response.body() != null) {
                    // Retrieve data.
                    val data = response.body()!!
                    print(data)
                    /*data.message?.let {
                        // Load URL into the ImageView using Coil.
                        //iv_dog_image.load(it)
                    }*/

                    when {
                        data.success != null -> {
                            //Paper.book().write("authToken", pin.success.token)
                            bnd.sendCodeForm?.visibility = View.VISIBLE
                            if (BuildConfig.DEBUG) {
                                bnd.pinTextEdit.setText(data.success.pin)
                            }
                        }
                        data.error != null -> {
                            bnd.pinErrorLabel.text = data.error.message
                        }
                    }


                } else {
                    // Show API error.
                    // This is when the server responded with an error.
                    /*Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG).show()*/
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Show API error. This is the error raised by the client.
                // The API probably wasn't called in this case, so better check before assuming.

                /*Toast.makeText(this@MainActivity,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()*/
            }
        }
        //**********************
    }

    private val countDownTimer = object : CountDownTimer(10000, 1000) {
        override fun onFinish() {
            runOnUiThread {
                bnd.sendAgainButton.isEnabled = true
                bnd.resendVia.text = ""
            }
        }

        override fun onTick(millisUntilFinished: Long) {
            runOnUiThread {
                val min = Math.ceil((millisUntilFinished / 60000).toDouble())
                val sec = Math.ceil(((millisUntilFinished / 1000) % 60).toDouble())

                val format = resources.getString(R.string.resend_via)
                bnd.resendVia.text = String.format(format, min, sec)

            }
        }
    }
}