plugins {
    // JVM support
    kotlin("jvm") version "1.8.21"
    application

    // Spring support
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.jpa") version "1.8.21"
    kotlin("plugin.allopen") version "1.8.21"
    kotlin("kapt") version "1.8.21"

    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

group = "net.medrag"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // makes @ConfigurationProperties work
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.vladmihalcea:hibernate-types-60:2.21.1")

    implementation("org.postgresql:postgresql:42.5.4")
    implementation("org.flywaydb:flyway-core:9.16.0")

    implementation("org.telegram:telegrambots:6.5.0")
    implementation("org.telegram:telegrambotsextensions:6.5.0")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("net.medrag.vocabot.VocaBotAppKt")
}

allOpen {
    annotation("org.springframework.boot.context.properties.ConfigurationProperties")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "17"
    }
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
