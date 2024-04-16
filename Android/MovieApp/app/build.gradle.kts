plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.movieapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.movieapp"
        minSdk = 26
        targetSdk = 33
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
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("androidx.activity:activity:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.android.material:material:1.11.0")

    implementation("com.makeramen:roundedimageview:2.3.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0-alpha01")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0-alpha01")

    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation ("com.auth0:java-jwt:3.18.2")

    //border radius image
    implementation("com.github.bumptech.glide:glide:4.12.0")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //retrofit
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")


    implementation("com.squareup.okhttp3:okhttp:3.12.1")
    implementation("com.squareup.okhttp3:logging-interceptor:3.6.0")


    implementation("androidx.fragment:fragment-ktx:1.4.0")


    //zalo pay
    implementation(files("libs/zpdk-release-v3.1.aar"))
    implementation("commons-codec:commons-codec:1.14")
    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Google ads
    implementation ("com.google.android.gms:play-services-ads:23.0.0")
}
