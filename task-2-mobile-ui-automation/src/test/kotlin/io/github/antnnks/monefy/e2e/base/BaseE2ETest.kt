package com.monefy.io.github.antnnks.monefy.e2e.base

import com.monefy.io.github.antnnks.monefy.e2e.config.AppiumConfig
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite
import java.time.Duration

abstract class BaseE2ETest {

    companion object {
        @Volatile
        var driver: AndroidDriver? = null
            private set

        @Volatile
        var wait: WebDriverWait? = null
            private set

        @Volatile
        private var teardownDone = false
    }

    protected fun getDriver(): AndroidDriver = driver!!
    protected fun getWait(): WebDriverWait = wait!!

    @BeforeSuite(alwaysRun = true)
    fun suiteSetUp() {
        if (driver != null) return
        val options = UiAutomator2Options().apply {
            setPlatformName("Android")
            setDeviceName(System.getProperty("appium.device", "emulator"))
            setAutomationName("UiAutomator2")
            setAppPackage(AppiumConfig.APP_PACKAGE)
            setAppActivity(AppiumConfig.MAIN_ACTIVITY)
            setNoReset(true)
            AppiumConfig.androidHome?.let { setCapability("appium:androidHome", it) }
        }
        driver = AndroidDriver(AppiumConfig.appiumUrl, options)
        driver!!.manage().timeouts().implicitlyWait(Duration.ofSeconds(AppiumConfig.IMPLICIT_WAIT_SECONDS))
        wait = WebDriverWait(driver!!, Duration.ofSeconds(AppiumConfig.EXPLICIT_WAIT_SECONDS))
    }

    @AfterSuite(alwaysRun = true)
    fun suiteTearDown() {
        if (teardownDone) return
        teardownDone = true
        driver?.quit()
        driver = null
        wait = null
    }
}
