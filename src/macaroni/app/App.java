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

        long fps = 60;
        long lastLoopTime = System.currentTimeMillis();
        final long OPTIMAL_TIME = 1000 / fps;

        while (running) {
            long currentLoopTime = System.currentTimeMillis();
            long elapsedTime = currentLoopTime - lastLoopTime;

            if (window.shouldClose()) {
                logger.info("Window should close. Stopping application.");
                running = false;
            }
          
            if (elapsedTime >= OPTIMAL_TIME) {
                window.repaint();
                lastLoopTime = currentLoopTime;
            }

            long sleepTime = lastLoopTime - System.currentTimeMillis() + OPTIMAL_TIME;

            if (sleepTime >= 0) {
                try {
                    logger.fine("Sleeping for %d ms.".formatted(sleepTime));
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Thread was interrupted. Stopping application.", e);
                    running = false;
                }
            }
        }

        window.close();
        logger.info("Application stopped.");
    }
}
