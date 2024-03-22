package com.android.algorithms.utils.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class for initializing Hilt.
 */
@HiltAndroidApp
class App : Application()