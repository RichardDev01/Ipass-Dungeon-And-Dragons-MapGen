import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMapGen {
    private List<Node> nodes;                                       //Array of all nodes.
    private List<Node> pathTakenThroughFSM = new ArrayList<>();     //Array of path through all nodes.
    private List<Node> bigRoomsList = new ArrayList<>();            //Array of bigRoomsList
    private int randomPath;                                         //int range from 0-1 for 50% change to go A or B
    private int maxBigRooms;                                        //int for max big rooms
    private int minBigRooms;                                        //int for min big rooms
    private int hallwayCounter = 0;                                 //int for counting hallways
    private int hallwaysForBigRoom;                                 //int to count towards for hallways

    public RandomMapGen(List<Node> nodes, int maxBigRooms, int minBigRooms,int hallwaysForBigRoom) {
        this.nodes = nodes;
        this.maxBigRooms = maxBigRooms;
        this.minBigRooms = minBigRooms+1;
        this.hallwaysForBigRoom = hallwaysForBigRoom/maxBigRooms+1;
    }

    //Get list of Nodes
    public List<Node> getNodes() {
        return nodes;
    }

    //Set list of Nodes
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    //Function of filling bigRoomList from all nodes
    private void addbigRoomsList(){
        for (var node : nodes){
            if (node.name.contains("bigRoom")){
                bigRoomsList.add(node);
            }
        }
    }

    /*
    startNode = This is the starting node of the sequence
    maxLength = Maximum length of path
    endNode   = This is the endNode of the sequence
     */
    public List<Node> run(Node startNode, int maxLength, Node endNode) {
        addbigRoomsList();
        Node currentNode = startNode;               //Set startNode
        pathTakenThroughFSM.add(currentNode);       //Add the starting node to list

        int i = 0;
        while (i  < maxLength) {                    //Maximum transistions that can happen
            randomPath = new Random().nextInt(currentNode.getSumMaxRandomnummer()+1); // generate 0-maxSum of chances aka, flexible percentage "0-21 or 0-4" ratio calculating
            Node newNode = currentNode.randomTransistion(randomPath);                       //  Get random transistion from staring node

            if (newNode != null) { //if NewNode has a node, make currentNode newNode
                try {
                    //hallwayCounter for hallway ratio
                    if (newNode.id < 19){
                      hallwayCounter++;
                    }
                    //if counter reached the set ratio, add bigroom to path
                    if (hallwayCounter == hallwaysForBigRoom){
                        hallwayCounter=0;
                        int index = new Random().nextInt(bigRoomsList.size());
                        newNode = bigRoomsList.get(index);
                    }

                    //check for bigRooms and keep track of counters
                    if (newNode.id > 19 && newNode.id < 30) {
                        minBigRooms--;
                        maxBigRooms--;
                    }
                } catch (Exception e) { //added as a safety, java really really wanted this for no reason
                    e.printStackTrace();
                }

                //if the minimum of bigrooms has not been reached but the newNode equels the end, get another node
                while (minBigRooms >0 && newNode.name.contains("endRoom")){
                   int connectedNodeslist = new Random().nextInt(currentNode.connectedNodes.size());
                   newNode = currentNode.connectedNodes.get(connectedNodeslist).n;
                }

                //if maximum bigrooms has been reached, goto end
                if (maxBigRooms ==0){
                    currentNode = endNode;
                }
                else {
                    currentNode = newNode;
                }

            } else {// Else, the end of path has been reached
                break; //Exit while loop because the end of path has been reached
            }

            pathTakenThroughFSM.add(newNode); // Add NewNode to list

            i++;
            //Check if maxLength has been reached, in case of infinite loop
            if (i == maxLength){
                System.out.println("Maximum pathlenth is reached"); // Debug
            }
        }
        //Just in case the machine ends early but can add an endRoom, add it
        if (pathTakenThroughFSM.get(pathTakenThroughFSM.size()-1)!= endNode){
            pathTakenThroughFSM.add(endNode);
        }
        System.out.println("Nodes: " + pathTakenThroughFSM); // Print pathTakenThroughFSM
        return pathTakenThroughFSM;
    }
}
