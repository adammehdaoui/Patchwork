package fr.uge.patchwork.controller;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import java.lang.Thread;

import java.awt.*;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import fr.uge.patchwork.view.*;
import fr.umlv.zen5.*;
import javax.imageio.ImageIO;

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
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        /*Application.run(Color.WHITE, context -> {

            BufferedImage player1;
            BufferedImage player2;

            try (var input = Files.newInputStream(Path.of("data/Player/player1.png"))) {
                player1 = ImageIO.read(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (var input = Files.newInputStream(Path.of("data/Player/player2.png"))) {
                player2 = ImageIO.read(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            context.renderFrame(graphics2D -> {
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(0, 0, (int) context.getScreenInfo().getWidth(), (int) context.getScreenInfo().getHeight());
                graphics2D.drawImage(player1, 0, 0, null);
                graphics2D.drawImage(player2, 60, 0, null);
            });

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            context.exit(0);
        });*/

        Game.start();
    }

}