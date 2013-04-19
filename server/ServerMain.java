package indexing.server;

/**
 * ServerMain
 */
public class ServerMain {
  public static void main(String [] args) throws Exception {
    if (args.length != 3) {
      System.out.print("Usage : ServerMain <index file> <content dir> <port>");
    }
    new Server().run(args[0], args[1], Integer.parseInt(args[2]));
  }
}
