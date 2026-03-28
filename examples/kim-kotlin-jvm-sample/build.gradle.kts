plugins {
    kotlin("jvm") version "2.0.0"
}

group = "de.stefan-oltmann"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("de.stefan-oltmann:kim:0.27.0")
}
