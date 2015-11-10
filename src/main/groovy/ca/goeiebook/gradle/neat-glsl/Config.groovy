package ca.goeiebook.gradle.neat_glsl

import org.gradle.api.Project

class Config {

    // Package name for the generated classes
    private String outputPackage

    Config(Project project) {
    }

    void outputPackage(String outputPackage) {
        this.outputPackage = outputPackage
    }

    String getOutputPackage() {
        return outputPackage
    }
}