plugins {
    id("java")
    id("io.github.rancraftplayz.remapper") version("1.0.2")
}

group = "com.eteryun"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/maven") }
    maven { url = uri("https://maven.elmakers.com/repository/") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    implementation(project(":eteryun-api"))

    remapLib("org.spigotmc:spigot:1.18.2-R0.1-SNAPSHOT:remapped-mojang")
    implementation("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    implementation("org.spongepowered:mixin:0.8.5")
}

spigot {
    version = "1.18.2"
}

tasks.remapJar {
    dependsOn("jar")
}