import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FsmRandomExtended {
    private List<Node> nodes = new ArrayList<Node>();  // Array of all nodes.
    private List<Node> pathTakenThroughFSM = new ArrayList<Node>();   // Array of path through all nodes.
    private List<Character> transistiesCases = new ArrayList<Character>(); // Array of all trasisties events
    private int randomPath;     // int range from 0-1 for 50% change to go A or B
    private int maxBigRooms;
    private int minBigRooms;

    public FsmRandomExtended(List<Node> nodes, int maxBigRooms, int minBigRooms) {
        this.nodes = nodes;
        this.maxBigRooms = maxBigRooms;
        this.minBigRooms = minBigRooms;
    }

    public FsmRandomExtended() {
    }

    public void run(Node startNode,int maxLength, Node endNode) {
        Node currentNode = startNode;               //Set startNode
        pathTakenThroughFSM.add(currentNode);      //Add the starting node to list

        int i = 0;
        while (i  < maxLength) {                    //Maximum Trasisties that can happen
            char ch;
            randomPath = new Random().nextInt(currentNode.getSumMaxRandomnummer()+1); // genarate 0-maxSum van kansen aka, flexibele kans "0-21 or 0-4"

            Node newNode = currentNode.randomTransistion(randomPath);

            //#TODO Minimale aantaal bigRooms
            int exitInt = 0;
            while (minBigRooms >0 && newNode == endNode){
                newNode = currentNode.randomTransistion(randomPath);
                exitInt++;
                if (exitInt ==5){
                    break;
                }
            }

            if (newNode != null) { //if NewNode has a node, make currentNode newNode

                currentNode = newNode;

                try {
                    //TODO remove bigrooms from trasistions
                if (newNode.id > 19 && newNode.id < 30) {
                    //System.out.println(newNode.name);
                    minBigRooms--;

                    maxBigRooms--;
                    if (maxBigRooms ==0){
                        currentNode = endNode;
                    }
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



            } else {// Else, the end of path has been reached
                System.out.println("end node = "+currentNode);
                break; //Exit while loop because the end of path has been reached
            }

            pathTakenThroughFSM.add(newNode); // Add NewNode to list

            i++;
            //Check if maxLength has been reached, in case of infinite loop
            if (i == maxLength){
                System.out.println("Maximum pathlenth is reached"); // Debug
            }
        }
        System.out.println("Nodes: " + pathTakenThroughFSM); // Print pathTakenThroughFSM
    }


}
