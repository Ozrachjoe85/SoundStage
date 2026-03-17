// app/build.gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.autoeq.studio'
    compileSdk 34

    defaultConfig {
        applicationId "com.autoeq.studio"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            // Debug-specific options if needed
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_26
        targetCompatibility JavaVersion.VERSION_26
    }

    kotlinOptions {
        jvmTarget = "26"
    }

    buildFeatures {
        viewBinding true
    }

    packaging {
        resources {
            excludes += ['META-INF/DEPENDENCIES', 'META-INF/LICENSE', 'META-INF/LICENSE.txt', 'META-INF/NOTICE', 'META-INF/NOTICE.txt']
        }
    }
}

dependencies {
    // Core
    implementation "androidx.core:core-ktx:1.12.0"
    implementation "androidx.appcompat:appcompat:1.7.0"
    implementation "com.google.android.material:material:1.11.0"
    implementation "androidx.constraintlayout:constraintlayout:2.2.0"

    // RecyclerView for track list
    implementation "androidx.recyclerview:recyclerview:1.3.2"

    // ExoPlayer for audio + visualizer
    implementation "com.google.android.exoplayer:exoplayer:2.19.1"
    implementation "com.google.android.exoplayer:exoplayer-ui:2.19.1"

    // Kotlin Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

    // Optional: animations / motion
    implementation "androidx.dynamicanimation:dynamicanimation:1.1.1"

    // Testing (optional)
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.6'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.6.0'
}

// --- IMPORTANT ---
// No afterEvaluate {} modifying dependencies. All declared here.
// This ensures GitHub Actions / Gradle 9.4 will not throw
// 'Cannot mutate dependencies' errors.
