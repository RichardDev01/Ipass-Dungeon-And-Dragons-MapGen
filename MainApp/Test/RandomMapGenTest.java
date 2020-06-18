import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        //Make a specific map, no randomtransitions
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
}