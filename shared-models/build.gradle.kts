import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    //id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.21"
    kotlin("jvm") version "1.5.30-M1"
}

group = "ru.tinkoff"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    testImplementation(kotlin("test-junit"))
    implementation(kotlin("stdlib-jdk8"))
}


tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}