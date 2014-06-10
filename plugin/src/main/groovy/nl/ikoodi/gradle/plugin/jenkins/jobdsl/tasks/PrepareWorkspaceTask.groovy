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
import org.gradle.api.tasks.TaskAction

import static nl.ikoodi.gradle.plugin.jenkins.jobdsl.JenkinsJobDslPlugin.*

class PrepareWorkspaceTask extends DefaultTask {
    String name = PREPARE_WORKSPACE_TASK_NAME
    String group = PLUGIN_GROUP_NAME
    String description = 'Prepare a single directory with all resources required to generate Jenkins jobs from the JobDSL scripts'

    @TaskAction
    def prepareWorkspace() {
        def extension = getExtension()
        workspaceBuildPath.mkdirs()
        project.copy {
            with extension.jobConfigs
            with extension.classpath
            into workspaceBuildPath
        }
    }

//    @OutputDirectory // Commented as this causes the workspace to never be updated after the first time
    File getWorkspaceBuildPath() {
        project.file getExtension().workspaceBuildPath
    }

    private JenkinsJobDslPluginExtension getExtension() {
        project.getExtensions().getByName(PLUGIN_EXT_NAME)
    }
}
