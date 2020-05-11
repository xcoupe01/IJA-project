package lines.Iline;

import lines.line.Timer;

/**
 * iTimer interface
 * implemented by class Timer
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iTimer {

    /**
     * Adds second to timer
     */
    void addSecond();

    /**
     * Subtracts second from timer
     */
    void subSecond();

    /**
     * Adds specified amount of seconds to timer
     * @param num is amount of seconds to be added (can be negative)
     */
    void addSeconds(int num);

    /**
     * Tells actual time seconds
     * @return actual time seconds
     */
    int getSeconds();

    /**
     * Tells actual time minutes
     * @return actual time minutes
     */
    int getMinutes();

    /**
     * Tells actual time hours
     * @return actual time hours
     */
    int getHours();

    /**
     * Sets timer to specified time
     * @param seconds is number of seconds (if the value is wrong it wont be added)
     * @param minutes is number of minutes (if the value is wrong it wont be added)
     * @param hours is number of hours (if the value is wrong it wont be added)
     */
    void set(int seconds, int minutes, int hours);

    /**
     * Tells if time of this timer is between the given time interval
     * @param timeStart is the start of the time interval
     * @param timeEnd is the end of time interval
     * @return true if it i in the time interval, false otherwise
     */
    boolean isTimeBetweenTwoTimes(Timer timeStart, Timer timeEnd);
}
