/*
Sources:
https://stackoverflow.com/questions/3922276/how-to-combine-multiple-pngs-into-one-big-png-file
https://stackoverflow.com/questions/20826216/copy-two-bufferedimages-into-one-image-side-by-side
https://docs.oracle.com/javase/tutorial/2d/images/drawonimage.html
https://www.javamex.com/tutorials/graphics/bufferedimage.shtml
 */


//#TODO Logica in het aan elkaar plakken
//#TODO Mogelijk croppen
//#TODO bigroomtile + 1 index gebruiken voor dead ends
//#TODO Check voor valid space voor tile

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class DrawMap {

    private  List<Node> allNodes = new ArrayList<>();
    private List<Node> wayToTheEndRoom = new ArrayList<>();  // Array of all nodes.
    private List<Node> wayToTheEndRoomChecked = new ArrayList<>();  // Array of all nodes after pathchecking.
    private int width = 8000;
    private int height = 8000;
    public  List<String> images= new ArrayList<>();
    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = result.getGraphics();

    public DrawMap(List<Node> wayToTheEndRoom,List<Node>allNodes) {
        this.wayToTheEndRoom = wayToTheEndRoom;
        this.allNodes = allNodes;
    }

    public void addImages(String s) {
        images.add(s);
    }

    public void nodesToString(){
        checkPath();
        for(var node : wayToTheEndRoomChecked){
            if (node.name.contains("startRoom")){
                images.add("./Resources/Default/"+node.name+"/"+node.name+".png");
            }
            if (node.name.contains("hallway")) {
                images.add("./Resources/Default/Hallways/"+node.name+"/"+node.name+".png");
            }
            if (node.name.contains("bigRoom")){
                images.add("./Resources/Default/Bigrooms/"+node.name+"/"+node.name+".png");
            }
            if (node.name.contains("endRoom")){
                images.add("./Resources/Default/"+node.name+"s/"+node.name+".png");
            }

        }
        System.out.println(images);
    }

    public void checkPath(){
        int indexOfList =0;
        System.out.println(wayToTheEndRoom);
        Node fixNode = new Node(99,"hallwayNOZW");
        for (var node : wayToTheEndRoom){
            //System.out.println(node);
            indexOfList++;
            wayToTheEndRoomChecked.add(node);
            if(node.name.contains("bigRoom")){
                if (wayToTheEndRoom.get(indexOfList-1).name.contains("N")&&wayToTheEndRoom.get(indexOfList).name.contains("N")){
                    wayToTheEndRoomChecked.add(indexOfList,fixNode);
                    continue;
                }
                /*
                if (wayToTheEndRoom.get(indexOfList-1).name.contains("Z")&&wayToTheEndRoom.get(indexOfList).name.contains("Z")){
                    wayToTheEndRoomChecked.add(indexOfList,fixNode);
                    continue;
                }
                if((wayToTheEndRoom.get(indexOfList-1).name.contains("O")==false)&&(wayToTheEndRoom.get(indexOfList-2).name.contains("O")==false)&&(wayToTheEndRoom.get(indexOfList-2).name.contains("startRoom")==false)){
                    //wayToTheEndRoom.add(indexOfList-1,allNodes.get(10));
                    wayToTheEndRoomChecked.add(indexOfList,fixNode);
                    continue;
                }

                 */
            }
            if(node.name.contains("hallway")){
                if (wayToTheEndRoom.get(indexOfList-2).name.contains("N")&&wayToTheEndRoom.get(indexOfList-1).name.contains("Z")){
                    wayToTheEndRoomChecked.add(indexOfList,fixNode);
                    continue;
                }
            }

        }
        System.out.println(wayToTheEndRoomChecked);
    }

    public void run() {
        int x =300;
        int y =4000;
        //System.out.println(images);
        int index = 0;

        //Create dead ends

        BufferedImage biEndN =null;
        BufferedImage biEndO =null;
        BufferedImage biEndZ =null;
        BufferedImage biEndW =null;
        try { biEndN = ImageIO.read(new File("./Resources/Default/Hallways/HallwayN/HallwayN.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndO = ImageIO.read(new File("./Resources/Default/Hallways/HallwayO/HallwayO.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndZ = ImageIO.read(new File("./Resources/Default/Hallways/HallwayZ/HallwayZ.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndW = ImageIO.read(new File("./Resources/Default/Hallways/HallwayW/HallwayW.png")); } catch (IOException e) { e.printStackTrace(); }

        for(String image : images){

            if (image.length() - image.lastIndexOf("l")<13 &&image.length() - image.lastIndexOf("l")>10 ){
                render();
            }

            index++;
            System.out.println(image);
            //System.out.println( image.length() - image.lastIndexOf("l"));
            BufferedImage bi = null;
            BufferedImage biLast = null;
            try {
                bi = ImageIO.read(new File(image));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(index == 1 ||index == 2 ){
                g.drawImage(bi, x, y, null);
                x += bi.getTileWidth();
                continue;
            }

            //add pixelchecker
            //Code for bigroom placement
            if(image.contains("bigRoom")){
                String previous = images.get(index-2);
                try {
                    biLast = ImageIO.read(new File(previous));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (previous.contains("O")){
                    g.drawImage(bi, x, y-biLast.getHeight(), null);

                    if(previous.contains("N")&& (images.get(index-3).contains("bigRoom")==false)||previous.contains("NOW")||previous.contains("NOZW")){
                        g.drawImage(biEndZ, x-biLast.getWidth(), y-biLast.getHeight(), null);
                    }

                    if(previous.contains("Z")&&images.get(index-3).contains("N")==false){
                        g.drawImage(biEndN, x-biLast.getWidth(), y+biLast.getHeight(), null);
                    }

                    x += bi.getTileWidth();
                    y -= biLast.getHeight();


                    continue;
                }
                else if (previous.contains("Z")){
                    if(previous.contains("W")&&checkFreeSpace(x-biLast.getWidth()*2,y,result)==true) {
                        g.drawImage(biEndO, x-biLast.getWidth()*2, y, null);
                    }


                    g.drawImage(bi, x-biLast.getTileWidth()*2, y+biLast.getTileHeight(), null);
                    //x -= biLast.getTileWidth()*2;
                    y += biLast.getHeight();
                    continue;
                }
                else if (previous.contains("N")){
                    g.drawImage(bi, x-biLast.getTileWidth()*2, y-biLast.getTileHeight()*2, null);
                    //x += bi.getTileWidth();
                    y -= bi.getHeight();
                    continue;
                }
            }

            if(image.contains("hallway")){
                String previous = images.get(index-2);
                try {
                    biLast = ImageIO.read(new File(previous));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(image.contains("hallwayNOZW")&&previous.contains("O")){
                    if (previous.contains("N")){
                        if (checkFreeSpace(x-biLast.getWidth(),y-biLast.getHeight(),result)==true){
                            g.drawImage(biEndZ, x-biLast.getWidth(), y-biLast.getHeight(), null);
                        }
                    }
                    if (previous.contains("Z")){
                        if (checkFreeSpace(x-biLast.getWidth(),y+biLast.getHeight(),result)==true){
                            g.drawImage(biEndN, x-biLast.getWidth(), y+biLast.getHeight(), null);
                        }
                    }
                    if (previous.contains("W")){
                        if (checkFreeSpace(x-biLast.getWidth()*2,y,result)==true){
                            g.drawImage(biEndO, x-biLast.getWidth()*2, y, null);
                        }
                    }

                    g.drawImage(bi, x, y, null);
                    x += bi.getTileWidth();


                    //y -= biLast.getHeight();
                    continue;
                }

                if(image.contains("hallwayNOZW")&&previous.contains("Z")){
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    //x += biLast.getTileWidth();
                    y += bi.getHeight();
                    continue;
                }

                if (previous.contains("bigRoom") && image.contains("hallwayOZ") && (image.contains("W")==false)){
                    g.drawImage(bi, x-bi.getTileWidth(), y-bi.getTileHeight(), null);
                    //x += biLast.getTileWidth();
                    y -= bi.getHeight();
                    continue;
                }
                if (previous.contains("bigRoom") && image.contains("hallwayNOZ")&&(image.contains("W")==false)){
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    //x += bi.getTileWidth();
                    y += biLast.getHeight();
                    continue;
                }

                if (previous.contains("bigRoom") && image.contains("hallwayNO")&&(image.contains("W")==false) &&(image.contains("Z")==false)){
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    //x += bi.getTileWidth();
                    y += biLast.getHeight();
                    continue;
                }
//                if((image.contains("NOZ")==false) && previous.contains("OZW")){
//                    g.drawImage(bi, x-bi.getTileWidth(), y, null);
//                    //x += biLast.getTileWidth();
//                    //y += bi.getHeight();
//                    continue;
//                }

//                if((image.contains("W")==false) && previous.contains("Z")){
//                    g.drawImage(bi, x-bi.getTileWidth(), y+bi.getTileHeight(), null);
//                    //x += biLast.getTileWidth();
//                    y += bi.getHeight();
//                    continue;
//                }

                if((image.contains("N")) && previous.contains("Z")){
                    creatEnds(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);

                    g.drawImage(bi, x-bi.getTileWidth(), y+bi.getTileHeight(), null);
                    //x += biLast.getTileWidth();
                    y += bi.getHeight();
                    continue;
                }

                if((image.contains("Z")) && previous.contains("N") && checkFreeSpace(x-bi.getTileWidth(),y-bi.getTileHeight(),result)){
                    creatEnds(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y-bi.getTileHeight(), null);
                    //x += biLast.getTileWidth();
                    y -= bi.getHeight();
                    continue;
                }


                if (previous.contains("O")){
                    creatEnds(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    x += bi.getTileWidth();
                    //y -= biLast.getHeight();
                    continue;
                }
                else if (previous.contains("Z")){
                    creatEnds(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    //x += biLast.getTileWidth();
                    y += bi.getHeight();
                    continue;
                }
                else if (previous.contains("N")){
                    creatEnds(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-biLast.getTileWidth(), y-biLast.getTileHeight(), null);
                    //x += bi.getTileWidth();
                    y -= bi.getHeight();
                    continue;
                }
            }
            /*
            if(image.contains("endRoom")){

                g.drawImage(bi, x-biLast.getTileWidth(), y-biLast.getTileHeight(), null);
                //x += bi.getTileWidth();
                //y -= bi.getHeight();
                continue;
            }

             */

            g.drawImage(bi, x, y, null);
            //y = bi.getTileHeight();
            x += bi.getTileWidth();

            //code om van rand teresetten
            if(x > result.getWidth()){
                x = 0;
                y += bi.getTileHeight();
            }
        }
        try {
            ImageIO.write(result,"png",new File("result.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(checkFreeSpace(0,0,result));
//        System.out.println(checkFreeSpace(300,4000,result));
    }

    private void render(){
        try {
            ImageIO.write(result,"png",new File("result.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creatEnds(int x, int y,BufferedImage biEndN,BufferedImage biEndO,BufferedImage biEndZ,BufferedImage biEndW,BufferedImage biLast, String previous){
        if (previous.contains("N")){
            if (checkFreeSpace(x-biLast.getWidth(),y-biLast.getHeight(),result)==true){
                g.drawImage(biEndZ, x-biLast.getWidth(), y-biLast.getHeight(), null);
            }
        }
        if (previous.contains("O")){
            if (checkFreeSpace(x,y,result)==true){
                g.drawImage(biEndW, x, y, null);
            }
        }
        if (previous.contains("W")){
            if (checkFreeSpace(x-biLast.getWidth()*2,y,result)==true){
                g.drawImage(biEndO, x-biLast.getWidth()*2, y, null);
            }
        }
        if (previous.contains("Z")){
            if (checkFreeSpace(x-biLast.getWidth(),y+biLast.getHeight(),result)==true){
                g.drawImage(biEndN, x-biLast.getWidth(), y+biLast.getHeight(), null);
            }
        }
    }
    public boolean checkFreeSpace(int x, int y, BufferedImage img){
        Color black = new Color(img.getRGB(0, 0));
        Color colCompare = new Color(img.getRGB(x, y));

        if (black.getRGB() != colCompare.getRGB()) {
            return false;
        }
        else{
            return true;
        }
    }

}
