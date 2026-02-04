plugins {
    kotlin("jvm") version "2.3.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.appium:java-client:9.2.2")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    testImplementation("org.testng:testng:7.10.2")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useTestNG {
        useDefaultListeners = true
        suites("src/test/resources/testng.xml")
    }
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    outputs.dir(layout.buildDirectory.dir("reports/tests/test"))
    doLast {
        copy {
            from(layout.buildDirectory.dir("reports/tests/test"))
            into("test-report")
        }
    }
}