plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.mcmouse88.kotlin_coroutines"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mcmouse88.kotlin_coroutines"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.google.code.gson)
    implementation(libs.bundles.retrofit)
    ksp(libs.androidx.room.compiler)
    debugImplementation(libs.google.truth)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.androidx.arch.core.testing)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.contrib)
    androidTestImplementation(libs.androidx.test.espresso.intents)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.jetbrains.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.arch.core.testing)
}
