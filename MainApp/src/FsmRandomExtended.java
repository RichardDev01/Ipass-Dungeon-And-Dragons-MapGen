import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FsmRandomExtended {
    private List<Node> nodes = new ArrayList<Node>();  // Array of all nodes.
    private List<Node> pathTakenThroughFSM = new ArrayList<Node>();   // Array of path through all nodes.
    private List<Character> transistiesCases = new ArrayList<Character>(); // Array of all trasisties events
    private int randomPath;     // int range from 0-1 for 50% change to go A or B
    private String exit;        //Exit String

    public FsmRandomExtended() {
    }

    public void run(Node startNode,int maxLength) {
        Node currentNode = startNode;               //Set startNode
        pathTakenThroughFSM.add(currentNode);      //Add the starting node to list

        int i = 0;
        while (i  < maxLength) {                    //Maximum Trasisties that can happen
            char ch;
            randomPath = new Random().nextInt(currentNode.getSumMaxRandomnummer()+1); // genarate 0-maxSum van kansen aka, flexibele kans "0-21 or 0-4"

            Node newNode = currentNode.randomTransistion(randomPath);

            if (newNode != null) { //if NewNode has a node, make currentNode newNode
                currentNode = newNode;
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
