package com.fuseanalytics.aws.ecr

import com.amazonaws.services.ecr.*
import com.amazonaws.services.ecr.model.*

import groovy.json.*
import java.text.SimpleDateFormat

public class EcrTokenService {

    private List<AuthorizationData> cacheTokens(File tokenFile, List<AuthorizationData> tokens) {
        tokenFile.withWriter { Writer writer ->
            writer.write( JsonOutput.toJson( tokens ) )
        }
        return tokens
    }

    private EcrToken decodeToken( AuthorizationData data ) {
        String token = data.getAuthorizationToken()
        String[] tokenArr = new String( token.decodeBase64(), "UTF-8").split(":")
        return new EcrToken( tokenArr[0], tokenArr[1] )
    }

    EcrToken getEcrToken() {
        File cachedToken = new File("${System.getProperty("user.home")}/.aws/cache-token.json")
        if( cachedToken.exists() ) {
            def json = new JsonSlurper().parseText( cachedToken.text )
            json.each { Map j -> j.expiresAt = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ").parse(j.expiresAt as String) }
            List<AuthorizationData> tokens = json.collect { Map tokenJson -> new AuthorizationData(tokenJson) }
            AuthorizationData token = tokens.find { AuthorizationData t ->
                if( System.currentTimeMillis() < t.expiresAt.time ) return true
                return false
            }
            if( token ) return decodeToken(token)
        }
        AmazonECR ecr = AmazonECRClient.builder().standard().build()
        GetAuthorizationTokenResult res = ecr.getAuthorizationToken( new GetAuthorizationTokenRequest() )
        List<EcrToken> tokens = cacheTokens(cachedToken, res.getAuthorizationData())
                .collect { AuthorizationData data -> decodeToken(data) }
        return tokens.first()
    }
}