import java.text.DecimalFormat;
import java.util.ArrayList;


public class Node {
    public int id;
    public String name;
    public ArrayList<Transistion> connectedNodes = new ArrayList<Transistion>();
    private int MaxRandomnummer;

    public Node(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Node randomTransistion(int pointer) {
        int target = getSumMaxRandomnummer();
        int index = 0;
        if (getSumMaxRandomnummer() == 0) {
            return null;
        } else {

            //met behulp van een pointer steeds dichter naar de juiste node gaan (note, dit gaan van upper naar lower)
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



