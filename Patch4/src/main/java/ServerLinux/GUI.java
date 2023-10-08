package ServerLinux;

import javax.swing.*;
import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

public class GUI {
    private String side;
    private JFrame jFrame;
    private DatagramSocket datagramSocket;
    private int portUDP;
    private String clientScreenSize;
    private ArrayList<Clients> clientsArrayList;
    public static int loopNum = 0;
    EventListener eventListener;
    public int val = 0;
    public GUI(String side, JFrame jFrame, DatagramSocket datagramSocket, int portUDP, String clientScreenSize, ArrayList<Clients> clientsArrayList) {
        this.side = side;
        this.datagramSocket = datagramSocket;
        this.portUDP = portUDP;
        this.clientScreenSize = clientScreenSize;
        this.jFrame = jFrame;
        this.clientsArrayList = clientsArrayList;

        GUIFunctionality();
    }

    public GUI(){
        // Just to access and modify the loopNum
    }

    public void setLoopNum(int loopNum) {
        GUI.loopNum = loopNum;
    }

    private void GUIFunctionality() {
        System.out.println("Screen Sharing started !!");
        System.out.println(clientsArrayList.get(0).getSide() + " " + clientScreenSize);

        while (true) {
//            System.out.println(loopNum);
            Point cursor = MouseInfo.getPointerInfo().getLocation();

            // Basically if the clientArrayList has One size then
            // if it is located on the left side set the if condition to the following

            if (loopNum == 0 && cursor.getX() < 2 && (val == 0)&& Objects.equals(clientsArrayList.get(0).getSide(), "Left")){
                jFrame.setVisible(true);
                System.out.println("Leaving Screen");
                System.out.println("Calling Keyboard Functionality");
                SwingUtilities.invokeLater(() -> {
                    eventListener = new EventListener(jFrame, datagramSocket, clientsArrayList.get(0).getInetAddress(), portUDP);
                });
                new CoordinatesSending(side, datagramSocket, clientsArrayList.get(0).getInetAddress(), portUDP, clientScreenSize);
                val++;

            }else if (loopNum == 0 && cursor.getX() > 2  && (val == 1) && Objects.equals(clientsArrayList.get(0).getSide(), "Left")) {
                System.out.println("Entering Screen");
                jFrame.dispose();
                eventListener.removeEventListeners();
                val--;
            }


//            if (!clientsArrayList.isEmpty()) {
//                System.out.println(clientsArrayList.size() + " " + clientsArrayList.get(0).getInetAddress());
//            }
//                try {
//                    Thread.sleep(1500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
        }
    }
}
