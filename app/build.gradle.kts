plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.a2026_ii_ejercicio7_retrofit"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.a2026_ii_ejercicio7_retrofit"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // View Binding: genera clases para acceder a las vistas sin findViewById
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)

    // ViewModel + LiveData (viewModelScope para las corrutinas)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Retrofit con el convertidor Gson (JSON -> objetos de Kotlin)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Glide para leer las imágenes desde Internet
    implementation(libs.glide)
}
