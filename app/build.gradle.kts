plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.sefaz_mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sefaz_mobile"
        minSdk = 26
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
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation ("br.pucrio.inf.lac:clientLib:3.0")
    implementation ("br.pucrio.inf.lac:contextnet:2.7")
    implementation ("br.pucrio.inf.lac:ExchangeData:1.0")
    implementation ("org.json:json:20180813")
    implementation ("com.googlecode.json-simple:json-simple:1.1")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.10.0")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.0")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.10.0")
    implementation ("org.slf4j:slf4j-api:2.0.0-alpha1")
    implementation ("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation ("org.apache.kafka:kafka-clients:2.8.0")


}