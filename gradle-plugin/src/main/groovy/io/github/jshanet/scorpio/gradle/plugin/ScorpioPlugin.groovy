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

    void extractResourceAsFile(Project project, String resouce, File dest) {
        URL url = this.getClass().getResource(resouce)
        if (url == null) {
            project.logger.error("resource not found: " + resouce + ". this is a BUG, report it please.")
            return
        }

        //mkdir and create new file
        dest.parentFile.mkdirs()
        dest.createNewFile()

        //copy file
        InputStream is = null
        FileOutputStream fos = null
        try {
            is = url.openStream()
            fos = new FileOutputStream(dest)
            byte[] buf = new byte[1024]
            int count = -1
            while ((count = is.read(buf)) != -1) {
                fos.write(buf, 0, count)
            }
        } finally {
            if (is != null) is.close()
            if (fos != null) fos.close()
        }
    }
}