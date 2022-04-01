package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController
{
    private final List<Image> images = new ArrayList<>();
    @FXML
    private Slider valueSlider;
    @FXML
    private Button btnLoad;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;

    private int currentImageIndex = 0;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    ScheduledExecutorService executor;

    public ImageViewerWindowController(){
        executor= Executors.newScheduledThreadPool(1);
    }

    @FXML
    private void handleBtnLoadAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                f.getName();
                images.add(new Image(f.toURI().toString()));
            });
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    public void handleStartChangeValueSlider() {
        stopRunning();
    }

    public void handleStopChangeValueSlider() {
        startRunning();
    }

    public void handlePlayBtn(ActionEvent actionEvent) {
        startRunning();
    }

    private void startRunning() {
        executor= Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(aRunnable, 0, (long) valueSlider.getValue(), TimeUnit.SECONDS);
    }

    Runnable aRunnable = new Runnable() {
        @Override
        public void run() {
            handleBtnNextAction();
        }
    };

    public void handleStopBtn(ActionEvent actionEvent) {
        stopRunning();
    }

    private void stopRunning() {
        executor.shutdown();
    }

}