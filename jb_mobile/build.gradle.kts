buildscript {
    val scope = this
    GradleConfigs.ProjectRoot.setupRootRepos(scope.repositories)
    GradleConfigs.ProjectRoot.setupClassPathDependencies(scope.dependencies)
}

GradleConfigs.ProjectRoot.setupTasks(tasks,rootProject.buildDir)