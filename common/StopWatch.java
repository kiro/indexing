package indexing.common;

/**
 * Measures the running time, and displays a dor every second to
 * show progress.
 */
public class StopWatch {
  private final String name;
  private long start;
  
  private StopWatch(String name) {
    this.name = name;
    start = System.currentTimeMillis();
    System.err.println(name + " started");
  }
  
  public static StopWatch start(String name) {
    Thread thread = new Thread() {
      @Override
      public void run() {
        try {
          while (true) {
            Thread.sleep(1000);
            System.err.print(".");
          }
        } catch (InterruptedException ex) {
          return;
        }
      }
    };

    thread.setDaemon(true);
    thread.start();

    return new StopWatch(name);
  }
  
  public void stop() {
    System.err.println();
    System.err.println(name + " finished in " + (System.currentTimeMillis() - start) / 1000 + "s");
  }  
}
