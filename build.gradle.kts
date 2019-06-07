plugins {
    java
    id("me.champeau.gradle.jmh") version "0.5.0-rc-1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

jmh {
    resultsFile = file("results.txt")
    failOnError = true
    fork = 0
    iterations = 1
    warmupIterations = 1
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.12")
}

version = "0.1"
