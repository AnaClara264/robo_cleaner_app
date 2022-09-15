package com.example.covid192

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.activity_bluetooth.*
import org.jetbrains.anko.toast

class BluetoothActivity : AppCompatActivity() {

    //Adição do bluetooth
    var m_bluetoothAdapter: BluetoothAdapter? = null
    lateinit var m_pairedDevices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object{
        val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        supportActionBar?.hide()

        /*val verify = 1
        val nextBluetooth: Button = findViewById(R.id.next_bluetooth)
        nextBluetooth.setOnClickListener {
            if(verify ==1){
                val intent = Intent(this@BluetoothActivity, AmbienteActivity::class.java)
                startActivity(intent)
            }else if(verify == 0){
                val window = PopupWindow(this)
                val view = layoutInflater.inflate(R.layout.activity_next_bluetooth, null)
                window.contentView = view
                Handler().postDelayed({
                    window.dismiss()
                }, 3000)
                window.showAsDropDown(nextBluetooth)
            }
        }*/

        val backBluetooth: ImageView = findViewById(R.id.back_bluetooth)
        backBluetooth.setOnClickListener {
            val intent = Intent(this@BluetoothActivity, AlertActivity::class.java)
            startActivity(intent)
        }

        val infoPopUp: ImageView = findViewById(R.id.info_bluetooth)
        infoPopUp.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_info_bluetooth, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 5000)
            window.showAsDropDown(infoPopUp)
        }


        //Bluetooth
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null){
            toast("this device doesn't support bluetooth")
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        next_bluetooth.setOnClickListener{ pairedDeviceList() }
    }
    //lista de pareados
    private fun pairedDeviceList(){
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list:ArrayList<BluetoothDevice> = ArrayList()

        if(!m_pairedDevices.isEmpty()){
            for(device: BluetoothDevice in m_pairedDevices){
                list.add(device)
                Log.i("device", ""+device)
            }
        }else{
            toast("Sem dispositivos pareados")
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, TimerActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(m_bluetoothAdapter!!.isEnabled){
                    toast("Bluetooth habilitado")
                }else{
                    toast("Bluetooth desabilitado.")
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                toast("Falha.")
            }
        }
    }
}