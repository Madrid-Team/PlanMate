plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "org.madrid"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // TEST
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // KOIN
    implementation("io.insert-koin:koin-core:3.5.3")
    // JUPITER
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    // TRUTH
    testImplementation ("com.google.truth:truth:1.4.4")
    // MOCKK
    testImplementation("io.mockk:mockk:1.14.0")
    // JSON
    implementation("com.google.code.gson:gson:2.10.1")
    // SERIALIZATION
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}