import java.awt.*;
import java.util.LinkedList;

public class BezierCurve{

    LinkedList<Point> controlPts;

    int lol=0;

    int[] x=new int[100];
    int[] y=new int[100];
    public BezierCurve(int x1,int y1) {
        this.controlPts=new LinkedList<>();
        controlPts.add(new Point(x1,y1));

    }
    public BezierCurve()
    {

    }

    public void addControlPoint(Point p)
    {
        Point temp = controlPts.getLast();
        controlPts.removeLast();
        controlPts.add(p);
        controlPts.add(temp);
    }

    public void editPoint(int pointIndex, int newX, int newY){
        controlPts.set(pointIndex, new Point(newX, newY));
    }

    public void resetBezier(){
        x=new int[100];
        y=new int[100];
        controlPts = new LinkedList<>();
    }

    public void readBezierInConsole(){
        for(int i=0;i<controlPts.size(); i++)
        System.out.println("Punkt kontrolny nr "+i+": X: " + controlPts.get(i).x + " Y: " + controlPts.get(i).y);
    }

}