package io.github.antnnks.monefy.e2e.config

import java.net.URI
import java.net.URL

object AppiumConfig {
    const val APP_PACKAGE = "com.monefy.app.lite"
    private const val DEFAULT_MAIN_ACTIVITY = "com.monefy.activities.main.MainActivity_"
    const val APPIUM_HOST = "127.0.0.1"
    const val APPIUM_PORT = 4723
    const val IMPLICIT_WAIT_SECONDS = 15L
    const val EXPLICIT_WAIT_SECONDS = 25L

    val appiumUrl: URL
        get() = URI("http://$APPIUM_HOST:$APPIUM_PORT").toURL()

    val mainActivity: String
        get() = System.getProperty("appium.activity") ?: DEFAULT_MAIN_ACTIVITY

    val waitActivity: String
        get() = System.getProperty("appium.waitActivity") ?: "com.monefy.*"

    val deviceName: String
        get() = System.getProperty("appium.device", "emulator-5554")

    val androidHome: String?
        get() = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
}
