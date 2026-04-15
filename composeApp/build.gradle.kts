import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    android {
        compileSdk = 37
        minSdk = 26
        namespace = "com.pl.myworkoutapp"
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            // Added uiTooling dependency to fix ClassNotFoundException: androidx.compose.ui.tooling.ComposeViewAdapter
            // This is required for Android Studio to render Composable previews in this module.
            implementation(libs.compose.uiTooling)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                api(libs.koin.core)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.jetbrains.compose.navigation)
                //implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.markdown)
                implementation(libs.compose.markdown.material3)

                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
                implementation(libs.kotlinx.datetime)
            }
            kotlin.srcDir(
                layout.buildDirectory.dir("generated/source/appConfig")
            )
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.webcam.capture)
            implementation(libs.slf4j.simple)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.pl.myworkoutapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.pl.myworkoutapp"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(file("src/commonMain/composeResources/drawable/app_icon.png"))
            }
        }
    }
}

//compose.resources {
//    publicResClass = true
//}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    kspAndroid(libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    dependsOn(tasks.named("generateAppConfig"))
//}
//
//fun _runGitCommand(vararg args: String): String {
//    return try {
//        val process = ProcessBuilder("git", *args)
//            .redirectErrorStream(true)
//            .start()
//        process.inputStream.bufferedReader().use { it.readText().trim() }
//    } catch (e: Exception) {
//        ""
//    }
//}
//fun _gitCommitHash(): String =
//    _runGitCommand("rev-parse", "--short=8", "HEAD").ifBlank { "nogit" }
//
//fun _gitCommitEpoch(): String =
//    _runGitCommand("log", "-1", "--format=%ct").ifBlank { "0" }
//
//tasks.register("generateAppConfig") {
////    inputs.property("gitHash", _gitCommitHash())
////    inputs.property("gitEpoch", _gitCommitEpoch())
//
//    val outputDir = layout.buildDirectory.dir("generated/source/appConfig")
//
//    outputs.dir(outputDir)
//
//    doLast {
//        val hash = _gitCommitHash()
//        val epoch = _gitCommitEpoch()
//
//        val file = outputDir.get().file("AppConfig.kt").asFile
//        file.parentFile.mkdirs()
//        file.writeText("""
//            package com.pl.myworkoutapp
//
//            object AppConfig {
//                const val API_URL = "https://api.example.com"
//                const val IS_DEBUG = true
//                const val VERSION_HASH = "$hash"
//                const val VERSION_EPOCH = $epoch
//            }
//        """.trimIndent())
//    }
//}

abstract class GenerateAppConfigTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    abstract val gitHash: Property<String>

    @get:Input
    abstract val gitEpoch: Property<String>

    @TaskAction
    fun generate() {
        val hash = gitHash.get()
        val epoch = gitEpoch.get()

        val file = outputDir.get().file("AppConfig.kt").asFile
        file.parentFile.mkdirs()

        file.writeText("""
            package com.pl.myworkoutapp

            object AppConfig {
                const val API_URL = "https://api.example.com"
                const val VERSION_HASH = "$hash"
                const val VERSION_EPOCH = $epoch
            }
        """.trimIndent())
    }
}

val generateAppConfig = tasks.register<GenerateAppConfigTask>("generateAppConfig") {
    outputDir.set(layout.buildDirectory.dir("generated/source/appConfig"))
    //outputs.upToDateWhen { false }
    gitHash.set(
        providers.exec {
            commandLine("git", "rev-parse", "--short=8", "HEAD")
        }.standardOutput.asText.map { it.trim() }.orElse("nogit")
    )
    gitEpoch.set(
        providers.exec {
            commandLine("git", "log", "-1", "--format=%ct")
        }.standardOutput.asText.map { it.trim() }.orElse("0")
    )
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(generateAppConfig)
}
tasks.withType<com.google.devtools.ksp.gradle.KspAATask>().configureEach {
    dependsOn(generateAppConfig)
}