plugins {
    kotlin("jvm") version "1.8.0"
    // java-library instead of java is the key to publish primary jar of kotlin codes.
    `java-library`
    `maven-publish`
    signing
}

group = "io.github.linhelurking"
version = "0.1.3"

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

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            pom {
                name.set("kt-snbt")
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

                // Must provide scm information. Otherwise, POM validation fails.
                scm {
                    connection.set("git@github.com:LinHeLurking/SNBT.KT.git")
                    developerConnection.set("git@github.com:LinHeLurking/SNBT.KT.git")
                    url.set("https://github.com/LinHeLurking/SNBT.KT")
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
    // This is correct. Sign for `configurations.archives` won't work.
    publishing.publications.forEach {
        sign(it)
    }
}