/*
https://stackoverflow.com/questions/7855387/percentage-of-two-int
 */


/*
Okay, ik denk dat de netste manier om dat te doen is: het aanpassen van de constructor van FsmRandomExtended en de lijsten
doorgeven vanuit Main (aangenomen dat je specifiek die lijsten al in Main nodig hebt en niet alleen in FsmRandomExtended)
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

//#TODO Correcte halway connections maken
//#TODO Drawing uitzoeken
//#TODO Drawing algoritme maken

public class Main {

    public static void main(String[] args) {

        //Creating array list for nodes and transistions
        ArrayList<Node> hallwaylist = new ArrayList<>();
        ArrayList<Node> hallwayEndlist = new ArrayList<>();
        ArrayList<Node> bigRoomlist = new ArrayList<>();
        ArrayList<Transistion> transistionlistHallway = new ArrayList<>();
        ArrayList<Transistion> transistionlistBigRoom = new ArrayList<>();

        //Creating Nodes (tilessets)
        //See schematic for meaning of N,NO,NZ... ect
        Node hallwayN = new Node(0,"hallwayN");
        Node hallwayNO = new Node(1,"hallwayNO");
        Node hallwayNZ = new Node(2,"hallwayNZ");
        Node hallwayNW = new Node(3,"hallwayNW");
        Node hallwayNOZ = new Node(4,"hallwayNOZ");
        Node hallwayNOW = new Node(5,"hallwayNOW");
        Node hallwayNOZW = new Node(6,"hallwayNOZW");
        Node hallwayNZW = new Node(7,"hallwayNZW");
        Node hallwayO = new Node(8,"hallwayO");
        Node hallwayOZ = new Node(9,"hallwayOZ");
        Node hallwayOW = new Node(10,"hallwayOW");
        Node hallwayOZW = new Node(11,"hallwayOZW");
        Node hallwayZ = new Node(12,"hallwayZ");
        Node hallwayZW = new Node(13,"hallwayZW");
        Node hallwayW = new Node(14,"hallwayW");

        //Adding hallways to list
        //Dev note, did not add dead ends to all node because it is handled in DrawMap Class

        //hallwaylist.add(hallwayN);
        hallwaylist.add(hallwayNO);
        hallwaylist.add(hallwayNZ);
        hallwaylist.add(hallwayNW);
        hallwaylist.add(hallwayNOZ);
        hallwaylist.add(hallwayNOW);
        hallwaylist.add(hallwayNOZW);
        hallwaylist.add(hallwayNZW);
        //hallwaylist.add(hallwayO);
        hallwaylist.add(hallwayOZ);
        hallwaylist.add(hallwayOW);
        hallwaylist.add(hallwayOZW);
        //hallwaylist.add(hallwayZ);
        hallwaylist.add(hallwayZW);
        //hallwaylist.add(hallwayW);

        hallwayEndlist.add(hallwayN);
        hallwayEndlist.add(hallwayO);
        hallwayEndlist.add(hallwayZ);
        hallwayEndlist.add(hallwayW);

        //Creating Nodes (tilessets)
        Node bigRoom1 = new Node(20,"bigRoom1");
        Node bigRoom2 = new Node(21,"bigRoom2");
        Node bigRoom3 = new Node(22,"bigRoom3");
        Node bigRoom4 = new Node(23,"bigRoom4");

        //Adding bigrooms to list
        bigRoomlist.add(bigRoom1);
        bigRoomlist.add(bigRoom2);
        bigRoomlist.add(bigRoom3);
        bigRoomlist.add(bigRoom4);

        //Creating Start and Endroom
        Node endRoom = new Node(30,"endRoom");
        Node startRoom = new Node(31,"startRoom");


        //User input stage ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //#TODO Replace with interface
        /*
        Scanner snrMaximaleGrote = new Scanner(System.in);
        System.out.println("Maximale opvervlakte?\n(9,16,25,36)");
        int  maxGroteTilesAantal = snrMaximaleGrote.nextInt();
         */
        int  maxGroteTilesAantal = 100;

        Scanner snrMinBigRooms = new Scanner(System.in);
        System.out.println("minimale aantal Bigrooms?\n(0,1,2,3,4) moet minder zijn dan maxaantal");
        int  aantalMinBigroom = snrMinBigRooms.nextInt();

        Scanner snrMaxBigRooms = new Scanner(System.in);
        System.out.println("Maximale Bigrooms?\n(1,2,3,4)");
        int  aantalBigroom = snrMaxBigRooms.nextInt();

        Scanner snrVerhouding = new Scanner(System.in);
        System.out.println("Welke verhouding van Hallways en Bigrooms wil je?\n(2,4,6,8)");
        int  aantalHalways = snrVerhouding.nextInt()*aantalBigroom;

        int  chanceForBigRoom = (aantalBigroom*100)/aantalHalways;
        //System.out.println(chanceForBigRoom);

        int chanceForHalway = 100-chanceForBigRoom;


        // Mogelijk obsalite code voor later omdat ik nu controleer hoeveel rooms ik gehad heb
        // Remove excess bigrooms
