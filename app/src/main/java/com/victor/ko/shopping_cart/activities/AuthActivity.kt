package com.victor.ko.shopping_cart.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        continue_button.setOnClickListener { requirePin() }
    }

    private fun requirePin() {

        continue_button?.visibility = View.GONE
        send_again_button.isEnabled = false
        //countDownTimer.cancel()
        //countDownTimer.start()

        try {
            val method = if (BuildConfig.DEBUG) {
                api.getPin("${phoneTextEdit.text} test", "1")
            } else {
                api.getPin(number, "0")
            }

            method.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : Observer<PIN> {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                            compositeDisposable.add(d)
                        }

                        override fun onNext(pin: PIN) {
                            when {
                                pin.success != null -> {
                                    Paper.book().write("authToken", pin.success.token)
                                    send_code_form?.visibility = View.VISIBLE
                                    if (BuildConfig.DEBUG) {
                                        pinTextEdit?.setText(pin.success.pin)
                                    }
                                }
                                pin.error != null -> {
                                    pin_error_label?.text = pin.error.message
                                }
                            }
                        }

                        override fun onError(e: Throwable) {
                            EventBus.getDefault().post(ErrorEvent(e))
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}