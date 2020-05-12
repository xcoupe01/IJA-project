package lines.Iline;

import lines.line.Timer;

/**
 * PTConnection interface
 * implemented by PTConnection
 * Used to represent scheduled connections on map
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iPTConnection {

    /**
     * Calculates arrival time and sets active value based on current map and public transport state
     */
    void refreshArrivalTime();

    /**
     * Checks if any of scheduled connections should depart or arrive
     */
    void tickCheck();

    /**
     * Returns the planned departure time
     * @return the planned departure time
     */
    Timer getDepartureTime();

    /**
     * Tells the planned vehicle orientation
     * @return the planned vehicle orientation
     */
    boolean getVehicleForward();

    /**
     * Prepares connection for deletion
     */
    void removeProcedure();

    /**
     * Tells the current assigned vehicle number
     * @return assigned vehicle number
     */
    int getVehicleNumber();
}
