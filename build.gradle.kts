plugins {
    kotlin("multiplatform") version "1.4.31"
}

group = "nl.avwie"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "12"
        }
    }

    js("frontend", IR) {
        browser {
            binaries.executable()
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val frontendMain by getting
        val frontendTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
