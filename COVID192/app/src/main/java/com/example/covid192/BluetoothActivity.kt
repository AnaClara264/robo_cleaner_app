package com.example.covid192

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.BatchingListUpdateCallback



class BluetoothActivity : AppCompatActivity() {

    lateinit var saida: TextView
    val REQUEST_ENABLE_BLUETOOTH = 1
    var btAdaptador: BluetoothAdapter? = null
    var btDeviceList = ArrayList<BluetoothDevice>()


    val verify = 1
    val nextBluetooth: Button = findViewById(R.id.next_bluetooth)
    val backBluetooth: ImageView = findViewById(R.id.back_bluetooth)
    val infoPopUp: ImageView = findViewById(R.id.info_bluetooth)
    val dispositivos: ListView = findViewById(R.id.dispositivos)

    companion object{
        val EXTRA_ADDRESS: String = "Device_address"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        supportActionBar?.hide()

        backBluetooth.setOnClickListener {
            val intent = Intent(this@BluetoothActivity, AlertActivity::class.java)
            startActivity(intent)
        }

        nextBluetooth.setOnClickListener {
            val intent = Intent(this@BluetoothActivity, AmbienteActivity::class.java)
            startActivity(intent)
        }

        infoPopUp.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_info_bluetooth, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 5000)
            window.showAsDropDown(infoPopUp)
        }

        if (!iniciarBluetooth()) return
        listarPareados()
        buscarDispositivos()

    } //fim OnCreate

    fun iniciarBluetooth() : Boolean{
        saida = findViewById<TextView>(R.id.dispositivos)
        saida.append("Iniciando aplicação")

        //obter o adaptador bluetooth
        btAdaptador = BluetoothAdapter.getDefaultAdapter()
        if (btAdaptador == null){
            saida.append("\nO dispositivo não suporta bluetooth")
            return false
        }
        saida.append("\nAdaptador Bluetooth padrão: " + btAdaptador)

        ///Verificar e habilitar
        if (btAdaptador?.isEnabled == false){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
            saida.append("\nBluetooth desabilitado, requisitando habilitação")
            return false
        }
        saida.append("Bluetooth habilitado.")

        //Registrar o BroadcastReceiver
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_UUID)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(receiver, filter)

        return true
    } //Fim iniciarBLuetooth

    @SuppressLint("MissingPermission")
    fun listarPareados(){
        saida.append("\nDispositivos pareados:")
        val pairedDevices : Set<BluetoothDevice>? = btAdaptador!!.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAdress = device.address
            saida.append("\n " + deviceName + " (" + deviceHardwareAdress + ")")
        }
    }// fim listarPareados

    @SuppressLint("MissingPermission")
    fun buscarDispositivos(){
        if(btAdaptador!!.isDiscovering()) {
            btAdaptador!!.cancelDiscovery()
            saida.append("Bluetooth ja em busca")
        }
        btAdaptador!!.startDiscovery()
        saida.append("\nBuscando...")
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver(){
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent){
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action){
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                saida.append("Dispositivo: ${device!!.name}, $device")
            }
            if (BluetoothDevice.ACTION_UUID == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID)
            }
        }
    }

}

private fun Button.setOnClickListener(pairedDeviceList: Unit) {

}
