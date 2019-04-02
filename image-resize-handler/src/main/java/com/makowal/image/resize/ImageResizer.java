package com.makowal.image.resize;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ImageResizer implements RequestHandler<Request, Response> {

    @Override
    public Response handleRequest(Request input, Context context) {

        Map<Integer, byte[]> rescaledImages = new HashMap<>();

        if (!validateType(input.getType())) {
            context.getLogger().log("request:type must be 'width', 'height' or 'auto'");
            return new Response(0, 0, rescaledImages);
        }

        BufferedImage image = null;
        try (ByteArrayInputStream is = new ByteArrayInputStream(input.getOriginalImage())) {
            image = ImageIO.read(is);

            for (Integer size : input.getSizes()) {

                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    context.getLogger().log("starting scale for " + input.getType() + ": " + size + ", image size: " + (input.getOriginalImage().length / 1024) + "KB");

                    BufferedImage rescaledImage = null;
                    switch (input.getType()) {
                        case "width":
                            rescaledImage = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, size, Scalr.OP_ANTIALIAS);
                            break;
                        case "height":
                            rescaledImage = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, size, Scalr.OP_ANTIALIAS);
                            break;
                        case "auto":
                            rescaledImage = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, size, Scalr.OP_ANTIALIAS);
                            break;
                    }

                    ImageIO.write(rescaledImage, "jpg", os);
                    byte[] bytes = os.toByteArray();

                    context.getLogger().log("scaling success new image size: " + (bytes.length / 1024) + "KB");
                    rescaledImages.put(size, bytes);
                } catch (Exception e) {
                    context.getLogger().log("Scaling exception: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            context.getLogger().log("ImageRead exception: " + e.getMessage());
        }


        return new Response(
                image != null ? image.getWidth() : 0,
                image != null ? image.getHeight() : 0,
                rescaledImages
        );
    }

    private boolean validateType(String type) {
        return Stream.of("auto", "width", "height").anyMatch(Predicate.isEqual(type));
    }

}
