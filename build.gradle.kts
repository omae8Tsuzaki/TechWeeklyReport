plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.json)
    implementation(platform(libs.spring.boot.bom))
    testImplementation(libs.spring.boot.starter.test)
    annotationProcessor (libs.spring.boot.configuration.processor)

    //Jackson
    implementation(libs.jackson.databind)
    // Rome
    implementation(libs.rome)

    // Junit
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}