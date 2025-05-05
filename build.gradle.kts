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
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
kotlin {
    jvmToolchain(8)
}

jacoco {
    toolVersion = "0.8.10"
}
val filteredCoverage = fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
    include("**/data/repository/**")
    include("**/data/source/**")
    include("**/domain/usecases/**")
    include("**/presentation/**")

    exclude("**/data/dto/**")
    exclude("**/data/mapper/**")
    exclude("**/di/**")
    exclude("**/domain/models/**")
    exclude("**/domain/repository/**")
    exclude("**/utils/**")
    exclude("**/*Dto.class")
    exclude("**/*Mapper.class")
    exclude("**/*Module.class")
    exclude("**/BuildConfig.*")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(filteredCoverage)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    classDirectories.setFrom(filteredCoverage)
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}
