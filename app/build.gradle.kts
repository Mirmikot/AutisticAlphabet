plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "com.example.autikids"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.autikids"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		vectorDrawables.useSupportLibrary = true
	}

	// Signing config: reads from Gradle properties or environment variables
	signingConfigs {
		create("release") {
			val releaseStoreFilePath: String? = (project.findProperty("RELEASE_STORE_FILE") as String?)
				?: System.getenv("RELEASE_STORE_FILE")
			val releaseStorePassword: String? = (project.findProperty("RELEASE_STORE_PASSWORD") as String?)
				?: System.getenv("RELEASE_STORE_PASSWORD")
			val releaseKeyAlias: String? = (project.findProperty("RELEASE_KEY_ALIAS") as String?)
				?: System.getenv("RELEASE_KEY_ALIAS")
			val releaseKeyPassword: String? = (project.findProperty("RELEASE_KEY_PASSWORD") as String?)
				?: System.getenv("RELEASE_KEY_PASSWORD")

			if (!releaseStoreFilePath.isNullOrBlank() && !releaseStorePassword.isNullOrBlank() && !releaseKeyAlias.isNullOrBlank() && !releaseKeyPassword.isNullOrBlank()) {
				storeFile = file(releaseStoreFilePath)
				this.storePassword = releaseStorePassword
				this.keyAlias = releaseKeyAlias
				this.keyPassword = releaseKeyPassword
			}
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			signingConfig = signingConfigs.getByName("release")
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlin {
		jvmToolchain(17)
	}

	buildFeatures { compose = true }

	composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }

	packaging {
		resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
	}
}

dependencies {
	implementation(platform("androidx.compose:compose-bom:2024.09.03"))
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
	implementation("androidx.activity:activity-compose:1.9.3")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3:1.3.0")
	implementation("io.coil-kt:coil-compose:2.6.0")
	implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")
	debugImplementation("androidx.compose.ui:ui-tooling")
}
