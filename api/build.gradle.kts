plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.checkerframework:checker-qual:3.21.4")

    api("org.apache.logging.log4j:log4j-core:2.17.2")

    api("org.ow2.asm:asm-tree:9.2")

    api("com.google.code.gson:gson:2.8.9")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.eteryun"
            artifactId = "eteryun-api"
            version = "1.0"

            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://repo.repsy.io/mvn/eteryun/eteryun")
            credentials() {
                username = "USERNAME"
                password = "PASSWORD"
            }
        }
    }
}