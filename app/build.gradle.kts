import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// local.properties 파일을 읽어오기 위한 객체 생성
val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.jgs.socketiochat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jgs.socketiochat"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // local.properties에 저장된 키값을 buildConfig에 type과 변수명을 입력해서 가져옴
        buildConfigField("String", "WITHCORN_URL", localProperties.getProperty("withcorn_url"))
        buildConfigField("String", "WITHCORN_CHAT_URL", localProperties.getProperty("withcorn_chat_url"))
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Socket.io
    implementation("io.socket:socket.io-client:2.0.0") {
        exclude ("org.json", "json")
    }

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide
    implementation("com.github.skydoves:landscapist-glide:1.4.7")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}