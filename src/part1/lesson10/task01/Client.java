package part1.lesson10.task01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.locks.LockSupport;


/**
 * Client.
 *
 * @author Aydar_Safiullin
 */
public class Client {
    private DatagramSocket socket;
    private InetAddress address;
    private String name;
    public static final String EXIT_MSG = "exit";
    protected boolean isOnline = true;

    public Client() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            startTalking();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LockSupport.parkNanos(1_500_000_000L);
            socket.close();
        }
    }

    /**
     * Start talking process.
     * @throws IOException
     */
    protected void startTalking() throws IOException {
        System.out.println("Введите своё имя");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        name = reader.readLine();
        sendMessage(name);

        startListening();
        sendMessage("подключился к чату");
        String msg;
        while (!EXIT_MSG.equals(msg = reader.readLine())) {
            sendMessage(msg);
        }
        sendMessage("покинул чат");
        sendMessage(EXIT_MSG);
        reader.close();
        isOnline = false;
    }

    /**
     * Receive messages.
     */
    protected void startListening() {
        Runnable listener = () -> {
            while (true) {
                try {
                    // Если мы вышли из чата, то прекращаем принимать сообщения.
                    if (!isOnline)
                        break;
                    byte[] listenBuffer = new byte[256];
                    DatagramPacket packet = new DatagramPacket(listenBuffer, listenBuffer.length);
                    socket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    System.out.println(received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(listener);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Method for messages sending.
     * @param msg - send message.
     * @throws IOException
     */
    protected void sendMessage(String msg) throws IOException {
        byte[] buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Server.PORT);
        socket.send(packet);
    }
}