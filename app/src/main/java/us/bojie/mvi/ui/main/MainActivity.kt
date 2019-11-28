package us.bojie.mvi.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import us.bojie.mvi.R
import us.bojie.mvi.ui.DataStateListener
import us.bojie.mvi.util.DataState

class MainActivity : AppCompatActivity(),
    DataStateListener {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        showMainFragment()
    }

    fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment(), "MainFragment")
            .commit()
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        handleDataStateChange(dataState)
    }

    private fun handleDataStateChange(dataState: DataState<*>?) {
        dataState?.let {
            // handle loading
            showProgressBar(it.loading)

            // handle message
            dataState.message?.let { event ->
                event.getContentIfNotHandled()?.let { message ->
                    showToast(message)
                }
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}
