import java.io.IOException;
import java.io.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
public class Main {

    private static boolean setupRequest = false;
    private static final byte[] getDate = {'G','E','T','D','A','T','E'};
    private static final byte[] date = {'D','A','T','E'};
    private static byte[] receiveData;
    private static byte[] sendData;
    private static DatagramSocket serverSocket;
    private static DatagramPacket receivePacket;


    public static void main(String[] args) throws Exception{
        serverSocket = new DatagramSocket(9876);
        String res = null;
        while(true)
        {
            System.out.println();
            receiveData = new byte[1024];
            sendData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            //String clientRequest = new String( receivePacket.getData());
            byte[] requestData = receivePacket.getData();
            System.out.println("Request data "+(new String(requestData)));
            if(bytesCompare(requestData,getDate)){
                sendDate();
            }else{
                sendResponse("WRONG REQUEST");
            }
            if (bytesCompare(requestData,date)){
                System.out.println("alright");
                append(new String(requestData));
            }else{
                sendResponse("WRONG REQUEST");
            }

        }
    }

    private static void append(String s ) throws IOException {
        String str = s;
        BufferedWriter writer = new BufferedWriter(new FileWriter("data.csv", true));
        writer.append(' ');
        writer.append(str);

        writer.close();
    }

    private static String waitInfo(){
        System.out.println("Waiting info from client...");
        String response = null;
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            response = new String(receivePacket.getData());
            receiveData = receivePacket.getData();
            System.out.println("Client Data" + response);
        }catch (IOException e){
            System.out.println("No datagram received");
        }
        return response;
    }

    private static boolean bytesCompare(byte[]data1,byte[]data2){
        System.out.println("comparing: "+(new String (data1))+" "+(new String (data2)));
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
            System.out.println("DATAGRAM SENT: "+ s);
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
