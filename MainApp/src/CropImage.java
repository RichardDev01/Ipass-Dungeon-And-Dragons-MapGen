//https://stackoverflow.com/questions/10678015/how-to-auto-crop-an-image-white-border-in-java
// This code is made by Todd from stackoverflow

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class CropImage {
 BufferedImage crop;

    public void autoCrop(BufferedImage sourceImage) {
        crop = getCroppedImage(sourceImage,0);
        render();
        return ;
    }

    private void render(){
        try { ImageIO.write(crop,"png",new File("resultCrop.png")); } catch (IOException e) { e.printStackTrace(); }
    }

    private BufferedImage cropImage(BufferedImage image,int startX,int startY, int endX,int endY){
        BufferedImage img = image.getSubimage(startX, startY, endX, endY);
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        return copyOfImage;
    }

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

    public BufferedImage getCroppedImage(BufferedImage source, double tolerance) {
        // Get our top-left pixel color as our "baseline" for cropping
        int baseColor = source.getRGB(2, 2);

        int width = source.getWidth();
        int height = source.getHeight();

        int topY = Integer.MAX_VALUE, topX = Integer.MAX_VALUE;
        int bottomY = -1, bottomX = -1;
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                if (colorWithinTolerance(baseColor, source.getRGB(x, y), tolerance)) {
                    if (x < topX) topX = x;
                    if (y < topY) topY = y;
                    if (x > bottomX) bottomX = x;
                    if (y > bottomY) bottomY = y;
                }
            }
        }

        BufferedImage destination = new BufferedImage( (bottomX-topX+1),
                (bottomY-topY+1), BufferedImage.TYPE_INT_ARGB);

        destination.getGraphics().drawImage(source, 0, 0,
                destination.getWidth(), destination.getHeight(),
                topX, topY, bottomX, bottomY, null);

        return destination;
    }

    private boolean colorWithinTolerance(int a, int b, double tolerance) {
        int aAlpha  = (int)((a & 0xFF000000) >>> 24);   // Alpha level
        int aRed    = (int)((a & 0x00FF0000) >>> 16);   // Red level
        int aGreen  = (int)((a & 0x0000FF00) >>> 8);    // Green level
        int aBlue   = (int)(a & 0x000000FF);            // Blue level

        int bAlpha  = (int)((b & 0xFF000000) >>> 24);   // Alpha level
        int bRed    = (int)((b & 0x00FF0000) >>> 16);   // Red level
        int bGreen  = (int)((b & 0x0000FF00) >>> 8);    // Green level
        int bBlue   = (int)(b & 0x000000FF);            // Blue level

        double distance = Math.sqrt((aAlpha-bAlpha)*(aAlpha-bAlpha) +
                (aRed-bRed)*(aRed-bRed) +
                (aGreen-bGreen)*(aGreen-bGreen) +
                (aBlue-bBlue)*(aBlue-bBlue));

        // 510.0 is the maximum distance between two colors
        // (0,0,0,0 -> 255,255,255,255)
        double percentAway = distance / 510.0d;

        return (percentAway > tolerance);
    }
}
