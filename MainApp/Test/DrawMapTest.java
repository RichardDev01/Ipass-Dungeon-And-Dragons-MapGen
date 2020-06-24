import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import static org.junit.Assert.*;

public class DrawMapTest {
    Node hallwayN;
    Node hallwayNO;
    Node hallwayNZ;
    Node hallwayNW;
    Node hallwayNOZ;

    Node hallwayNOW;

    Node hallwayNOZW;
    Node hallwayNZW;
    Node hallwayO;
    Node hallwayOZ;
    Node hallwayOW;
    Node hallwayOZW;
    Node hallwayZ;
    Node hallwayZW;
    Node hallwayW;


    Node bigRoom1;

    Node endRoom;
    Node startRoom;

    Transistion t1;
    Transistion t2;
    Transistion t3;
    Transistion t4;
    Transistion t5;
    Transistion t6;
    Transistion t7;

    ArrayList<Node> allNodes;
    String FilePath;

    @Before
    public void Setup(){

        String FilePath = "./Resources/default";
        hallwayN = new Node(0,"hallwayN");
        hallwayNO = new Node(1,"hallwayNO");
        hallwayNZ = new Node(2,"hallwayNZ");
        hallwayNW = new Node(3,"hallwayNW");
        hallwayNOZ = new Node(4,"hallwayNOZ");
        hallwayNOW = new Node(5,"hallwayNOW");
        hallwayNOZW = new Node(6,"hallwayNOZW");
        hallwayNZW = new Node(7,"hallwayNZW");
        hallwayO = new Node(8,"hallwayO");
        hallwayOZ = new Node(9,"hallwayOZ");
        hallwayOW = new Node(10,"hallwayOW");
        hallwayOZW = new Node(11,"hallwayOZW");
        hallwayZ = new Node(12,"hallwayZ");
        hallwayZW = new Node(13,"hallwayZW");
        hallwayW = new Node(14,"hallwayW");



        bigRoom1 = new Node(20,"bigRoom1");

        endRoom = new Node(30,"endRoom");
        startRoom = new Node(31,"startRoom");

        t1 = new Transistion(hallwayNOW,10,0);
        t2 = new Transistion(hallwayOZ,10,1);
        t3 = new Transistion(hallwayOW,10,2);
        t4 = new Transistion(hallwayOZW,10,3);
        t5 = new Transistion(bigRoom1,10,20);
        t6 = new Transistion(endRoom,10,30);
        t7 = new Transistion(hallwayNZ,10,4);

        hallwayNZ.addConnectie(t1);
        hallwayNOW.addConnectie(t2);
        hallwayOZ.addConnectie(t3);
        hallwayOW.addConnectie(t4);
        hallwayOZW.addConnectie(t5);
        bigRoom1.addConnectie(t6);
        startRoom.addConnectie(t7);

        allNodes = new ArrayList<>();

        allNodes.add(hallwayNZ);
        allNodes.add(hallwayNOW);
        allNodes.add(hallwayOZ);
        allNodes.add(hallwayOW);
        allNodes.add(hallwayOZW);
        allNodes.add(bigRoom1);

        ArrayList<Node> checkNodes = new ArrayList<>();
        checkNodes.add(startRoom);
        checkNodes.addAll(allNodes);
        checkNodes.add(endRoom);
    }


    @Test
    public void addImages() {
        DrawMap dm1 = new DrawMap(allNodes,allNodes,true,FilePath,false);
        dm1.addImages("./Resources/Default/Hallways/hallwayNOW/hallwayNOW.png");
        assertEquals("./Resources/Default/Hallways/hallwayNOW/hallwayNOW.png",dm1.images.get(dm1.images.size()-1));
    }

    @Test
    public void nodesToString() {
        FilePath = "./Resources/Default";
        ArrayList<Node> checkNodes = new ArrayList<>();
        checkNodes.add(startRoom);
        checkNodes.addAll(allNodes);
        checkNodes.add(endRoom);
        DrawMap dm1 = new DrawMap(checkNodes,checkNodes,true,FilePath,false);
        dm1.nodesToString();
        int index =0;
        for(String path : dm1.images){
            assertTrue(path.contains(checkNodes.get(index).name));
            index++;
        }
    }

    @Test
    public void checkPath() {
        FilePath = "./Resources/Default";
        ArrayList<Node> checkNodes = new ArrayList<>();
        checkNodes.add(startRoom);
        checkNodes.addAll(allNodes);
        checkNodes.add(endRoom);

        ArrayList<Node> wayToEnd = new ArrayList<>();

        wayToEnd.add(startRoom);
        wayToEnd.add(hallwayNOW);
        wayToEnd.add(hallwayNW);
        wayToEnd.add(bigRoom1);
        wayToEnd.add(endRoom);

        DrawMap dm1 = new DrawMap(wayToEnd,checkNodes,true,FilePath,false);
        dm1.nodesToString();
        assertFalse(dm1.getWayToTheEndRoomChecked() == wayToEnd);
        wayToEnd.add(3,hallwayNOZ);
        assertEquals(dm1.getWayToTheEndRoomChecked().size(), wayToEnd.size());
        //assertTrue(dm1.getWayToTheEndRoomChecked() == wayToEnd); //java is derpy af
    }

    @Test
    public void run() {
        FilePath = "../Resources/default";
        ArrayList<Node> checkNodes = new ArrayList<>();
        checkNodes.add(startRoom);
        checkNodes.addAll(allNodes);
        checkNodes.add(endRoom);

        ArrayList<Node> wayToEnd = new ArrayList<>();

        wayToEnd.add(startRoom);
        wayToEnd.add(hallwayNOZW);
        wayToEnd.add(hallwayNZW);
        wayToEnd.add(bigRoom1);
        wayToEnd.add(hallwayNOZ);
        wayToEnd.add(hallwayNW);
        wayToEnd.add(bigRoom1);
        wayToEnd.add(endRoom);

        DrawMap dm1 = new DrawMap(wayToEnd,checkNodes,true,FilePath,true);
        dm1.nodesToString();
        dm1.run();

        //Check of result == stock image

    }

    @Test
    public void checkFreeSpace() {
        FilePath = "../Resources/Default";
        ArrayList<Node> checkNodes = new ArrayList<>();
        checkNodes.add(startRoom);
        checkNodes.addAll(allNodes);
        checkNodes.add(endRoom);
        DrawMap dm1 = new DrawMap(checkNodes,checkNodes,true,FilePath,true);

        BufferedImage debugImg =null;
        String FilePathToImage = "../Resources/debug/debugpic.png";

        try { debugImg = ImageIO.read(new File(FilePathToImage)); } catch (IOException e) { e.printStackTrace(); }

        assertTrue(dm1.checkFreeSpace(50,50,debugImg)); //Top left
        assertFalse(dm1.checkFreeSpace(debugImg.getWidth()-2,50,debugImg)); //top Right
        assertTrue(dm1.checkFreeSpace(debugImg.getWidth()-2,debugImg.getHeight()-2,debugImg));
        assertFalse(dm1.checkFreeSpace(50,debugImg.getHeight()-2,debugImg));
    }
}