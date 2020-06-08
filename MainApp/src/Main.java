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

        Node hallwayN = new Node(0);
        Node hallwayNO = new Node(1);
        Node hallwayNZ = new Node(2);
        Node hallwayNW = new Node(3);
        Node hallwayNOZ = new Node(4);
        Node hallwayNOW = new Node(5);
        Node hallwayNOZW = new Node(6);
        Node hallwayNWZ = new Node(7);
        Node hallwayO = new Node(8);
        Node hallwayOZ = new Node(9);
        Node hallwayOW = new Node(10);
        Node hallwayOZW = new Node(11);
        Node hallwayZ = new Node(12);
        Node hallwayZW = new Node(13);
        Node hallwayW = new Node(14);

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

        Node bigRoom1 = new Node(20);
        Node bigRoom2 = new Node(21);
        Node bigRoom3 = new Node(22);
        Node bigRoom4 = new Node(23);

        bigRoomlist.add(bigRoom1);
        bigRoomlist.add(bigRoom2);
        bigRoomlist.add(bigRoom3);
        bigRoomlist.add(bigRoom4);

        Node endRoom = new Node(30);

        /*
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Maximale Bigrooms?\n(1,2,3,4)");
        int  maxGrote = sc1.nextInt();
         */
        int  maxGrote = 9;

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




        for (var h : hallwaylist){
            Transistion t1 = new Transistion(h,chanceForHalway);
            transistionlistHallway.add(t1);
        }

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

        //TODO Adding a end room

        System.out.println(hallwayN.getChance());
        //Create nodes Summatieve Opdracht 2
        Node d0 = new Node(0);
        Node d1 = new Node(1);
        Node d2 = new Node(2);
        Node d3 = new Node(3);
        Node d4 = new Node(4);
        Node d5 = new Node(5);
        Node d6 = new Node(6);
        Node d7 = new Node(7);
        Node d8 = new Node(8);
        Node d9 = new Node(9);
        Node d10 = new Node(10);
        Node d11 = new Node(11);

        //Create transistions
        Transistion t1 = new Transistion(d1,50);
        Transistion t2 = new Transistion(d6,50);
        d0.addConnectie(t1);
        d0.addConnectie(t2);

        Transistion t3 = new Transistion(d2,100);
        d1.addConnectie(t3);

        Transistion t4 = new Transistion(d3, 10);
        Transistion t5 = new Transistion(d4, 80);
        Transistion t6 = new Transistion(d5, 10);
        d2.addConnectie(t4);
        d2.addConnectie(t5);
        d2.addConnectie(t6);

        Transistion t7 = new Transistion(d7, 10);
        Transistion t8 = new Transistion(d8, 70);
        Transistion t9 = new Transistion(d9, 20);
        d6.addConnectie(t7);
        d6.addConnectie(t8);
        d6.addConnectie(t9);

        Transistion t10 = new Transistion(d10, 10);
        Transistion t11 = new Transistion(d11, 90);
        d7.addConnectie(t10);
        d7.addConnectie(t11);

        Transistion t12 = new Transistion(d5, 50);
        Transistion t13 = new Transistion(d11, 50);
        d9.addConnectie(t12);
        d9.addConnectie(t13);

        System.out.println();
        System.out.println("Fsm 3 voor Summatieve Opdracht 2");
        //Create Fsm for Summatieve Opdracht 2

        /*
        System.out.println(d0.getChance());
        System.out.println(d1.getChance());
        System.out.println(d2.getChance());
        System.out.println(d6.getChance());
        System.out.println(d7.getChance());
        System.out.println(d9.getChance());
        */

        FsmRandomExtended fsmR2 = new FsmRandomExtended();
        fsmR2.run(d0,15);

    }
}
