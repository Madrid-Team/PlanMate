plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "2.1.20"
    id("jacoco")
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
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // KOIN
    implementation("io.insert-koin:koin-core:3.5.3")
    // JUPITER
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    // TRUTH
    testImplementation("com.google.truth:truth:1.4.4")
    // MOCKK
    testImplementation("io.mockk:mockk:1.14.0")
    // JSON
    implementation("com.google.code.gson:gson:2.10.1")
    // SERIALIZATION
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    //mongoDB
    implementation(platform("org.mongodb:mongodb-driver-bom:5.4.0"))
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine")
    implementation("org.mongodb:bson-kotlinx:5.4.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}

jacoco {
    toolVersion = "0.8.10"
}

val excludedClasses = listOf(
    "**/di/**",
    "**/data/dto/**",
    "**/data/mapper/**",
    "**/data/utils/**",
    "**/domain/models/**",
    "**/domain/utils/**",
    "**/presentation/Main.kt",
)

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude(excludedClasses)
        }
    )
}

tasks.jacocoTestCoverageVerification {
    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude(excludedClasses)
        }
    )
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}