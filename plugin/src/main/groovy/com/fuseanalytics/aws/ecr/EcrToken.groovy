package com.fuseanalytics.aws.ecr

import org.gradle.api.provider.Property

public class EcrToken  {

    String user

    String password

    EcrToken(String user, String password) {
        this.user = user
        this.password = password
    }
}