import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MainFrame extends JFrame {

    int selectedPointIndex=-1;

    MouseListener mouseListener;
    CanvasPanel canvasPanel=new CanvasPanel();
    int currentX, currentY;

    boolean settingPointsMode = false;
    int x1,x2,y1;

    int maxPointsNumber,currentPointsNumber;

BezierCurve bezierCurve=new BezierCurve();
    JPanel toolsPanel = new JPanel();

    public MainFrame(){
        setSize(1000,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Rysowanie Krzywej Beziera");
        Container content = getContentPane();
        content.setLayout(new BorderLayout());

        content.add(canvasPanel, BorderLayout.CENTER);

        initToolsPanel();

        content.add(toolsPanel, BorderLayout.NORTH);

        initMouseEditTool();

        setVisible(true);



      //  add(canvasPanel);
    }

    private void initMouseEditTool() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!settingPointsMode) {

                    selectPointId(new Point(currentX, currentY));

                }else System.out.println("nie wyszukuje punktu -> nie skończono ustawiać");

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                canvasPanel.setBezierCurve(bezierCurve);
                canvasPanel.repaint();
                selectedPointIndex=-1;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void selectPointId(Point point){

        boolean foundAPoint=false;
        for (Point currentlyChekingPoint: bezierCurve.controlPts
             ) {
            System.out.println("Wyszukuję punkt na pozycji X:"+point.x+" Y: "+point.y);
            System.out.println("Porównywany punkt z krzywej X:"+ currentlyChekingPoint.x+" Y: "+ currentlyChekingPoint.y);

            //sprawdzanie kliknięcia w odległości mniejszej niż 20
            if(((Math.abs(point.x-currentlyChekingPoint.x)<20)&&(Math.abs(point.y-currentlyChekingPoint.y)<20))) {

                System.out.println("Odnaleziono punkt!");
                foundAPoint=true;
                selectedPointIndex = bezierCurve.controlPts.indexOf(currentlyChekingPoint);
                break;
            }

        }
        if(!foundAPoint){
            System.out.println("Nie odnaleziono punktu :C");
            selectedPointIndex=-1;
        }
    }

    private void initToolsPanel() {
        toolsPanel.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolsPanel.add(toolBar, BorderLayout.PAGE_START);
        JButton confirmPointAmountButton = new JButton("Potwierdź Ilość");
        Dimension dimension = new Dimension(50,100);
        JTextField pointsNumber = new JTextField("5");
        pointsNumber.setColumns(2);
        pointsNumber.setMaximumSize(dimension);
        toolBar.add(confirmPointAmountButton);
        toolBar.add(pointsNumber);

        JLabel mousePositionInfoLabel = new JLabel(" Pozycja myszki");
        JLabel mousePositionXLabel = new JLabel(" X: 100 ");
        JLabel mousePositionYLabel = new JLabel(" Y: 200 ");
        mousePositionChecker(mousePositionXLabel, mousePositionYLabel);

        toolBar.add(mousePositionInfoLabel);
        toolBar.add(mousePositionXLabel);
        toolBar.add(mousePositionYLabel);

        JButton confirmPointPositionButton = new JButton("Potwierdź nowy punkt:");
        toolBar.add(confirmPointPositionButton);

        JTextField newPointXField = new JTextField("100");
        JTextField newPointYField = new JTextField("100");
        newPointXField.setMaximumSize(dimension);
        newPointYField.setMaximumSize(dimension);

        toolBar.add(newPointXField);
        toolBar.add(newPointYField);

        confirmPointAmountButton.addActionListener(e -> ConfirmedPointsAmount(Integer.parseInt(pointsNumber.getText())));
        confirmPointPositionButton.addActionListener(e-> ConfirmedNewPointPosition(Integer.parseInt(newPointXField.getText()),Integer.parseInt(newPointYField.getText())));

        JButton editPointButton = new JButton("Edytuj punkt tekstowo:");
        toolBar.add(editPointButton);

        JTextField editPointNumberField = new JTextField("1");
        JTextField editPointXField = new JTextField("100");
        JTextField editPointYField = new JTextField("100");



        editPointNumberField.setMaximumSize(dimension);
        editPointXField.setMaximumSize(dimension);
        editPointYField.setMaximumSize(dimension);

        editPointButton.addActionListener(e -> editPointPositionUsingTextBox(Integer.parseInt(editPointNumberField.getText()), Integer.parseInt(editPointXField.getText()), Integer.parseInt(editPointYField.getText())));



        toolBar.add(editPointNumberField);
        toolBar.add(editPointXField);
        toolBar.add(editPointYField);

    }



    private void editPointPositionUsingTextBox(int pointNumber, int newX, int newY) {

        if(pointNumber==1){
            bezierCurve.editPoint(maxPointsNumber-1, newX, newY);
        }else {
            bezierCurve.editPoint(pointNumber - 2, newX, newY);
        }
        bezierCurve.readBezierInConsole();
        canvasPanel.setBezierCurve(bezierCurve);
        canvasPanel.PaintBezierCurve(bezierCurve);

    }


    private void ConfirmedNewPointPosition(int x, int y) {
        System.out.println("Potwierdzono przyciskiem punkt X:  " + x + " Y: "+y);
        if (currentPointsNumber <= maxPointsNumber) {
            if (currentPointsNumber == 1) {
                System.out.println("Zapisano X1 i Y1");

                bezierCurve = new BezierCurve(x, y);
                canvasPanel.setBezierCurve(bezierCurve);
                canvasPanel.PaintBezierCurve(bezierCurve);

            } else {
                System.out.println("Zapisano Kolejny X i Y");
                System.out.println("Rysuję Beziera");
                bezierCurve.addControlPoint(new Point(x, y));
                canvasPanel.setBezierCurve(bezierCurve);
                canvasPanel.PaintBezierCurve(bezierCurve);
            }
            System.out.println("Narysowano łącznie " + String.valueOf(currentPointsNumber) + " punktów");
            if (currentPointsNumber != maxPointsNumber)
                System.out.println("Przechodzę do punktu " + String.valueOf(currentPointsNumber + 1));
            currentPointsNumber++;

            if (currentPointsNumber == maxPointsNumber) settingPointsMode = false;
        }
    }


    private void ConfirmedPointsAmount(int pointsNumber) {
        removeMouseListener(mouseListener);
        canvasPanel.clear();
        mouseListener=null;
        settingPointsMode=true;

        System.out.println("Zaczynam Rysować Krzywą o "+pointsNumber+" punktach");
        x1=-1;
        x2=-1;
        maxPointsNumber=pointsNumber;
        currentPointsNumber=1;
        bezierCurve=new BezierCurve();
        bezierCurve.resetBezier();
        canvasPanel.setBezierCurve(bezierCurve);

        addMouseListener(mouseListener=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(currentPointsNumber<=maxPointsNumber) {
                    if (currentPointsNumber==1) {
                        System.out.println("Zapisano X1 i Y1");
                        x1 = e.getX();
                        y1 = e.getY();
                        bezierCurve = new BezierCurve(x1, y1);
                        canvasPanel.setBezierCurve(bezierCurve);
                        canvasPanel.PaintBezierCurve(bezierCurve);

                    } else {
                        System.out.println("Zapisano Kolejny X i Y");
                        System.out.println("Rysuję Beziera");
                        bezierCurve.addControlPoint(new Point(e.getX(), e.getY()));
                        canvasPanel.setBezierCurve(bezierCurve);
                        canvasPanel.PaintBezierCurve(bezierCurve);
                    }
                    System.out.println("Narysowano łącznie "+String.valueOf(currentPointsNumber)+" punktów");
                    if(currentPointsNumber!=maxPointsNumber)System.out.println("Przechodzę do punktu "+String.valueOf(currentPointsNumber+1));
                    currentPointsNumber++;
                    if(currentPointsNumber==maxPointsNumber)settingPointsMode=false;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void mousePositionChecker(JLabel xLabel, JLabel yLabel){
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xLabel.setText(" X: "+String.valueOf(e.getX()));
                yLabel.setText(" Y: "+String.valueOf(e.getY()));
                currentX=e.getX();
                currentY=e.getY();

                if(selectedPointIndex!=-1){
                    System.out.println("EDYTUJĘ W CZASIE RZECZYWISTYM");
                    System.out.println("Przenoszę punkt X: "+bezierCurve.controlPts.get(selectedPointIndex).x+" Y: "+bezierCurve.controlPts.get(selectedPointIndex).y);
                    System.out.println("Na pozycję X: "+currentX+" Y: "+currentY);
                    bezierCurve.editPoint(selectedPointIndex, currentX, currentY);
                    canvasPanel.setBezierCurve(bezierCurve);
                    canvasPanel.PaintBezierCurve(bezierCurve);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                xLabel.setText(" X: "+String.valueOf(e.getX()));
                yLabel.setText(" Y: "+String.valueOf(e.getY()));
                currentX=e.getX();
                currentY=e.getY();
                canvasPanel.repaint();
                if(selectedPointIndex!=-1) {
                    canvasPanel.PaintBezierCurve(bezierCurve);
                }


            }
        });
    }


}
