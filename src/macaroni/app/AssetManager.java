package macaroni.app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Loads and stores static assets, namely images and fonts.
 */
public class AssetManager {

    private AssetManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map of font names to fonts.
     */
    private static final Map<String, Font> fonts = new HashMap<>();
    /**
     * Map of image names to images.
     */
    private static final Map<String, Image> images = new HashMap<>();

    static {
        try (Stream<Path> fontStream = Files.walk(Path.of("./assets/fonts"));
             Stream<Path> imgStream = Files.walk(Path.of("./assets/images"))) {
            fontStream.filter(path -> Files.isRegularFile(path) && !path.endsWith("OFL.txt"))
                    .forEach(path ->
                    {
                        try {
                            File file = path.toFile();
                            fonts.put(file.getName(),
                                    Font.createFont(Font.TRUETYPE_FONT, file));
                        } catch (IOException | FontFormatException e) {
                            e.printStackTrace();
                        }
                    }
            );
            imgStream.filter(Files::isRegularFile).forEach(path ->
                    {
                        try {
                            File file = path.toFile();
                            images.put(file.getName(), ImageIO.read(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a loaded font.
     * @param name the name of the font to be loaded. Same as its filename
     * @return the font, null if not found
     */
    public static Font getFont(String name) {
        Font font = fonts.get(name);
        // Return a fallback font if the custom font cannot be loaded
        if (font == null) return new Font("Arial", Font.PLAIN, 24);
        else return font;
    }

    /**
     * Gets a loaded image.
     * @param name the name of the image to be loaded. Same as its filename
     * @return the image, null if not found
     */
    public static Image getImage(String name) {
        return images.get(name);
    }
}
