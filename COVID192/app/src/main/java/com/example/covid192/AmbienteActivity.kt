package com.example.covid192

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.covid192.databinding.ActivityAmbienteBinding

const val EXTRA_MESSAGE = "com.example.covid192.MESSAGE"

class AmbienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAmbienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmbienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val nextAmbiente: Button = findViewById(R.id.next_ambiente)
        nextAmbiente.setOnClickListener {
            next()
        }

        val backAmbiente: ImageView = findViewById(R.id.back_ambiente)
        backAmbiente.setOnClickListener {
            val intent = Intent(this@AmbienteActivity, BluetoothActivity::class.java)
            startActivity(intent)
        }

        val infoPopUp: ImageView = findViewById(R.id.info_ambiente)
        infoPopUp.setOnClickListener {
            popUp()
        }
    }

    private fun popUp() {
        val infoPopUp: ImageView = findViewById(R.id.info_ambiente)
        val window = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.activity_info_ambiente, null)
        window.contentView = view
        Handler().postDelayed({
            window.dismiss()
        }, 5000)
        window.showAsDropDown(infoPopUp)
    }

    private fun next() {
        val nextAmbiente: Button = findViewById(R.id.next_ambiente)
        val radioGroup: RadioGroup = findViewById(R.id.ambiente)
        var time = 60000

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            val window1 = PopupWindow(this)
            val view1 = layoutInflater.inflate(R.layout.activity_next_ambiente, null)
            window1.contentView = view1
            Handler().postDelayed({
                window1.dismiss()
            }, 3000)
            window1.showAsDropDown(nextAmbiente)

        } else {
            fun onRadioButtonClicked(view: View) {

                if (view is RadioButton) {
                    val checked = view.isChecked

                    when (view.getId()) {
                        R.id.ambP ->
                            if (checked) {
                                time = 60000
                                val i = Intent(applicationContext, TimerActivity::class.java)
                                i.putExtra("timer", time)
                                startActivity(i)
                            }
                        R.id.ambM ->
                            if (checked) {
                                time = 300000
                                val i = Intent(applicationContext, TimerActivity::class.java)
                                i.putExtra("timer", time)
                                startActivity(i)
                            }
                        R.id.ambG ->
                            if (checked) {
                                time = 600000
                                val i = Intent(applicationContext, TimerActivity::class.java)
                                i.putExtra("timer", time)
                                startActivity(i)
                            }
                    }
                }
            }
        }
        val intent = Intent(this@AmbienteActivity, TimerActivity::class.java)
        intent.putExtra("time", time.toLong())
        startActivity(intent)
    }
}