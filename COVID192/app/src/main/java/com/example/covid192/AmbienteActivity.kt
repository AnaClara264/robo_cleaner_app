package com.example.covid192

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*

class AmbienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambiente)

        supportActionBar?.hide()

        val nextAmbiente: Button = findViewById(R.id.next_ambiente)
        nextAmbiente.setOnClickListener {
            val radioGroup : RadioGroup = findViewById(R.id.ambiente)
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                val window1 = PopupWindow(this)
                val view1 = layoutInflater.inflate(R.layout.activity_next_ambiente, null)
                window1.contentView = view1
                Handler().postDelayed({
                    window1.dismiss()
                }, 3000)
                window1.showAsDropDown(nextAmbiente)
            } else {
                val intent = Intent(this@AmbienteActivity, TimerActivity::class.java)
                startActivity(intent)
            }

        }

        val backAmbiente: ImageView = findViewById(R.id.back_ambiente)
        backAmbiente.setOnClickListener {
            val intent = Intent(this@AmbienteActivity, BluetoothActivity::class.java)
            startActivity(intent)
        }

        val infoPopUp: ImageView = findViewById(R.id.info_ambiente)
        infoPopUp.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_info_ambiente, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 5000)
            window.showAsDropDown(infoPopUp)
        }
    }

}