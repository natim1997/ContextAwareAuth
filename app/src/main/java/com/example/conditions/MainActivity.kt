package com.example.conditions

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var tvCondition1: TextView
    private lateinit var tvCondition2: TextView
    private lateinit var tvCondition3: TextView
    private lateinit var tvCondition4: TextView
    private lateinit var tvCondition5: TextView
    private lateinit var btnLogin: Button
    private lateinit var etPassword: EditText

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var audioManager: AudioManager

    private var gravityY = 0f
    private var currentBatteryLevel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCondition1 = findViewById(R.id.tvCondition1)
        tvCondition2 = findViewById(R.id.tvCondition2)
        tvCondition3 = findViewById(R.id.tvCondition3)
        tvCondition4 = findViewById(R.id.tvCondition4)
        tvCondition5 = findViewById(R.id.tvCondition5)
        btnLogin = findViewById(R.id.btnLogin)
        etPassword = findViewById(R.id.etPassword)

        btnLogin.isEnabled = false
        btnLogin.alpha = 0.5f

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        btnLogin.setOnClickListener {


            val part1 = currentBatteryLevel.toString()

            var sum = 0
            var num = currentBatteryLevel
            while (num > 0) {
                sum += num % 10
                num /= 10
            }
            val part2 = sum.toString()

            val part3 = part1.reversed()

            val secretPassword = part1 + part2 + part3

            val userInput = etPassword.text.toString()

            if (userInput == secretPassword) {
                Toast.makeText(this, "ACCESS APPROVED!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "WRONG PASSWORD!", Toast.LENGTH_LONG).show()
                etPassword.error = "Incorrect Password"
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gravityY = event.values[1]
            checkAllConditions()
        }
    }

    private fun checkAllConditions() {
        val isUpsideDown = gravityY < -8.0
        updateUI(tvCondition1, isUpsideDown, "1. Phone Upside Down")

        val isAirplane = Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        updateUI(tvCondition2, isAirplane, "2. Airplane Mode On")

        val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        currentBatteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 0

        val isEven = (currentBatteryLevel % 2 == 0)
        updateUI(tvCondition3, isEven, "3. Battery % is Even ($currentBatteryLevel%)")

        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val isSilent = currentVolume == 0
        updateUI(tvCondition4, isSilent, "4. Volume is 0 (Silent)")

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        updateUI(tvCondition5, isDarkMode, "5. Dark Mode Active")

        if (isUpsideDown && isAirplane && isEven && isSilent && isDarkMode) {
            btnLogin.isEnabled = true
            btnLogin.alpha = 1.0f
            btnLogin.setBackgroundColor(Color.parseColor("#6200EE"))
        } else {
            btnLogin.isEnabled = false
            btnLogin.alpha = 0.5f
            btnLogin.setBackgroundColor(Color.GRAY)
        }
    }

    private fun updateUI(tv: TextView, met: Boolean, text: String) {
        if (met) {
            tv.text = "✅ $text"
            tv.setTextColor(Color.GREEN)
        } else {
            tv.text = "❌ $text"
            tv.setTextColor(Color.RED)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        checkAllConditions()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}