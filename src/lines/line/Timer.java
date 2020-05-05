package lines.line;

import lines.Iline.iTimer;

public class Timer implements iTimer {
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

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

    public int getSeconds(){ return this.seconds; }
    public int getMinutes(){ return this.minutes; }
    public int getHours(){ return this.hours; }
    public void set(int seconds, int minutes, int hours){
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
    }

}
