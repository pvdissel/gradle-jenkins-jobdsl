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

import org.gradle.api.Task
import org.gradle.api.file.CopySpec
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class CommonUtil {
    private static Logger logger = Logging.getLogger(Task.class);

    static def getAllFilesFromCopySpec(CopySpec copySpec) {
        copySpec.getSourcePaths().collectAll {
            if (it instanceof File) {
                return it
            }
            if (it instanceof FileCollection) {
                return it.files
            }
            logger.warn "Found unexpected type [${it.class}]"
            println "Found unexpected type [${it.class}]"
        }.findAll().flatten()
    }
}
