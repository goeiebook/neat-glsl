# Neat Glsl
This is an Android [Gradle plugin](https://plugins.gradle.org/plugin/ca.goeiebook.gradle.neat-glsl) for working with [GLSL](https://en.wikipedia.org/wiki/OpenGL_Shading_Language). It faciliates development by
allowing you to incorporate GLSL as a source file instead of as a raw resource or a sticky ball of concatenated
strings. With this plugin you control where the glsl files reside, and by keeping them out of the project's res/
folder you avoid exposing your raw shader code to production. Also, IDE plugins like [glsl4idea](https://github.com/Darkyenus/glsl4idea) can help you during development.

Inspired by [Manabu-GT's plugin](https://github.com/Manabu-GT/glsl-gradle-plugin), this from-scratch
implementation uses [Android Gradle](http://google.github.io/android-gradle-dsl/current/) features
such as [JavaGeneratingTask](https://android.googlesource.com/platform/tools/build/+/ef84df96b7b1a26a96a37506d95e3d427ee830ed/tests/genFolderApi/build.gradle). Java is ultimately generated using Sun's [CodeModel](https://codemodel.java.net/).

## How it works, how to use it
The plugin scans configurable locations for GLSL files and outputs a Java class for each. A generated class encapsulates
a string constant named **code** containing the raw shader source. You pass that string to the GPU when
compiling shaders.

For example, a file named _MyVertexShader.glsl_ results in a class named
**MyVertexShader**, which you could use as follows:

```
    // create a shader of desired type
    int shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

    // Add the source code to the shader and compile it
    GLES20.glShaderSource(shader, MyVertexShader.code);
    GLES20.glCompileShader(shader);
```
 
## How to install, how to configure 
The plugin is availble through [plugins.gradle.org](https://plugins.gradle.org/plugin/ca.goeiebook.gradle.neat-glsl)

Add the following to the root build.gradle in your Android project:

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  
  dependencies {
    classpath "gradle.plugin.ca.goeiebook.gradle:neat-glsl:0.4.0"
  }
}
```

And then for each module with GLSL code, add the following to the build.gradle:
```
apply plugin: "ca.goeiebook.gradle.neat-glsl"

sourceSets {
        main {
            // Specify where to look for glsl files.
            glsl.srcDir 'src/main'
        }
    }

    neatGlslConfig {
        // This is the package name used for the generated Java classes.
        outputPackage "com.the_desired.package_name.for.generated.glsl"
    }
}
```
