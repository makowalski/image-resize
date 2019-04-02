package com.makowal.image.resize;


public class Request {

    String type;
    Integer[] sizes;
    byte[] originalImage;

    public Request() {
    }

    public Request(String type, Integer[] sizes, byte[] originalImage) {
        this.type = type;
        this.sizes = sizes;
        this.originalImage = originalImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer[] getSizes() {
        return sizes;
    }

    public void setSizes(Integer[] sizes) {
        this.sizes = sizes;
    }

    public byte[] getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(byte[] originalImage) {
        this.originalImage = originalImage;
    }
}
