package fr.uge.patchwork.controller;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Main controller of the game.
 */
public class Main {

    /**
     * Principal method of the game.
     * @param args command line arguments (not used in this context)
     * @throws ClassNotFoundException if the class is not found in controllers
     * @throws FileNotFoundException if the file is not found in controllers
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException, FontFormatException {
        Game.start();
    }

}