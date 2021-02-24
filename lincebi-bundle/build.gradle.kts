plugins {
	id("maven-publish")
}

tasks.register<Zip>("build") {
	dependsOn(project(":${rootProject.name}-backend").tasks.named("build"))
	dependsOn(project(":${rootProject.name}-frontend").tasks.named("build"))

	archiveFileName.set("${rootProject.name}.zip")
	destinationDirectory.set(file("${buildDir}/"))

	from("${projectDir}/src/", {
		into("/")
	})

	from("${project(":${rootProject.name}-backend").buildDir}/libs/", {
		into("/pentaho-solutions/system/lincebi/lib/")
	})

	from("${project(":${rootProject.name}-frontend").buildDir}/", {
		into("/tomcat/webapps/pentaho/")
	})
}

tasks.register<Delete>("clean") {
	delete("${buildDir}/")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "${project.property("group")}"
			artifactId = "${project.property("artifact")}"
			version = "${project.property("version")}${project.property("versionSuffix")}"
			artifact("${buildDir}/${rootProject.name}.zip") {
				extension = "zip"
				builtBy(tasks.named("build"))
			}
		}
	}

	repositories {
		maven {
			val releasesRepoUrl = "https://repo.stratebi.com/repository/lincebi-mvn-releases/"
			val snapshotsRepoUrl = "https://repo.stratebi.com/repository/lincebi-mvn-snapshots/"
			url = uri(if ("${project.property("versionSuffix")}".endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
			credentials {
				username = System.getenv("REPO_MAVEN_LINCEBI_RW_USER") ?: ""
				password = System.getenv("REPO_MAVEN_LINCEBI_RW_PASSWORD")?: ""
			}
		}
	}
}
