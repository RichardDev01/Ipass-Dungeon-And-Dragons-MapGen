import java.text.DecimalFormat;
import java.util.ArrayList;


public class Node {
    public int id;                                                          //Id of Node
    public String name;                                                     //Name of Node
    public ArrayList<Transistion> connectedNodes = new ArrayList<>();       //Array list of transition from this node to other nodes
    private int MaxRandomnummer;                                            //Integer for calculation MaxRandomnumber

    //Node constructor
    public Node(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //Add Transition tonode
    public void addConnectie(Transistion t){
        connectedNodes.add(t);
    }

    // Get sum of all chance values
    public int getSumMaxRandomnummer(){
        MaxRandomnummer =0;
        for(var transistion : connectedNodes)
        {
            MaxRandomnummer += transistion.getChance();
        }
        return MaxRandomnummer;
    }

    //Selecting the next Node with an integer (this integer can be a specific number or use a random number between 0 and (getSumMaxRandomnummer) function)
    public Node randomTransistion(int pointer) {
        int target = getSumMaxRandomnummer();
        int index = 0;
        // if there is no transisiton, return null
        if (getSumMaxRandomnummer() == 0) {
            return null;
        } else {
            //using a pointer to move closer to provided int(it counts from upper limit to lower)
            for (var transistion : connectedNodes) {
                if (pointer > target) {
                    index = connectedNodes.indexOf(transistion) - 1;
                    if (connectedNodes.get(index).n != null) {
                        return connectedNodes.get(index).n;
                    } else {
                        return transistion.n;
                    }
                } else {
                    target -= transistion.chance;
                }
            }
            return connectedNodes.get(connectedNodes.size() - 1).n;
        }
    }

    @Override
    public String toString() {
        return  name;
    }

    //Debug function with feedback of chances
    public String getChance(){
        DecimalFormat df = new DecimalFormat("0.00");
        String result;
        float maximumNumber = getSumMaxRandomnummer();
        result = "Dit zijn de kansen voor deze Node "+toString()+ " := ";

        //Calculating chance for each path
        for (var transistion : connectedNodes){
            result += transistion.n  + " ";
            result += df.format(transistion.chance/maximumNumber*100) + "%  ";
        }
        return result;
    }

}



