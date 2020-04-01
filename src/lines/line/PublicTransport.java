package lines.line;

import javafx.scene.layout.Pane;
import lines.Iline.iPublicTransport;

public class PublicTransport implements iPublicTransport {
    private java.util.List<PTLine> lines = new java.util.ArrayList<>();
    private int timer;

    public PublicTransport(){

    }

    public void addLine(PTLine line){ this.lines.add(line); }
    public java.util.List<PTLine> getLines(){ return this.lines; }
    public void setTimer(int time){ this.timer = time; }
    public void run(Pane mapCanvas){
        while(true){
            for(int i = 0; i < this.lines.size(); i++){
                this.lines.get(i).rideVehicles(mapCanvas);
            }
        }
    }
}