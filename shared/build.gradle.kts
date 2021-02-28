import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    //id("org.jetbrains.kotlin.native.cocoapods")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    ios()
    /**
     * Required for working iOS dependencies
     * From: https://github.com/cashapp/sqldelight/issues/2044
     */
//    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
//    if (onPhone) {
//        iosArm64("ios")
//    } else {
//        iosX64("ios")
//    }

    sourceSets {

        /**
         * Common
         */

        val commonMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:runtime:${Dependencies.SQLDELIGHT}")
            }
        }

        /**
         * Platforms
         */

        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
                implementation("com.squareup.sqldelight:android-driver:${Dependencies.SQLDELIGHT}")
                implementation("net.zetetic:android-database-sqlcipher:4.4.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:${Dependencies.SQLDELIGHT}")
                implementation("co.touchlab:sqliter:0.7.1") {
                    version {
                        strictly("0.7.1")
                    }
                }
            }
        }

        /**
         * Tests
         */

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val iosTest by getting
    }

    cocoapods {
        homepage = "https://elevenetc.com"
        summary = "Zipher project"
        setVersion("0.0.1")
        frameworkName = "shared"
        ios.deploymentTarget = "13.5"
        //pod("AFNetworking", "~> 4.0.1")
        //pod("SQLCipher", "~> 4.0")
    }

    targets.withType<KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            isStatic = true
            //linkerOpts.add("-lsqlite3")
        }
    }
}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
}

val packForXcode by tasks.creating(Sync::class) {

    val logPrefix = "packForXcode"

    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val binaries = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries
    binaries.forEach {
        val buildType = it.buildType
        val name = it.baseName
        val debuggable = it.debuggable
        val targetName = it.target.name
        val outputKind = it.outputKind
        val targetPlatformType = it.target.platformType
        println("$logPrefix: framework-binary: $outputKind, $targetPlatformType, $targetName, $buildType, $name, $debuggable")
    }
    val framework = binaries.getFramework(mode)

    println("$logPrefix: isStatic: " + framework.isStatic)

    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)

sqldelight {
    database("AppDatabase") {
        packageName = "com.elevenetc.zipher"
        linkSqlite = false
    }
}