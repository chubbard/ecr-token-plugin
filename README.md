# ECR Token Plugin

## About

Gradle plugin to help generate an AWS ECR authentication token using AWS' API for use in your gradle builds.
This allows you to use tools like [Google's Jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin) 
to create docker containers within your gradle build without needing external dependencies to manage the token.
 Just declare the plugin and use the`ecrToken` variable in your Jib authentication block.

## Example

```
apply {
    id "com.fuseanalytics.gradle.ecrToken"
}

jib {
    container {
        entrypoint = ["..."]
        ...
    }
    from {
        image = "amazoncorretto:17-alpine"      // this only an example image not required
    }
    to {
        // your ECR repository goes here
        image = "xxxxx.dkr.ecr.us-east-1.amazonaws.com/yourProject:${project.version}-${buildNumber}"
    }
    auth {
        // this is where you use the `ecrToken` variable to get the token
        EcrToken token = ecrToken.get()
        username = token.user
        password = token.password
    }
    ...
```

You'll need to set up your AWS credentials as normal with your `aws_access_key_id` and `aws_secret_access_key`.
Consult [AWS documentation](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials-temporary.html#credentials-temporary-from-portal) 
on how to exactly do that.  You can't provide your access key and secret access key in the build because
that will be too likely that you'll check in those secrets in your repo and that's a serious no-no.  That feature
does NOT exist to keep you from doing something insecure.

Good news is there is nothing else to configure.  It's very simple.