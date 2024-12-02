plugins {
    kotlin("jvm") version "2.1.0"
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.18.0"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks {
    wrapper {
        gradleVersion = "8.10.2"
    }
}
