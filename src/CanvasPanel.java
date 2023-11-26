import javax.swing.*;
import java.awt.*;


public class CanvasPanel extends JPanel {

    private Image image;
    private Graphics2D g2;
    private Graphics g;
    int curvePrecision = 100;

    BezierCurve bezierCurve = new BezierCurve();

    private int width=1000;
    private int height=500;


    public CanvasPanel(){
        setDoubleBuffered(false);
        setVisible(true);

    }
    @Override
    public void paint(Graphics g){
        paintComponent(g);

    }
    @Override
    protected void paintComponent(Graphics g) {
        if(image==null){
            System.out.println("Image=null, drawing a clear Image");

            image=createImage(width,height);
            g2=(Graphics2D)image.getGraphics();

            clear();
        }
        g.drawImage(image,0,0,null);


    }

    public void clear() {
        System.out.println("Clearing");
        g2.setPaint(Color.white);

        g2.fillRect(0,0,getSize().width,getSize().height);
        g2.setPaint(Color.black);
        repaint();

    }




    public void PaintBezierCurve(BezierCurve bezierCurve){

        clear();
        calculateBezierCurve(bezierCurve);
        g2.setColor(Color.red);

        for(int i=0;i<bezierCurve.controlPts.size();i++)
        {
            g2.drawRect(bezierCurve.controlPts.get(i).x,bezierCurve.controlPts.get(i).y-60,6,6);
            g2.fillRect(bezierCurve.controlPts.get(i).x,bezierCurve.controlPts.get(i).y-60,6,6);
        }

        g2.setColor(Color.black);
        g2.drawPolyline(bezierCurve.x, bezierCurve.y,bezierCurve.x.length );
    }

    public void calculateBezierCurve(BezierCurve bezierCurve) {

        int n=bezierCurve.controlPts.size() - 1;
        bezierCurve.x=new int[curvePrecision];
        bezierCurve.y=new int[curvePrecision];

        for(int i = 0; i < curvePrecision; i++) {
            double t=(double)i/(curvePrecision-1);
            int x=0;
            int y=0;

            for(int j=0;j<=n;j++) {
                //wedle wozru podanego na wykładzie
                double bernsteinBasePolynomial = symbNewt(n,j)*Math.pow(t,j)*Math.pow(1-t,n-j);
                x+=bernsteinBasePolynomial * bezierCurve.controlPts.get(j).x;
                y+=bernsteinBasePolynomial * bezierCurve.controlPts.get(j).y;
            }
            bezierCurve.x[i]=x;
            bezierCurve.y[i]=y-60;
        }
    }
    public void setBezierCurve(BezierCurve bezierCurve){
        this.bezierCurve=bezierCurve;
        System.out.println("Ustawiono BezierCurve w CanvasPanel!");
    }
    private static int symbNewt(int n, int k) {
        if(k==0||k==n)return 1;
        else return symbNewt(n - 1, k - 1) + symbNewt(n - 1, k);
    }


}
