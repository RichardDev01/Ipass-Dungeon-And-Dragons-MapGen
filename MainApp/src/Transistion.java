public class Transistion {
    public Node n;
    public int chance;
    public int id;

    public Transistion(Node n, int chance, int id) {
        this.n = n;
        this.chance = chance;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
