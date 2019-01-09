public class Util {
  public static void printClientPacket(byte[] data) {
    printServerPacket(data);
    System.out.println("Packet string: " + new String(data));
  }

  public static void printServerPacket(byte[] data) {
    System.out.print("Packet bytes: ");
    for (byte c : data) {
      System.out.printf("%d ", c);
    }
    System.out.println();
  }
}
