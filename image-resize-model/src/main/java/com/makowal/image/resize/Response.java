package com.makowal.image.resize;


import java.util.Map;

public class Response {

    Integer originalWidth;
    Integer originalHeight;

    Map<Integer, byte[]> scaledImages;

    public Response() {
    }

    public Response(Integer originalWidth, Integer originalHeight, Map<Integer, byte[]> scaledImages) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.scaledImages = scaledImages;
    }

    public Map<Integer, byte[]> getScaledImages() {
        return scaledImages;
    }

    public void setScaledImages(Map<Integer, byte[]> scaledImages) {
        this.scaledImages = scaledImages;
    }

    public Integer getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(Integer originalWidth) {
        this.originalWidth = originalWidth;
    }

    public Integer getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(Integer originalHeight) {
        this.originalHeight = originalHeight;
    }
}
