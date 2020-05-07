package lines.line;

import javafx.application.Platform;

public class PTAnimator implements Runnable {

    private PublicTransport mainPubTrans;
    private Thread t;

    PTAnimator(PublicTransport mainPubTrans){
        this.mainPubTrans = mainPubTrans;
    }

    public void run(){
        try{
            while(!this.mainPubTrans.getStopAnimator()){
                Platform.runLater(
                        () -> this.mainPubTrans.animationStep()
                );
                Thread.sleep(this.mainPubTrans.getAnimationStepDelay());
            }
        } catch (InterruptedException e){
            System.out.println("Animator Interrupted");
        }
    }

    void start(){
        this.mainPubTrans.setStopAnimator(false);
        if(t == null){
            t = new Thread(this, "Animator");
            t.start();
        }
    }

    void resetThread(){
        this.t = null;
    }
}
