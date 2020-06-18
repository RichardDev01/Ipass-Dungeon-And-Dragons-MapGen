/*
Sources:
https://stackoverflow.com/questions/3922276/how-to-combine-multiple-pngs-into-one-big-png-file
https://stackoverflow.com/questions/20826216/copy-two-bufferedimages-into-one-image-side-by-side
https://docs.oracle.com/javase/tutorial/2d/images/drawonimage.html
https://www.javamex.com/tutorials/graphics/bufferedimage.shtml
 */
//#TODO Mogelijk croppen

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrawMap {

    private  List<Node> allNodes = new ArrayList<>();
    private List<Node> wayToTheEndRoom = new ArrayList<>();  // Array of all nodes.
    private List<Node> wayToTheEndRoomChecked = new ArrayList<>();  // Array of all nodes after pathchecking.
    private int width = 8000;
    private int height = 8000;
    private boolean forceRender = false;
    private boolean debugPixels = true;
    private boolean overLappingCheck = false;
    private boolean falsePositiveCheck = false;
    private String FilePath = "./Resources/default";
    public  List<String> images= new ArrayList<>();
    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = result.getGraphics();

    public DrawMap(List<Node> wayToTheEndRoom,List<Node>allNodes,boolean forceRender,String FilePath, boolean debugPixels) {
        this.wayToTheEndRoom = wayToTheEndRoom;
        this.allNodes = allNodes;
        this.forceRender = forceRender;
        this.FilePath = FilePath;
        this.debugPixels = debugPixels;
    }

    public List<Node> getWayToTheEndRoomChecked() {
        return wayToTheEndRoomChecked;
    }

    public void setWayToTheEndRoom(List<Node> wayToTheEndRoom) {
        this.wayToTheEndRoom = wayToTheEndRoom;
    }

    public void addImages(String s) {
        images.add(s);
    }

    public void nodesToString(){
        images.clear();
        checkPath();
        for(var node : wayToTheEndRoomChecked){
            if (node.name.contains("startRoom")){
                images.add(FilePath+"/"+node.name+"/"+node.name+".png");
            }
            if (node.name.contains("hallway")) {
                images.add(FilePath+"/Hallways/"+node.name+"/"+node.name+".png");
            }
            if (node.name.contains("bigRoom")){
                images.add(FilePath+"/Bigrooms/"+node.name+"/"+node.name+".png");
            }
            if (node.name.contains("endRoom")){
                images.add(FilePath+"/"+node.name+"s/"+node.name+".png");
            }
        }
        System.out.println(images);
    }

    public void checkPath(){
        int indexOfList =0;
        System.out.println(wayToTheEndRoom);
        Node fixNode = new Node(99,"hallwayNOZW");
        Node fixNode2 = new Node(100,"hallwayNOZ");
        for (var node : wayToTheEndRoom){
            //System.out.println(node);
            indexOfList++;
            wayToTheEndRoomChecked.add(node);
            if(node.name.contains("bigRoom")){
                if (wayToTheEndRoom.get(indexOfList-1).name.contains("N")&&wayToTheEndRoom.get(indexOfList).name.contains("N")){
                    wayToTheEndRoomChecked.add(indexOfList,fixNode);
                    continue;
                }
                if (wayToTheEndRoom.get(indexOfList-2).name.contains("hallwayNW")){
                    wayToTheEndRoomChecked.add(indexOfList-1,fixNode2);
                    continue;
                }
            }
        }
        System.out.println(wayToTheEndRoomChecked);
    }

    public boolean run() {

        int x =600;
        int y =4000;
        //System.out.println(images);
        int index = 0;

        //Create dead ends
        BufferedImage biEndN =null;
        BufferedImage biEndO =null;
        BufferedImage biEndZ =null;
        BufferedImage biEndW =null;
        try { biEndN = ImageIO.read(new File(FilePath+"/Hallways/HallwayN/HallwayN.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndO = ImageIO.read(new File(FilePath+"/Hallways/HallwayO/HallwayO.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndZ = ImageIO.read(new File(FilePath+"/Hallways/HallwayZ/HallwayZ.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndW = ImageIO.read(new File(FilePath+"/Hallways/HallwayW/HallwayW.png")); } catch (IOException e) { e.printStackTrace(); }

        for(String image : images){

            if (overLappingCheck ==true && forceRender == false){
                System.out.println("error making a map");
                return false;
            }

            if (image.length() - image.lastIndexOf("l")<13 &&image.length() - image.lastIndexOf("l")>10 ){
                render();
            }

            index++;
            System.out.println(image);
            BufferedImage bi = null;
            BufferedImage biLast = null;
            try {
                bi = ImageIO.read(new File(image));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(index == 1 ||index == 2 ){
                g.drawImage(bi, x, y, null);
                if(debugPixels == true){debugPixels(x, y, bi);}
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
                if (previous.contains("O")&& collisionCheck(x,y-biLast.getHeight(),bi)== false){
                    if (collisionCheck(x,y-biLast.getHeight(),bi)== true){
                        overLappingCheck = true;
                    }
                    g.drawImage(bi, x, y-biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x, y-biLast.getHeight(), bi);}

                    creatEndsBigRooms(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);

                    x += bi.getTileWidth();
                    y -= biLast.getHeight();
                    continue;
                }
                else if (previous.contains("Z")){
                    if (collisionCheck(x-biLast.getTileWidth()*2,y+biLast.getHeight(),bi)== true){
                        overLappingCheck = true;
                    }

                    g.drawImage(bi, x-biLast.getTileWidth()*2, y+biLast.getTileHeight(), null);

                    if(debugPixels == true){debugPixels(x-biLast.getTileWidth()*2, y+biLast.getHeight(), bi);}

                    creatEndsBigRooms(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);

                    //x -= biLast.getTileWidth()*2;
                    y += biLast.getHeight();
                    continue;
                }
                //else if (previous.contains("N")&& colisionCheck(x-biLast.getTileWidth()*2,y-biLast.getHeight()*2,bi)== false){
                else if (previous.contains("N")){
                    if (collisionCheck(x-biLast.getTileWidth()*2,y-biLast.getHeight()*2,bi)== true){
                        overLappingCheck = true;
                    }
                    creatEndsBigRooms(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth()*2, y-biLast.getHeight()*2, null);
                    if(debugPixels == true){debugPixels(x-biLast.getTileWidth()*2, y-biLast.getHeight()*2, bi);}

                    //x += bi.getTileWidth();
                    y -= bi.getHeight();
                    continue;
                }

                else if (previous.contains("W")){
                    //Buggy als de pest (Dev note)
                    if (collisionCheck(x-(bi.getWidth()+biLast.getWidth())-20,y+20,bi)== true){
                        falsePositiveCheck = true;
                        System.out.println("check Bigrooms");
                    }

                    creatEndsBigRooms(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth()-biLast.getWidth(), y, null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth()-biLast.getWidth(), y, bi);}

                    x -= bi.getWidth();
                    y += biLast.getHeight();

                    continue;
                }

                System.out.println("where do i fit? #error");
                falsePositiveCheck = true;
                overLappingCheck = true;
            }

            if(image.contains("hallway")){
                String previous = images.get(index-2);
                try {
                    biLast = ImageIO.read(new File(previous));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(image.contains("hallwayNOZW")&&previous.contains("hallwayNOZW")){
                    continue;
                }

                if(image.contains("hallwayNOZW")&&previous.contains("NZW")){

                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-biLast.getWidth(), y-biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), bi);}
                    //x += bi.getWidth();
                    y -= biLast.getHeight();
                    continue;
                }
                if(image.contains("hallwayNOZW")&&previous.contains("NZ")){

                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-biLast.getWidth(), y-biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), bi);}

                    x += bi.getWidth();
                    y -= biLast.getHeight()*2;
                    continue;
                }

                if(image.contains("hallwayNOZW")&&previous.contains("O")){

                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getWidth();
                    //y -= biLast.getHeight();
                    continue;
                }

                if(image.contains("hallwayNOZW")&&previous.contains("ZW")){
                    g.drawImage(bi, x-bi.getTileWidth()*2, y, null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth()*2, y, bi);}
                    x -= biLast.getTileWidth()*3;
                    y -= bi.getHeight();
                    continue;
                }
                if(image.contains("hallwayNOZW")&&previous.contains("Z")){
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth(), y+biLast.getTileHeight(), bi);}
                    //x += biLast.getTileWidth();
                    y += bi.getHeight();
                    continue;
                }
                if(image.contains("hallwayNOZW")&&previous.contains("NW")){
                    g.drawImage(bi, x-bi.getTileWidth()*2, y+biLast.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth()*2, y+biLast.getTileHeight(), bi);}
                    x -= biLast.getTileWidth()*2;
                    y += bi.getHeight()*2;
                    continue;
                }

                if (previous.contains("bigRoom") && image.contains("hallwayNOZW")){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getTileWidth();
                    continue;
                }

                if (previous.contains("bigRoom") && image.contains("hallwayNOZ")&& collisionCheck(x-bi.getWidth(),y,bi)== false){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y-bi.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth(), y-bi.getTileHeight(), bi);}
                    //x += biLast.getTileWidth();
                    y -= bi.getHeight();
                    continue;
                }


                if (previous.contains("bigRoom") && image.contains("hallwayOZ") && (image.contains("W")==false)){
                    g.drawImage(bi, x-bi.getWidth(), y-bi.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y-bi.getHeight(), bi);}
                    x += bi.getTileWidth();
                    y -= bi.getHeight()*2;
                    continue;
                }
                if (previous.contains("bigRoom") && image.contains("hallwayNOZ")&&(image.contains("W")==false)){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y+biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+biLast.getHeight(), bi);}
                    //x += bi.getTileWidth();
                    y += biLast.getHeight();

                    continue;
                }

                if (previous.contains("bigRoom") && image.contains("hallwayNO")&&(image.contains("W")==false) &&(image.contains("Z")==false)){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y+biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+biLast.getHeight(), bi);}
                    //x += bi.getWidth();
                    y += biLast.getHeight();

                    continue;
                }

                if(previous.contains("bigRoom") && image.contains("W")){
                    if (collisionCheck(x,y+50,bi)== true){
                        System.out.println("error?");
                    }
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getWidth();
                    continue;
                }
                if(previous.contains("bigRoom") && image.contains("N")&& collisionCheck(x-bi.getTileWidth()+10,y+biLast.getTileHeight()+10,bi)== false){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth(), y+biLast.getTileHeight(), bi);}
                    //x += bi.getTileWidth();
                    y += biLast.getHeight();
                    continue;
                }

                if (previous.contains("bigRoom")){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                }
                if((image.contains("NZW")) && previous.contains("NOZW")){
                    creatEndsHallways(x-bi.getTileWidth(),y+bi.getTileHeight(),biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y+bi.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth(), y+bi.getTileHeight(), bi);}
                    //x += biLast.getTileWidth();
                    y += bi.getHeight();

                    continue;
                }

                if(image.contains("W")&&previous.contains("O")&& collisionCheck(x+bi.getTileWidth()-10,y,bi)== false){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getWidth();
                    //y -= bi.getHeight();
                    continue;
                }

                if((image.contains("N")) && previous.contains("Z")){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y+bi.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth(), y+bi.getTileHeight(), bi);}
                    //x += biLast.getTileWidth();
                    y += bi.getHeight();
                    continue;
                }

                if(image.contains("O")&&previous.contains("W")&& collisionCheck(x-bi.getTileWidth()*2,y+bi.getHeight(),bi)== false){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth()*2, y, null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth()*2, y, bi);}
                    x -= bi.getTileWidth();
                    //y -= bi.getHeight();
                    continue;
                }

                if((image.contains("Z")) && previous.contains("N") && collisionCheck(x-bi.getTileWidth(),y-bi.getTileHeight(),bi)== false){
                    if (collisionCheck(x-bi.getTileWidth(),y-bi.getTileHeight(),bi)== true){
                        overLappingCheck = true;
                    }
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y-bi.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth(), y-bi.getTileHeight(), bi);}
                    //x += biLast.getTileWidth();
                    y -= bi.getHeight();

                    continue;
                }

                if(image.contains("W")&& previous.contains("O")){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getTileWidth();
                    //y += bi.getHeight();

                    continue;
                }
                System.out.println("i dont fit me stupid tile");
                overLappingCheck = true;
                index --;
            }

            if(image.contains("endRoom")){
                String previous = images.get(index-2);
                try {
                    biLast = ImageIO.read(new File(previous));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                g.drawImage(bi, x-biLast.getWidth()/2-bi.getWidth()/2, y+biLast.getHeight()/2-bi.getHeight()/2, null);
                continue;
            }

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
        return true;
    }

    private void render(){
        try {
            ImageIO.write(result,"png",new File("result.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  boolean collisionCheck(int x, int y , BufferedImage bi){
        //Bottom left, Top Left, Bottom Right, Top Right
        if(checkFreeSpace(x+100,y-100,result)==false ||
                checkFreeSpace(x+100,y-bi.getHeight()+100,result)==false ||
                checkFreeSpace(x+bi.getTileWidth()-100,y-100,result)==false ||
                checkFreeSpace(x+bi.getTileWidth()-100,y-bi.getHeight()+100,result)==false){
            return true;
        }
        return false;
    }

    private  void   debugPixels(int x, int y, BufferedImage bi){
        g.setColor(Color.WHITE);

        y +=bi.getHeight();
        g.drawRect(x+100,y-100,3,3);
        g.drawRect(x+100,y-bi.getHeight()+100,3,3);
        g.drawRect(x+bi.getTileWidth()-100,y-100,3,3);
        g.drawRect(x+bi.getTileWidth()-100,y-bi.getHeight()+100,3,3);
    }

    private void creatEndsBigRoomsself(int x, int y, BufferedImage biEndN, BufferedImage biEndO, BufferedImage biEndZ, BufferedImage biEndW, BufferedImage biLast, String previous){
            if (checkFreeSpace(x-biLast.getWidth()/2+10,y-biLast.getHeight()/2+10,result)==true){
                g.drawImage(biEndZ, x-biLast.getWidth()/2, y-biLast.getHeight()/2, null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth()/2+10, y-biLast.getHeight()/2+10, biEndZ);}
            }
            if (checkFreeSpace(x+10,y+10,result)==true){
                g.drawImage(biEndW, x, y, null);
                if(debugPixels == true){debugPixels(x, y, biEndW);}
            }
            if (checkFreeSpace(x-biLast.getWidth()-biEndO.getWidth()+10,y+biLast.getHeight()/2,result)==true){
                g.drawImage(biEndO, x-biLast.getWidth()-biEndO.getWidth(), y+biLast.getHeight()/2, null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth()-biEndO.getWidth(), y+biLast.getHeight()/2, biEndO);}
            }
            if (checkFreeSpace(x-biLast.getWidth()/2,y+biLast.getHeight(),result)==true){
                g.drawImage(biEndN, x-biLast.getWidth()/2, y+biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth()/2, y+biLast.getHeight(), biEndO);}
            }
    }


    private void creatEndsBigRooms(int x, int y, BufferedImage biEndN, BufferedImage biEndO, BufferedImage biEndZ, BufferedImage biEndW, BufferedImage biLast, String previous){
        if (previous.contains("N")){
            if (checkFreeSpace(x-biLast.getWidth(),y-biLast.getHeight(),result)==true){
                g.drawImage(biEndZ, x-biLast.getWidth(), y-biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), biEndZ);}
            }
        }
        if (previous.contains("O")){
            if (checkFreeSpace(x,y,result)==true){
                g.drawImage(biEndW, x, y, null);
                if(debugPixels == true){debugPixels(x, y, biEndW);}
            }
        }
        if (previous.contains("W")){
            if (checkFreeSpace(x-biLast.getWidth()*2+10,y+10,result)==true){
                g.drawImage(biEndO, x-biLast.getWidth()*2, y, null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth()*2, y, biEndO);}
            }
        }
        if (previous.contains("Z")){
            if (checkFreeSpace(x-biLast.getWidth()+10,y+biLast.getHeight()+10,result)==true){
                g.drawImage(biEndN, x-biLast.getWidth(), y+biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y+biLast.getHeight(), biEndO);}
            }
        }
    }

