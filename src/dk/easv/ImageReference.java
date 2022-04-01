package dk.easv;

import javafx.scene.image.Image;

public class ImageReference {

    private Image image;
    private String fileName;

    public ImageReference(Image image, String fileName) {
        this.image = image;
        this.fileName = fileName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
