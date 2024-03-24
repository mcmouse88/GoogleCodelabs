plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.mcmouse88.common"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    testOptions.unitTests {
        isIncludeAndroidResources = true
    }
}

dependencies {

    implementation(libs.bundles.coroutines)
    implementation(libs.androidx.media)
    implementation(libs.google.code.gson)

    // ExoPlayer dependencies
    implementation(libs.google.android.exoplayer.core)
    implementation(libs.google.android.exoplayer.ui)
    implementation(libs.google.android.exoplayer.extension.mediasession)

    implementation("com.google.android.exoplayer:exoplayer:2.19.1")

    // Glide dependencies
    implementation(libs.github.bumptech.glide)
    ksp(libs.github.bumptech.glide.ksp)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}