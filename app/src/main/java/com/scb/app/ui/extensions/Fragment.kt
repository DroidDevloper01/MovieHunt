/**
 * Copyright (C) 2020 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scb.app.util

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.scb.app.R
import com.scb.app.network.api.MoviesRepository
import com.scb.app.network.api.MoviesService
import com.scb.app.ui.core.BaseActivity
import com.scb.app.ui.core.BaseFragment
import com.scb.app.ui.details.usecases.GetMovieDetails
import com.scb.app.ui.home.usecases.GetMovies

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()

fun BaseFragment.close() = fragmentManager?.popBackStack()

val BaseFragment.viewContainer: View get() = (activity as BaseActivity).findViewById(R.id.fragmentContainer)

val BaseFragment.appContext: Context get() = activity?.applicationContext!!
