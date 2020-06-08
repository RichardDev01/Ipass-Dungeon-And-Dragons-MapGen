public class Transistion {
    public Node n;
    public int chance;


    public Transistion(Node n, int chance) {
        this.n = n;
        this.chance = chance;
    }

    public Node getNode() {
        return n;
    }

    public void setNode(Node n) {
        this.n = n;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
