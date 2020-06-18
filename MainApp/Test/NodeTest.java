import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    Node n0;
    Node n1;
    Node n2;

    Transistion t1;
    Transistion t2;
    Transistion t3;

    @Before
    public void Setup(){
        n0 = new Node(0,"Node 1");
        n1 = new Node(1,"Node 2");
        n2 = new Node(2,"Node 3");

        t1 = new Transistion(n0, 20,0);
        t2 = new Transistion(n1, 25,1);
        t3 = new Transistion(n2, 15,2);
    }

    @Test
    public void getSumMaxRandomnummer() {
        n0.addConnectie(t1);
        n0.addConnectie(t2);
        n0.addConnectie(t3);
        assertEquals(n0.getSumMaxRandomnummer(),60);
    }

    @Test
    public void randomTransistion() {
        n0.addConnectie(t1);
        n0.addConnectie(t2);
        n0.addConnectie(t3);

        //Insert value to see what node should be selected(normally, this is a random int)
        assertEquals(n0.randomTransistion(10),n2);
        assertEquals(n0.randomTransistion(25),n1);
        assertEquals(n0.randomTransistion(50),n0);
    }

    @Test
    public void getChance() {
        n0.addConnectie(t1);
        n0.addConnectie(t2);
        n0.addConnectie(t3);
        assertEquals("Dit zijn de kansen voor deze Node Node 1 := Node 1 33.33%  Node 2 41.67%  Node 3 25.00%  ",n0.getChance());
    }
}