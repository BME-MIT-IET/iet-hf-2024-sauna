package macaroni.app.logic;

import java.io.File;

/**
 * Holds key information for setting up a game
 *
 * @param mapFile the file of the map
 * @param plumberNicknames the nicknames of the plumbers
 * @param saboteurNicknames the nicknames of the saboteurs
 */
public record GameInfo(File mapFile, String[] plumberNicknames, String[] saboteurNicknames) {}
