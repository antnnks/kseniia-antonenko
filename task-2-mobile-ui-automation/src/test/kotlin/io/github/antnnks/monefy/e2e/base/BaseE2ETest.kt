package io.github.antnnks.monefy.e2e.base

import io.github.antnnks.monefy.e2e.config.AppiumConfig
import io.github.antnnks.monefy.e2e.pages.MainPage
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite
import org.testng.annotations.BeforeMethod
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
        clearAppDataIfRequested()
        val options = UiAutomator2Options().apply {
            setPlatformName("Android")
            setDeviceName(AppiumConfig.deviceName)
            setAutomationName("UiAutomator2")
            setAppPackage(AppiumConfig.APP_PACKAGE)
            setAppActivity(AppiumConfig.mainActivity)
            setAppWaitActivity(AppiumConfig.waitActivity)
            setAppWaitPackage(AppiumConfig.APP_PACKAGE)
            setNoReset(true)
            AppiumConfig.androidHome?.let { setCapability("appium:androidHome", it) }
            setCapability("appium:adbExecTimeout", 120000)
            setCapability("appium:appWaitDuration", 30000)
        }
        driver = AndroidDriver(AppiumConfig.appiumUrl, options)
        driver!!.manage().timeouts().implicitlyWait(Duration.ZERO)
        wait = WebDriverWait(driver!!, Duration.ofSeconds(AppiumConfig.EXPLICIT_WAIT_SECONDS))
    }

    @BeforeMethod(alwaysRun = true)
    fun testSetUp() {
        clearAppDataIfRequested()
        val activeDriver = driver ?: return
        activeDriver.activateApp(AppiumConfig.APP_PACKAGE)
        MainPage(activeDriver, getWait()).ensureOnHome()
    }

    private fun clearAppDataIfRequested() {
        if (System.getProperty("appium.clean", "true").lowercase() == "false") return
        val adbPath = AppiumConfig.androidHome
            ?.let { "$it/platform-tools/adb" }
            ?: "adb"
        val device = AppiumConfig.deviceName
        try {
            ProcessBuilder(adbPath, "-s", device, "shell", "pm", "clear", AppiumConfig.APP_PACKAGE)
                .redirectErrorStream(true)
                .start()
                .waitFor()
        } catch (_: Exception) {
            // Best-effort cleanup; continue even if adb isn't available.
        }
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
