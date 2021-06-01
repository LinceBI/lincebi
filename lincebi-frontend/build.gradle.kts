tasks.register<Sync>("build") {
	dependsOn("npmRunBuild")

	from("${projectDir}/packages/login/dist/", {
		into("/Login/")
	})

	from("${projectDir}/packages/home/dist/", {
		into("/Home/")
	})

	into("${buildDir}/")
}

tasks.register<Delete>("clean") {
	delete("${buildDir}/")
	delete("${projectDir}/packages/login/dist/")
	delete("${projectDir}/packages/home/dist/")
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
