package dk.easv;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;

public class ImageReference {

    private Image image;
    private String fileName;

    private ObservableList<PieChart.Data> data;

    private ImageAnalyser analyzer;

    public ImageReference(Image image, String fileName) {
        this.image = image;
        this.fileName = fileName;
        analyzer = new ImageAnalyser();
        data = analyzer.analyse(image);
    }

    public Image getImage() {
        return image;
    }

    public ObservableList<PieChart.Data> getData() {
        return data;
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
