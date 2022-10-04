package ru.kalievmars.coroutinestartlearn

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.core.view.isVisible
import ru.kalievmars.coroutinestartlearn.databinding.ActivityMainBinding
import kotlin.concurrent.thread

@SuppressLint("HandlerLeak")
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) { // обработка сообщения
            super.handleMessage(msg)

            print("MY_MESSAGE $msg")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonDownload.setOnClickListener {
            loadData()
        }

        handler.sendMessage(Message.obtain(handler, 0, 1))
    }

    private fun loadData() {
        binding.progressBar.isVisible = true
        binding.buttonDownload.isEnabled = false
        loadCity {city ->
            binding.valueCity.text = city
            loadTemperature(city) { temp ->
                binding.valueTemperature.text = temp
                binding.progressBar.isVisible = false
                binding.buttonDownload.isEnabled = true
            }
        }
    }


    private fun loadCity(callback: (String) -> Unit) {
        thread {
            Looper.prepare()
            // need create Looper for new thread (in default thread don't use looper except main thread)
            Handler(Looper.myLooper()!!) // send runnable or message to looper in this thread
            Thread.sleep(5000)
            runOnUiThread { // same as Handler(Looper.getMainLooper()).post
                callback(resources.getString(R.string.tyumen))
            }
        }
    }

    private fun loadTemperature(city: String, callback: (String) -> Unit) {
        thread {
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Search temperature from $city",
                    Toast.LENGTH_LONG
                ).show()
            }
            Thread.sleep(5000)

            runOnUiThread {
                callback(resources.getString(R.string.value_temperature))
            }
        }
    }

}