package lines.line;

import javafx.scene.layout.Pane;
import lines.Iline.iPTConnection;
import map.maps.Map;

/**
 * PTConnection class
 * implements interface iPTConnection
 * Used to represent scheduled connections on map
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class PTConnection implements iPTConnection {

    /** Departure time of the scheduled connection*/
    private Timer departureTime;
    /** Arrival time of the scheduled connection*/
    private Timer arrivalTime;
    /** Assigned vehicle number*/
    private int vehicleNumber = -1;
    /** Vehicle orientation*/
    private boolean vehicleForward;
    /** Line connection*/
    private PTLine line;
    /** Map connection*/
    private Map mainMap;
    /** Public transport connection*/
    private PublicTransport mainPubTrans;
    /** tells if the scheduled connection fulfills 24 hours only duration*/
    private boolean active = false;
    /** Information pane connection*/
    private Pane informationPane;
    /** Map canvas connection*/
    private Pane mapCanvas;

    /**
     * Native constructor of PTConnection class
     * @param line is the line the scheduled connection is planed on
     * @param departureTime is the departure time
     * @param vehicleForward is the vehicle orientation
     * @param mainMap is the map connection
     * @param mainPubTrans is the public transport connection
     * @param informationPane is the information pane connection
     * @param mapCanvas is the map pane connection
     */
    public PTConnection(PTLine line, Timer departureTime, boolean vehicleForward, Map mainMap, PublicTransport mainPubTrans, Pane informationPane, Pane mapCanvas){
        this.line = line;
        this.mapCanvas = mapCanvas;
        this.informationPane = informationPane;
        this.departureTime = departureTime;
        this.vehicleForward = vehicleForward;
        this.mainMap = mainMap;
        this.mainPubTrans = mainPubTrans;
        this.refreshArrivalTime();
        if(this.active && this.mainPubTrans.getTimer().isTimeBetweenTwoTimes(this.departureTime, this.arrivalTime)){
            if(this.vehicleForward){
                this.line.addVehicle(0, this.mainPubTrans, this.mapCanvas);
            } else {
                this.line.addVehicle(this.line.getRoute().getRoute().size() - 1, this.mainPubTrans, this.mapCanvas);
            }
            this.vehicleNumber = this.line.getVehicles().get(line.getVehicles().size() - 1).getVehicleNumber();
            this.line.getVehicles().get(line.getVehicles().size() - 1).setForward(this.vehicleForward);
            this.line.getVehicles().get(line.getVehicles().size() - 1).setScheduled();
            this.line.getVehicles().get(line.getVehicles().size() - 1).draw();
            this.line.getVehicles().get(line.getVehicles().size() - 1).setInformationPane(this.informationPane);
            Timer tmpTimer = new Timer();
            tmpTimer.set(0, this.departureTime.getMinutes(), this.departureTime.getHours());
            while(!(this.mainPubTrans.getTimer().getHours() == tmpTimer.getHours() &&
                    this.mainPubTrans.getTimer().getMinutes() == tmpTimer.getMinutes() &&
                    this.mainPubTrans.getTimer().getSeconds() < tmpTimer.getSeconds())){
                tmpTimer.addSeconds(mainPubTrans.getTickMeansSec());
                this.line.getVehicles().get(line.getVehicles().size() - 1).ride();
            }
        }
    }

    /**
     * Calculates arrival time and sets active value based on current map and public transport state
     */
    public void refreshArrivalTime(){
        Timer tmpTimer = new Timer();
        int tickCounter = 0;
        Vehicle tmpVehicle;
        this.active = false;
        tmpTimer.set(this.departureTime.getSeconds(), this.departureTime.getMinutes(), this.departureTime.getHours());
        tmpVehicle = new Vehicle(this.line, line.getRoute().getRoute().get(0), 0, this.mainMap, this.mainPubTrans, null);
        tmpVehicle.setForward(this.vehicleForward);
        while(!tmpVehicle.getStartCoordinate().equals(line.getRoute().getRoute().get(this.line.getRoute().getRoute().size() - 1))){
            tmpVehicle.ride();
            tickCounter ++;
        }
        if(tickCounter * this.mainPubTrans.getTickMeansSec() < 86400){
            this.active = true;
        }
        tmpTimer.addSeconds(tickCounter * this.mainPubTrans.getTickMeansSec());
        this.arrivalTime = tmpTimer;
    }

    /**
     * Checks if any of scheduled connections should depart or arrive
     */
    public void tickCheck(){
        Timer mainTimer = this.mainPubTrans.getTimer();
        if(this.mainPubTrans.getAnimationForward()){
            if(mainTimer.getHours() == departureTime.getHours() &&
                    mainTimer.getMinutes() == departureTime.getMinutes() &&
                    this.active && this.vehicleNumber == -1){
                if(this.vehicleForward){
                    this.line.addVehicle(0, this.mainPubTrans, this.mapCanvas);
                } else {
                    this.line.addVehicle(this.line.getRoute().getRoute().size() - 1, this.mainPubTrans, this.mapCanvas);
                }
                this.vehicleNumber = this.line.getVehicles().get(line.getVehicles().size() - 1).getVehicleNumber();
                this.line.getVehicles().get(line.getVehicles().size() - 1).setForward(this.vehicleForward);
                this.line.getVehicles().get(line.getVehicles().size() - 1).setScheduled();
                this.line.getVehicles().get(line.getVehicles().size() - 1).draw();
                this.line.getVehicles().get(line.getVehicles().size() - 1).setInformationPane(this.informationPane);
            }
            if(this.vehicleNumber != -1){
                if(this.vehicleForward){
                    if(this.line.getVehicleByNumber(this.vehicleNumber).getStartCoordinate().equals(this.line.getRoute().getRoute().get(line.getRoute().getRoute().size() - 1))){
                        line.getVehicleByNumber(this.vehicleNumber).erase();
                        line.getVehicleByNumber(this.vehicleNumber).removeProcedure();
                        this.line.removeVehicleByNumber(this.vehicleNumber);
                        this.vehicleNumber = -1;
                    }
                } else {
                    if(this.line.getVehicleByNumber(this.vehicleNumber).getStartCoordinate().equals(this.line.getRoute().getRoute().get(0))){
                        line.getVehicleByNumber(this.vehicleNumber).erase();
                        line.getVehicleByNumber(this.vehicleNumber).removeProcedure();
                        this.line.removeVehicleByNumber(this.vehicleNumber);
                        this.vehicleNumber = -1;
                    }
                }
            }
        } else {
            if(mainTimer.getHours() == this.arrivalTime.getHours() &&
                    mainTimer.getMinutes() == this.arrivalTime.getMinutes() &&
                    this.active && this.vehicleNumber == -1){
                if(!this.vehicleForward){
                    this.line.addVehicle(0, this.mainPubTrans, this.mapCanvas);
                } else {
                    this.line.addVehicle(this.line.getRoute().getRoute().size() - 1, this.mainPubTrans, this.mapCanvas);
                }
                this.vehicleNumber = this.line.getVehicles().get(line.getVehicles().size() - 1).getVehicleNumber();
                this.line.getVehicles().get(line.getVehicles().size() - 1).setForward(!this.vehicleForward);
                this.line.getVehicles().get(line.getVehicles().size() - 1).setScheduled();
                this.line.getVehicles().get(line.getVehicles().size() - 1).draw();
                this.line.getVehicles().get(line.getVehicles().size() - 1).setInformationPane(this.informationPane);
            }
            if(this.vehicleNumber != -1){
                if(!this.vehicleForward){
                    if(this.line.getVehicleByNumber(this.vehicleNumber).getStartCoordinate().equals(this.line.getRoute().getRoute().get(line.getRoute().getRoute().size() - 1))){
                        line.getVehicleByNumber(this.vehicleNumber).erase();
                        line.getVehicleByNumber(this.vehicleNumber).removeProcedure();
                        this.line.removeVehicleByNumber(this.vehicleNumber);
                        this.vehicleNumber = -1;
                    }
                } else {
                    if(this.line.getVehicleByNumber(this.vehicleNumber).getStartCoordinate().equals(this.line.getRoute().getRoute().get(0))){
                        line.getVehicleByNumber(this.vehicleNumber).erase();
                        line.getVehicleByNumber(this.vehicleNumber).removeProcedure();
                        this.line.removeVehicleByNumber(this.vehicleNumber);
                        this.vehicleNumber = -1;
                    }
                }
            }
        }
    }

    /**
     * Returns the planned departure time
     * @return the planned departure time
     */
    public Timer getDepartureTime(){ return this.departureTime; }

    /**
     * Tells the planned vehicle orientation
     * @return the planned vehicle orientation
     */
    public boolean getVehicleForward(){ return this.vehicleForward; }

    /**
     * Prepares connection for deletion
     */
    public void removeProcedure(){
        if(this.vehicleNumber != -1){
            this.line.getVehicleByNumber(this.vehicleNumber).removeProcedure();
        }
        this.line.removeVehicleByNumber(this.vehicleNumber);
    }

    /**
     * Tells the current assigned vehicle number
     * @return assigned vehicle number
     */
    public int getVehicleNumber(){ return this.vehicleNumber; }
}
