import gradle.kotlin.dsl.accessors._4ae9a357be730be19e1f9aadcbf2b289.test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
    maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
    reports.junitXml.includeSystemOutLog = false
    reports.junitXml.includeSystemErrLog = false
    testLogging {
        showExceptions = true
        showStandardStreams = false
        exceptionFormat = TestExceptionFormat.FULL
        // events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn("ktlintFormat")
}
