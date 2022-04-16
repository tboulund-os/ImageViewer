package dk.easv;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class ImageAnalyser {

    static int mostlyRedPixels = 0;
    static int mostlyGreenPixels = 0;
    static int mostlyBluePixels = 0;

    public ObservableList<PieChart.Data> analyse(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image,null);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int a = 0,r = 0,g = 0,b = 0;

        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                int p = bufferedImage.getRGB(j,i);
                // get red
                r = (p >> 16) & 0xff;

                // get green
                g = (p >> 8) & 0xff;

                // get blue
                b = p & 0xff;

                incrementBiggestValue(r,g,b);

            }
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.addAll(new PieChart.Data("Red",mostlyRedPixels),
                    new PieChart.Data("Blue",mostlyBluePixels),
                    new PieChart.Data("Green",mostlyGreenPixels));
        return data;

    }

    private void incrementBiggestValue(int r, int g, int b) {
        int biggest = 0;

        if (r>biggest)
            biggest = r;
        if (g>biggest)
            biggest = g;
        if (b>biggest)
            biggest = b;


        if (biggest==r)
            mostlyRedPixels++;
        if (biggest==g)
            mostlyGreenPixels++;
        if (biggest==b)
            mostlyBluePixels++;
    }
}
