import java.util.ArrayList;

public class Generator {
    //Creating array list for nodes and transistions
    private ArrayList<Node> hallwaylist = new ArrayList<>();
    ArrayList<Node> hallwayEndlist = new ArrayList<>();
    ArrayList<Node> bigRoomlist = new ArrayList<>();
    ArrayList<Transistion> transistionlistHallway = new ArrayList<>();
    ArrayList<Transistion> transistionlistBigRoom = new ArrayList<>();

    private void setup(){
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
        //Dev note, did not add dead ends to list because it is handled in DrawMap Class
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

        //Adding halwayends to list
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




        //interface






    }
    public void generate(int aantalBigroom,int aantalHalways,int aantalMinBigroom, int maxGroteTilesAantal){
        setup();
        //Creating Start and Endroom
        Node endRoom = new Node(30,"endRoom");
        Node startRoom = new Node(31,"startRoom");

        //creating hallway list with trasistions
        int  chanceForBigRoom = (aantalBigroom*100)/aantalHalways;
        //System.out.println(chanceForBigRoom);

        int chanceForHalway = 100-chanceForBigRoom;


        int counterHalway =0;
        for (var h : hallwaylist){
            Transistion t1 = new Transistion(h,chanceForHalway,counterHalway);
            transistionlistHallway.add(t1);
            counterHalway++;
        }


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
                if(h.name.contains("N") && t.n.name.contains("Z")){
                    hallwaylist.get(hallwaylist.indexOf(h)).addConnectie(t);
                    continue;
                }

            }
        }

        //Adding code for bigroom trasistions
        for (var br : bigRoomlist){
            for (var t : transistionlistHallway){
                if(t.n.name == "hallwayNW" || t.n.name == "hallwayZW"|| t.n.name == "hallwayOZ" || t.n.name == "hallwayNO"|| (t.n.name.contains("O"))==false){
                    continue;
                }
                bigRoomlist.get(bigRoomlist.indexOf(br)).addConnectie(t);
            }
        }

        //Adding chance for Endroom
        int endChance = (bigRoomlist.get(0).getSumMaxRandomnummer() + hallwaylist.get(0).getSumMaxRandomnummer())/10;
        Transistion end = new Transistion(endRoom,endChance,30);

        //Adding a end room
        for (var br : bigRoomlist){
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


        //TODO, make this a run void in main
        //Creating the generator
        //RandomMapGen fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom);
        RandomMapGen fsmR2 = null;
        //DrawMap dm1 = new DrawMap(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom),allNodes,false);
        DrawMap dm1 = null;
        boolean check = false;
        int counterOfSimulations = 1;
        while (check==false && counterOfSimulations<10){
            System.out.println("sim: "+ counterOfSimulations);
            fsmR2 = new RandomMapGen(allNodes,aantalBigroom,aantalMinBigroom,aantalHalways);
            dm1 = new DrawMap(fsmR2.run(startRoom,maxGroteTilesAantal,endRoom),allNodes,false);
            dm1.nodesToString();
            check = dm1.run();
            counterOfSimulations++;
        }
        if (counterOfSimulations ==10){
            System.out.println("couldn't make map, plz try again");
            System.out.println("latest result has been dumped");
        }

    }
}
