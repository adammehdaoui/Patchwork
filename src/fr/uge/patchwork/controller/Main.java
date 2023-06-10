package fr.uge.patchwork.controller;

import java.awt.*;
import java.io.IOException;
import static java.lang.System.exit;

/**
 * Main class of the game.
 */
public class Main {

    /**
     * Principal method of the game.
     * @param args command line arguments (not used in this context)
     * @throws ClassNotFoundException if the class is not found in controllers
     * @throws IOException if there is an error while reading the file
     * @throws FontFormatException if there is an error while loading the font
     * @throws InterruptedException if there is an error while loading the font
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException, FontFormatException,
            InterruptedException {
        Game.start();

        exit(0);
    }

}