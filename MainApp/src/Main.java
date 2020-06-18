import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    private JButton btnRender;
    private JPanel panel1;
    private JSlider slrRatioBigRooms;
    private JSlider slrMaxBigRooms;
    private JSlider slrMinBigRooms;
    private JPanel JpanelImage;
    private JLabel lbimg;
    private JSlider slrMaxRenderSize;
    private JLabel lblMaxRenderSize;
    private JLabel lblValueMinBigRooms;
    private JLabel lblValueMaxBigRooms;
    private JLabel lblValueRatioBetweenBigRooms;
    private JCheckBox checkBoxRunning;
    private JTextField filePathTextbox;
    private JCheckBox debugCheckBox;

    public static void main(String[] args) {
        //~~~~ interface code
        JFrame frame = new JFrame("GUIDnDMapGen");
        frame.setContentPane(new Main().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400 ,720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        ////~~~////

        //Crop function needs to be done sometime
        BufferedImage cropImg =null;
        try {cropImg = ImageIO.read(new File("result.png"));} catch (IOException e) { e.printStackTrace(); }

    }

    public Main() {
        //Setting logo for Gui
        BufferedImage img =null;
        try {img = ImageIO.read(new File("logo.png"));} catch (IOException e) { e.printStackTrace(); }
        ImageIcon icon = new ImageIcon(img);
        lbimg.setIcon(icon);

        //initialising display value's gui
        lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
        lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
        lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
        lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));

        //initialising generator class
        Generator gen1 = new Generator();

        //Toggle for render function, sets of an event
        btnRender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBoxRunning.setSelected(true);
            }
        });

        //events for updating gui values of sliders
        slrMaxRenderSize.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
            }
        });

        //events for updating gui values of sliders
        slrMinBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
            }
        });

        //events for updating gui values of sliders
        slrMaxBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
            }
        });

        //events for updating gui values of sliders
        slrRatioBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));
            }
        });

        //Toggle event for render, Start rendering 1 time per event
        checkBoxRunning.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
                    int aantalBigroom = slrMaxBigRooms.getValue();
                    int aantalHalways = slrRatioBigRooms.getValue();
                    int aantalMinBigRooms = slrMinBigRooms.getValue();
                    int maxGroteTilesAantal = slrMaxRenderSize.getValue();
                    boolean debugPixels = debugCheckBox.isSelected();
                    String FilePath = filePathTextbox.getText();

                    if (aantalBigroom < aantalMinBigRooms){
                        aantalBigroom = aantalMinBigRooms;
                    }
                    //initialising generate function
                    gen1.generate(aantalBigroom,aantalHalways,aantalMinBigRooms,maxGroteTilesAantal,FilePath,debugPixels);
                }
                checkBoxRunning.setSelected(false);
            }
        });
    }
}
