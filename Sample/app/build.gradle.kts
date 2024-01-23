plugins {
    id("com.veepee.kotlin.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.veepee.sample"
}

dependencies {
    testImplementation(libs.test.junit)

    implementation(project(":feature_a"))
    implementation(project(":feature_b"))
    implementation(project(":login"))
    implementation(project(":routes"))
    implementation(libs.support.design)
    implementation(libs.compose.material)
}
