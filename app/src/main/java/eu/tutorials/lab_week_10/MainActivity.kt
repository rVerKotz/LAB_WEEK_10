package eu.tutorials.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import eu.tutorials.lab_week_10.R
import eu.tutorials.lab_week_10.viewmodels.TotalViewModel
import androidx.lifecycle.ViewModelProvider
class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }
    private var total: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViewModel()
    }


    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }
    private fun prepareViewModel(){
        updateText(viewModel.total)
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
            updateText(viewModel.total)
        }
    }
}