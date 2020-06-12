import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMapGen {
    private List<Node> nodes = new ArrayList<>();  // Array of all nodes.
    private List<Node> pathTakenThroughFSM = new ArrayList<>();   // Array of path through all nodes.
    private List<Character> transistiesCases = new ArrayList<>(); // Array of all trasisties events
    private int randomPath;     // int range from 0-1 for 50% change to go A or B
    private int randomMinRoom;
    private int maxBigRooms;    //int for max big rooms
    private int minBigRooms;    //int for min big rooms

    public RandomMapGen(List<Node> nodes, int maxBigRooms, int minBigRooms) {
        this.nodes = nodes;
        this.maxBigRooms = maxBigRooms;
        this.minBigRooms = minBigRooms+1;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Node> run(Node startNode, int maxLength, Node endNode) {
        Node currentNode = startNode;               //Set startNode
        pathTakenThroughFSM.add(currentNode);      //Add the starting node to list

        int i = 0;
        while (i  < maxLength) {                    //Maximum Trasisties that can happen
            char ch;
            randomPath = new Random().nextInt(currentNode.getSumMaxRandomnummer()+1); // generate 0-maxSum van kansen aka, flexibele kans "0-21 or 0-4"

            Node newNode = currentNode.randomTransistion(randomPath);

            if (newNode != null) { //if NewNode has a node, make currentNode newNode
                try {
                    //remove bigrooms from trasistions (aanpak is controle op hoeveelheid rooms maar niet remove)
                if (newNode.id > 19 && newNode.id < 30) {
                    //System.out.println(newNode.name);
                    minBigRooms--;
                    maxBigRooms--;

                    //Removal code for trasistion (bugged)
                    /*
                    for (var no : nodes) {
                        for (var tr : no.connectedNodes) {
                            for (var cn : tr.n.connectedNodes){
                                if(cn.id == no.id){
                                    //tr.n.connectedNodes.remove(cn);
                                    tr.n.connectedNodes.remove(tr.n.connectedNodes.size()-1);

                                }
                            }

                           // if (tr.n == newNode){
                                //System.out.println(no.connectedNodes.indexOf(tr.getNode()));
                                //int index = tr.n.connectedNodes.
                                //tr.n.connectedNodes.remove()

                            //}
                        }
                    }

                     */
                    /*
                    for (var no : nodes) {
                        for (var tr : no.connectedNodes) {
                            //no.connectedNodes.remove(no.connectedNodes.indexOf(tr));
                            if (tr.n == newNode) {
                                no.connectedNodes.remove(no.connectedNodes.indexOf(tr));
                            }
                        }
                    }

                     */
                }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Minimale aantal bigRooms (lijkt werkend)
                if (minBigRooms >0 && newNode == endNode){
                   //int connectedNodeslist = currentNode.connectedNodes.size()-(maxBigRooms+1);
                   int connectedNodeslist = new Random().nextInt(currentNode.connectedNodes.size());
                   //randomMinRoom = new Random().nextInt(connectedNodeslist+2);
                   newNode = currentNode.connectedNodes.get(connectedNodeslist).n;
                }

                //Oude code voor minimale aantal bigrooms
                /*
                int exitInt = 0;
                if (minBigRooms >0 && newNode == endNode){
                    randomPath = new Random().nextInt(currentNode.getSumMaxRandomnummer()+1);
                    newNode = currentNode.randomTransistion(randomPath);
                    exitInt++;
                    if (exitInt ==5){
                        break;
                    }
                }
*/
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
