package macaroni.app.menu_view;

import macaroni.app.AssetManager;
import macaroni.app.Controller;
import macaroni.app.GameColors;
import macaroni.app.logic.GameInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.awt.event.WindowEvent;             

public final class MainMenu extends JPanel implements MenuPanel.PanelListener {
    /**
     * Class used for storing menu images.
     */
    private static class MenuImage {
        private Image image;
        private Dimension size;
        private Point position;

        public void scaleToSize(Dimension size) {
            setImage(getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH));
            this.setSize(size);
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Dimension getSize() {
            return size;
        }

        public void setSize(Dimension size) {
            this.size = size;
        }

        public Point getPosition() {
            return position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }
    }

    private final List<File> mapFiles = new ArrayList<>();
    private final transient Controller controller;

    private MenuPanel plumberPanel;
    private MenuPanel mapPanel;
    private MenuStartButton startButton;
    private MenuPanel saboteurPanel;
    private final List<MenuImage> images = new ArrayList<>();

    private Point teamPanelOffset;

    /**
     * Constructs the main menu.
     *
     * @param controller the controller
     */
    public MainMenu(Controller controller, Dimension size) {
        this.controller = controller;

        setPreferredSize(size);
        setSize(size);
        reloadComponents();
    }

    /**
     * Sets up the layout and behaviour of the main menu.
     */
    private void reloadComponents() {
        JButton closeButton;
        if (getWidth() < 25 || getHeight() < 25) {
            return;
        }

        removeAll();

        setLayout(null);
        setBackground(GameColors.desertYellow);

        teamPanelOffset = new Point(getWidth() / 6, 3 * getHeight() / 5);

        Point mapPanelPosition = new Point(getWidth() / 2, getHeight() / 3);

        Dimension teamPanelSize = new Dimension(getWidth() / 5, getHeight() / 2);
        Dimension mapPanelSize = new Dimension(getWidth() / 5, getHeight() / 3);

        int listElementHeight = getHeight() / 25;

        plumberPanel = new TeamPanel("Plumbers", new Point(teamPanelOffset.x, teamPanelOffset.y), teamPanelSize, listElementHeight, this);
        add(plumberPanel);

        mapPanel = new MenuPanel("Maps", mapPanelPosition, mapPanelSize, listElementHeight, true, this);
        add(mapPanel);

        saboteurPanel = new TeamPanel("Saboteurs", new Point(getWidth() - teamPanelOffset.x, teamPanelOffset.y), teamPanelSize, listElementHeight, this);
        add(saboteurPanel);

        loadMaps();
        loadImages();

        startButton = new MenuStartButton(new Point(getWidth() / 2, (int) (getHeight() / 5.0 * 3.9)), new Dimension(mapPanelSize.width, getHeight() / 7), this::startClick);
        add(startButton);
        startButton.setEnabled(false);

        closeButton = new CloseButton(new Point(getWidth() - 90, 10), new Dimension(80, 40), e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            }
        }, "Exit");
        add(closeButton);
    }

    @Override
    public void onPanelChange() {
        String[] plumbers = plumberPanel.elementList.getElements().toArray(new String[0]);
        String[] saboteurs = saboteurPanel.elementList.getElements().toArray(new String[0]);
        String selectedMap = mapPanel.elementList.getSelectedElement();

        if (plumbers.length >= 2 && saboteurs.length >= 2 && selectedMap != null){
            startButton.setEnabled(true);
        }
    }

    /**
     * Starts the game if at least two players are in each team and a map is selected
     */
    private void startClick(ActionEvent e) {
        String[] plumbers = plumberPanel.elementList.getElements().toArray(new String[0]);
        String[] saboteurs = saboteurPanel.elementList.getElements().toArray(new String[0]);
        String selectedMap = mapPanel.elementList.getSelectedElement();

        if (plumbers.length < 2 || saboteurs.length < 2 || selectedMap == null) return;

        mapFiles.stream()
                .filter(file -> file.getName().startsWith(selectedMap))
                .findFirst()
                .ifPresent(file -> {
                    controller.setGameInfo(
                            new GameInfo(
                                    file,
                                    plumbers,
                                    saboteurs));
                    controller.setCurrentMenu(Controller.MenuTag.GAME);
                });
    }

    /**
     * Loads in the map files from the 'assets' folder.
     */
    private void loadMaps() {
        File mapsDir = new File("./assets/maps");
        if (mapsDir.isDirectory()) {
            for (File mapFile : Objects.requireNonNull(mapsDir.listFiles())) {
                if (mapFile.getName().endsWith(".map")
                        && mapFile.getName().chars().filter(ch -> ch == '.').count() == 1) {
                    mapFiles.add(mapFile);
                    mapPanel.addElement(mapFile.getName().substring(0, mapFile.getName().lastIndexOf('.')));
                }
            }
        }
    }

    /**
     * Loads in the images from the 'images' folder.
     */
    private void loadImages() {
        Dimension imageSize = new Dimension(getWidth() / 15, getWidth() / 15);

        MenuImage plumberImage = new MenuImage();
        plumberImage.setImage(AssetManager.getImage("MenuPlumber.png"));
        plumberImage.scaleToSize(imageSize);
        plumberImage.setPosition(new Point(teamPanelOffset.x - imageSize.width / 2, getHeight() / 3 - imageSize.height));
        images.add(plumberImage);

        MenuImage saboteurImage = new MenuImage();
        saboteurImage.setImage(AssetManager.getImage("MenuSaboteur.png"));
        saboteurImage.scaleToSize(imageSize);
        saboteurImage.setPosition(new Point(getWidth() - teamPanelOffset.x - imageSize.width / 2, getHeight() / 3 - imageSize.height));
        images.add(saboteurImage);

        MenuImage mapImage = new MenuImage();
        mapImage.setImage(AssetManager.getImage("MenuCistern.png"));
        mapImage.scaleToSize(imageSize);
        mapImage.setPosition(new Point(getWidth() / 2 - imageSize.width / 2, getHeight() / 3 * 2 - imageSize.height));
        images.add(mapImage);
    }

    /**
     * Draws the images on the screen.
     *
     * @param g the graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        for (MenuImage image : images) {
            if (image != null) {
                g2.drawImage(image.getImage(), image.getPosition().x, image.getPosition().y, null);
            }
        }
    }
}
