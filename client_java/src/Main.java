import java.io.*;
import java.net.*;

public class Main {

    private static byte[] sendData;
    private static byte[] receiveData;
    private static InetAddress IPAddress;
    private static DatagramSocket clientSocket;
    private static boolean hasBeenSetupped = false;

    public static void main(String[] args) throws Exception{
        clientSocket = new DatagramSocket();
        IPAddress = InetAddress.getByName("localhost");
        while(!hasBeenSetupped) {

            sendData = new byte[1024];
            receiveData = new byte[1024];

            sendRequest("GETDATE");
            getResponse();
            hasBeenSetupped = isDate();
            Thread.sleep(2000);
            //clientSocket.close();
        }
        System.out.println("Client Setup Done!");
        while(true){

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

}
