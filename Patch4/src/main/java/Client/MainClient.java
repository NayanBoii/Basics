package Client;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class MainClient {
    DatagramSocket datagramSocket;
    int port = 12345;
    boolean connectionEstablished;
    String side = "";
    InetAddress inetAddress;

    {
        try {
            inetAddress = InetAddress.getByName("10.200.233.107");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public MainClient(){
        UDPConnection();
        if (connectionEstablished){
            System.out.println("Connection has been established ...");
            new ObjectsReceiving(side, inetAddress, datagramSocket, port);
        }
    }

    private void UDPConnection() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        Thread senderThread = new Thread(() -> {
            while (!connectionEstablished) {
                try {
                    datagramSocket = new DatagramSocket(port);
                    String sendClient = "StartingUDP:  " + (int) dimension.getHeight() + " " + (int) dimension.getWidth();
                    byte[] sendData = sendClient.getBytes();

                    DatagramPacket acknowledgmentPacket = new DatagramPacket(sendData, sendData.length, inetAddress, port);
                    datagramSocket.send(acknowledgmentPacket);
                    Thread.sleep(1500);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread receiverThread = new Thread(() -> {
            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                datagramSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Acknowledgement message from the server : " + msg);
                side = msg;
                connectionEstablished = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        senderThread.start();
        receiverThread.start();

        try {
            senderThread.join();
            receiverThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
