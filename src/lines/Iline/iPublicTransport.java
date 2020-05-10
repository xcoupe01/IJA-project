package lines.Iline;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lines.line.PTLine;
import lines.line.Timer;
import lines.line.Vehicle;
import map.maps.Map;

/**
 * iPublicTransport interface
 * implemented by class PublicTransport
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iPublicTransport{

    /**
     * Adds line to public transport
     * @param line is the line to be added
     */
    void addLine(PTLine line);

    /**
     * Returns the list of lines of the public transport
     * @return list of lines of public transport
     */
    java.util.List<PTLine> getLines();

    /**
     * Moves all the objects of public transport
     * @param mapCanvas is the Pane where the public transport is being moved
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    void updatePTPos(Pane mapCanvas, int x, int y);

    /**
     * Loads the public transport form given file and draws all the vehicles on the given Pane
     * @param mapCanvas is the Pane where the vehicles is going to be drawn
     * @param filePath is the path to file
     * @param mainMap is connection to map
     * @param lineInformationPane is connection to information Pane
     * @return true if successful false otherwise
     */
    boolean loadPTFromFile(Pane mapCanvas, String filePath, Map mainMap, Pane lineInformationPane);

    /**
     * Saves current public transport to a given file path
     * @param filePath is the file to be saved to
     * @param mainMap is the connection to map
     */
    void savePTToFile(String filePath, Map mainMap);

    /**
     * Draws all line route highlights on a given Pane
     * @param mapCanvas is the Pane where the highlights are going to be drawn
     */
    void highlightAllRoutesOn(Pane mapCanvas);

    /**
     * Erases all line route highlights on a given Pane
     * @param mapCanvas is the Pane where the highlights are going to be erased
     */
    void highlightAllRoutesOff(Pane mapCanvas);

    /**
     * Toggles ale line route highlights on a given Pane
     * @param mapCanvas is the Pane where the line route highlights are going to be toggled
     */
    void toggleHighlight(Pane mapCanvas);

    /**
     * Draws all vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be drawn
     */
    void drawAllVehicles(Pane mapCanvas);

    /**
     * Erases all vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be erased
     */
    void eraseAllVehicles(Pane mapCanvas);

    /**
     * Makes one animation step
     */
    void animationStep();

    /**
     * The same as animationStep function, but it subtracts from timer
     */
    void oppositeAnimationStep();

    /**
     * Attaches connection to time display label
     * @param timeDisplay is the label to be attached
     */
    void setTimeDisplay(Label timeDisplay);

    /**
     * Updates time display label with values from timer
     */
    void updateTimeDisplay();

    /**
     * Returns timer of public transport
     * @return timer of public transport
     */
    Timer getTimer();

    /**
     * Sets how many seconds means one animation step
     * @param num the number in seconds the animation step takes in application timer
     */
    void setTickMeansSec(int num);

    /**
     * Sets how many animation steps will the vehicles wait at stops
     * @param num the amount of animation steps vehicles will wait at stops
     */
    void setTicksAtStop(int num);

    /**
     * Sets all vehicles information Pane occupy value to false
     */
    void setAllVehiclesInformationPaneOccupyFalse();

    /**
     * Tells which vehicle currently occupies the information Pane
     * @return vehicle that occupies the information pane, null if none
     */
    Vehicle getVehicleOccupyInformationPaneTrue();

    /**
     * Tells if the animations are stopped
     * @return true if the animations are stopped
     */
    boolean getStopAnimator();

    /**
     * Sets animation stop value
     * @param stopAnimator true means stop false means run
     */
    void setStopAnimator(boolean stopAnimator);

    /**
     * Starts the animator thread and the animations
     */
    void playAnimator();

    /**
     * Sets the animation step delay
     * @param num is the animation step delay in milliseconds
     */
    void setAnimationStepDelay(int num);

    /**
     * Tells whats current animation step delay
     * @return animation step delay in milliseconds
     */
    int getAnimationStepDelay();

    /**
     * Tells how manny seconds means one animation step in application time
     * @return number that represents how manny seconds means one animation step in application time
     */
    int getTickMeansSec();

    /**
     * Switch direction of all vehicles in the public transport system
     */
    void switchDirectionAllVehicles();

    /**
     * Executes route check function on all line routes (check updateRouteByMap function in Route class)
     * @param mapCanvas is the Pane where the route highlights can be visible
     */
    void correctLineRoutes(Pane mapCanvas);

    /**
     * Tells how many ticks every vehicle waits on stops
     * @return number of ticks vehicles waits on stops
     */
    int getTicksAtStop();
}