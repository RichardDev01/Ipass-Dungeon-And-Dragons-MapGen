//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseMotionAdapter;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class GuiDnD {
//    private JButton btnRender;
//    private JPanel panel1;
//    private JSlider slrRatioBigRooms;
//    private JSlider slrMaxBigRooms;
//    private JSlider slrMinBigRooms;
//    private JPanel JpanelImage;
//    private JLabel lbimg;
//    private JSlider slrMaxRenderSize;
//    private JLabel lblMaxRenderSize;
//    private JLabel lblValueMinBigRooms;
//    private JLabel lblValueMaxBigRooms;
//    private JLabel lblValueRatioBetweenBigRooms;
//
//    public void startGUI(){
//        //~~~~ interface code
//        JFrame frame = new JFrame("GUIDnDMapGen");
//        frame.setContentPane(new Main().panel1);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//        frame.setSize(400 ,640);
//        frame.setResizable(false);
//        frame.setLocationRelativeTo(null);
//        ////~~~////
//    }
//    public void Main() {
//        BufferedImage img =null;
//        try {img = ImageIO.read(new File("logo.png"));} catch (IOException e) { e.printStackTrace(); }
//        ImageIcon icon = new ImageIcon(img);
//        lbimg.setIcon(icon);
//        lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
//        lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
//        lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
//        lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));
//
//        btnRender.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //runMachine(); Dit is wat ik wil
//            }
//        });
//
//
//        slrMaxRenderSize.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//                lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                super.mouseMoved(e);
//                lblMaxRenderSize.setText(String.valueOf(slrMaxRenderSize.getValue()));
//            }
//        });
//        slrMinBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//                lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
//                //writeMinBigValue(slrMinBigRooms.getValue());
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                super.mouseMoved(e);
//                lblValueMinBigRooms.setText(String.valueOf(slrMinBigRooms.getValue()));
//                //writeMinBigValue(slrMinBigRooms.getValue());
//            }
//        });
//        slrMaxBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//                lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
//                //writeMaxBigRoomsValue(slrMaxBigRooms.getValue());
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                super.mouseMoved(e);
//                lblValueMaxBigRooms.setText(String.valueOf(slrMaxBigRooms.getValue()));
//            }
//        });
//        slrRatioBigRooms.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//                lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                super.mouseMoved(e);
//                lblValueRatioBetweenBigRooms.setText(String.valueOf(slrRatioBigRooms.getValue()));
//            }
//        });
//    }
//}
