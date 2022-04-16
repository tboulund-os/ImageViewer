package dk.easv;


import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Scheduler implements Runnable{

    BlockingQueue<List<ImageReference>> blockingQueue;
    ImageViewerWindowController controller;

    public Scheduler(BlockingQueue blockingQueue, ImageViewerWindowController controller){
        this.blockingQueue = blockingQueue;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true){
            try {
                List<ImageReference> firstInQueue = blockingQueue.take();
                blockingQueue.put(firstInQueue);
                controller.changeSlideList(firstInQueue);
                sleep();
                //System.out.println("still here");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep(){
        try{
            Thread.sleep(20000);
        }
        catch (InterruptedException e){
            System.out.println("You interrupted the thread while it was sleeping ;(" +
                                "\n" +
                                "Let the poor thread sleep");
        }
    }
}
