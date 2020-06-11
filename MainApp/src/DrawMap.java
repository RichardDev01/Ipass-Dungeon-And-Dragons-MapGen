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
    private int width = 2000;
    private int height = 2000;
    public  List<String> images= new ArrayList<>();
    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = result.getGraphics();

    public DrawMap(List<Node> wayToTheEndRoom) {
        this.wayToTheEndRoom = wayToTheEndRoom;
    }

    public void addImages(String s) {
        images.add(s);
    }

    public void run() {
        int x =0;
        int y =0;
        System.out.println(images);
        for(String image : images){
            System.out.println(image);
            BufferedImage bi = null;
            try {
                bi = ImageIO.read(new File(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(bi, x, y, null);
            x += 256;
            if(x > result.getWidth()){
                x = 0;
                y += bi.getHeight();
            }
        }
        try {
            ImageIO.write(result,"png",new File("result.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
