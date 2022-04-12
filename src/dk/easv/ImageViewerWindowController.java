package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController
{
    public Label fileLabel;
    @FXML
    private Slider valueSlider;
    @FXML
    private Button btnLoad;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;
    @FXML
    Parent root;

    @FXML
    private ImageView imageView;
    private int currentImageIndex = 0;
    ScheduledExecutorService executor;

    List<ImageReference> slideList;
    BlockingQueue<List<ImageReference>> blockingQueue;
    Scheduler scheduler;
    Thread schedulerThread;

    public ImageViewerWindowController(){
        executor= Executors.newScheduledThreadPool(1);
        blockingQueue = new ArrayBlockingQueue<>(3);
        scheduler = new Scheduler(blockingQueue,this);
        schedulerThread = new Thread(scheduler);
        System.out.println(Thread.currentThread().getName());
    }

    @FXML
    private void handleBtnLoadAction() throws InterruptedException {
        List<ImageReference> images = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (files==null)
            return;

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                Image image = new Image(f.toURI().toString());
                String name = f.getName();
                images.add(new ImageReference(image,name));
            });
            //displayImage();
        }

        blockingQueue.put(images);
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        if (!slideList.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + slideList.size()) % slideList.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        if (!slideList.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % slideList.size();
            displayImage();
        }
    }

    private void displayImage()
    {
        if (slideList!=null || !slideList.isEmpty())
        {
            ImageReference reference = slideList.get(currentImageIndex);
            imageView.setImage(reference.getImage());
            fileLabel.setText(reference.getFileName());
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
        schedulerThread.start();
    }

    Runnable aRunnable = new Runnable() {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    handleBtnNextAction();
                }
            });
        }
    };

    public void handleStopBtn(ActionEvent actionEvent) {
        stopRunning();
    }

    private void stopRunning() {
        executor.shutdown();
        schedulerThread.interrupt();
    }

    public void changeSlideList(List<ImageReference> firstInQueue) {
        slideList = firstInQueue;
        currentImageIndex = 0;
    }
}