plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
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

	composeOptions { kotlinCompilerExtensionVersion = libs.versions.composeCompilerExtension.get() }

	packaging {
		resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
	}
}

dependencies {
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.coil.compose)
	implementation(libs.accompanist.systemuicontroller)
	debugImplementation(libs.androidx.compose.ui.tooling)
}
