package com.example.covid192

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import java.io.IOException
import java.util.*

class TimerActivity : AppCompatActivity() {
    companion object{
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }

    private var contando = false
    private var time = 60000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        m_address = intent.getStringExtra(BluetoothActivity.EXTRA_ADDRESS).toString()


        supportActionBar?.hide()

        val backTimer: ImageView = findViewById(R.id.back_timer)
        backTimer.setOnClickListener {
            sendCommand("des")
            disconnect()
            val intent = Intent(this@TimerActivity, AmbienteActivity::class.java)
            startActivity(intent)
        }

        val infoPopUp: ImageView = findViewById(R.id.info_timer)
        infoPopUp.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_info_timer, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 5000)
            window.showAsDropDown(infoPopUp)
        }

        val start: Button = findViewById(R.id.startStopButton)
        start.setOnClickListener {
            if (contando == false) {
                start.text = "encerrar"
                val clock: TextView = findViewById(R.id.timeTV)
                clock.textSize = 70F
                val timer = object : CountDownTimer(time.toLong(), 1000) {
                    override fun onTick(i: Long) {
                        val seg = i / 1000
                        clock.text = "${seg}"
                    }

                    override fun onFinish() {
                        sendCommand("des")
                        clock.textSize = 20F
                        clock.text = "Finalizado!"
                    }
                }

                contando = true
                sendCommand("lig")
                timer.start()

            } else {
                sendCommand("des")
                disconnect()
                val intent = Intent(this@TimerActivity, AlertActivity::class.java)
                startActivity(intent)
            }

        }
        ConnectToDevice(this).execute()
    }
    private fun sendCommand(input: String){
        if(m_bluetoothSocket != null){
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            }catch(e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun disconnect(){
        if(m_bluetoothSocket != null){
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            }catch(e: IOException){
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>(){
        private var connectSucess: Boolean = true
        private val context: Context

        init{
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Conectando...", "aguarde")
        }

        override fun doInBackground(vararg p0: Void?): String {
            try{
                if(m_bluetoothSocket == null || !m_isConnected){
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()

                }
            }catch (e: IOException){
                connectSucess = false
                e.printStackTrace()
            }
            return null.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(!connectSucess){
                Log.i("data", "falha na conexão")
            }else{
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
}


