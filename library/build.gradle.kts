plugins {
    id("com.veepee.kotlin.android.library")
    id("com.veepee.compose.android.library")
    id("kotlin-parcelize")
    id("com.veepee.publishing.android.library")
}

android {
    namespace = "com.veepee.vpcore.link.route"

    buildTypes.getByName("release").isMinifyEnabled = false
}


dependencies {
    implementation(libs.lang.kotlin)

    implementation(libs.support.androidcorektx)
    implementation(libs.support.fragment)
    implementation(libs.compose.foundation)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.support.core)
    testImplementation(libs.test.compose)
    testImplementation(libs.mockito.kotlin)

}

