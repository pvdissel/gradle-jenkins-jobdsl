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
package nl.ikoodi.gradle.plugin.jenkins.jobdsl.tasks

import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.FileJobManagement
import javaposse.jobdsl.dsl.GeneratedItems
import javaposse.jobdsl.dsl.ScriptRequest
import nl.ikoodi.gradle.plugin.jenkins.jobdsl.GradleFileJobManagement
import nl.ikoodi.gradle.plugin.jenkins.jobdsl.JenkinsJobDslPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import static nl.ikoodi.gradle.plugin.jenkins.jobdsl.JenkinsJobDslPlugin.*

class GenerateConfigsTask extends DefaultTask {
    String name = GENERATE_TASK_NAME
    String group = PLUGIN_GROUP_NAME
    String description = 'Generate Jenkins Jobs/Views config file from the JobDSL scripts'

    @TaskAction
    def compileAction() {
        def pathsToDslFiles = findPathsToDslFiles(workspaceBuildPath)
        processJobDslFiles(pathsToDslFiles)
    }

    @InputDirectory
    File getWorkspaceBuildPath() {
        project.file getExtension().workspaceBuildPath
    }

    @OutputDirectory
    File getGeneratedOutputPath() {
        project.file getExtension().generatedOutputPath
    }

    private List<File> findPathsToDslFiles(File from) {
        def files = []
        def dslFiles = project.fileTree(from)
        dslFiles.include(getExtension().dslFilePattern)
        dslFiles.files.each { file ->
            def shortPath = getShortenedPath(file, from)
            logger.info "Adding DSL file [${shortPath}] for processing"
            files.add(file)
        }
        return files
    }

    private void processJobDslFiles(List<File> scripts) {
        File buildWorkspaceOutputDirectory = getWorkspaceBuildPath()
        buildWorkspaceOutputDirectory.mkdirs()

        File generatedFilesOutputDirectory = getGeneratedOutputPath();
        generatedFilesOutputDirectory.mkdirs()

        FileJobManagement jm = createJobManagement(
                buildWorkspaceOutputDirectory,
                generatedFilesOutputDirectory,
                project.projectDir
        );
        scripts.each { File scriptFile ->
            processDslScript(jm, buildWorkspaceOutputDirectory, scriptFile)
        }
    }

    private FileJobManagement createJobManagement(File workspace, File outputDirectory, File projectDirectory) {
        def jm = new GradleFileJobManagement(workspace, outputDirectory, projectDirectory)
        configureJobManagement(jm)
        return jm
    }

    private void configureJobManagement(GradleFileJobManagement jm) {
        jm.getParameters().putAll(System.getenv());
        for (Map.Entry entry : System.getProperties().entrySet()) {
            jm.getParameters().put(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    /**
     * Made public... because else it cannot be found by scripts.each {} ?!?!?!
     */
    void processDslScript(FileJobManagement jm, File workspace, File scriptFile) {
        def workspaceUrl = workspace.toURI().toURL()
        ScriptRequest request = new ScriptRequest(scriptFile.absolutePath, null, workspaceUrl, false);
        GeneratedItems generatedItems = DslScriptLoader.runDslEngine(request, jm);

        def shortScriptPath = getShortenedPath(scriptFile, workspace)
        generatedItems.jobs.each { job ->
            logger.info "From [${shortScriptPath}], Generated job: ${job}"
        }
        generatedItems.views.each { view ->
            logger.info "From [${shortScriptPath}], Generated view: ${view}"
        }
    }

    String getShortenedPath(File file, File basePath) {
        def base = basePath.absolutePath
        if (!base.endsWith(File.separator)) {
            base = "${base}${File.separator}"
        }
        def path = file.absolutePath.replace(base, '')
        return path
    }

    private JenkinsJobDslPluginExtension getExtension() {
        project.getExtensions().getByName(PLUGIN_EXT_NAME)
    }
}
