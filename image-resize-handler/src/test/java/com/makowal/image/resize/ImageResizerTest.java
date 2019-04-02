package com.makowal.image.resize;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


public class ImageResizerTest {

    private final ImageResizer imageResizer = new ImageResizer();
    private final String testImage = "testImage.jpg";
    private final String textFile = "simpleText.txt";
    private final String resizeWidthImage = "resizeTestWidth320.jpg";
    private final String resizeHeightImage = "resizeTestHeight320.jpg";
    private final String resizeAutoImage = "resizeTestAuto320.jpg";
    private final String resizeBigImage = "resizeTest1920.jpg";
    private Context contextMock;
    private LambdaLogger logger;

    @Before
    public void setup() {
        contextMock = mock(Context.class);
        logger = mock(LambdaLogger.class);
        when(contextMock.getLogger()).thenReturn(logger);
    }

    @Test
    public void testNullImageByteArray() {
        Request request = new Request();
        request.setType("width");
        imageResizer.handleRequest(request, contextMock);

        verify(logger).log("ImageRead exception: null");
    }

    @Test
    public void testEmptyImageByteArray() {
        Request request = new Request();
        request.setType("width");
        request.setOriginalImage(new byte[0]);
        imageResizer.handleRequest(request, contextMock);

        verify(logger).log("ImageRead exception: null");
    }

    @Test
    public void testNullSizeArray() throws IOException {
        Request request = new Request();
        request.setType("width");
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(testImage).getFile()).toPath()));

        imageResizer.handleRequest(request, contextMock);

        verify(logger).log("ImageRead exception: null");
    }

    @Test
    public void testEmptySizeArray() throws IOException {
        Request request = new Request();
        request.setType("width");
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(testImage).getFile()).toPath()));
        request.setSizes(new Integer[0]);

        imageResizer.handleRequest(request, contextMock);

        verify(logger, never()).log(anyString());
    }

    @Test
    public void testNullType() throws IOException {
        Request request = new Request();
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(textFile).getFile()).toPath()));
        request.setSizes(new Integer[]{320});
        imageResizer.handleRequest(request, contextMock);

        verify(logger).log("request:type must be 'width', 'height' or 'auto'");
    }

    @Test
    public void testEmptyType() throws IOException {
        Request request = new Request();
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(textFile).getFile()).toPath()));
        request.setSizes(new Integer[]{320});
        request.setType("");
        imageResizer.handleRequest(request, contextMock);

        verify(logger).log("request:type must be 'width', 'height' or 'auto'");
    }

    @Test
    public void testDummyType() throws IOException {
        Request request = new Request();
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(textFile).getFile()).toPath()));
        request.setSizes(new Integer[]{320});
        request.setType("dummy");

        Response response = imageResizer.handleRequest(request, contextMock);

        assertEquals(new Integer(0), response.getOriginalWidth());
        assertEquals(new Integer(0), response.getOriginalHeight());
        verify(logger).log("request:type must be 'width', 'height' or 'auto'");
    }


    @Test
    public void testResizingNonImageByteArray() throws IOException {
        Request request = new Request();
        request.setType("width");
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(textFile).getFile()).toPath()));
        request.setSizes(new Integer[]{320});

        Response response = imageResizer.handleRequest(request, contextMock);

        assertEquals(new Integer(0), response.getOriginalWidth());
        assertEquals(new Integer(0), response.getOriginalHeight());
        verify(logger).log("starting scale for width: 320, image size: 0KB");
        verify(logger).log("Scaling exception: src cannot be null");
    }

    @Test
    public void testResizingWidth() throws IOException {
        Request request = new Request();
        request.setType("width");
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(testImage).getFile()).toPath()));
        request.setSizes(new Integer[]{320});

        Response response = imageResizer.handleRequest(request, contextMock);

        byte[] expectedImage = Files.readAllBytes(new File(getClass().getResource(resizeWidthImage).getFile()).toPath());
        assertTrue(response.getScaledImages().containsKey(320));
        assertArrayEquals(expectedImage, response.getScaledImages().get(320));
        assertEquals(new Integer(1280), response.getOriginalWidth());
        assertEquals(new Integer(720), response.getOriginalHeight());
        verify(logger).log("starting scale for width: 320, image size: 314KB");
        verify(logger).log("scaling success new image size: 11KB");
    }

    @Test
    public void testResizingHeight() throws IOException {
        Request request = new Request();
        request.setType("height");
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(testImage).getFile()).toPath()));
        request.setSizes(new Integer[]{320});

        Response response = imageResizer.handleRequest(request, contextMock);

        byte[] expectedImage = Files.readAllBytes(new File(getClass().getResource(resizeHeightImage).getFile()).toPath());
        assertTrue(response.getScaledImages().containsKey(320));
        assertArrayEquals(expectedImage, response.getScaledImages().get(320));
        assertEquals(new Integer(1280), response.getOriginalWidth());
        assertEquals(new Integer(720), response.getOriginalHeight());
        verify(logger).log("starting scale for height: 320, image size: 314KB");
        verify(logger).log("scaling success new image size: 31KB");
    }

    @Test
    public void testResizingAutoDimension() throws IOException {
        Request request = new Request();
        request.setType("auto");
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(testImage).getFile()).toPath()));
        request.setSizes(new Integer[]{320});

        Response response = imageResizer.handleRequest(request, contextMock);

        byte[] expectedImage = Files.readAllBytes(new File(getClass().getResource(resizeAutoImage).getFile()).toPath());
        assertTrue(response.getScaledImages().containsKey(320));
        assertArrayEquals(expectedImage, response.getScaledImages().get(320));
        assertEquals(new Integer(1280), response.getOriginalWidth());
        assertEquals(new Integer(720), response.getOriginalHeight());
        verify(logger).log("starting scale for auto: 320, image size: 314KB");
        verify(logger).log("scaling success new image size: 11KB");
    }

    @Test
    public void testResizingBiggerThanOriginalSize() throws IOException {
        Request request = new Request();
        request.setType("width");
        request.setOriginalImage(Files.readAllBytes(new File(getClass().getResource(testImage).getFile()).toPath()));
        request.setSizes(new Integer[]{1920});

        Response response = imageResizer.handleRequest(request, contextMock);

        byte[] expectedImage = Files.readAllBytes(new File(getClass().getResource(resizeBigImage).getFile()).toPath());
        assertTrue(response.getScaledImages().containsKey(1920));
        assertArrayEquals(expectedImage, response.getScaledImages().get(1920));
        verify(logger).log("starting scale for width: 1920, image size: 314KB");
        verify(logger).log("scaling success new image size: 311KB");
    }
}