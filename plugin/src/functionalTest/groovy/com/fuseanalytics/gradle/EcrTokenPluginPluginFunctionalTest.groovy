/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package com.fuseanalytics.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.TempDir
import org.gradle.testkit.runner.GradleRunner

/**
 * A simple functional test for the 'com.fuseanalytics.gradle.ecrToken' plugin.
 */
class EcrTokenPluginPluginFunctionalTest extends Specification {
    @TempDir
    private File projectDir

    private getBuildFile() {
        new File(projectDir, "build.gradle")
    }

    private getSettingsFile() {
        new File(projectDir, "settings.gradle")
    }

    def "can token be used"() {
        given:
        settingsFile << ""
        buildFile << """
import com.fuseanalytics.aws.ecr.EcrToken

plugins {
    id('com.fuseanalytics.gradle.ecrToken')
}

task useToken {
    EcrToken token = ecrToken.get()
    println( "User=\${token.user} and Password=\${token.password.length()}" )
}
"""

        when:
        GradleRunner runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("useToken", "--stacktrace")
        runner.withProjectDir(projectDir)
        BuildResult result = runner.build()

        then:
        result.output.readLines()
                .find { it.startsWith("User")}
                .trim()
                .matches(/User=(\w+) and Password=(\d+)/)
    }
}
