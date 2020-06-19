public class Transistion {
    public Node n;          //Node to transition too
    public int chance;      //number in ratio compared to other transition to goto Node
    public int id;          //Id of keeping track of transitions

    //Transition Constructor
    public Transistion(Node n, int chance, int id) {
        this.n = n;
        this.chance = chance;
        this.id = id;
    }

    //Get Node
    public Node getNode() {
        return n;
    }

    //set Node
    public void setNode(Node n) {
        this.n = n;
    }

    //Get chance
    public double getChance() {
        return chance;
    }

    //Get Id
    public int getId() {
        return id;
    }

    //Set Id
    public void setId(int id) {
        this.id = id;
    }

    //Set Chance
    public void setChance(int chance) {
        this.chance = chance;
    }
}
