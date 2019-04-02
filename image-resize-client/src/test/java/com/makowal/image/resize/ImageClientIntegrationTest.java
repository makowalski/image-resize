package com.makowal.image.resize;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ImageClientIntegrationTest {

    @Test
    public void endToEndTest() throws IOException {
        // given
        System.setProperty("aws.accessKeyId", "YOUR_AWS_ACCESS_KEY");
        System.setProperty("aws.secretKey", "YOUR_AWS_SECRET_KEY");

        URL testImageUrl = getClass().getResource("testImage.jpg");
        String resizeFileNamePrefix = "resizeTest";
        Integer[] sizes = new Integer[]{640, 320, 50};
        byte[] originalImageAsByteArray = Files.readAllBytes(new File(testImageUrl.getFile()).toPath());

        ImageResizerClient client = new ImageResizerClient();

        Request request = new Request();
        request.setSizes(sizes);
        request.setType("width");
        request.setOriginalImage(originalImageAsByteArray);


        // when
        Response response = client.resizeImage(request);


        // then
        Arrays.stream(sizes).forEach(size -> {
            try {
                byte[] expectedImage = Files.readAllBytes(
                        new File(getClass().getResource(resizeFileNamePrefix + size + ".jpg").getFile()).toPath()
                );
                byte[] actualResizeImage = response.getScaledImages().get(size);

                assertArrayEquals(expectedImage, actualResizeImage);
            } catch (Exception e) {
                fail(e.getMessage());
            }
        });
        assertEquals(new Integer(1280), response.getOriginalWidth());
        assertEquals(new Integer(720), response.getOriginalHeight());
    }
}
