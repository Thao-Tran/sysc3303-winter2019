import java.net.*;
import java.util.Arrays;
import java.util.zip.*;

public class Server {
  private DatagramSocket receiveSocket;
  private DatagramPacket packet;

  public Server() {
    try {
      receiveSocket = new DatagramSocket(2069, InetAddress.getLocalHost());
    } catch (Exception e) {
      System.out.println(e.toString());
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    Server server = new Server();

    while (true) {
      server.receivePacket();
      try {
        server.parsePacket();
        server.sendPacket();
      } catch (DataFormatException e) {
        System.out.println(e.toString());
        break;
      }
    }
  }

  private void receivePacket() {
    packet = new DatagramPacket(new byte[100], 100);
    try {
      receiveSocket.receive(packet);
      packet.setData(Arrays.copyOf(packet.getData(), packet.getLength()));
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  private void parsePacket() throws DataFormatException {
    if (packet == null) {
      throw new DataFormatException("Null packet received");
    }

    Util.printClientPacket(packet.getData());

    if (packet.getLength() < 4) {
      throw new DataFormatException("Packet length too small");
    }

    byte[] data = packet.getData();

    if (data[0] != 0 || (data[1] != 1 && data[1] != 2)) {
      throw new DataFormatException("Packet incorrectly formatted");
    }

    int numOfZeroBytes = 0;

    for (byte c : data) {
      if (c == 0) {
        numOfZeroBytes++;
      }
      if (numOfZeroBytes > 3) {
        throw new DataFormatException("Number of zero bytes exceeds 3");
      }
    }

    if (data[data.length - 1] != 0) {
      throw new DataFormatException("Last byte is not a 0");
    }
  }

  private void sendPacket() {
    byte[] sendPacket = new byte[4];
    if (packet.getData()[1] == 1) {
      sendPacket[1] = 3;
      sendPacket[3] = 1;
    } else {
      sendPacket[1] = 4;
    }

    Util.printServerPacket(sendPacket);

    try {
      DatagramSocket socket = new DatagramSocket();
      socket.send(
          new DatagramPacket(sendPacket, sendPacket.length, packet.getAddress(), packet.getPort()));
      socket.close();
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
