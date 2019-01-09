import java.net.*;

public class Client {
  private DatagramSocket socket;
  private DatagramPacket packet;
  private InetAddress localHost;

  public static void main(String[] args) {
    Client client = new Client();
    String[] modes =
        new String[] {
          "netascii",
          "Netascii",
          "netasciI",
          "nEtAsCiI",
          "NETASCII",
          "octet",
          "Octet",
          "octeT",
          "OcTeT",
          "OCTET"
        };
    for (int i = 0; i < 10; i++) {
      client.newRequest(i % 2, "file" + i + ".txt", modes[i]);
      client.send();
      client.receive();
    }

    client.sendInvalidPacket();
  }

  public Client() {
    try {
      socket = new DatagramSocket();
      localHost = InetAddress.getLocalHost();
    } catch (Exception e) {
      System.out.println(e.toString());
      System.exit(1);
    }
  }

  private void newRequest(int type, String filename, String mode) {
    byte[] request = new byte[4 + filename.length() + mode.length()];
    request[1] = (byte) (type + 1);

    for (int i = 0; i < filename.length(); i++) {
      request[i + 2] = filename.getBytes()[i];
    }

    for (int i = 0; i < mode.length(); i++) {
      request[i + 3 + filename.length()] = mode.toLowerCase().getBytes()[i];
    }

    packet = new DatagramPacket(request, request.length, localHost, 2023);
  }

  private void send() {
    Util.printClientPacket(packet.getData());

    try {
      socket.send(packet);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  private void sendInvalidPacket() {
    packet = new DatagramPacket(new byte[12], 12, localHost, 2023);

    send();
  }

  private void receive() {
    packet = new DatagramPacket(new byte[4], 4);
    try {
      socket.receive(packet);
      Util.printServerPacket(packet.getData());
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
