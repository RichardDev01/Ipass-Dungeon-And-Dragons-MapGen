import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransistionTest {
    Node n0;
    Node n1;
    Node n2;

    Transistion t1;
    Transistion t2;
    Transistion t3;

    @Before
    public void Setup() {
        n0 = new Node(0, "Node 1");
        n1 = new Node(1, "Node 2");
        n2 = new Node(2, "Node 3");

        t1 = new Transistion(n0, 20, 0);
        t2 = new Transistion(n1, 25, 1);
        t3 = new Transistion(n2, 15, 2);
    }

    @Test
    public void getNode() {
        assertEquals(n0,t1.getNode());
    }

    @Test
    public void setNode() {
        assertNotEquals(n1,t1.getNode());
        t1.setNode(n1);
        assertEquals(n1,t1.getNode());
    }

    @Test
    public void getId() {
    assertEquals(0,t1.getId());
    }

    @Test
    public void setId() {
        assertNotEquals(10,t1.getId());
        t1.setId(10);
        assertEquals(10,t1.getId());
    }

    @Test
    public void setChance() {
        assertNotEquals(10,t1.getChance());
        t1.setChance(10);
        assertEquals(10,t1.chance);
    }
}