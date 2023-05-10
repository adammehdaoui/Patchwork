package fr.uge.patchwork.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.Thread;

import java.awt.*;
import fr.umlv.zen5.*;

/**
 * Main controller of the game.
 */
public class Main {

    /**
     * Principal method of the game.
     * @param args : command line arguments (not used in this context)
     * @throws ClassNotFoundException :  if the class is not found in controllers
     * @throws FileNotFoundException : if the file is not found in controllers
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
//        Application.run(Color.WHITE, context -> {
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            context.exit(0);
//        });

        Game.start();
    }

}