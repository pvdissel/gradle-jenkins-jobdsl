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
package nl.ikoodi.gradle.plugin.jenkins.jobdsl

import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.util.ConfigureUtil

class JenkinsJobDslPluginExtension {
    private final Project project
    private String baseOutputPath = new File(project.buildDir, 'jobDsl').absolutePath
    final CopySpec jobConfigs
    final CopySpec classpath
    String workspaceBuildPath = new File(baseOutputPath, 'workspace').absolutePath
    String generatedOutputPath = new File(baseOutputPath, 'generated').absolutePath

    JenkinsJobDslPluginExtension(Project project) {
        this.project = project
        this.jobConfigs = project.copySpec {}
        this.classpath = project.copySpec {}
    }

    /**
     * Configures the jobConfigs copy spec.
     * @param closure the configuration closure
     */
    void jobConfigs(Closure closure) {
        ConfigureUtil.configure(closure, jobConfigs)
    }

    /**
     * Configures the classpath copy spec.
     * @param closure the configuration closure
     */
    void classpath(Closure closure) {
        ConfigureUtil.configure(closure, classpath)
    }
}