//Deze functie
    private void creatEndsHallways(int x, int y, BufferedImage biEndN, BufferedImage biEndO, BufferedImage biEndZ, BufferedImage biEndW, BufferedImage biLast, String previous){
        if (previous.contains("N")){
            if (checkFreeSpace(x-biLast.getWidth(),y-biLast.getHeight(),result)==true){
                g.drawImage(biEndZ, x-biLast.getWidth(), y-biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), biEndZ);}
            }
        }
        if (previous.contains("O")){
            if (checkFreeSpace(x,y,result)==true){
                g.drawImage(biEndW, x, y, null);
                if(debugPixels == true){debugPixels(x, y, biEndW);}
            }
        }
        if (previous.contains("W")){
            if (checkFreeSpace(x-biLast.getWidth()*2,y,result)==true){
                g.drawImage(biEndO, x-biLast.getWidth()*2, y, null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth()*2, y, biEndO);}
            }
        }
        if (previous.contains("Z")){
            if (checkFreeSpace(x-biLast.getWidth()+10,y+biLast.getHeight()+10,result)==true){
                g.drawImage(biEndN, x-biLast.getWidth(), y+biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y+biLast.getHeight(), biEndN);}
            }
        }
    }


    /*
    Geef de x en y waarden op met de image
    Deze functie controleerd de x en y met 1 pixel links boven in(normaal is deze zwart)
    Als dit niet overeen komt, dan return false
    */
    public boolean checkFreeSpace(int x, int y, BufferedImage img){
        Color black = new Color(img.getRGB(2, 2));
        Color colCompare = new Color(img.getRGB(x, y));

        if (black.getRGB() != colCompare.getRGB()) {
            return false;
        }
        else{
            return true;
        }
    }
}
