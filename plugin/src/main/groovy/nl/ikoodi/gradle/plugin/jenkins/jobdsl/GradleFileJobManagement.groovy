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

import javaposse.jobdsl.dsl.ConfigurationMissingException
import javaposse.jobdsl.dsl.FileJobManagement
import javaposse.jobdsl.dsl.NameNotProvidedException

class GradleFileJobManagement extends FileJobManagement {

    File outputDirectory

    GradleFileJobManagement(File workspace, File outputDirectory) {
        super(workspace)
        this.outputDirectory = outputDirectory
    }

    @Override
    boolean createOrUpdateConfig(String jobName, String config, boolean ignoreExisting)
            throws NameNotProvidedException, ConfigurationMissingException {
        writeConfig(jobName, config)
        return true
    }

    @Override
    void createOrUpdateView(String viewName, String config, boolean ignoreExisting)
            throws NameNotProvidedException, ConfigurationMissingException {
        writeConfig(viewName, config)
    }

    @Override
    String getCredentialsId(String credentialsDescription) {
        return credentialsDescription
    }

    private void writeConfig(String jobName, String config) {
        validateUpdateArgs(jobName, config);
        new File(outputDirectory, jobName + ext).write(config)
    }
}
