import java.io.*;
import java.net.*;

public class Main {
    public static String hostname_="192.168.8.24";
    public static int port_=51900;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String hostname = hostname_;
        int port = port_;

        try {
            // byte[] ipAddr = new byte[] { 192, 168, 1, 87 };
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();

            while (true) {
                byte[] get = {'G','E','T'};

                DatagramPacket request = new DatagramPacket(get,get.length, address, port);
                socket.send(request);
                System.out.println(request.toString());
/*
                byte[] buffer = new byte[512];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);
                System.out.println(buffer.toString());

                String quote = new String(buffer, 0, response.getLength());
                System.out.println(quote);

                System.out.println(quote);
                System.out.println();
*/
                Thread.sleep(1000);
            }

        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
