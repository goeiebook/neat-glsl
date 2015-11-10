# Neat Glsl
This is a [gradle plugin](https://plugins.gradle.org/plugin/ca.goeiebook.gradle.neat-glsl) which generates
Java classes to encapsulate your glsl files as string constants. In other words, now you can work with GLSL
as a file in your project tree, instead of as a sticky ball of strings, pluses and newlines. Previously we used
[Manabu-GT's plugin](https://github.com/Manabu-GT/glsl-gradle-plugin), but it stopped working with Android
Studio 1.4+.

This implementation is written from the ground up, using new [Android Gradle Plugin]
(http://google.github.io/android-gradle-dsl/current/) features such as SourceSets and JavaGeneratingTask, along
with [CodeModel](https://codemodel.java.net/) to generate the Java code. The result is a neat, understandable plugin.
You can incorporate it into your build through [plugins.gradle.org](https://plugins.gradle.org/plugin/ca.goeiebook.gradle.neat-glsl)

## How it works
The plugin scans the source tree for GLSL files, and uses their contents to generate class files. Inside the classes
is a static constant string named 'code' which contains the contents of the glsl file. You are then able to reference
this class and upload the GLSL to the GPU through the OpenGL's loadProgram() method.

## How to use it

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

And then to build.gradle for modules containing GLSL code:
```
apply plugin: "ca.goeiebook.gradle.neat-glsl"

sourceSets {
        main {
            glsl.srcDir 'src/main'
        }
    }

    neatGlslConfig {
        outputPackage "com.fqn.of.desired.package.for.generated.glsl.classes"
    }
```
