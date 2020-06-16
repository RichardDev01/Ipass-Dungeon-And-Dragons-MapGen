import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMapGen {
    private List<Node> nodes = new ArrayList<>();  // Array of all nodes.
    private List<Node> pathTakenThroughFSM = new ArrayList<>();   // Array of path through all nodes.
    private List<Character> transistiesCases = new ArrayList<>(); // Array of all trasisties events
    private List<Node> bigRoomsList = new ArrayList<>();

    private int randomPath;     // int range from 0-1 for 50% change to go A or B
    private int randomMinRoom;
    private int maxBigRooms;    //int for max big rooms
    private int minBigRooms;    //int for min big rooms
    private int hallwayCounter = 0;
    private int hallwaysForBigRoom;

    public RandomMapGen(List<Node> nodes, int maxBigRooms, int minBigRooms,int hallwaysForBigRoom) {
        this.nodes = nodes;
        this.maxBigRooms = maxBigRooms;
        this.minBigRooms = minBigRooms+1;
        this.hallwaysForBigRoom = hallwaysForBigRoom/maxBigRooms+1;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    private void addbigRoomsList(){
        for (var node : nodes){
            if (node.name.contains("bigRoom")){
                bigRoomsList.add(node);
            }
        }
    }

    public List<Node> run(Node startNode, int maxLength, Node endNode) {
        addbigRoomsList();
        Node currentNode = startNode;               //Set startNode
        pathTakenThroughFSM.add(currentNode);      //Add the starting node to list

        int i = 0;
        while (i  < maxLength) {                    //Maximum Trasisties that can happen
            char ch;
            randomPath = new Random().nextInt(currentNode.getSumMaxRandomnummer()+1); // generate 0-maxSum van kansen aka, flexibele kans "0-21 or 0-4"

            Node newNode = currentNode.randomTransistion(randomPath);

            if (newNode != null) { //if NewNode has a node, make currentNode newNode
                try {
                    //hallwayCounter
                    if (newNode.id < 19){
                      hallwayCounter++;
                    }
                    if (hallwayCounter == hallwaysForBigRoom){
                        hallwayCounter=0;
                        int index = new Random().nextInt(bigRoomsList.size());
                        newNode = bigRoomsList.get(index);
                    }
                        //remove bigrooms from trasistions (aanpak is controle op hoeveelheid rooms maar niet remove)
                    if (newNode.id > 19 && newNode.id < 30) {
                        //System.out.println(newNode.name);
                        minBigRooms--;
                        maxBigRooms--;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Als minimale big rooms er niet is maar wel het einden, neem ander pad
                if (minBigRooms >0 && newNode == endNode){
                   int connectedNodeslist = new Random().nextInt(currentNode.connectedNodes.size());
                   newNode = currentNode.connectedNodes.get(connectedNodeslist).n;
                }

                if (maxBigRooms ==0){
                    currentNode = endNode;
                }
                else {
                    currentNode = newNode;
                }

            } else {// Else, the end of path has been reached
                //System.out.println("end node = "+currentNode);  //oude code
                break; //Exit while loop because the end of path has been reached
            }

            pathTakenThroughFSM.add(newNode); // Add NewNode to list

            i++;
            //Check if maxLength has been reached, in case of infinite loop
            if (i == maxLength){
                System.out.println("Maximum pathlenth is reached"); // Debug
            }
        }
        if (pathTakenThroughFSM.get(pathTakenThroughFSM.size()-1)!= endNode){
            pathTakenThroughFSM.add(endNode);
        }
        System.out.println("Nodes: " + pathTakenThroughFSM); // Print pathTakenThroughFSM
        return pathTakenThroughFSM;
    }
}
