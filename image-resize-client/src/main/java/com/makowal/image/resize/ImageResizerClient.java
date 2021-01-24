package com.makowal.image.resize;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;

public class ImageResizerClient {

    private final ImageLambdaClient lambdaClient;

    public ImageResizerClient(String accessKey, String secretKey, String region) {
        AWSLambdaClientBuilder clientBuilder = AWSLambdaClientBuilder
                .standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        accessKey,
                                        secretKey
                                )
                        )
                )
                .withRegion(region);

        this.lambdaClient = LambdaInvokerFactory.builder()
                .lambdaClient(clientBuilder.build())
                .build(ImageLambdaClient.class);
    }

    public Response resizeImage(Request request) {
        return lambdaClient.resizeImage(request);
    }

}
