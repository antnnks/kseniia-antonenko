package com.monefy.io.github.antnnks.monefy.e2e.config

import java.net.URI
import java.net.URL

/**
 * Central configuration for Appium and Monefy app under test.
 * Override via environment variables or system properties if needed.
 */
object AppiumConfig {
    const val APP_PACKAGE = "com.monefy.app.lite"
    const val MAIN_ACTIVITY = "com.monefy.app.MainActivity"
    const val APPIUM_HOST = "127.0.0.1"
    const val APPIUM_PORT = 4723
    const val IMPLICIT_WAIT_SECONDS = 15L
    const val EXPLICIT_WAIT_SECONDS = 25L

    val appiumUrl: URL
        get() = URI("http://$APPIUM_HOST:$APPIUM_PORT").toURL()

    val androidHome: String?
        get() = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
}
