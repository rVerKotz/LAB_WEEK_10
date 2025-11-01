package eu.tutorials.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import eu.tutorials.lab_week_10.viewmodels.TotalViewModel
import androidx.lifecycle.ViewModelProvider
import eu.tutorials.lab_week_10.database.Total
import eu.tutorials.lab_week_10.database.TotalDatabase
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    private val ID: Long = 1
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).allowMainThreadQueries().build()
    }
    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            db.totalDao().insert(Total(id = 1, total = 0))
        } else {
            viewModel.setTotal(total.first().total)
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
        db.totalDao().update(Total(ID, viewModel.total.value!!))
    }
}