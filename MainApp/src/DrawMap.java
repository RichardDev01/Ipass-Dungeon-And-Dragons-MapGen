/*
Sources:
https://stackoverflow.com/questions/3922276/how-to-combine-multiple-pngs-into-one-big-png-file
https://stackoverflow.com/questions/20826216/copy-two-bufferedimages-into-one-image-side-by-side
https://docs.oracle.com/javase/tutorial/2d/images/drawonimage.html
https://www.javamex.com/tutorials/graphics/bufferedimage.shtml
 */

//#TODO een aantal images aan elkaar krijgen (Rechte lijn)
//#TODO Logica in het aan elkaar plakken
//#TODO Mogelijk croppen
//#TODO Uitlezen van pixels om deadends toe tevoegen

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrawMap {

    private List<Node> wayToTheEndRoom = new ArrayList<>();  // Array of all nodes.
    private int width = 8000;
    private int height = 8000;
    public  List<String> images= new ArrayList<>();
    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = result.getGraphics();

    public DrawMap(List<Node> wayToTheEndRoom) {
        this.wayToTheEndRoom = wayToTheEndRoom;
    }

    public void addImages(String s) {
        images.add(s);
    }

    public void nodesToString(){
        for(var node : wayToTheEndRoom){
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

    public void run() {
        int x =300;
        int y =1600;
        System.out.println(images);
        int index = 0;
        for(String image : images){

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
                x += bi.getTileWidth();
                continue;
            }
            if(image.contains("bigRoom")){
                String previous = images.get(index-2);
                try {
                    biLast = ImageIO.read(new File(previous));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (previous.contains("O")){
                    g.drawImage(bi, x, y-biLast.getHeight(), null);
                    x += bi.getTileWidth();
                    y = bi.getHeight();
                    continue;
                }
                else if (previous.contains("Z")){
                    g.drawImage(bi, x-bi.getTileWidth(), y+biLast.getTileHeight(), null);
                    x += biLast.getTileWidth();
                    y += bi.getHeight();
                    continue;
                }
                else if (previous.contains("N")){
                    g.drawImage(bi, x-biLast.getTileWidth()*2, y-biLast.getTileHeight()*2, null);
                    //x += bi.getTileWidth();
                    y += bi.getHeight();
                    continue;
                }

            }
            g.drawImage(bi, x, y, null);
            y = bi.getTileHeight();
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

    }

}
