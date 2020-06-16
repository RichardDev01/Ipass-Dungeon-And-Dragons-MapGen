/*
https://stackoverflow.com/questions/7855387/percentage-of-two-int
 */


/*
Okay, ik denk dat de netste manier om dat te doen is: het aanpassen van de constructor van FsmRandomExtended en de lijsten
doorgeven vanuit Main (aangenomen dat je specifiek die lijsten al in Main nodig hebt en niet alleen in FsmRandomExtended)
 */
import javax.imageio.ImageIO;
import javax.swing.*;
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
        frame.setSize(400 ,640);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        ////~~~////

        //Creating array list for nodes and transistions
        ArrayList<Node> hallwaylist = new ArrayList<>();
        ArrayList<Node> hallwayEndlist = new ArrayList<>();
        ArrayList<Node> bigRoomlist = new ArrayList<>();
        ArrayList<Transistion> transistionlistHallway = new ArrayList<>();
        ArrayList<Transistion> transistionlistBigRoom = new ArrayList<>();

        //Creating Nodes (tilessets)
        //See schematic for meaning of N,NO,NZ... ect
        Node hallwayN = new Node(0,"hallwayN");
        Node hallwayNO = new Node(1,"hallwayNO");
        Node hallwayNZ = new Node(2,"hallwayNZ");
        Node hallwayNW = new Node(3,"hallwayNW");
        Node hallwayNOZ = new Node(4,"hallwayNOZ");
        Node hallwayNOW = new Node(5,"hallwayNOW");
        Node hallwayNOZW = new Node(6,"hallwayNOZW");
        Node hallwayNZW = new Node(7,"hallwayNZW");
        Node hallwayO = new Node(8,"hallwayO");
        Node hallwayOZ = new Node(9,"hallwayOZ");
        Node hallwayOW = new Node(10,"hallwayOW");
        Node hallwayOZW = new Node(11,"hallwayOZW");
        Node hallwayZ = new Node(12,"hallwayZ");
        Node hallwayZW = new Node(13,"hallwayZW");
        Node hallwayW = new Node(14,"hallwayW");

        //Adding hallways to list
        //Dev note, did not add dead ends to all node because it is handled in DrawMap Class

        //hallwaylist.add(hallwayN);
        hallwaylist.add(hallwayNO);
        hallwaylist.add(hallwayNZ);
        hallwaylist.add(hallwayNW);
        hallwaylist.add(hallwayNOZ);
        hallwaylist.add(hallwayNOW);
        hallwaylist.add(hallwayNOZW);
        hallwaylist.add(hallwayNZW);
        //hallwaylist.add(hallwayO);
        hallwaylist.add(hallwayOZ);
        hallwaylist.add(hallwayOW);
        hallwaylist.add(hallwayOZW);
        //hallwaylist.add(hallwayZ);
        hallwaylist.add(hallwayZW);
        //hallwaylist.add(hallwayW);

        hallwayEndlist.add(hallwayN);
        hallwayEndlist.add(hallwayO);
        hallwayEndlist.add(hallwayZ);
        hallwayEndlist.add(hallwayW);

        //Creating Nodes (tilessets)
        Node bigRoom1 = new Node(20,"bigRoom1");
        Node bigRoom2 = new Node(21,"bigRoom2");
        Node bigRoom3 = new Node(22,"bigRoom3");
        Node bigRoom4 = new Node(23,"bigRoom4");

        //Adding bigrooms to list
        bigRoomlist.add(bigRoom1);
        bigRoomlist.add(bigRoom2);
        bigRoomlist.add(bigRoom3);
        bigRoomlist.add(bigRoom4);

        //Creating Start and Endroom
        Node endRoom = new Node(30,"endRoom");
        Node startRoom = new Node(31,"startRoom");

        //User input stage ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //#TODO Replace with interface
        /*
        Scanner snrMaximaleGrote = new Scanner(System.in);
        System.out.println("Maximale opvervlakte?\n(9,16,25,36)");
        int  maxGroteTilesAantal = snrMaximaleGrote.nextInt();
         */
        int  maxGroteTilesAantal = 30;

        Scanner snrMinBigRooms = new Scanner(System.in);
        System.out.println("minimale aantal Bigrooms?\n(0,1,2,3,4) moet minder zijn dan maxaantal");
        int  aantalMinBigroom = snrMinBigRooms.nextInt();

        Scanner snrMaxBigRooms = new Scanner(System.in);
        System.out.println("Maximale Bigrooms?\n(1,2,3,4)");
        int  aantalBigroom = snrMaxBigRooms.nextInt();

        Scanner snrVerhouding = new Scanner(System.in);
        System.out.println("Welke verhouding van Hallways en Bigrooms wil je?\n(2,4,6,8)");
        int  aantalHalways = snrVerhouding.nextInt()*aantalBigroom;

        int  chanceForBigRoom = (aantalBigroom*100)/aantalHalways;
        //System.out.println(chanceForBigRoom);

        int chanceForHalway = 100-chanceForBigRoom;

        //creating hallway list
        int counterHalway =0;
        for (var h : hallwaylist){
            Transistion t1 = new Transistion(h,chanceForHalway,counterHalway);
            transistionlistHallway.add(t1);
            counterHalway++;
        }

        //adding hallways to hallways
        int counterBigroom = 20;
        for (var h : hallwaylist){
            //adding hallways to hallways
            for (var t : transistionlistHallway){

                //filtering and checking if certain tiles can be connected or not, this can be improved
                if (h.name.contains("NO") && t.n.name.contains("OZ")){
                    continue;
                }
                if (h.name.contains("NZ") && t.n.name.contains("OZ")){
                    continue;
                }
                if (h.name.contains("OZ") && t.n.name.contains("NO")){
                    continue;
                }
                if (h.name.contains("Z") && t.n.name.contains("N")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }
                if(h.name.contains("O") && t.n.name.contains("W")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }
                if(h.name.contains("N") && t.n.name.contains("Z")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }

            }
        }

        //Adding code for bigroom trasistions
        for (var br : bigRoomlist){
            for (var t : transistionlistHallway){
                if(t.n.name == "hallwayNW" || t.n.name == "hallwayZW"|| t.n.name == "hallwayOZ" || t.n.name == "hallwayNO"|| (t.n.name.contains("O"))==false){
                    continue;
                }
                bigRoomlist.get(bigRoomlist.indexOf(br)).addConnectie(t);
            }
        }


        //Adding chance for Endroom
        int endChance = (bigRoomlist.get(0).getSumMaxRandomnummer() + hallwaylist.get(0).getSumMaxRandomnummer())/10;
        Transistion end = new Transistion(endRoom,endChance,30);

        //Adding a end room
        for (var br : bigRoomlist){
            bigRoomlist.get(bigRoomlist.indexOf(br)).addConnectie(end);
        }

        //StartNode adding hallways
        for (var t : transistionlistHallway){
            if(t.n.name.contains("W")){
                startRoom.addConnectie(t);
            }
        }

        //Create 1 list with all nodes
        ArrayList<Node> allNodes = new ArrayList<>(bigRoomlist);
        allNodes.addAll(hallwaylist);
        allNodes.addAll(hallwayEndlist);

        //TODO, make this a run void in main
        //Creating the generator
        //RandomMapGen fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom);
        RandomMapGen fsmR2 = null;
        //DrawMap dm1 = new DrawMap(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom),allNodes,false);
        DrawMap dm1 = null;
        boolean check = false;
        int counterOfSimulations = 1;
        while (check==false && counterOfSimulations<10){
            System.out.println("sim: "+ counterOfSimulations);
            fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom,aantalHalways);
            dm1 = new DrawMap(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom),allNodes,false);
            dm1.nodesToString();
            check = dm1.run();
            counterOfSimulations++;
        }
        if (counterOfSimulations ==10){
            System.out.println("couldn't make map, plz try again");
            System.out.println("latest result has been dumped");
        }

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

        btnRender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //runMachine(); Dit is wat ik wil
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
                //writeMinBigValue(slrMinBigRooms.getValue());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
                //writeMinBigValue(slrMinBigRooms.getValue());
            }
        });
        slrMaxBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
                //writeMaxBigRoomsValue(slrMaxBigRooms.getValue());
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
    }


    public void setup(){
        // ik denk alle code hierheen om dit te laten werken?
    }

    public void runMachine(List<Node> allNodes, int aantalBigroom, int aantalMinBigroom, int aantalHalways, Node startRoom, int maxGroteTilesAantal, Node endRoom){

    //public void runMachine(){
        //Creating the generator
        RandomMapGen fsmR2 = null;
        DrawMap dm1 = null;
        boolean check = false;
        int counterOfSimulations = 1;
        while (check==false && counterOfSimulations<10){
            System.out.println("sim: "+ counterOfSimulations);
            fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom,aantalHalways);
            dm1 = new DrawMap(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom),allNodes,false);
            dm1.nodesToString();
            check = dm1.run();
            counterOfSimulations++;
        }
        if (counterOfSimulations ==10){
            System.out.println("couldn't make map, plz try again");
            System.out.println("latest result has been dumped");
        }
    }


}
