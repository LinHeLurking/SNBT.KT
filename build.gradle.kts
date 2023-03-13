plugins {
    kotlin("jvm") version "1.8.0"
    `maven-publish`
    signing
}

group = "io.github.linhelurking"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("snbt") {
            pom {
                name.set("snbt")
                description.set("A SNBT file reader for Kotlin language")
                url.set("https://github.com/LinHeLurking/SNBT.KT")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://spdx.org/licenses/MIT.html")
                    }
                }

                developers {
                    developer {
                        id.set("LinHeLurking")
                        name.set("LinHe")
                        email.set("LinHe.Lurking@gmail.com")
                    }
                }
            }
            from(components["java"])
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = properties["ossrhUsername"] as String?
                password = properties["ossrhPassword"] as String?
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(configurations.archives.get())
}