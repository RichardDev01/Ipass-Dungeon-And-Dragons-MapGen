/*
https://stackoverflow.com/questions/7855387/percentage-of-two-int
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private JButton btnRender;
    private JPanel panel1;
    private JSlider slrRatioBigRooms;
    private JSlider slrMaxBigRooms;
    private JSlider slrMinBigRooms;
    private JPanel JpanelImage;
    private JLabel lbimg;
    private JSlider slrMaxRenderSize;
    private JLabel lblMaxRenderSize;
    private JLabel lblValueMinBigRooms;
    private JLabel lblValueMaxBigRooms;
    private JLabel lblValueRatioBetweenBigRooms;
    private JCheckBox checkBoxRunning;
    private JTextField filePathTextbox;
    public int value1;
    public int value2;
    public int value3;
    public int value4;

    //Hier denk ik alle variablen declareren?
    //private Node hallwayN;
    //private static Node hallwayN;

    public static void main(String[] args) {
        //~~~~ interface code
        JFrame frame = new JFrame("GUIDnDMapGen");
        frame.setContentPane(new Main().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400 ,720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        ////~~~////


        //~~~~ till here

        //This is for debug purpose only, delete when done
        //Debug Array
//        ArrayList<Node> debugNodeslist = new ArrayList<>();
//
//        debugNodeslist.add(startRoom);
//        debugNodeslist.add(hallwayNOZW);
//        debugNodeslist.add(bigRoom1);
//        debugNodeslist.add(hallwayNOZW);
//        debugNodeslist.add(hallwayOW);
//        debugNodeslist.add(hallwayZW);
//        debugNodeslist.add(bigRoom1);
//
//        debugNodeslist.add(endRoom);
//
//        DrawMap dm1Debug = new DrawMap(debugNodeslist,allNodes,false);
//        dm1Debug.nodesToString();
        //dm1Debug.run();
        //~~~~~till here

    }

    public Main() {
        BufferedImage img =null;
        try {img = ImageIO.read(new File("logo.png"));} catch (IOException e) { e.printStackTrace(); }
        ImageIcon icon = new ImageIcon(img);
        lbimg.setIcon(icon);
        lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
        lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
        lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
        lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));

        Generator gen1 = new Generator();
        btnRender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBoxRunning.setSelected(true);
            }
        });


        slrMaxRenderSize.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
            }
        });
        slrMinBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
            }
        });
        slrMaxBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
            }
        });
        slrRatioBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));
            }
        });

        checkBoxRunning.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
                    int aantalBigroom = slrMaxBigRooms.getValue();
                    int aantalHalways = slrRatioBigRooms.getValue();
                    int aantalMinBigRooms = slrMinBigRooms.getValue();
                    int maxGroteTilesAantal = slrMaxRenderSize.getValue();
                    String FilePath = filePathTextbox.getText();

                    if (aantalBigroom < aantalMinBigRooms){
                        aantalBigroom = aantalMinBigRooms;
                    }

                    System.out.println(aantalBigroom+" "+aantalHalways+" "+aantalMinBigRooms+" "+maxGroteTilesAantal);

                    gen1.generate(aantalBigroom,aantalHalways,aantalMinBigRooms,maxGroteTilesAantal,FilePath);
                    //gen1.generate(aantalBigroom,aantalHalways+2,aantalMinBigRooms,maxGroteTilesAantal);
                }

                checkBoxRunning.setSelected(false);
            }
        });
    }
}
