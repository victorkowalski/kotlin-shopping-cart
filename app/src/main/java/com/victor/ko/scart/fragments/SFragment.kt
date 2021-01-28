package com.victor.ko.scart.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.victor.ko.scart.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class SFragment : Fragment() {
    private val TAG = "SFragment"

    var actionBarTitle = ""

    fun printError(payload: JsonObject) {
        CoroutineScope(Dispatchers.Main).launch() {
            val errorObject = payload.getAsJsonObject("error")
            if (errorObject.has("message")) {
                Toast.makeText(context, String.format(
                    getString(R.string.error),
                    errorObject.get("message").toString()
                ), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun getMainActivityToolbar(): Toolbar {
        return ((activity as AppCompatActivity).supportActionBar) as Toolbar
    }

    private fun setFilterIconVisible(v: Int) {
        val filterIcon = getMainActivityToolbar().findViewById<ImageView>(R.id.filter_icon)
        filterIcon?.visibility = v
    }

    private fun setFilterIconClickListener(listener: () -> Unit) {
        try {
            val filterIcon = getMainActivityToolbar().findViewById<ImageView>(R.id.filter_icon)
            filterIcon?.onClick {
                /*val args = Bundle()
                navController.navigate(R.id.filterFragment, args)*/
                listener()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            this is CatalogFragment -> {
                setFilterIconVisible(View.VISIBLE)
                this.setFilterIconClickListener {
                    findNavController().navigate(R.id.filterFragment)
                }
            }
            this is FilterResultFragment -> {
                setFilterIconVisible(View.VISIBLE)
                this.setFilterIconClickListener {
                    activity?.onBackPressed()
                }
            }
            else -> setFilterIconVisible(View.GONE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivityToolbar().findViewById<TextView>(R.id.title).text = this.actionBarTitle
    }
}