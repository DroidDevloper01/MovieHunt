package com.scb.app.ui.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scb.app.R
import com.scb.app.util.inTransaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_layout.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        setSupportActionBar(toolbar)
    }

/*    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }*/




}
