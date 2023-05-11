package com.fuseanalytics.aws.ecr

import org.gradle.api.Plugin
import org.gradle.api.Project

class EcrTokenPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        final EcrTokenService service = new EcrTokenService()

        project.extensions.add("ecrToken", project.provider { service.getEcrToken() })
    }
}
