/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.ikoodi.gradle.plugin.jenkins.jobdsl.tasks

import nl.ikoodi.gradle.plugin.jenkins.jobdsl.JenkinsJobDslPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import static nl.ikoodi.gradle.plugin.jenkins.jobdsl.CommonUtil.getAllFilesFromCopySpec
import static nl.ikoodi.gradle.plugin.jenkins.jobdsl.JenkinsJobDslPlugin.*

class PrepareWorkspaceTask extends DefaultTask {
    String name = PREPARE_WORKSPACE_TASK_NAME
    String group = PLUGIN_GROUP_NAME
    String description = 'Prepare a single directory with all resources required to generate Jenkins jobs from the JobDSL scripts'

    @TaskAction
    def prepareWorkspace(IncrementalTaskInputs inputs) {
        !workspaceBuildPath.exists() && workspaceBuildPath.mkdirs()
//        inputs.outOfDate { change ->
//            logger.info "outOfDate = ${change}"
//            project.copy {
//                from change.file
//                into workspaceBuildPath
//            }
//        }
//        inputs.removed { change ->
//            logger.info "removed = ${change}"
//            change.file.directory ? change.file.deleteDir() : change.file.delete()
////            project.sync {
////                from change.file
////                into workspaceBuildPath
////            }
//        }

        def extension = getExtension()
        workspaceBuildPath.mkdirs()
        project.sync {
            with extension.jobConfigs
            with extension.classpath
            into workspaceBuildPath
        }
    }

//    @InputFiles
    def getJobConfigInputFiles() {
        getAllFilesFromCopySpec(getExtension().jobConfigs) as List
    }

//    @InputFiles
    def getClasspathInputFiles() {
        getAllFilesFromCopySpec(getExtension().classpath) as List
    }

//    @OutputDirectory
    File getWorkspaceBuildPath() {
        project.file getExtension().workspaceBuildPath
    }

    private JenkinsJobDslPluginExtension getExtension() {
        project.getExtensions().getByName(PLUGIN_EXT_NAME)
    }
}
