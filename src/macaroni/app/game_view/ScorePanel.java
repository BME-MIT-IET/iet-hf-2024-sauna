package macaroni.app.game_view;

import macaroni.app.AssetManager;
import macaroni.app.GameColors;
import macaroni.app.logic.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for showing the scores
 */
public final class ScorePanel extends JPanel {

    /**
     * the scores
     */
    private final JLabel scoreLabel;
    /**
     * the rounds
     */
    private final JLabel roundLabel;

    /**
     * Creates a new ScoreLabel.
     *
     * @param size the size of the label
     */
    public ScorePanel(Point positionOfCenter, Dimension size) {
        setPreferredSize(size);
        setSize(size);
        setLocation(positionOfCenter.x - getWidth() / 2, positionOfCenter.y - getHeight() / 2);

        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);

        JLabel teamLabel = new JLabel("Plumbers   Saboteurs");
        teamLabel.setFont(AssetManager.getFont("ChakraPetch-Light.ttf").deriveFont(Font.BOLD, getHeight() / 6.f));
        teamLabel.setForeground(GameColors.oceanBlue);
        add(teamLabel);

        scoreLabel = new JLabel("0 : 0");
        scoreLabel.setFont(AssetManager.getFont("ChakraPetch-Bold.ttf").deriveFont(Font.BOLD, getHeight() / 2.5f));
        scoreLabel.setForeground(GameColors.oceanBlue);
        add(scoreLabel);

        roundLabel = new JLabel("0 / " + Game.MAXIMUM_NUMBER_OF_ROUNDS);
        roundLabel.setFont(AssetManager.getFont("ChakraPetch-Bold.ttf").deriveFont(Font.ITALIC, getHeight() / 10.f));
        roundLabel.setForeground(GameColors.oceanBlue);
        add(roundLabel);
    }

    /**
     * Updates the score displayed
     *
     * @param plumberScore  the score of the plumbers
     * @param saboteurScore the score of the saboteurs
     */
    public void updateScore(int plumberScore, int saboteurScore) {
        scoreLabel.setText(plumberScore + " : " + saboteurScore);
    }

    /**
     * Updates the rounds displayed
     *
     * @param currentRound the number of the current round
     */
    public void updateRound(int currentRound) {
        roundLabel.setText(currentRound + " / " + Game.MAXIMUM_NUMBER_OF_ROUNDS);
    }
}
