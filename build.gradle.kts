plugins {
	id("nebula.dependency-lock") version "12.4.3"
}

subprojects {
	apply(plugin = "nebula.dependency-lock")

	tasks.register<DependencyReportTask>("dependenciesForAll")
}
