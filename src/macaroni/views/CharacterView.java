package macaroni.views;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;

import java.awt.*;

/**
 * A karakterek megjelenítéséért felelős absztrakt osztály.
 */
public abstract class CharacterView extends View {

    /**
     * A karakter neve.
     */
    protected final String name;

    /**
     * Létrehoz egy CharacterView példányt.
     *
     * @param position a karakter helye.
     * @param name a karakter neve.
     */
    protected CharacterView(Position position, String name) {
        super(position);
        this.name = name;
    }

    /**
     * Beállítja a nézet pozícióját a paraméterként kapott pozícióra.
     *
     * @param pos az új pozíció
     */
    public void setPosition(Position pos) {
        position = pos;
    }

    /**
     * kirajzolja a karakter nevét a megadott graphics objektum x,y koordinátáira.
     */
    protected void drawName(Graphics g, int x, int y) {
        g.setColor(GameColors.snowWhite);
        g.setFont(AssetManager.getFont("ChakraPetch-SemiBold.ttf").deriveFont(15.f));
        g.drawString(name, x, y);
    }
}
