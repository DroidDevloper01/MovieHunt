package com.scb.app.ui.home

import android.content.Context
import android.content.Intent
import com.scb.app.BuildConfig
import com.scb.app.ui.core.BaseActivity
import com.scb.app.ui.home.fragments.MoviesFragment

private const val INTENT_EXTRA_PARAM_MOVIE = BuildConfig.APPLICATION_ID

class MoviesActivity : BaseActivity() {
    companion object {
        fun callingIntent(context: Context) = Intent(context, MoviesActivity::class.java)
    }


}
