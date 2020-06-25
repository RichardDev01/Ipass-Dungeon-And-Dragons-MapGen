import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RandomMapGenTest {
    Node n0;
    Node n1;
    Node n2;

    Transistion t1;
    Transistion t2;
    Transistion t3;

    List<Node> nodes;

    RandomMapGen rmg;

    @Before
    public void Setup(){
        n0 = new Node(0,"Node 1");
        n1 = new Node(1,"Node 2");
        n2 = new Node(2,"Node 3");

        t1 = new Transistion(n0, 20,0);
        t2 = new Transistion(n1, 25,1);
        t3 = new Transistion(n2, 15,2);

        n0.addConnectie(t1);
        n0.addConnectie(t2);
        n0.addConnectie(t3);

        n1.addConnectie(t1);
        n1.addConnectie(t2);
        n1.addConnectie(t3);

        n2.addConnectie(t1);
        n2.addConnectie(t2);
        n2.addConnectie(t3);

        List<Node> nodes = new ArrayList<>();

        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);

        rmg = new RandomMapGen(nodes,2,0,5);
    }

    @Test
    public void getNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        assertEquals(nodes,rmg.getNodes());
    }


    @Test
    public void setNodes() {

        List<Node> nodes = new ArrayList<>();
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        rmg.setNodes(nodes);
        assertEquals(nodes,rmg.getNodes());
    }

    @Test
    public void run() {
        //Make a specific map, no random transitions test
        Node hallwayNZ = new Node(2,"hallwayNZ");
        Node hallwayNOW = new Node(5,"hallwayNOW");
        Node hallwayOZ = new Node(9,"hallwayOZ");
        Node hallwayOW = new Node(10,"hallwayOW");
        Node hallwayOZW = new Node(11,"hallwayOZW");

        Node bigRoom1 = new Node(20,"bigRoom1");

        Node endRoom = new Node(30,"endRoom");
        Node startRoom = new Node(31,"startRoom");

        Transistion t1 = new Transistion(hallwayNOW,10,0);
        Transistion t2 = new Transistion(hallwayOZ,10,1);
        Transistion t3 = new Transistion(hallwayOW,10,2);
        Transistion t4 = new Transistion(hallwayOZW,10,3);
        Transistion t5 = new Transistion(bigRoom1,10,20);
        Transistion t6 = new Transistion(endRoom,10,30);
        Transistion t7 = new Transistion(hallwayNZ,10,4);

        hallwayNZ.addConnectie(t1);
        hallwayNOW.addConnectie(t2);
        hallwayOZ.addConnectie(t3);
        hallwayOW.addConnectie(t4);
        hallwayOZW.addConnectie(t5);
        bigRoom1.addConnectie(t6);
        startRoom.addConnectie(t7);

        ArrayList<Node> allNodes = new ArrayList<>();

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

        rmg = new RandomMapGen(allNodes,2,1,10);

        assertEquals(checkNodes,rmg.run(startRoom,30,endRoom));

    }

    @Test
    public void minimumBigRooms(){
        //This test is made for checking if the minimum count and maximum of bigRooms there are in normal generator circumstances
        //Creating array list for nodes and transistions
        ArrayList<Node> hallwaylist = new ArrayList<>();
        ArrayList<Node> hallwayEndlist = new ArrayList<>();
        ArrayList<Node> bigRoomlist = new ArrayList<>();
        List<Node> checkList = new ArrayList<>();
        ArrayList<Transistion> transistionlistHallway = new ArrayList<>();

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
        //Dev note, did not add dead ends to list because it is handled in DrawMap Class
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

        //Adding halwayends to list
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

        int aantalMinBigroom= 10;
        int aantalBigroom = 20;
        int aantalHalways = 3;

        //Creating Start and Endroom
        Node endRoom = new Node(30,"endRoom");
        Node startRoom = new Node(31,"startRoom");

        //Calculating chance for bigroom
        int  chanceForBigRoom = (aantalBigroom*100)/aantalHalways;

        //Calculating chance for Hallway
        int chanceForHallway = Math.abs(100-chanceForBigRoom);

        //Creating hallway transisiton list
        int counterHallway =0;
        for (var h : hallwaylist){
            Transistion t1 = new Transistion(h,chanceForHallway,counterHallway);
            transistionlistHallway.add(t1);
            counterHallway++;
        }

        //adding hallways to hallways
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
                }
            }
        }

        //Adding code for bigroom trasistions
        for (var br : bigRoomlist){
            for (var t : transistionlistHallway){
                //filtering and checking if certain tiles can be connected or not, this can be improved
                if(t.n.name.equals("hallwayNW") || t.n.name.equals("hallwayZW") || t.n.name.equals("hallwayOZ") || t.n.name.equals("hallwayNO") || !(t.n.name.contains("O"))){
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
            //Startroom only has a East exit so the next tile must have West
            if(t.n.name.contains("W")){
                startRoom.addConnectie(t);
            }
        }

        //Create 1 list with all nodes
        ArrayList<Node> allNodes = new ArrayList<>(bigRoomlist);
        allNodes.addAll(hallwaylist);
        allNodes.addAll(hallwayEndlist);

        RandomMapGen fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom,aantalHalways);
        checkList = fsmR2.run(startRoom,50,endRoom);

        int count = 0;
        for (var nodes : checkList){
            if (nodes.name.contains("bigRoom")){
                count++;
            }
        }
        System.out.println("aantalMinBigroom given = "+ aantalMinBigroom + "\n" + "aantalBigroomMax Given = "+aantalBigroom+"\n"+"amount bigroom pressent = "+ count);
        assertTrue(count > aantalMinBigroom);
        assertTrue(count <= aantalBigroom);
    }
}