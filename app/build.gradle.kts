import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.ksp)
}


android {
    compileSdk = 35
    buildToolsVersion = "35.0.0"
    buildFeatures.dataBinding = true

    buildFeatures.viewBinding = true
    buildFeatures.buildConfig = true

    namespace = "de.dertyp7214.rboardimetester"

    defaultConfig {
        applicationId = "de.dertyp7214.rboardimetester"
        minSdk = 23
        targetSdk = 35
        versionCode = 126000
        versionName = "1.2.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resourceConfigurations += listOf(
            "ar", "cs", "da", "de",
            "el", "en", "es", "fi",
            "fr", "hi", "hu", "in",
            "it", "ja", "nl", "no",
            "pl", "pt-rBR", "ro", "ru",
            "sv", "uk", "vi",
            "zh-rCN", "zh-rTW"
        )
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = JvmTarget.JVM_21.description
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {
    implementation(project(":colorutilsc"))
    implementation(project(":rboardcomponents"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.legacy.support.v4)
    implementation(libs.browser)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs.nio)
}