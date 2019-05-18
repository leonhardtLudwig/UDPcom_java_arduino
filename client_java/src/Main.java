import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    private static byte[] sendData;
    private static byte[] receiveData;
    private static InetAddress IPAddress;
    private static DatagramSocket clientSocket;
    private static boolean hasBeenSetupped = false;
    private static Date date;
    private static int lux;
    private static Scanner t = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        System.out.println("Insert Server IP");
        String ip = t.nextLine();
        clientSocket = new DatagramSocket();
        IPAddress = InetAddress.getByName(ip);
        String buf = new String();
        System.out.println("Client running at "+IPAddress);
        while(!hasBeenSetupped) {

            sendData = new byte[2000];
            receiveData = new byte[2000];

            sendRequest("GETDATE");
            buf = getResponse();
            hasBeenSetupped = isDate();
            Thread.sleep(2000);
            //clientSocket.close();
        }
        System.out.println("Client Setup Done!");
        date = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").parse(buf);
        System.out.println(date.toString());
        Random rand = new Random();
        while(true){
            sendData = new byte[35];
            receiveData = new byte[35];
            System.out.println();
            lux = rand.nextInt(999);
            if (lux>500){
                receiveData = new byte[20000];
                fileRequest();
            }else{
                sendInformations();
            }
            Thread.sleep(2000);
        }
    }


    private static boolean isDate(){
        //dd/MM/yyyy_HH:mm:ss
        String rec = new String(receiveData);
        String format = "dd/MM/yyyy_HH:mm:ss";
            if (rec.indexOf('/') == format.indexOf('/')) {
                if (rec.indexOf('/', rec.indexOf('/')) == format.indexOf('/', format.indexOf('/'))) {
                    if (rec.indexOf('_') == format.indexOf('_')) {
                        if (rec.indexOf(':') == format.indexOf(':')) {
                            if (rec.indexOf(':',rec.indexOf(':'))==format.indexOf(':',format.indexOf(':'))) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
    }

    private static boolean bytesCompare(byte[]data1,byte[]data2){
        //System.out.println(new String(data1)+new String(data2));
        boolean doesItMatch = false;
        for(int i = 0; !doesItMatch && (i<data1.length && i<data2.length);i++){
            doesItMatch = data2[i]==data1[i];
        }
        return doesItMatch;
    }

    private static void sendRequest(String r){
        sendData = r.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        try{
            System.out.println("Sending: "+(new String(sendData)));
            clientSocket.send(sendPacket);
        }catch (IOException e){
            System.out.println("ERROR!DATAGRAM HAS NOT BEEN SENT");
        }
    }

    private static String getResponse(){
        String response = null;
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            response = new String(receivePacket.getData());
            receiveData = receivePacket.getData();
            System.out.println("FROM SERVER:" + response);
        }catch (IOException e){
            System.out.println("No datagram received");
        }
        return response;
    }

    private static void sendInformations(){
        System.out.println("Date: "+currentDate()+", Lux: "+lux);
        sendData = ("Date: "+currentDate()+", Lux: "+lux).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        try{
            System.out.println("Sending: "+(new String(sendData)));
            clientSocket.send(sendPacket);
        }catch (IOException e){
            System.out.println("ERROR!DATAGRAM HAS NOT BEEN SENT");
        }
    }

    private static String currentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private static boolean youCanSend(){
        return bytesCompare(receiveData,"1".getBytes());
    }

    private static void fileRequest(){
        sendRequest("GETFILE");
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            receiveData = receivePacket.getData();
            System.out.println(receiveData.length);
            append(new String(receiveData));
            System.out.println("Data received from server" );
        }catch (IOException e){
            System.out.println("No datagram received");
        }


    }

    private static void append(String s ) throws IOException {
        if(s.charAt(0)!='1') {
            System.out.println("Received data: "+s);
            File file = new File("data.txt");
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(s);
            pw.close();
        }
    }

}
