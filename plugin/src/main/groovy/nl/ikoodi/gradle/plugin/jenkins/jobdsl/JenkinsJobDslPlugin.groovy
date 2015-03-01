/*
 * Copyright 2015 the original author or authors.
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
package nl.ikoodi.gradle.plugin.jenkins.jobdsl

import nl.ikoodi.gradle.plugin.jenkins.jobdsl.tasks.GenerateConfigsTask
import nl.ikoodi.gradle.plugin.jenkins.jobdsl.tasks.PrepareWorkspaceTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class JenkinsJobDslPlugin implements Plugin<Project> {

    static final String PLUGIN_EXT_NAME = 'jenkinsJobDsl'
    static final String PLUGIN_GROUP_NAME = 'Jenkins JobDSL'
    static final String GENERATE_TASK_NAME = 'generateJenkinsJobs'
    static final String PREPARE_WORKSPACE_TASK_NAME = 'prepareJobDslWorkspace'

    @Override
    void apply(Project project) {
        project.extensions.create(PLUGIN_EXT_NAME, JenkinsJobDslPluginExtension, project)
        project.task(PREPARE_WORKSPACE_TASK_NAME, type: PrepareWorkspaceTask)
        project.task(GENERATE_TASK_NAME, type: GenerateConfigsTask, dependsOn: PREPARE_WORKSPACE_TASK_NAME)
    }
}
