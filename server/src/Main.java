import java.io.*;
import java.net.*;
public class Main {

    private static boolean setupRequest = false;

    public static void main(String[] args) throws Exception{
        DatagramSocket serverSocket = new DatagramSocket(9876);

        while(true)
        {
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());

            setupRequest = sentence.equals("GETDATE");
            System.out.println(sentence.equals("GETDATE"));
            //if(setupRequest) {
                System.out.println("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            //}
        }
    }
}
