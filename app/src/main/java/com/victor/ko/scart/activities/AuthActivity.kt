package com.victor.ko.scart.activities

import android.os.Bundle
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
        //countDownTimer.cancel()
        //countDownTimer.start()

//***************
        CoroutineScope(Dispatchers.Main).launch() {
            // Try catch block to handle exceptions when calling the API.
            try {
                val response = ApiAdapter.apiClient.getPin("${bnd.phoneTextEdit.text} test", "1")
                // Check if response was successful
                if (response.isSuccessful && response.body() != null) {
                    // Retrieve data.
                    val data = response.body()!!
                    /*data.message?.let {
                        // Load URL into the ImageView using Coil.
                        //iv_dog_image.load(it)
                    }*/
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
}