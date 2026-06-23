plugins {
	id("com.netflix.nebula.dependency-lock") version "17.1.0"
}

subprojects {
	apply(plugin = "com.netflix.nebula.dependency-lock")

	tasks.register<DependencyReportTask>("dependenciesForAll")
}
