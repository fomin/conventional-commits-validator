plugins {
    application
    id("errorprone-conventions")
    id("java-conventions")
    id("publish-conventions")
    id("spotless-java-conventions")
}

application {
    mainClass = "io.github.fomin.conventionalcommits.validator.Main"
}

dependencies {
    implementation(project(":validator-jre"))
    implementation("info.picocli:picocli:+")
    annotationProcessor("info.picocli:picocli-codegen:+")
    testImplementation("org.eclipse.jgit:org.eclipse.jgit:+")
}

val versionSourcesDir = layout.buildDirectory.dir("generated-sources/version/java")

val generateVersionJavaFile by tasks.registering {
    inputs.property("version", rootProject.version)
    outputs.dir(versionSourcesDir)
    doLast {
        val fileDir = versionSourcesDir.get().file("io/github/fomin/conventionalcommits/validator").asFile
        fileDir.mkdirs()
        val file = fileDir.resolve("Version.java")
        file.writeText(
            """
            package io.github.fomin.conventionalcommits.validator;

            public final class Version {
              private Version() {}

              public static final String VERSION = "${rootProject.version}";
            }
            
            """.trimIndent()
        )
    }
}

tasks.compileJava {
    dependsOn(generateVersionJavaFile)
}

tasks.spotlessJava {
    dependsOn(generateVersionJavaFile)
}

tasks.sourcesJar {
    dependsOn(generateVersionJavaFile)
}

sourceSets {
    main {
        java {
            srcDir(versionSourcesDir)
        }
    }
}
