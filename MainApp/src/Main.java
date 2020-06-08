/*
https://stackoverflow.com/questions/7855387/percentage-of-two-int
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.function.ToDoubleBiFunction;


public class Main {

    public static void main(String[] args) {

        ArrayList<Node> hallwaylist = new ArrayList<>();
        ArrayList<Node> bigRoomlist = new ArrayList<>();
        ArrayList<Transistion> transistionlistHallway = new ArrayList<>();
        ArrayList<Transistion> transistionlistBigRoom = new ArrayList<>();

        Node hallwayN = new Node(0,"hallwayN");
        Node hallwayNO = new Node(1,"hallwayNO");
        Node hallwayNZ = new Node(2,"hallwayNZ");
        Node hallwayNW = new Node(3,"hallwayNW");
        Node hallwayNOZ = new Node(4,"hallwayNOZ");
        Node hallwayNOW = new Node(5,"hallwayNOW");
        Node hallwayNOZW = new Node(6,"hallwayNOZW");
        Node hallwayNWZ = new Node(7,"hallwayNWZ");
        Node hallwayO = new Node(8,"hallwayO");
        Node hallwayOZ = new Node(9,"hallwayOZ");
        Node hallwayOW = new Node(10,"hallwayOW");
        Node hallwayOZW = new Node(11,"hallwayOZW");
        Node hallwayZ = new Node(12,"hallwayZ");
        Node hallwayZW = new Node(13,"hallwayZW");
        Node hallwayW = new Node(14,"hallwayW");

        hallwaylist.add(hallwayN);
        hallwaylist.add(hallwayNO);
        hallwaylist.add(hallwayNZ);
        hallwaylist.add(hallwayNW);
        hallwaylist.add(hallwayNOZ);
        hallwaylist.add(hallwayNOW);
        hallwaylist.add(hallwayNOZW);
        hallwaylist.add(hallwayNWZ);
        hallwaylist.add(hallwayO);
        hallwaylist.add(hallwayOZ);
        hallwaylist.add(hallwayOW);
        hallwaylist.add(hallwayOZW);
        hallwaylist.add(hallwayZ);
        hallwaylist.add(hallwayZW);
        hallwaylist.add(hallwayW);

        Node bigRoom1 = new Node(20,"bigRoom1");
        Node bigRoom2 = new Node(21,"bigRoom2");
        Node bigRoom3 = new Node(22,"bigRoom3");
        Node bigRoom4 = new Node(23,"bigRoom4");

        bigRoomlist.add(bigRoom1);
        bigRoomlist.add(bigRoom2);
        bigRoomlist.add(bigRoom3);
        bigRoomlist.add(bigRoom4);

        Node endRoom = new Node(30,"endRoom");
        Node startRoom = new Node(31,"startRoom");



        /*
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Maximale opvervlakte?\n(9,16,25,36)");
        int  maxGrote = sc1.nextInt();
         */
        int  maxGrote = 16;

        Scanner sc2 = new Scanner(System.in);
        System.out.println("Maximale Bigrooms?\n(1,2,3,4)");
        int  aantalBigroom = sc2.nextInt();

        Scanner sc3 = new Scanner(System.in);
        System.out.println("Welke verhouding van Hallways en Bigrooms wil je?\n(2,4,6,8)");
        int  aantalHalways = sc3.nextInt()*aantalBigroom;

        int  chanceForBigRoom = (aantalBigroom*100)/aantalHalways;
        System.out.println(chanceForBigRoom);

        int chanceForHalway = 100-chanceForBigRoom;

        int aantalRoomsVerwijderen = bigRoomlist.size() - aantalBigroom;

        //System.out.println(bigRoomlist.size());
        //System.out.println(bigRoomlist);
        int randomPop;
        for (int i = 0; i < aantalRoomsVerwijderen; i++) {
            randomPop = new Random().nextInt(bigRoomlist.size());
            //System.out.println("randompop "+randomPop);
            bigRoomlist.remove(randomPop);
        }
        //System.out.println(bigRoomlist.size());
        //System.out.println(bigRoomlist);




        //creating hallway list
        for (var h : hallwaylist){
            Transistion t1 = new Transistion(h,chanceForHalway);
            transistionlistHallway.add(t1);
        }

        //StartNode adding hallways
        for (var t : transistionlistHallway){
            startRoom.addConnectie(t);
        }

        //adding hallways to hallways
        for (var h : hallwaylist){
            for (var t : transistionlistHallway){
                h.addConnectie(t);
            }

            //TODO Correcte berekening maken voor bigrooms
            double chanceForBigRoomDB = chanceForBigRoom/10;
            //System.out.println(chanceForBigRoomDB);
            Double chanceDb = h.getSumMaxRandomnummer()/chanceForBigRoomDB;
            //System.out.println(chanceDb);
            int chanceint = chanceDb.intValue();
            for (var b : bigRoomlist){
                Transistion t1 = new Transistion(b,chanceint);
                transistionlistBigRoom.add(t1);
            }

            for (var th : transistionlistBigRoom){
                h.addConnectie(th);
            }
            //System.out.println(h.getSumMaxRandomnummer()*0.25);
        }

        //TODO Adding code for bigroom trasistions

        for (var br : bigRoomlist){
            for (var t : transistionlistHallway){
                br.addConnectie(t);
            }
            //br.addConnectie();
        }
        //TODO Add chance for Endroom
        int endChance = (bigRoomlist.get(0).getSumMaxRandomnummer() + hallwaylist.get(0).getSumMaxRandomnummer())/10;
        System.out.println(endChance);
        Transistion end = new Transistion(endRoom,endChance);

        //TODO Adding a end room
        for (var br : bigRoomlist){
            br.addConnectie(end);
        }


        System.out.println(hallwaylist.get(0).getChance());
        System.out.println(bigRoomlist.get(0).getChance());
        FsmRandomExtended fsmR2 = new FsmRandomExtended();
        fsmR2.run(startRoom,maxGrote);

    }
}
