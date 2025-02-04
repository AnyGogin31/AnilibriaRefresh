plugins {
    alias(libs.plugins.xbot.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.baselineprofile)
}

dependencies {
    // Project-level dependencies
    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.media)
    implementation(projects.anilibriaApi)
    baselineProfile(projects.baselineprofile)

    // Koin dependencies
    implementation(platform(libs.koinBom))
    implementation(libs.koinCore)
    implementation(libs.koinAndroid)
    implementation(libs.koinAndroidCompose)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    // AndroidX dependencies
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.interpolator)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.navigation)
    implementation(libs.androidx.profileinstaller)

    // Compose dependencies
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.google.fonts)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation("com.github.skydoves:cloudy:0.2.3")
    implementation(libs.haze.compose)
    implementation(libs.haze.compose.materials)
    implementation(libs.material.motion.compose.core)
    implementation(libs.shimmer.compose)

    // Jetpack Media 3 dependencies
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.media3.session)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.compose.ui.test)

    // Debug dependencies
    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

android {
    namespace = "com.xbot.anilibriarefresh"
}
