import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.screenshot)
}

val appSettings = Properties().apply {
    val file = rootProject.file("claramente.properties")
    if (file.isFile) file.inputStream().use { load(it) }
}

fun setting(name: String, default: String): String =
    (project.findProperty(name) as String?) ?: appSettings.getProperty(name) ?: System.getenv(name) ?: default

val signingProperties = Properties().apply {
    val path = System.getenv("CLARAMENTE_SIGNING_PROPERTIES")
        ?: "${System.getProperty("user.home")}/.claramente-signing/keystore.properties"
    val file = File(path)
    if (file.isFile) file.inputStream().use { load(it) }
}

android {
    namespace = "com.claramente"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.claramente"
        minSdk = 24
        targetSdk = 36
        versionCode = 4
        versionName = "1.1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val mockAuth = setting("CLARAMENTE_MOCK_AUTH", "true").trim().let { it.equals("true", ignoreCase = true) || it == "1" }
        buildConfigField("String", "API_BASE_URL", "\"${setting("CLARAMENTE_API_URL", "http://10.0.2.2:5090")}\"")
        buildConfigField("String", "AR_WEB_URL", "\"${setting("CLARAMENTE_AR_WEB_URL", "")}\"")
        buildConfigField("boolean", "MOCK_AUTH", mockAuth.toString())
    }

    signingConfigs {
        if (signingProperties.getProperty("storeFile") != null) {
            create("release") {
                storeFile = file(signingProperties.getProperty("storeFile"))
                storePassword = signingProperties.getProperty("storePassword")
                keyAlias = signingProperties.getProperty("keyAlias")
                keyPassword = signingProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            ndk {
                abiFilters += listOf("arm64-v8a", "x86_64")
            }
        }
        release {
            ndk {
                abiFilters += listOf("arm64-v8a")
            }
            signingConfigs.findByName("release")?.let { signingConfig = it }
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:session"))
    implementation(project(":feature:login"))
    implementation(project(":feature:hub"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:ar"))
    implementation(project(":feature:classroom"))
    implementation(project(":feature:teacher"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.browser)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.okhttp)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    screenshotTestImplementation(platform(libs.androidx.compose.bom))
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)
    screenshotTestImplementation(libs.screenshot.validation.api)
}
