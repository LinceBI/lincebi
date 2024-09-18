plugins {
	id("java")
	id("com.gradleup.shadow") version "8.3.0"
}

group = "${project.property("group")}"
version = "${project.property("version")}${project.property("versionSuffix")}"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://repo.stratebi.com/repository/pentaho-mvn/") }
}

dependencies {
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.0")
	implementation("com.fasterxml.jackson.core:jackson-core:2.16.0")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
	implementation("com.microsoft.azure:msal4j:1.17.1")
	implementation("com.networknt:json-schema-validator:1.4.0")
	implementation("commons-io:commons-io:2.16.1")
	implementation("jakarta.ws.rs:jakarta.ws.rs-api:2.1.6")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
	implementation("org.apache.commons:commons-lang3:3.17.0")
	implementation("org.codehaus.enunciate:enunciate-core-annotations:1.31")
	implementation("org.ehcache:ehcache:3.10.8")
	implementation("org.slf4j:slf4j-reload4j:2.0.16")
	implementation("org.springframework:spring-web:5.3.29")
	implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")

	compileOnly("javax.jcr:jcr:2.0") { isTransitive = false }
	compileOnly("org.apache.jackrabbit:jackrabbit-core:2.21.19") { isTransitive = false }
	compileOnly("org.apache.jackrabbit:jackrabbit-data:2.21.19") { isTransitive = false }
	compileOnly("org.apache.jackrabbit:jackrabbit-jcr-commons:2.21.19") { isTransitive = false }
	compileOnly("org.apache.jackrabbit:oak-jackrabbit-api:1.48.0") { isTransitive = false }
	compileOnly("org.pentaho:commons-database-model:9.3.0.9-878") { isTransitive = false }
	compileOnly("org.pentaho:pentaho-metadata:9.3.0.9-878") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-api:9.3.0.9-878") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-core:9.3.0.9-878") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-extensions:9.3.0.9-878") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-repository:9.3.0.9-878") { isTransitive = false }

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}

tasks.build {
	dependsOn("shadowJar")
}

tasks.jar {
	manifest {
		attributes(
			"Implementation-Version" to version
		)
	}
}

tasks.shadowJar {
	archiveClassifier.set("bundle")
	mergeServiceFiles()

	val prefix = "${project.property("group")}.shaded"
	fun shade(path : String) { relocate(path, "${prefix}.${path}") }

	shade("com.ethlo")
	shade("com.fasterxml")
	shade("com.microsoft")
	shade("com.networknt")
	shade("com.nimbusds")
	shade("com.sun.istack")
	shade("com.sun.xml")
	shade("javassist")
	shade("net.jcip")
	shade("net.minidev")
	shade("ognl")
	shade("org.apache.commons")
	shade("org.apache.log4j")
	shade("org.attoparser")
	shade("org.codehaus")
	shade("org.ehcache")
	shade("org.jvnet")
	shade("org.objectweb")
	shade("org.springframework")
	shade("org.thymeleaf")
	shade("org.unbescape")
	shade("org.yaml")
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<AbstractArchiveTask>().configureEach {
	isPreserveFileTimestamps = false
	isReproducibleFileOrder = true
}
