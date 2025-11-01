package eu.tutorials.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import eu.tutorials.lab_week_10.viewmodels.TotalViewModel
import androidx.lifecycle.ViewModelProvider
import eu.tutorials.lab_week_10.database.Total
import eu.tutorials.lab_week_10.database.TotalDatabase
import androidx.room.Room
import eu.tutorials.lab_week_10.database.TotalObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private fun getCurrentDateTimeString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
    private val ID: Long = 1
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).allowMainThreadQueries().build()
    }
    private fun initializeValueFromDatabase() {
        val totalList = db.totalDao().getTotal(ID)

        if (totalList.isEmpty()) {
            val now  = getCurrentDateTimeString()
            val newTotalObject = TotalObject(value = 0, date = getCurrentDateTimeString())
            db.totalDao().insert(Total(id = ID, total = newTotalObject))

            viewModel.setTotal(0)

            Toast.makeText(this, "Create new instance: $now", Toast.LENGTH_LONG).show()
        } else {
            val existingTotalObject = totalList.first().total
            viewModel.setTotal(existingTotalObject.value)

            val lastUpdateDate = existingTotalObject.date
            Toast.makeText(this, "Last updated: $lastUpdateDate", Toast.LENGTH_LONG).show()
        }


    }



    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()

        prepareViewModel()
    }


    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }
    private fun prepareViewModel(){
        viewModel.total.observe(this, {
            updateText(it)
        })
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    override fun onPause() {
        super.onPause()
        val currentValue = viewModel.total.value ?: 0
        val updatedTotalObject = TotalObject(value = currentValue, date = getCurrentDateTimeString())

        db.totalDao().update(Total(id = ID, total = updatedTotalObject))
    }
}