package com.marcokenata.wifijnidemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var wifiSwitch: Switch? = null
    private var wifiManager: WifiManager? = null

    private val wifiStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wifiStateExtra = intent.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN
            )

            when (wifiStateExtra) {
                WifiManager.WIFI_STATE_ENABLED -> {
                    wifiSwitch!!.isChecked = true
                    wifiSwitch!!.text = "WiFi is ON"
                    val networkSSID = "FNJ"
                    val networkPass = "titikaja"

                    val conf = WifiConfiguration()
                    conf.SSID = "\"" + networkSSID + "\""
                    conf.preSharedKey = "\""+ networkPass +"\"";

                    val list = wifiManager!!.getConfiguredNetworks()
                    for (i in list) {
                        if (i.SSID != null && i.SSID == "\"" + networkSSID + "\"") {
                            wifiManager!!.disconnect()
                            wifiManager!!.enableNetwork(i.networkId, true)
                            wifiManager!!.reconnect()
                            break
                        }
                    }
                }
                WifiManager.WIFI_STATE_DISABLED -> {
                    wifiSwitch!!.isChecked = false
                    wifiSwitch!!.text = "WiFi is OFF"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiSwitch = findViewById(R.id.wifi_switch)
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        wifiSwitch!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                wifiManager!!.isWifiEnabled = true
                wifiSwitch!!.text = "WiFi is ON"
                val networkSSID = "FNJ"
                val networkPass = "titikaja"

                val conf = WifiConfiguration()
                conf.SSID = "\"" + networkSSID + "\""
                conf.preSharedKey = "\""+ networkPass +"\"";

                val list = wifiManager!!.getConfiguredNetworks()
                for (i in list) {
                    if (i.SSID != null && i.SSID == "\"" + networkSSID + "\"") {
                        wifiManager!!.disconnect()
                        wifiManager!!.enableNetwork(i.networkId, true)
                        wifiManager!!.reconnect()
                        break
                    }
                }
            } else {
                wifiManager!!.isWifiEnabled = false
                wifiSwitch!!.text = "WiFi is OFF"
            }
        }

        bt_sum.setOnClickListener {
            tv_sum.text = sumTwoValues(argument_a.text.toString().toInt(),argument_b.text.toString().toInt()).toString()
        }

    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(wifiStateReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiStateReceiver)
    }

    external fun sumTwoValues(a: Int, b : Int) : Int

    companion object {
        init {
            System.loadLibrary("kotlin-jni")
        }
    }

}

