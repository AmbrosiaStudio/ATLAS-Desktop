plugins {
    kotlin("jvm") version "2.4.20"
    id("org.jetbrains.compose") version "1.12.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.20"
}

group = "ir.mrmabrosia.atlas"
version = "0.2.0"

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.foundation)
    implementation(compose.ui)
    implementation(compose.runtime)

    implementation("org.xerial:sqlite-jdbc:3.50.3.0")

    testImplementation(kotlin("test"))
}

compose.desktop {
    application {
        mainClass = "ir.mrmabrosia.atlas.MainKt"

        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi)
            packageName = "ATLAS"
            packageVersion = project.version.toString()
            description = "Your world of knowledge"
            vendor = "Ambrosia Studio"

            windows {
                menuGroup = "Ambrosia Studio"
                upgradeUuid = "9B5C9B2B-5D0A-4F85-9A44-0D45B4A21C71"
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
