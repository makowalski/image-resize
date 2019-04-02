package com.makowal.image.resize;

import com.amazonaws.services.lambda.invoke.LambdaFunction;


public interface ImageLambdaClient {

    @LambdaFunction(functionName="resizeImage")
    Response resizeImage(Request input);
}