//        int aantalRoomsVerwijderen = bigRoomlist.size() - aantalBigroom;
//        int randomPop;
//        for (int i = 0; i < aantalRoomsVerwijderen; i++) {
//            randomPop = new Random().nextInt(bigRoomlist.size());
//            //System.out.println("randompop "+randomPop);
//            bigRoomlist.remove(randomPop);
//        }


        //creating hallway list
        int counterHalway =0;
        for (var h : hallwaylist){
            Transistion t1 = new Transistion(h,chanceForHalway,counterHalway);
            transistionlistHallway.add(t1);
            counterHalway++;
        }

        //Deze 2 for loops aanpassen
        //adding hallways to hallways
        int counterBigroom = 20;
        for (var h : hallwaylist){
            //adding hallways to hallways
            for (var t : transistionlistHallway){

                //filtering and checking if certain tiles can be connected or not, this can be improved
                if (h.name.contains("NO") && t.n.name.contains("OZ")){
                    continue;
                }
                if (h.name.contains("NZ") && t.n.name.contains("OZ")){
                    continue;
                }
                if (h.name.contains("OZ") && t.n.name.contains("NO")){
                    continue;
                }
                if (h.name.contains("Z") && t.n.name.contains("N")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }
                if(h.name.contains("O") && t.n.name.contains("W")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }
                //Van West naar Oost rekenen, dus niet terug
                /*
                if(h.name.contains("W") && t.n.name.contains("O")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }
                 */
                if(h.name.contains("N") && t.n.name.contains("Z")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }
                //hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                //t.n.name.contains("o");
                //h.name.contains("o");
            }


            //TODO Correcte berekening maken voor bigrooms
            double chanceForBigRoomDB = chanceForBigRoom/10;
            //System.out.println(chanceForBigRoomDB);
            Double chanceDb = h.getSumMaxRandomnummer()/chanceForBigRoomDB;
            //System.out.println(chanceDb);
            int chanceint = chanceDb.intValue();
            chanceint = 50; //magic number
            for (var b : bigRoomlist){
                Transistion t1 = new Transistion(b,chanceint,counterBigroom);
                transistionlistBigRoom.add(t1);
                //bigRoomlist.get(bigRoomlist.indexOf(b)).addConnectie(t1);
                counterBigroom++;
            }

            for (var th : transistionlistBigRoom){
                //h.addConnectie(th);
                hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(th);
            }
            //System.out.println(h.getSumMaxRandomnummer()*0.25);
        }

        //Adding code for bigroom trasistions
        for (var br : bigRoomlist){
            for (var t : transistionlistHallway){
                if(t.n.name == "hallwayNW" || t.n.name == "hallwayZW"|| t.n.name == "hallwayOZ" || t.n.name == "hallwayNO"|| (t.n.name.contains("O"))==false){
                    continue;
                }
                //br.addConnectie(t);
                bigRoomlist.get(bigRoomlist.indexOf(br)).addConnectie(t);
            }
            //br.addConnectie();
        }


        //Adding chance for Endroom
        int endChance = (bigRoomlist.get(0).getSumMaxRandomnummer() + hallwaylist.get(0).getSumMaxRandomnummer())/10;
        //System.out.println(endChance);
        Transistion end = new Transistion(endRoom,endChance,30);

        //Adding a end room
        for (var br : bigRoomlist){
            //br.addConnectie(end);
            bigRoomlist.get(bigRoomlist.indexOf(br)).addConnectie(end);
        }

        //StartNode adding hallways
        for (var t : transistionlistHallway){
            if(t.n.name.contains("W")){
                startRoom.addConnectie(t);
            }

        }

        //Create 1 list with all nodes
        ArrayList<Node> allNodes = new ArrayList<>(bigRoomlist);
        allNodes.addAll(hallwaylist);
        allNodes.addAll(hallwayEndlist);

        //Debug strings
        //System.out.println(hallwaylist.get(0).getChance());
        //System.out.println(bigRoomlist.get(0).getChance());



//        //TODO, make this a run void in main
//        //Creating the generator
//        //RandomMapGen fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom);
//        RandomMapGen fsmR2 = null;
//        //DrawMap dm1 = new DrawMap(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom),allNodes,false);
//        DrawMap dm1 = null;
//        boolean check = false;
//        int counterOfSimulations = 1;
//        while (check==false && counterOfSimulations<5){
//            System.out.println("sim: "+ counterOfSimulations);
//            fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom);
//            dm1 = new DrawMap(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom),allNodes,false);
//            //dm1.setWayToTheEndRoom(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom));
//            dm1.nodesToString();
//            check = dm1.run();
//            counterOfSimulations++;
//        }

        //~~~~ till here



        //TODO, This is for debug purpose only, delete when done

        //Debug Array


//        ArrayList<Node> debugNodeslist = new ArrayList<>();
//
//        debugNodeslist.add(startRoom);
//        debugNodeslist.add(hallwayNOZW);
//        debugNodeslist.add(bigRoom1);
//        debugNodeslist.add(hallwayNOZW);
//        debugNodeslist.add(hallwayOW);
//        debugNodeslist.add(hallwayZW);
//        debugNodeslist.add(bigRoom1);
//
//        debugNodeslist.add(endRoom);
//
//        DrawMap dm1Debug = new DrawMap(debugNodeslist,allNodes,false);
//        dm1Debug.nodesToString();

        //dm1Debug.run();
        //~~~~~till here

    }


}
