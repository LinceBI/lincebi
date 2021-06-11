tasks.register<Sync>("build") {
	if (!File("${projectDir}/packages/login/build/").exists() ||
		!File("${projectDir}/packages/home/build/").exists()
	) {
		dependsOn("npmRunBuild")
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

tasks.register<Exec>("npmRunBuild") {
	dependsOn("npmInstall")

	commandLine("npm", "run", "build")

	outputs.upToDateWhen { false }
}

tasks.register<Exec>("npmInstall") {
	commandLine("npm", "install")

	outputs.upToDateWhen { false }
}
