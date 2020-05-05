package lines.Iline;

public interface iTimer {

    void addSecond();
    void addSeconds(int num);
    int getSeconds();
    int getMinutes();
    int getHours();
    void subSecond();
    void set(int seconds, int minutes, int hours);
}
