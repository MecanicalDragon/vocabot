plugins {
    // JVM support
    kotlin("jvm") version "1.6.20"
    application

    // Spring support
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version "1.6.20"
    kotlin("plugin.jpa") version "1.6.20"
    kotlin("plugin.allopen") version "1.6.20"

    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
//    kotlin("kapt") version "1.6.20"
}

group = "net.medrag"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.20")
//    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // makes @ConfigurationProperties work
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.vladmihalcea:hibernate-types-52:2.16.2")

    implementation("org.postgresql:postgresql:42.3.4")
    implementation("org.flywaydb:flyway-core:8.5.8")

    implementation("org.telegram:telegrambots:6.0.1")
    implementation("org.telegram:telegrambotsextensions:6.0.1")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("net.medrag.vocabot.VocaBotAppKt")
}

allOpen {
    annotation("org.springframework.boot.context.properties.ConfigurationProperties")
    // annotations("com.another.Annotation", "com.third.Annotation")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.named("build") {
    doLast {
        copy {
            from(layout.buildDirectory.dir("libs"))
            into(layout.buildDirectory.dir("lib"))
        }
        delete(fileTree(layout.projectDirectory.dir("build")).exclude("bootScripts", "lib"))
    }
}
