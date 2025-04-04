plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.veepee.sample"

    defaultConfig {
        minSdk = 21
        targetSdk = 35
        compileSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    testImplementation(libs.test.junit)

    implementation(project(":feature_a"))
    implementation(project(":feature_b"))
    implementation(project(":login"))
    implementation(project(":routes"))
    implementation(libs.androidx.design)
    implementation(libs.androidx.compose.material)
}
