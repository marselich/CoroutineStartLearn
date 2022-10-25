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
import kotlinx.coroutines.async
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
//            lifecycleScope.launch {
//                loadData()
//            }
            binding.progressBar.isVisible = true
            binding.buttonDownload.isEnabled = false
            val jobCity = lifecycleScope.async {
                loadCity().apply {
                    binding.valueCity.text = this
                }
            }
            val jobTemp = lifecycleScope.async {
                loadTemperature().apply {
                    binding.valueTemperature.text = this
                }
            }

            lifecycleScope.launch {
                val city = jobCity.await()
                val temp = jobTemp.await()
                Toast.makeText(
                    this@MainActivity,
                    "$city $temp",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.isVisible = false
                binding.buttonDownload.isEnabled = true
            }
        }

    }

    private suspend fun loadData() {
        binding.progressBar.isVisible = true
        binding.buttonDownload.isEnabled = false
        val city = loadCity()
        binding.valueCity.text = city
        val temp = loadTemperature()
        binding.valueTemperature.text = temp
        binding.progressBar.isVisible = false
        binding.buttonDownload.isEnabled = true
    }

    // coroutines under the hood is callbacks ( state machine (конечный автомат) )
    private fun loadDataWithoutCoroutines(step: Int = 0, obj: Any? = null) {
        when(step) {
            0 -> {
                binding.progressBar.isVisible = true
                binding.buttonDownload.isEnabled = false
                loadCityWithoutCoroutines {
                    loadDataWithoutCoroutines(1, it)
                }
            }
            1 -> {
                val city = obj as String
                binding.valueCity.text = city
                loadTemperatureWithoutCoroutines(city) {
                    loadDataWithoutCoroutines(2, it)
                }
            }
            2 -> {
                val temp = obj as String
                binding.valueTemperature.text = temp
                binding.progressBar.isVisible = false
                binding.buttonDownload.isEnabled = true
            }
        }
    }

    private suspend fun loadCity(): String {
        delay(5000)
        return resources.getString(R.string.tyumen)
    }

    private fun loadCityWithoutCoroutines(callback: (String) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            callback(resources.getString(R.string.tyumen))
        }, 5000)
    }

    private suspend fun loadTemperature(): String {
        delay(5000)
        return resources.getString(R.string.value_temperature)
    }

    private fun loadTemperatureWithoutCoroutines(city: String, callback: (String) -> Unit){
        Toast.makeText(
            this,
            "Search temperature from $city",
            Toast.LENGTH_LONG
        ).show()
        Handler(Looper.getMainLooper()).postDelayed({
            callback(resources.getString(R.string.value_temperature))
        }, 5000)
    }

}