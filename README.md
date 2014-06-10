gradle-jenkins-jobdsl
=====================

Gradle plugin that lets you compile/verify your Jenkins JobDSL scripts. Based on the setup done by [sheehan/job-dsl-gradle-example](https://github.com/sheehan/job-dsl-gradle-example)

It supports:

- Single and multiple JobDSL scripts
- Groovy support classes that can be used in the JobDSL scripts

It works in two steps:

1. Gather all resources (JobDSL scripts, support classes, whatever you say it needs)
  and places them together in
  `${project.buildDir}/jobDsl/workspace`
2. From the workspace path, execute the JobDSL scripts and place the generated
  Jenkins config xml files in
  `${project.buildDir}/jobDsl/generated`

With this, configuration of the JobDSL seed job in Jenkins can be done as following:

- Create a freeform Job
- Add your project to the SCM/Version-Control, so the project gets checked out
- Add a Gradle builder that executes the `prepareJobDslWorkspace` task
- Add the JobDSL builder that is set to the following path:
  `${project.buildDir}/jobDsl/workspace`
  So when your Gradle buildDir is kept at the default, the workspace path will be:
  `build/jobDsl/workspace`

Adding the plugin
-----------------

Add the following lines to your build to use the `gradle-jenkins-jobdsl` plugin.

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'nl.ikoodi.gradle.plugin:gradle-jenkins-jobdsl:<version>'
    }
}

apply plugin: 'jenkins-jobdsl'
```

Using the plugin in Gradle
--------------------------

### Basic usage, single job config file

When you have a single job config in the root of your project and want to verify
that it compiles to the wanted Jenkins config xml file before you commit it,
you can use the following configuration in your gradle build file:

```groovy
jenkinsJobDsl {
    jobConfigs {
        from file('project.dsl.groovy')
    }
}
```

### Usage with multiple job config files and groovy classes

When you have one repository containing multiple job config files and
optionally support classes that can be used in the job configs, with
for example the following project structure:

    .
    ├── jobs              # DSL script files
    ├── src
    │   └── main
    │       └── groovy    # helper/convention/support classes
    │   └── test
    │       └── groovy    # specs
    └── build.gradle      # build file

You can use the following configuration in your gradle build file:

```groovy
jenkinsJobDsl {
    jobConfigs {
        from fileTree(dir: 'jobs')
    }
    classpath {
        from sourceSets.main.groovy
    }
}
```

This setup is an example of a seed job repository for team/project/company wide jobs set-up.
Or it can be a repository that contains helper/convention/support classes to be used by others
in their job configs. Where the `jobs` directory contains example job configs.
