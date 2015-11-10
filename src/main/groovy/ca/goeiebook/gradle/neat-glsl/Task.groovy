package ca.goeiebook.gradle.neat_glsl

import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JExpr
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction

import static com.sun.codemodel.JMod.*

class Task extends DefaultTask {
    String outputPackageName
    FileCollection sources
    File outputDir

    @TaskAction
    def convertGlslToJava() {
        if (outputDir.exists()) {
            logger.debug("$outputDir")
            outputDir.delete()
        }

        outputDir.mkdirs()

        sources.each { File glslFile ->
            JCodeModel cm = new JCodeModel();
            JDefinedClass _class = cm._class(getFQCN(glslFile.name))
            String glslString = glslFileToString(glslFile)
            _class.field(PUBLIC|FINAL|STATIC, String.class, "code", JExpr.lit(glslString))
            cm.build(outputDir)
        }
    }

    String getFQCN(String filename) {
        def filenameWithoutExtension = getFilenameWithoutExtension(filename)
        def className = filenameWithoutExtension.capitalize()
        return "${outputPackageName}.${className}"
    }

    String glslFileToString(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file))
        StringBuilder sb = new StringBuilder()
        String line
        while((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                sb.append("$line\n")
            }
        }
        return sb.toString()
    }

    String getFilenameWithoutExtension(String name) {
        int extStart = name.lastIndexOf '.'
        return extStart == -1 ? name : name.substring(0, extStart)
    }
}

