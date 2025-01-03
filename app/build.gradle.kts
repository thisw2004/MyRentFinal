plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.myrentapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myrentapp"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("junit:junit:4.13.2")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui-test-junit4")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-test-manifest")
    // JUnit extension for Android
    implementation ("androidx.test.ext:junit:1.1.5")

    // Espresso core library
    implementation ("androidx.test.espresso:espresso-core:3.5.1")

    // (Optional) Espresso contrib library for advanced interactions
    implementation ("androidx.test.espresso:espresso-contrib:3.5.1")

    //for navigation
    implementation ("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation ("androidx.activity:activity-compose:1.4.0-alpha03")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")


    //for calling the cam
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.activity:activity-ktx:1.3.1")
    implementation ("androidx.fragment:fragment-ktx:1.3.6")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gson
    implementation("com.google.code.gson:gson:2.8.9")

    //coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    //layout
    implementation ("androidx.compose.material3:material3:1.1.0")// or the latest stable version
    implementation ("androidx.compose.ui:ui:1.4.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    implementation ("com.google.accompanist:accompanist-permissions:0.31.1-alpha")
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.maps.android:maps-compose:6.1.0")

    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.0")
    implementation("androidx.navigation:navigation-compose:2.7.2")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")

    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation("androidx.compose.material3:material3:1.2.0-alpha01")

    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.ui:ui-tooling:1.5.0")
    implementation("androidx.compose.foundation:foundation:1.5.0")
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.material:material-icons-core:1.5.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.navigation:navigation-compose:2.7.2")

    implementation ("io.insert-koin:koin-core:3.3.3")

    // Koin for Android
    implementation ("io.insert-koin:koin-android:3.3.3")

    // Koin Test (if you want to use Koin in tests)
    implementation ("io.insert-koin:koin-test:3.3.3")
    implementation ("io.insert-koin:koin-test-junit4:3.3.3")

    implementation("junit:junit:4.13.2")
    implementation("org.mockito:mockito-core:4.8.0")
    implementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    implementation("org.objenesis:objenesis:3.3")
    implementation("org.mockito:mockito-core:4.8.0")
    implementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    implementation("org.mockito:mockito-core:4.8.0")
    implementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    implementation("androidx.arch.core:core-testing:2.1.0")

    implementation ("androidx.compose.ui:ui-test-junit4:1.5.1")
    implementation ("androidx.compose.ui:ui-test-manifest:1.5.1")





}