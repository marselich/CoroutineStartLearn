package ru.kalievmars.coroutinestartlearn

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kalievmars.coroutinestartlearn.databinding.ActivityMainBinding
import kotlin.concurrent.thread

@SuppressLint("HandlerLeak")
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonDownload.setOnClickListener {
            lifecycleScope.launch {
                loadData()
            }
        }

    }

    private suspend fun loadData() {
        binding.progressBar.isVisible = true
        binding.buttonDownload.isEnabled = false
        val city = loadCity()
        binding.valueCity.text = city
        val temp = loadTemperature(city)
        binding.valueTemperature.text = temp
        binding.progressBar.isVisible = false
        binding.buttonDownload.isEnabled = true
    }

    private suspend fun loadCity(): String {
        delay(5000)
        return resources.getString(R.string.tyumen)
    }

    private suspend fun loadTemperature(city: String): String {
        Toast.makeText(
            this,
            "Search temperature from $city",
            Toast.LENGTH_LONG
        ).show()
        delay(5000)
        return resources.getString(R.string.value_temperature)
    }

}