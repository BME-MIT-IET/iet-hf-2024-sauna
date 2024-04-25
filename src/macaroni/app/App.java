package macaroni.app;

/**
 * Represents the main application
 */
public final class App {
    /**
     * number of frames per second
     */
    private final long FPS = 60;
    /**
     * shows whether the app is currently running
     */
    boolean running = false;
    /**
     * the window of the application
     */
    private final Window window = new Window();

    /**
     * Runs the application
     */
    public void run() {
        running = true;
        window.open();

        long lastLoopTime = System.currentTimeMillis();
        while (running) {
            if (window.shouldClose()) {
                running = false;
            }
            window.repaint();

            long currentLoopTime = System.currentTimeMillis();
            long sleepTime = 1000 / FPS - (currentLoopTime - lastLoopTime);
            if (sleepTime >= 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
            lastLoopTime = currentLoopTime;
        }

        window.close();
    }
}
