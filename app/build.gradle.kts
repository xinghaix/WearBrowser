plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("io.gitlab.arturbosch.detekt")
}

android {
    namespace = "com.wearbrowser"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wearbrowser"
        minSdk = 26
        targetSdk = 35
        versionCode = 133
        versionName = "1.3.3-foundation-sprint-4"
    }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("../config/detekt/detekt.yml"))
}

dependencies {
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui:1.7.5")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.5")
    testImplementation("junit:junit:4.13.2")
}

