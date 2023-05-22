package fr.uge.patchwork.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;

import java.awt.*;
import java.nio.file.Path;

import fr.uge.patchwork.model.PlayerBoard;
import fr.uge.patchwork.model.TimeBoard;
import fr.umlv.zen5.*;
import javax.imageio.ImageIO;

public class View {

    public static void statusView(ApplicationContext context, TimeBoard timeBoard, PlayerBoard player1Board, PlayerBoard player2Board) {
        BufferedImage filledSquare = fileToImage("data/Board/filledSquare.png", 30, 30);
        BufferedImage outlineSquare = fileToImage("data/Board/outlineSquare.png", 30, 30);
        BufferedImage outlineSquareTB = fileToImage("data/Board/outlineSquare.png", 60, 60);
        BufferedImage button = fileToImage("data/Board/button.png", 30, 30);
        BufferedImage patch = fileToImage("data/Board/patch.png", 30, 30);

        BufferedImage player1 = fileToImage("data/Player/player1.png", 15, 15);
        BufferedImage player2 = fileToImage("data/Player/player2.png", 14, 14);
        BufferedImage tagPlayer1 = fileToImage("data/Player/tagPlayer1.png", 45, 45);
        BufferedImage tagPlayer2 = fileToImage("data/Player/tagPlayer2.png", 45, 45);

        context.renderFrame(graphics2D -> {
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, (int) context.getScreenInfo().getWidth(), (int) context.getScreenInfo().getHeight());

            /* Drawing the tag of players */
            graphics2D.drawImage(tagPlayer1, 0, 0, null);
            graphics2D.drawImage(tagPlayer2, (int) context.getScreenInfo().getWidth() - 65, 0, null);

            /* Drawing the board of player 1 and player 2 */
            int i;
            int j;
            for (i = 0; i < 9; i++) {
                for (j = 0; j < 9; j++) {
                    /* Player 1 board */
                    if(!player1Board.board().get(i).get(j)) {
                        graphics2D.drawImage(outlineSquare, 65 + j * 32, i * 32, null);
                    }
                    else {
                        graphics2D.drawImage(filledSquare, 65 + j * 32, i * 32, null);
                    }

                    /* Player 2 board */
                    if(!player2Board.board().get(i).get(j)) {
                        graphics2D.drawImage(outlineSquare, (int) (context.getScreenInfo().getWidth() / 1.3 + j * 32),
                                i * 32, null);
                    }
                    else {
                        graphics2D.drawImage(outlineSquare, (int) (context.getScreenInfo().getWidth() / 1.3 + j * 32),
                                i * 32, null);
                    }
                }
            }

            int xDrawSquare;
            int yDrawSquare;
            int cellInd;
            /* Drawing the time board */
            for (i = 0; i < 6; i++) {
                for (j = 0; j < 8; j++) {
                    xDrawSquare = (int) ((context.getScreenInfo().getWidth() / 2) + i * 80 - 220);
                    yDrawSquare = (int) ((context.getScreenInfo().getHeight() / 2) + j * 60 - 120);
                    cellInd = j * 6 + i;

                    graphics2D.drawImage(outlineSquareTB, xDrawSquare, yDrawSquare, null);

                    if (timeBoard.cells.get(cellInd).button()) {
                        graphics2D.drawImage(button, xDrawSquare + 35, yDrawSquare + 15, null);
                    }
                    if (timeBoard.cells.get(cellInd).patch()) {
                        graphics2D.drawImage(patch, xDrawSquare + 35, yDrawSquare + 15, null);
                    }
                    if (timeBoard.cells.get(cellInd).player1() != null) {
                        graphics2D.drawImage(player1, xDrawSquare + 15, yDrawSquare + 15, null);
                    }
                    if (timeBoard.cells.get(cellInd).player2() != null) {
                        graphics2D.drawImage(player2, xDrawSquare + 15, yDrawSquare + 33, null);
                    }
                }
            }
        });
    }

    public static void turnView(ApplicationContext context, int idPlayerPrior) {
        BufferedImage turnPlayer1 = fileToImage("data/Messages/turnPlayer1.png", 225, 15);
        BufferedImage turnPlayer2 = fileToImage("data/Messages/turnPlayer2.png", 225, 15);
        int width = (int) context.getScreenInfo().getWidth();
        int height = (int) context.getScreenInfo().getHeight();

        context.renderFrame(graphics2D -> {
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect((int)width*5/6, (int)height*19/20, 15, 15);

            if (idPlayerPrior == 1) {
                graphics2D.drawImage(turnPlayer1, (int)width*5/6, (int)height*19/20, null);
            }
            else {
                graphics2D.drawImage(turnPlayer2, (int)width*5/6, (int)height*19/20, null);
            }
        });
    }
    public static void clearView(ApplicationContext context) {
        context.renderFrame(graphics2D -> {
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, (int)context.getScreenInfo().getWidth(), (int)context.getScreenInfo().getHeight());
        });
    }

    public static BufferedImage fileToImage(String path, int w, int h) {
        BufferedImage img;
        try (var input = Files.newInputStream(Path.of(path))) {
            img = ImageIO.read(input);
            img = scale(img, w, h);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public static BufferedImage scale(BufferedImage src, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++)
            ys[y] = y * hh / h;
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = src.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

}
