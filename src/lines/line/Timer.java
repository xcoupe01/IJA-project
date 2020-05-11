package lines.line;

import lines.Iline.iTimer;

/**
 * Timer class
 * implements interface iTimer
 * Used to represent time
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Timer implements iTimer {

    /** Seconds, max value 59*/
    private int seconds = 0;
    /** Minutes equals 60 seconds, max value 59*/
    private int minutes = 0;
    /** Hours equals 60 minutes, max value 23*/
    private int hours = 0;

    /**
     * Adds second to timer
     */
    public void addSecond(){
        this.seconds++;
        if(this.seconds >= 60){
            this.minutes ++;
            this.seconds -= 60;
        }
        if(this.minutes >= 60){
            this.hours ++;
            this.minutes -= 60;
        }
        if(this.hours >= 24){
            this.hours -= 24;
        }
    }

    /**
     * Subtracts second from timer
     */
    public void subSecond(){
        this.seconds --;
        if(this.seconds < 0){
            this.seconds += 60;
            this.minutes --;
        }
        if(this.minutes < 0){
            this.minutes += 60;
            this.hours --;
        }
        if(this.hours < 0){
            this.hours += 24;
        }
    }

    /**
     * Adds specified amount of seconds to timer
     * @param num is amount of seconds to be added (can be negative)
     */
    public void addSeconds(int num){
        int i;
        if(num > 0){
            for(i = 0; i < num; i++){
                this.addSecond();
            }
        }
        if(num < 0){
            for(i = 0; i > num; i--){
                this.subSecond();
            }
        }
    }

    /**
     * Tells actual time seconds
     * @return actual time seconds
     */
    public int getSeconds(){ return this.seconds; }

    /**
     * Tells actual time minutes
     * @return actual time minutes
     */
    public int getMinutes(){ return this.minutes; }

    /**
     * Tells actual time hours
     * @return actual time hours
     */
    public int getHours(){ return this.hours; }

    /**
     * Sets timer to specified time
     * @param seconds is number of seconds (if the value is wrong it wont be added)
     * @param minutes is number of minutes (if the value is wrong it wont be added)
     * @param hours is number of hours (if the value is wrong it wont be added)
     */
    public void set(int seconds, int minutes, int hours){
        if(seconds >= 0 && seconds < 60){
            this.seconds = seconds;
        }
        if(minutes >= 0 && minutes < 60){
            this.minutes = minutes;
        }
        if(hours >= 0 && hours < 60){
            this.hours = hours;
        }
    }

    /**
     * Tells if time of this timer is between the given time interval
     * @param timeStart is the start of the time interval
     * @param timeEnd is the end of time interval
     * @return true if it i in the time interval, false otherwise
     */
    public boolean isTimeBetweenTwoTimes(Timer timeStart, Timer timeEnd){
        Timer begin = new Timer();
        begin.set(timeStart.getSeconds(), timeStart.getMinutes(), timeStart.getHours());
        while(!(begin.getHours() == timeEnd.getHours() &&
                begin.getMinutes() == timeEnd.getMinutes() &&
                begin.getSeconds() == timeEnd.getSeconds())){
            begin.addSecond();
            if(this.hours == begin.getHours() && this.minutes == begin.getMinutes() && this.seconds == begin.getSeconds()){
                return true;
            }
        }
        return false;
    }

}
