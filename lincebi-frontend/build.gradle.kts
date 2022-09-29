val version = "${project.property("version")}${project.property("versionSuffix")}"
val gtagId = project.property("gtagId")
val datadogSite = project.property("datadogSite")
val datadogApiKey = project.property("datadogApiKey")
val datadogClientToken = project.property("datadogClientToken")
val datadogMinifiedPathPrefix = project.property("datadogMinifiedPathPrefix")

tasks.register<Sync>("build") {
	if (!File("${projectDir}/packages/login/build/").exists() ||
		!File("${projectDir}/packages/home/build/").exists()
	) {
		dependsOn("pnpmRunBuild")
	}

	from("${projectDir}/packages/login/build/", {
		into("/Login/")
	})

	from("${projectDir}/packages/home/build/", {
		into("/Home/")
	})

	into("${buildDir}/")
}

tasks.register<Delete>("clean") {
	delete("${buildDir}/")
	delete("${projectDir}/packages/login/build/")
	delete("${projectDir}/packages/home/build/")
}

tasks.register<Exec>("pnpmRunBuild") {
	if (!File("${projectDir}/node_modules/").exists() ||
		!File("${projectDir}/packages/login/node_modules/").exists() ||
		!File("${projectDir}/packages/home/node_modules/").exists()
	) {
		dependsOn("pnpmInstall")
	}

	environment(mapOf(
		"VUE_APP_VERSION" to version,
		"VUE_APP_GTAG_ID" to gtagId,
		"VUE_APP_DATADOG_SITE" to datadogSite,
		"VUE_APP_DATADOG_CLIENT_TOKEN" to datadogClientToken
	))

	commandLine("pnpm", "run", "build")

	outputs.upToDateWhen { false }
}

tasks.register<Exec>("pnpxDatadogSourcemapsUpload") {
	if (!File("${projectDir}/node_modules/").exists()) {
		dependsOn("pnpmInstall")
	}

	environment(mapOf(
		"DATADOG_SITE" to datadogSite,
		"DATADOG_API_KEY" to datadogApiKey
	))

	commandLine("pnpx", "@datadog/datadog-ci", "sourcemaps", "upload", "${buildDir}/",
		"--service", "lincebi",
		"--release-version", version.toLowerCase(),
		"--minified-path-prefix", datadogMinifiedPathPrefix)

	outputs.upToDateWhen { false }
}

tasks.register<Exec>("pnpmInstall") {
	commandLine("pnpm", "install")

	outputs.upToDateWhen { false }
}

tasks.withType<AbstractArchiveTask>().configureEach {
	isPreserveFileTimestamps = false
	isReproducibleFileOrder = true
}
