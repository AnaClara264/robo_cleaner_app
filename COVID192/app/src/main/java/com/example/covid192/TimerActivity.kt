package com.example.covid192

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.example.covid192.databinding.ActivityAmbienteBinding
import com.example.covid192.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_timer)

        supportActionBar?.hide()

        val infoPopUp: ImageView = findViewById(R.id.info_timer)
        infoPopUp.setOnClickListener {
            popUp()
        }

        val start: Button = findViewById(R.id.startStopButton)
        start.setOnClickListener {
            contador()
        }

        val backTimer: ImageView = findViewById(R.id.back_timer)
        backTimer.setOnClickListener {
            val intent = Intent(this@TimerActivity, AmbienteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun popUp() {
        val infoPopUp: ImageView = findViewById(R.id.info_timer)
        val window = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.activity_info_timer, null)
        window.contentView = view
        Handler().postDelayed({
            window.dismiss()
        }, 5000)
        window.showAsDropDown(infoPopUp)
    }

    private fun contador() {
        var contando = false
        var time = intent.getStringExtra("time")

        val start: Button = findViewById(R.id.startStopButton)
        if (contando == false) {
            start.text = "encerrar"
            val clock: TextView = findViewById(R.id.timeTV)
            clock.textSize = 70F
            val timer = object : CountDownTimer(time?.toLong()!!, 1000) {
                override fun onTick(i: Long) {
                    val seg = i / 1000
                    clock.text = "${seg}"
                }

                override fun onFinish() {
                    clock.textSize = 20F
                    clock.text = "Finalizado!"
                }
            }
            contando = true
            timer.start()

        } else {
            val intent = Intent(this@TimerActivity, AlertActivity::class.java)
            startActivity(intent)
        }
    }
}