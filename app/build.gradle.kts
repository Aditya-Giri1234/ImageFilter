plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    kotlin("kapt")
    id ("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.aditya.imagefilter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aditya.imagefilter"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        release {
            isMinifyEnabled = true
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
    // Allow references to generated code
    kapt {
        correctErrorTypes=true
    }


    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // Scalable size unit
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.1.0")

    //rounded image
    implementation ("com.makeramen:roundedimageview:2.3.0")

    //Gpu image
    implementation ("jp.co.cyberagent.android:gpuimage:2.1.0")

    dependencies {
        implementation("com.google.dagger:hilt-android:2.50")
        kapt("com.google.dagger:hilt-android-compiler:2.50")
    }



    //ViewModel
    val lifecycle_version = "2.7.0"
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    kapt ("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")


    // Navigation Component
    val nav_version = "2.7.5"

    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")





}