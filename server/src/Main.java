import java.io.IOException;
import java.io.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
public class Main {

    private static boolean setupRequest = false;
    private static final byte[] getDate = {'G','E','T','D','A','T','E'};
    private static byte[] receiveData;
    private static byte[] sendData;
    private static DatagramSocket serverSocket;
    private static DatagramPacket receivePacket;


    public static void main(String[] args) throws Exception{
        serverSocket = new DatagramSocket(9876);

        while(true)
        {
            receiveData = new byte[1024];
            sendData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            //String clientRequest = new String( receivePacket.getData());
            byte[] requestData = receivePacket.getData();
            System.out.println(new String(requestData));
            if(bytesCompare(requestData,getDate)){
                sendDate();
            }else{
                sendResponse("WRONG REQUEST");
            }

        }
    }

    private static boolean bytesCompare(byte[]data1,byte[]data2){
        boolean doesItMatch = false;
        for(int i = 0; !doesItMatch && (i<data1.length && i<data2.length);i++){
            doesItMatch = data2[i]==data1[i];
        }
        return doesItMatch;
    }

    private static void sendResponse(String s){
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();
        //String capitalizedSentence = r.toUpperCase();
        sendData = s.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        //serverSocket.send(sendPacket);
        try{
            serverSocket.send(sendPacket);
            System.out.println("DATAGRAM SENT");
        }catch (IOException e){
            System.out.println("ERROR!DATAGRAM HAS NOT BEEN SENT");
        }
    }

    private static String server_CurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private static void sendDate(){
        String date = server_CurrentDate();
        sendResponse(date);
    }
    
}
