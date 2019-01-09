import java.net.*;
import java.util.Arrays;

public class Proxy {
  private DatagramSocket receiveSocket;
  private DatagramSocket sendReceiveSocket;
  private DatagramPacket packet;
  private int clientPort;
  private int serverPort;

  public Proxy() {
    try {
      receiveSocket = new DatagramSocket(2023);
      sendReceiveSocket = new DatagramSocket();
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  public static void main(String[] args) {
    Proxy proxy = new Proxy();
    while (true) {
      proxy.receivePacket();
      proxy.sendPacket();
      proxy.receivePacket();
      proxy.sendPacket();
    }
  }

  private void receivePacket() {
    DatagramSocket socket;
    if (packet == null || packet.getPort() == clientPort) {
      socket = receiveSocket;
    } else {
      socket = sendReceiveSocket;
    }

    packet = new DatagramPacket(new byte[100], 100);

    try {
      socket.receive(packet);
      packet.setData(Arrays.copyOf(packet.getData(), packet.getLength()));

      if (clientPort == 0) {
        clientPort = packet.getPort();
      }

      if (packet.getPort() == clientPort) {
        Util.printClientPacket(packet.getData());
      } else {
        Util.printServerPacket(packet.getData());
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  private void sendPacket() {
    if (packet.getPort() == clientPort) {
      packet.setPort(2069);
    } else {
      packet.setPort(clientPort);
    }
    try {
      sendReceiveSocket.send(packet);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
