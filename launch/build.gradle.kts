import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.spongepowered.org/maven/")
    }
    maven {
        url = uri("https://files.minecraftforge.net/maven/")
    }
}

dependencies {
    implementation(project(":eteryun-api"))

    compileOnly("org.checkerframework:checker-qual:3.21.4")

    implementation("org.apache.logging.log4j:log4j-core:2.17.2")

    implementation("com.google.guava:guava:31.1-jre") { // 21.0 -> 22.0
        exclude(group = "com.google.code.findbugs", module = "jsr305")
    }

    implementation("org.ow2.asm:asm:9.2")
    implementation("org.ow2.asm:asm-commons:9.2")
    implementation("org.ow2.asm:asm-analysis:9.2")
    implementation("org.ow2.asm:asm-tree:9.2")
    implementation("org.ow2.asm:asm-util:9.2")

    implementation("org.spongepowered:mixin:0.8.5")

    implementation("cpw.mods:modlauncher:8.1.3") {
        exclude(group = "com.google.code.findbugs", module = "jsr305")
    }

    implementation("cpw.mods:modlauncher:8.1.3:api") {
        exclude(group = "com.google.code.findbugs", module = "jsr305")
    }

    implementation("cpw.mods:grossjava9hacks:1.3.3")

    implementation("com.google.code.gson:gson:2.8.9")
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "com.eteryun.launch.agent.Agent",
            "Agent-Class" to "com.eteryun.launch.agent.Agent",
            "Launcher-Agent-Class" to "com.eteryun.launch.agent.Agent",
            "Main-Class" to "com.eteryun.launch.EteryunBootstrap",
            "Multi-Release" to true
        )

        attributes(
            "com/eteryun/launch/",
            "Specification-Title" to project.name,
            "Specification-Vendor" to "Eteryun",
            "Specification-Version" to 1.0,
            "Implementation-Title" to project.name,
            "Implementation-Version" to rootProject.version.toString(),
            "Implementation-Vendor" to "Eteryun"
        )

        from({
            zipTree { configurations.runtimeClasspath.get().files.find { entry -> entry.name.contains("modlauncher") } }.matching {
                include(
                    "**/MANIFEST.MF"
                )
            }.singleFile
        })
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(project.name)
    archiveClassifier.set("")
    archiveVersion.set(rootProject.version.toString())

    mergeServiceFiles()

    from(tasks.jar)

    exclude("META-INF/versions/*/module-info.class")
    exclude("module-info.class")

    exclude("com/google/common/escape/*")
    exclude("com/google/common/eventbus/*")
    exclude("com/google/common/html/*")
    exclude("com/google/common/net/*")
    exclude("com/google/common/xml/*")
    exclude("com/google/thirdparty/**")

    dependencies {
        // Checkerframework
        exclude(dependency("org.checkerframework:checker-qual"))

        // Google
        exclude(dependency("com.google.errorprone:error_prone_annotations"))
        exclude(dependency("com.google.j2objc:j2objc-annotations"))
    }
}