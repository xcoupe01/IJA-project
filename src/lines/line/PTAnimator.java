package lines.line;

import javafx.application.Platform;

/**
 * PTAnimator class
 * implements interface Runnable
 * Used to run the animation separated which allows the interface to be responsible
 * while running the animation
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class PTAnimator implements Runnable {

    /** Connection to public transport*/
    private PublicTransport mainPubTrans;
    /** Animator thread*/
    private Thread t;

    /**
     * Native constructor of PTAnimator class
     * @param mainPubTrans is the connection to public transport
     */
    PTAnimator(PublicTransport mainPubTrans){
        this.mainPubTrans = mainPubTrans;
    }

    /**
     * Animation cycle
     */
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

    /**
     * Thread starter
     */
    void start(){
        this.mainPubTrans.setStopAnimator(false);
        if(t == null){
            t = new Thread(this, "Animator");
            t.start();
        }
    }

    /**
     * Thread reseter
     */
    void resetThread(){
        this.t = null;
    }
}
