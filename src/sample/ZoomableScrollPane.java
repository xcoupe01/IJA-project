package sample;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * ZoomableScrollPane class
 * implements interface ScrollPane
 * Used to make zooming to map possible
 * Found on https://stackoverflow.com/questions/39827911/javafx-8-scaling-zooming-scrollpane-relative-to-mouse-position
 * Slightly modified to match our needs
 *
 * @author Daniel Hári
 * edited by Vojtěch Čoupek (xcoupe01) and Tadeáš Jůza (xjuzat00)
 */
public class ZoomableScrollPane extends ScrollPane {

    /** Current scale*/
    private double scaleValue = 1;
    /** Zoom intensity of mouse wheel*/
    private double zoomIntensity = 0.001;
    /** Target node to be zoomed*/
    private Node target;
    /** Is the zoom node to be displayed*/
    private Node zoomNode;

    /**
     * Native constructor of ZoomableScrollPane class
     * @param target is the node that we want to make zoomable
     */
    public ZoomableScrollPane(Node target) {
        super();
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center

        updateScale();
    }

    /**
     * Adds the target listener for zooming
     * @param node is the node that we want to add the zoom to
     * @return the zoomable node
     */
    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    /**
     * Creates wrap node around our target
     * @param node node that's going to be wrapped
     * @return wrap with target node
     */
    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setPrefWidth(this.getPrefWidth());
        vBox.setPrefHeight(this.getPrefHeight());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    /**
     * Scales the target as needed
     */
    private void updateScale() {
        this.target.setScaleX(scaleValue);
        this.target.setScaleY(scaleValue);
    }

    /**
     * Function that makes the zoom possible
     * @param wheelDelta is the mouse wheel scroll amount
     * @param mousePoint is the position of the mouse
     */
    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        if(this.scaleValue *  zoomFactor > 0.5) {
            Bounds innerBounds = zoomNode.getLayoutBounds();
            Bounds viewportBounds = getViewportBounds();

            // calculate pixel offsets from [0, 1] range
            double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
            double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

            scaleValue = scaleValue * zoomFactor;
            updateScale();
            this.layout(); // refresh ScrollPane scroll positions & target bounds

            // convert target coordinates to zoomTarget coordinates
            Point2D posInZoomTarget = this.target.parentToLocal(zoomNode.parentToLocal(mousePoint));

            // calculate adjustment of scroll position (pixels)
            Point2D adjustment = this.target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

            // convert back to [0, 1] range
            // (too large/small values are automatically corrected by ScrollPane)
            Bounds updatedInnerBounds = this.zoomNode.getBoundsInLocal();
            this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
            this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
        }
    }
}
