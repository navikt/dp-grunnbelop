group = "no.nav.dagpenger"

val artifactDescription = "Libraries for Dagpenger"

plugins {
    id("common")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

val cucumberVersion = "7.34.3"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.3")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")
    testImplementation("com.approvaltests:approvaltests:26.7.1")
    testImplementation("org.junit.platform:junit-platform-suite:1.12.2")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-java8:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.3")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    add("archives", sourcesJar)
}

val githubUser: String? by project
val githubPassword: String? by project

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/dp-grunnbelop")
            credentials {
                username = githubUser
                password = githubPassword
            }
        }
    }
    publications {
        create<MavenPublication>("github") {
            from(components["java"])
            artifact(sourcesJar.get())

            pom {
                description.set(artifactDescription)
                name.set(project.name)
                url.set("https://github.com/navikt/dp-grunnbelop")
                withXml {
                    asNode().appendNode("packaging", "jar")
                }
                licenses {
                    license {
                        name.set("MIT License")
                        name.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        organization.set("NAV (Arbeids- og velferdsdirektoratet) - The Norwegian Labour and Welfare Administration")
                        organizationUrl.set("https://www.nav.no")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/navikt/dp-grunnbelop.git")
                    developerConnection.set("scm:git:https://github.com/navikt/dp-grunnbelop.git")
                    url.set("https://github.com/navikt/dp-grunnbelop")
                }
            }
        }
    }
}
