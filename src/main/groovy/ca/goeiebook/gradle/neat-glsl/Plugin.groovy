package ca.goeiebook.gradle.neat_glsl
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.DefaultSourceDirectorySet

class Plugin implements org.gradle.api.Plugin<Project> {

    void apply(Project project) {
        project.extensions.create('neatGlslConfig', ca.goeiebook.gradle.neat_glsl.Config, project)

        // Add 'glsl' as a source set extension
        project.android.sourceSets.all { sourceSet ->
            sourceSet.extensions.create(
                    'glsl', DefaultSourceDirectorySet, 'glsl', project.fileResolver)
        }

        project.afterEvaluate {
            def variants = null
            if (project.android.hasProperty('applicationVariants')) {
                variants = project.android.applicationVariants
            }
            else if (project.android.hasProperty('libraryVariants')) {
                variants = project.android.libraryVariants
            }
            else {
                throw new IllegalStateException('expected applicationVariants or libraryVariants')
            }

            variants.all { variant ->
                FileCollection glslFiles = project.files()
                variant.sourceSets.each { sourceSet ->
                    FileCollection filteredCollection = sourceSet.glsl.filter { File file ->
                        file.name.endsWith '.glsl'
                    }
                    glslFiles.from filteredCollection
                }

                if (glslFiles.empty) {
                    return
                }

                def taskName = "generateNeatGlslJava${variant.name.capitalize()}"
                def taskType = ca.goeiebook.gradle.neat_glsl.Task
                def taskOut = "$project.buildDir/generated/source/$flavorName/$buildType.name/java-glsl"

                def task = project.task(taskName, type: taskType ) {
                    outputPackageName = project.neatGlslConfig.getOutputPackage()
                    sources = glslFiles
                    outputDir = project.file(taskOut)
                }

                variant.registerJavaGeneratingTask(task, task.outputDir)
            }
        }
    }

}