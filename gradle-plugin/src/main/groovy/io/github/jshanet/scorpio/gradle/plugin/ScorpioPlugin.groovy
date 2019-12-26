package io.github.jshanet.scorpio.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ScorpioPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        project.extensions.create('scorpio', ScorpioPluginExtension)
        applyGradleScript(project)
    }

    void applyGradleScript(Project project) {
        URI uri = this.class.getResource("/origin.scorpio.gradle").toURI()
        project.apply from: uri
    }
}