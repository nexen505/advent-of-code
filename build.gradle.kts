plugins {
    kotlin("jvm") version "2.2.21"
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.20.1"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
