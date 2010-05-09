package ox.net.udpsend.simulator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import ox.net.udpsend.Packet;


public class Sink {

    public static final int SINK_PORT = 7070;
    public static void main(String[] args) throws SocketException, IOException {
        DatagramSocket sock = new DatagramSocket(SINK_PORT);
        byte[] data = new byte[100];
        DatagramPacket pack = new DatagramPacket(data, data.length);
        for (;;) {
            sock.receive(pack);
            byte[] recvdata = pack.getData();
            Packet pp = new Packet(recvdata);
            System.out.println("Sequence no: " + pp.getSequence()
                    +  " curr24 =" + pp.getValue24(0));

            if (pp.getSequence() == 50)
                break;
        }
    }
}
