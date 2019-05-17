import java.io.IOException;
import java.io.*;
import java.net.*;
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
        IPAddress = InetAddress.getByName("192.168.1.105");
        //serverSocket = new DatagramSocket(9876);
        //192.168.1.105
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
            //String clientRequest = new String( receivePacket.getData());
            byte[] requestData = receivePacket.getData();
            System.out.println("Arrived data contains: "+(new String(requestData)));
            if(bytesCompare(requestData,getDate)){
                System.out.println("Date request");
                sendDate();
                requestData = null;
            }
            if (requestData!=null&&bytesCompare(requestData,date)){
                System.out.println("Client information");
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
        FileWriter fw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(s);
        pw.close();
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

    private static void sendFile(){
        try{
        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data.txt"))));
        String line = "";
        try{
        while((line = fr.readLine()) != null) {
            System.out.println(line);
        }
        }catch (IOException e){
            System.out.println("Corrupted file");
        }}catch(FileNotFoundException e){
            System.out.println("File doesn't exist");
        }
    }
}
