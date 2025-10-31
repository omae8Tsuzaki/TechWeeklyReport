plugins {
    id("java")
    id ("org.springframework.boot") version ("3.5.7")
    id ("io.spring.dependency-management") version ("1.1.5")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("com.rometools:rome:2.1.0")
    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}