package macaroni.app;

import java.util.logging.Logger;
import java.util.logging.Level;

public final class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    boolean running = false;
    private final Window window = new Window();

    public void run() {
        logger.info("Application started.");
        running = true;
        window.open();
        long lastLoopTime = System.currentTimeMillis();

        while (running) {
            if (window.shouldClose()) {
                logger.info("Window should close. Stopping application.");
                running = false;
            }

            window.repaint();

            long currentLoopTime = System.currentTimeMillis();
            long fps = 60;
            long sleepTime = 1000 / fps - (currentLoopTime - lastLoopTime);
            if (sleepTime >= 0) {
                try {
                    logger.fine("Sleeping for %d ms.".formatted(sleepTime));
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Thread was interrupted. Stopping application.", e);
                    running = false;
                }
            }
            lastLoopTime = currentLoopTime;
        }

        window.close();
        logger.info("Application stopped.");
    }
}
