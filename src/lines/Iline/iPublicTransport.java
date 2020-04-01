package lines.Iline;

import lines.line.PTLine;

public interface iPublicTransport{
    void addLine(PTLine line);
    java.util.List<PTLine> getLines();
    void setTimer(int time);
}