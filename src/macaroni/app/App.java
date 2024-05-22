package macaroni.app;

/**
 * Represents the main application
 */
public final class App {
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

        long fps = 60;
        long lastLoopTime = System.currentTimeMillis();
        final long OPTIMAL_TIME = 1000 / fps;

        while (running) {
            long currentLoopTime = System.currentTimeMillis();
            long elapsedTime = currentLoopTime - lastLoopTime;

            if (window.shouldClose()) {
                running = false;
            }
            if (elapsedTime >= OPTIMAL_TIME) {
                window.repaint();
                lastLoopTime = currentLoopTime;
            }

            long sleepTime = lastLoopTime - System.currentTimeMillis() + OPTIMAL_TIME;
            if (sleepTime >= 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }

        window.close();
    }
}
