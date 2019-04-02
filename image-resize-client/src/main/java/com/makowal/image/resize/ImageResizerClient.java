package com.makowal.image.resize;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;

public class ImageResizerClient {

    private final ImageLambdaClient lambdaClient;

    public ImageResizerClient() {
        final AWSLambdaClientBuilder clientBuilder = AWSLambdaClientBuilder.standard();
        clientBuilder.withRegion(Regions.US_EAST_1);

        this.lambdaClient = LambdaInvokerFactory.builder()
                .lambdaClient(clientBuilder.build())
                .build(ImageLambdaClient.class);
    }

    public Response resizeImage(Request request) {
        return lambdaClient.resizeImage(request);
    }

}
