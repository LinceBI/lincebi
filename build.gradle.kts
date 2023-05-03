plugins {
	id("com.netflix.nebula.dependency-lock") version "13.2.2"
}

subprojects {
	apply(plugin = "com.netflix.nebula.dependency-lock")

	tasks.register<DependencyReportTask>("dependenciesForAll")
}
