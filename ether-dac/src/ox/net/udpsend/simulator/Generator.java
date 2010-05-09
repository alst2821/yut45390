package ox.net.udpsend.simulator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ox.net.udpsend.Packet;

/**
 * generates UDP packets and sends them out
 *
 * @author stcrm
 *
 */
public class Generator {

    /**
     * @param args
     */
    public static void main(String[] args) throws SocketException,
        UnknownHostException, IOException, InterruptedException {
        DatagramSocket sock = new DatagramSocket();
        Packet p = new Packet();
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        byte[] data = p.getBytes();
        DatagramPacket pack = new DatagramPacket(data, data.length);
        pack.setAddress(addr);
        pack.setPort(Sink.SINK_PORT);
        for (int i = 0; i < 700; i++){
            p.next();
            p.v_pattern(0,300);
            pack.setData(p.getBytes());
            pack.setLength(p.getLength());
            sock.send(pack);
            Thread.sleep(50);
            if (i % 10 == 0) {
                System.out.print(".");
            }
        }
    }
};

