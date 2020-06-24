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

    private  List<Node> allNodes;                                                         // Array of all nodes
    private List<Node> wayToTheEndRoom;                                                   // Array of all nodes to end.
    private List<Node> wayToTheEndRoomChecked = new ArrayList<>();                        // Array of all nodes to end after pathchecking.
    private int width = 8000;                                                             // Image Width
    private int height = 8000;                                                            // Image Height
    private boolean forceRender;                                                          // ForceRender forces the program to ignore safety and places tiles on top of each other if need (useful for debugging)
    private boolean debugPixels;                                                          // Displays collision checking Pixels (useful for debugging)
    private boolean overLappingCheck = false;                                             // Error boolean, if true, re-run program
    private boolean falsePositiveCheck = false;                                           // Error Boolean, but this one is used for debuging
    private String FilePath = "./Resources/default";                                      // Filepath for resources
    public  List<String> images= new ArrayList<>();                                       // List of Strings of tile image resources
    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  // Creating an buffered to write to
    Graphics g = result.getGraphics();                                                    // Creating a graphics class to write with to the buffered image


    /*
    wayToTheEndRoom =Array of all nodes to end
    allNodes        =Array of all nodes
    forceRender     =ForceRender forces the program to ignore safety and places tiles on top of each other if need (useful for debugging)
    FilePath        =Filepath for resources
    debugPixels     =Displays collision checking Pixels (useful for debugging)
     */
    public DrawMap(List<Node> wayToTheEndRoom,List<Node>allNodes,boolean forceRender,String FilePath, boolean debugPixels) {
        this.wayToTheEndRoom = wayToTheEndRoom;
        this.allNodes = allNodes;
        this.forceRender = forceRender;
        this.FilePath = FilePath;
        this.debugPixels = debugPixels;
    }

    //Get List of Nodes of all nodes to end
    public List<Node> getWayToTheEndRoomChecked() {
        return wayToTheEndRoomChecked;
    }

    //Set List of Nodes of all nodes to end
    public void setWayToTheEndRoom(List<Node> wayToTheEndRoom) {
        this.wayToTheEndRoom = wayToTheEndRoom;
    }

    //Add Image String to List of Strings of tile image resources (useful for debugging)
    public void addImages(String s) {
        images.add(s);
    }

    //Transforms nodes list (wayToTheEndRoom) to image List (strings of tiles)
    public void nodesToString(){
        images.clear(); //Clear image list
        checkPath();    //Small check if path can work (this can be expanded)

        //Read name from nodes and makes filtepath to tile images
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
        System.out.println(images); //Debug printout, usefull
    }

    public void checkPath(){
        int indexOfList =0;
        System.out.println(wayToTheEndRoom);                    //List of nodes before
        Node fixNode = new Node(99,"hallwayNOZW");     //fixNode 1
        Node fixNode2 = new Node(100,"hallwayNOZ");    //fixNode 2

        //iterate over nodes list
        for (var node : wayToTheEndRoom){
            indexOfList++;
            wayToTheEndRoomChecked.add(node); // adding node to list

            //checking nodes before a bigroom and add node if statement is true
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
        System.out.println(wayToTheEndRoomChecked);             //List of nodes after
    }

    //Running the draw map function
    public boolean run() {

        //Starting coordinates of placing tiles
        int x =600;
        int y =4000;
        int index = 0;

        //Create dead ends images
        BufferedImage biEndN =null;
        BufferedImage biEndO =null;
        BufferedImage biEndZ =null;
        BufferedImage biEndW =null;
        try { biEndN = ImageIO.read(new File(FilePath+"/Hallways/HallwayN/HallwayN.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndO = ImageIO.read(new File(FilePath+"/Hallways/HallwayO/HallwayO.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndZ = ImageIO.read(new File(FilePath+"/Hallways/HallwayZ/HallwayZ.png")); } catch (IOException e) { e.printStackTrace(); }
        try { biEndW = ImageIO.read(new File(FilePath+"/Hallways/HallwayW/HallwayW.png")); } catch (IOException e) { e.printStackTrace(); }

        for(String image : images){

            //if collision is detected and force Render is off, set of error
            if (overLappingCheck ==true && forceRender == false){
                System.out.println("error making a map");
                return false;
            }

            //Check if current tile has more than 2 ends, if so, render the map (this is for collision detection)
            if (image.length() - image.lastIndexOf("l")<13 &&image.length() - image.lastIndexOf("l")>10 ){
                render();
            }

            index++;
            System.out.println(image);  //Debug string for current tile that wants to be placed
            BufferedImage bi = null;    //current image to buffered image
            BufferedImage biLast = null;//Previous image to buffered image (this only happens after the first 2 tiles*)
            try { bi = ImageIO.read(new File(image)); } catch (IOException e) { e.printStackTrace(); }

            //Place startRoom and tile after that next to each other ->
            if(index == 1 ||index == 2 ){
                g.drawImage(bi, x, y, null);
                if(debugPixels == true){debugPixels(x, y, bi);}
                collisionCheck(x, y, bi,Color.BLUE);
                x += bi.getWidth();
                //y = y;
                continue;
            }

            //Code for bigroom placement
            if(image.contains("bigRoom")){

                //writing Previous image to buffered image
                String previous = images.get(index-2);
                try { biLast = ImageIO.read(new File(previous)); } catch (IOException e) { e.printStackTrace(); }

                //Place bigroom ro the right ->
                if (previous.contains("O")&& collisionCheck(x,y-biLast.getHeight(),bi,Color.RED)== false){
                    if (collisionCheck(x,y-biLast.getHeight(),bi,Color.RED)== true){
                        overLappingCheck = true;
                    }
                    g.drawImage(bi, x, y-biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x, y-biLast.getHeight(), bi);}

                    creatEndsBigRooms(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);

                    x += bi.getWidth();
                    y -= biLast.getHeight();
                    continue;
                }

                //place bigroom ↓
                else if (previous.contains("Z")){
                    if (collisionCheck(x-biLast.getWidth()*2,y+biLast.getHeight(),bi,Color.RED)== true){
                        overLappingCheck = true;
                    }

                    g.drawImage(bi, x-biLast.getWidth()*2, y+biLast.getHeight(), null);

                    if(debugPixels == true){debugPixels(x-biLast.getWidth()*2, y+biLast.getHeight(), bi);}

                    creatEndsBigRooms(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);

                    //x = x;
                    y += biLast.getHeight();
                    continue;
                }

                //place bigroom ↑
                //else if (previous.contains("N")&& colisionCheck(x-biLast.getWidth()*2,y-biLast.getHeight()*2,bi)== false){
                else if (previous.contains("N")){
                    if (collisionCheck(x-biLast.getWidth()*2,y-biLast.getHeight()*2,bi,Color.RED)== true){
                        overLappingCheck = true;
                    }
                    creatEndsBigRooms(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth()*2, y-biLast.getHeight()*2, null);
                    if(debugPixels == true){debugPixels(x-biLast.getWidth()*2, y-biLast.getHeight()*2, bi);}

                    //x = x;
                    y -= bi.getHeight();
                    continue;
                }

                // place bigroom <-
                else if (previous.contains("W")){
                    //Buggy als de pest (Dev note)
                    if (collisionCheck(x-(bi.getWidth()+biLast.getWidth())-20,y+20,bi,Color.RED)== true){
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

                //if you come to here, well, there has been a placeing error
                System.out.println("where do i fit? #error");
                falsePositiveCheck = true;
                overLappingCheck = true;
            }

            //code for place hallways
            if(image.contains("hallway")){
                //writing Previous image to buffered image
                String previous = images.get(index-2);
                try { biLast = ImageIO.read(new File(previous)); } catch (IOException e) { e.printStackTrace(); }

                //place specific tile ↑ from previous
                if(image.contains("hallwayNOZW")&&previous.contains("NZW")){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-biLast.getWidth(), y-biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), bi);}
                    //x = x;
                    y -= biLast.getHeight();
                    continue;
                }

                //place specific tile ↑ from previous
                if(image.contains("hallwayNOZW")&&previous.contains("NZ")){

                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-biLast.getWidth(), y-biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), bi);}
                    //x += bi.getWidth();
                    y -= biLast.getHeight();
                    continue;
                }

                //place specific tile → from previous
                if(image.contains("hallwayNOZW")&&previous.contains("O")){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getWidth();
                    //y = y;
                    continue;
                }

                //place specific tile ← from previous
                if(image.contains("hallwayNOZW")&&previous.contains("ZW")){
                    g.drawImage(bi, x-bi.getWidth()*2, y, null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth()*2, y, bi);}
                    x -= biLast.getWidth()*3;
                    y -= bi.getHeight();
                    continue;
                }

                //place specific tile ↓ from previous
                if(image.contains("hallwayNOZW")&&previous.contains("Z")){
                    g.drawImage(bi, x-bi.getWidth(), y+biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+biLast.getHeight(), bi);}
                    //x = x;
                    y += bi.getHeight();
                    continue;
                }

                //place specific tile ← from previous
                if(image.contains("hallwayNOZW")&&previous.contains("NW")){
                    g.drawImage(bi, x-bi.getWidth()*2, y+biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth()*2, y+biLast.getHeight(), bi);}
                    x -= biLast.getWidth()*2;
                    y += bi.getHeight()*2;
                    continue;
                }

                //place specific tile after bigroom → from previous
                if (previous.contains("bigRoom") && image.contains("hallwayNOZW")){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getWidth();
                    //y = y;
                    continue;
                }

                //place specific tile after bigroom ↑ from previous
                if (previous.contains("bigRoom") && image.contains("hallwayNOZ")&& collisionCheck(x-bi.getWidth(),y,bi,Color.pink)== false){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y-bi.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y-bi.getHeight(), bi);}
                    //x =x;
                    y -= bi.getHeight();
                    continue;
                }

                //place specific tile after bigroom ↑ from previous
                if (previous.contains("bigRoom") && image.contains("hallwayOZ") && (image.contains("W")==false)){
                    g.drawImage(bi, x-bi.getWidth(), y-bi.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y-bi.getHeight(), bi);}
                    x += bi.getWidth();
                    y -= bi.getHeight()*2;
                    continue;
                }

                //place specific tile after bigroom ↓ from previous
                if (previous.contains("bigRoom") && image.contains("hallwayNOZ")&&(image.contains("W")==false)){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y+biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+biLast.getHeight(), bi);}
                    //x = x;
                    y += biLast.getHeight();
                    continue;
                }

                //place specific tile after bigroom ↓ from previous
                if (previous.contains("bigRoom") && image.contains("hallwayNO")&&(image.contains("W")==false) &&(image.contains("Z")==false)){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y+biLast.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+biLast.getHeight(), bi);}
                    //x = x;
                    y += biLast.getHeight();
                    continue;
                }

                //place specific tile after bigroom → from previous
                if(previous.contains("bigRoom") && image.contains("W")){
                    if (collisionCheck(x,y+50,bi,Color.pink)== true){
                        System.out.println("error?");
                    }
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getWidth();
                    //y = y
                    continue;
                }

                //place specific tile after bigroom ↓ from previous
                if(previous.contains("bigRoom") && image.contains("N")&& collisionCheck(x-bi.getWidth()+10,y+biLast.getHeight()+10,bi,Color.pink)== false){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+biLast.getHeight(), bi);}
                    //x = x;
                    y += biLast.getHeight();
                    continue;
                }

                //if code somehow doesn't make endrooms after bigroom, execute this code
                if (previous.contains("bigRoom")){
                    creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                }

                //place specific tile ↓ from previous
                if((image.contains("NZW")) && previous.contains("NOZW")){
                    creatEndsHallways(x-bi.getWidth(),y+bi.getHeight(),biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y+bi.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+bi.getHeight(), bi);}
                    //x = x;
                    y += bi.getHeight();
                    continue;
                }

                //place specific tile → from previous
                if(image.contains("W")&&previous.contains("O")&& collisionCheck(x,y,bi,Color.yellow)== false){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x, y, null);
                    if(debugPixels == true){debugPixels(x, y, bi);}
                    x += bi.getWidth();
                    //y = y;
                    System.out.println("i'm here");
                    continue;
                }

                //place specific tile ↓ from previous
                if((image.contains("N")) && previous.contains("Z")){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y+bi.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y+bi.getHeight(), bi);}
                    //x = x;
                    y += bi.getHeight();
                    continue;
                }

                //place specific tile ← from previous
                if(image.contains("O")&&previous.contains("W")&& collisionCheck(x-bi.getWidth()*2,y+bi.getHeight(),bi,Color.yellow)== false){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth()*2, y, null);
                    if(debugPixels == true){debugPixels(x-bi.getTileWidth()*2, y, bi);}
                    x -= bi.getWidth();
                    //y = y;
                    continue;
                }

                //place specific tile ↑ from previous
                if((image.contains("Z")) && previous.contains("N") && collisionCheck(x-bi.getWidth(),y-bi.getHeight(),bi,Color.yellow)== false){
                    creatEndsHallways(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                    g.drawImage(bi, x-bi.getWidth(), y-bi.getHeight(), null);
                    if(debugPixels == true){debugPixels(x-bi.getWidth(), y-bi.getHeight(), bi);}
                    //x = x;
                    y -= bi.getHeight();

                    continue;
                }

                System.out.println("i dont fit me stupid tile ಠ_ಠ");
                overLappingCheck = true;
                index --;
            }

            //Places endRoom in center of lastroom (should be a bigroom).. those are chest
            if(image.contains("endRoom")){
                String previous = images.get(index-2);
                try { biLast = ImageIO.read(new File(previous)); } catch (IOException e) { e.printStackTrace(); }
                creatEndsBigRoomsself(x,y,biEndN,biEndO,biEndZ,biEndW,biLast,previous);
                g.drawImage(bi, x-biLast.getWidth()/2-bi.getWidth()/2, y+biLast.getHeight()/2-bi.getHeight()/2, null);
                continue;
            }

            //if the dungeon somehow reached the edge of the canvas, roll back to the left
            if(x > result.getWidth()){
                x = 0;
                y += bi.getHeight();
            }
        }
        render();

        return true;
    }

    //Write buffered image to i/o.. Render image
    private void render(){
        try { ImageIO.write(result,"png",new File("result.png")); } catch (IOException e) { e.printStackTrace(); }
    }

    /*
    x   = The starting location of x
    y   = The starting location of y
    bi  = The image that you want to draw or collision check with
     */
    private  boolean collisionCheck(int x, int y , BufferedImage bi, Color debugcolor){
        if(debugPixels) {
            int debugy = y - 2000;
            int debugx = x;
            g.setColor(debugcolor);
            debugy += bi.getHeight();
            g.drawRect(debugx + 100, debugy - 100, 3, 3);
            g.drawRect(debugx + 100, debugy - bi.getHeight() + 100, 3, 3);
            g.drawRect(debugx + bi.getTileWidth() - 100, debugy - 100, 3, 3);
            g.drawRect(debugx + bi.getTileWidth() - 100, debugy - bi.getHeight() + 100, 3, 3);
        }
        y+=bi.getHeight();
        //Bottom left, Top Left, Bottom Right, Top Right
        if(checkFreeSpace(x+100,y-100,result)==false ||
                checkFreeSpace(x+100,y-bi.getHeight()+100,result)==false ||
                checkFreeSpace(x+bi.getTileWidth()-100,y-100,result)==false ||
                checkFreeSpace(x+bi.getTileWidth()-100,y-bi.getHeight()+100,result)==false){
            return true;
        }
        return false;
    }

    /*
    x   = The starting location of x
    y   = The starting location of y
    bi  = The image that you want to draw or collision check with
    Renders debug pixels on top of tiles
    */
    private  void   debugPixels(int x, int y, BufferedImage bi){
        g.setColor(Color.WHITE);

        y +=bi.getHeight();
        g.drawRect(x+100,y-100,3,3);
        g.drawRect(x+100,y-bi.getHeight()+100,3,3);
        g.drawRect(x+bi.getTileWidth()-100,y-100,3,3);
        g.drawRect(x+bi.getTileWidth()-100,y-bi.getHeight()+100,3,3);
    }

    /*
    Use this function after the tile is placed after the bigroom
    x = topright of cordinate of bigroom
    y = topright of cordinate of bigroom
    biEndN = Dead end North
    biEndO = Dead end East
    biEndZ = Dead end South
    biEndW = Dead end West
    biLast = previous image (Bigroom)
    previous =/ unused
     */
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

    /*
    Use this function after placing a bigroom to make dead ends for the tile before the bigroom
    x = topright of cordinate of last tile
    y = topright of cordinate of last tile
    biEndN = Dead end North
    biEndO = Dead end East
    biEndZ = Dead end South
    biEndW = Dead end West
    biLast = previous image
    previous = Read exits of previous tile where dead ends can be placed
     */
    private void creatEndsBigRooms(int x, int y, BufferedImage biEndN, BufferedImage biEndO, BufferedImage biEndZ, BufferedImage biEndW, BufferedImage biLast, String previous){
        if (previous.contains("N")){
            if(!collisionCheck(x-biLast.getWidth(),y-biLast.getHeight(),biLast,Color.LIGHT_GRAY)){
            //if (checkFreeSpace(x-biLast.getWidth(),y-biLast.getHeight(),result)==true){
                g.drawImage(biEndZ, x-biLast.getWidth(), y-biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), biEndZ);}
            }
        }
        if (previous.contains("O")){
            if(!collisionCheck(x,y,biLast,Color.LIGHT_GRAY)){
            //if (checkFreeSpace(x,y,result)==true){
                g.drawImage(biEndW, x, y, null);
                if(debugPixels == true){debugPixels(x, y, biEndW);}
            }
        }
        if (previous.contains("W")){
            if(!collisionCheck(x-biLast.getWidth()*2,y,biLast,Color.LIGHT_GRAY)){
            //if (checkFreeSpace(x-biLast.getWidth()*2+10,y+10,result)==true){
                g.drawImage(biEndO, x-biLast.getWidth()*2, y, null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth()*2, y, biEndO);}
            }
        }
        if (previous.contains("Z")){
            if(!collisionCheck(x-biLast.getWidth(),y+biLast.getHeight(),biLast,Color.LIGHT_GRAY)){
            //if (checkFreeSpace(x-biLast.getWidth()+10,y+biLast.getHeight()+10,result)==true){
                g.drawImage(biEndN, x-biLast.getWidth(), y+biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y+biLast.getHeight(), biEndO);}
            }
        }
    }

    /*
    Use this function after placing a hallway tile to make dead ends for the tile before hand
    x = topright of cordinate of last tile
    y = topright of cordinate of last tile
    biEndN = Dead end North
    biEndO = Dead end East
    biEndZ = Dead end South
    biEndW = Dead end West
    biLast = previous image
    previous = Read exits of previous tile where dead ends can be placed
     */
    private void creatEndsHallways(int x, int y, BufferedImage biEndN, BufferedImage biEndO, BufferedImage biEndZ, BufferedImage biEndW, BufferedImage biLast, String previous){
        if (previous.contains("N")){
            if(!collisionCheck(x-biLast.getWidth(),y-biLast.getHeight(),biLast,Color.white)){
            //if (checkFreeSpace(x-biLast.getWidth(),y-biLast.getHeight(),result)==true){
                g.drawImage(biEndZ, x-biLast.getWidth(), y-biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y-biLast.getHeight(), biEndZ);}
            }
        }
        if (previous.contains("O")){
            if(!collisionCheck(x,y,biLast,Color.white)){
            //if (checkFreeSpace(x,y,result)==true){
                g.drawImage(biEndW, x, y, null);
                if(debugPixels == true){debugPixels(x, y, biEndW);}
            }
        }
        if (previous.contains("W")){
            if(!collisionCheck(x-biLast.getWidth()*2,y,biLast,Color.white)){
            //if (checkFreeSpace(x-biLast.getWidth()*2,y,result)==true){
                g.drawImage(biEndO, x-biLast.getWidth()*2, y, null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth()*2, y, biEndO);}
            }
        }
        if (previous.contains("Z")){
            if(!collisionCheck(x-biLast.getWidth(),y+biLast.getHeight(),biLast,Color.white)){
            //if (checkFreeSpace(x-biLast.getWidth()+10,y+biLast.getHeight()+10,result)==true){
                g.drawImage(biEndN, x-biLast.getWidth(), y+biLast.getHeight(), null);
                if(debugPixels == true){debugPixels(x-biLast.getWidth(), y+biLast.getHeight(), biEndN);}
            }
        }
    }

    /*
    checks if given coordinate is free by comparing the colour on the top left(this should be black by default)
    x   = Coordinate you want to check
    y   = Coordinate you want to check
    img = What image you want to check
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
