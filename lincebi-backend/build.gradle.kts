plugins {
	id("java-library")
}

group = "${project.property("group")}"
version = "${project.property("version")}${project.property("versionSuffix")}"

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	maven { url = uri("https://repo.stratebi.com/repository/pentaho-mvn/") }
}

dependencies {
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
	implementation("com.microsoft.azure:msal4j:1.11.0")
	implementation("com.networknt:json-schema-validator:1.0.61")
	implementation("commons-io:commons-io:2.11.0")
	implementation("jakarta.ws.rs:jakarta.ws.rs-api:2.1.6")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("org.codehaus.enunciate:enunciate-core-annotations:1.31")
	implementation("org.ehcache:ehcache:3.9.6")
	implementation("org.slf4j:slf4j-log4j12:1.7.32")
	implementation("org.springframework:spring-web:5.3.10")
	implementation("org.thymeleaf:thymeleaf:3.0.12.RELEASE")

	compileOnly("org.pentaho:commons-database-model:8.3.0.23-1295") { isTransitive = false }
	compileOnly("org.pentaho:pentaho-metadata:8.3.0.23-1295") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-api:8.3.0.23-1295") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-core:8.3.0.23-1295") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-extensions:8.3.0.23-1295") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-repository:8.3.0.23-1295") { isTransitive = false }

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.build {
	dependsOn("copyDependencies")
}

tasks.register<Copy>("copyDependencies") {
	from(configurations.runtimeClasspath)

	into("${buildDir}/libs/")
}

tasks.test {
	useJUnitPlatform()
}
