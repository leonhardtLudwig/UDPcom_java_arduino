import java.io.IOException;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;
public class Main {

    private static boolean setupRequest = false;
    private static final byte[] getDate = {'G','E','T','D','A','T','E'};
    private static final byte[] date = {'D','A','T','E'};
    private static final byte[] getFile = {'G','E','T','F','I','L','E'};
    private static byte[] receiveData;
    private static byte[] sendData;
    private static DatagramSocket serverSocket;
    private static DatagramPacket receivePacket;
    private static InetAddress IPAddress;
    private static OutputStream ouStream = null;
    private static Scanner t = new Scanner(System.in);


    public static void main(String[] args) throws Exception{
        IPAddress = InetAddress.getByName("192.168.8.25");
        serverSocket = new DatagramSocket(9876,IPAddress);
        String res = null;
        System.out.println("Server running at "+serverSocket.getLocalAddress()+":"+serverSocket.getLocalPort());
        while(true)
        {
            System.out.println();
            receiveData = new byte[35];
            sendData = new byte[35];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            byte[] requestData = receivePacket.getData();
            System.out.println("Arrived data contains: "+(new String(requestData)));
            if(requestData!=null&&bytesCompare(requestData,getDate)){
                System.out.println("Date request");
                sendDate();
            }
            if (requestData!=null&&bytesCompare(requestData,date)&&!bytesCompare(requestData,getFile)&&!bytesCompare(requestData,getDate)){
                System.out.println("Client information from: "+receivePacket.getAddress());
                append(new String(requestData));
            }
            if(requestData!=null&&bytesCompare(requestData,getFile)){
                System.out.println("File request");
                sendFile();
            }

        }
    }

    private static void append(String s ) throws IOException {
        File file = new File("data.txt");
        if(!s.equals("GETFILE\0")&&!s.equals("GETDATE\0")){
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(s);
        pw.close();
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
        sendData = s.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
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

    private static void sendFile(){

        try{
            byte[]send = Files.readAllBytes(Paths.get("data.txt"));
            System.out.println("File size "+send.length+" bytes");
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            sendData = new byte[send.length];
            sendData = send;
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
            System.out.println("File successfully sent to "+ IPAddress);
        }catch (IOException e){
            System.out.println("ERROR!DATAGRAM HAS NOT BEEN SENT");
        }
    }



}
