package ServerLinux;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Main {
    public static ArrayList<Clients> clients = new ArrayList<>();
    int numberOfClients = 2;
    static JFrame jFrame = new JFrame();
    DatagramSocket datagramSocket;
    String clientScreenSize;
    int portUDP = 12345;
    String[] arr;
    String msgFromClient = "";
    String side = "";

    public Main(){

        System.out.println("-------Program Started-------");
        // Creating a JFrame
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        jFrame.setSize(screenWidth, screenHeight);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setUndecorated(true);
        jFrame.setAlwaysOnTop(true);
        jFrame.setBackground(new Color(0,0,0,3));
        jFrame.setType(Window.Type.UTILITY);
        Image blankImage = toolkit.createImage(new byte[0]);
        Cursor blankCursor = toolkit.createCustomCursor(blankImage, new Point(0, 0), "blankCursor");
        jFrame.setCursor(blankCursor);
        UDPConnectionValidation();
        GUIAndMouse();
        System.out.println("ENDED");
    }

    private void GUIAndMouse() {
        new GUI( side, jFrame, datagramSocket, portUDP, clientScreenSize, clients);

    }

    private void UDPConnectionValidation() {
        // Making the UDP Connection
        try {
            datagramSocket = new DatagramSocket(portUDP);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        Thread acceptingClientConnection = new Thread(() -> {
            try {
                System.out.println("Starting to accept connections");
                while (numberOfClients > 0){

                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    datagramSocket.receive(receivePacket);
                    String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println(msg);
                    arr = msg.split("  ");
                    clientScreenSize = arr[1];

                    if (numberOfClients == 2) {
                        side = "Left";
                    } else if ( numberOfClients == 1) {
                        side = "Right";
                    }

                    byte[] sendData = side.getBytes();
                    DatagramPacket acknowledgmentPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), portUDP);
                    datagramSocket.send(acknowledgmentPacket);

                    Clients client = new Clients(receivePacket.getAddress(), side, true);
                    addProduct(client);
                    numberOfClients--;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        acceptingClientConnection.start();
        while (clients.isEmpty()) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Moved On From Thread for accepting connection");

    }
    private void addProduct(Clients client){
        clients.add(client);
    }
}