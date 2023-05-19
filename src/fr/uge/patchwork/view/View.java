package fr.uge.patchwork.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;

import java.awt.*;
import java.nio.file.Path;
import java.util.Arrays;

import fr.uge.patchwork.model.Cell;
import fr.uge.patchwork.model.TimeBoard;
import fr.umlv.zen5.*;
import javax.imageio.ImageIO;

public class View {
    public static void initView(TimeBoard timeBoard){
        Application.run(Color.BLACK, context -> {

            BufferedImage filledSquare = fileToImage("data/Default (64px)/d6.png", 30, 30);
            BufferedImage outlineSquare = fileToImage("data/Default (64px)/d6_outline.png", 30, 30);

            BufferedImage player1 = fileToImage("data/Player/player1.png", 45, 45);
            BufferedImage player2 = fileToImage("data/Player/player2.png", 45, 45);

            BufferedImage button = fileToImage("data/Default (64px)/d2.png", 40, 40);
            BufferedImage patch = fileToImage("data/Default (64px)/card_add.png", 40, 40);

            Cell[][] tb = timeBoard.formSpiralMatrix();

            context.renderFrame(graphics2D -> {
                graphics2D.setColor(Color.BLACK);
                graphics2D.fillRect(0, 0, (int) context.getScreenInfo().getWidth(), (int) context.getScreenInfo().getHeight());

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++){
                        graphics2D.drawImage(outlineSquare, j * 32 + 20, i * 32 + 20, null);
                        graphics2D.drawImage(outlineSquare, ((int) context.getScreenInfo().getWidth() - (j * 32 + 60)), i * 32 + 20, null);
                    }
                }

                // double for du dessin de la spirale: ne marche pas car formSpiralMatrix() creer 64 case et non 54
//                for (int i = 0; i < 8; i++) {
//                    for (int j = 0; j < 8; j++){
//                        graphics2D.drawImage(outlineSquare, (((int) context.getScreenInfo().getWidth()/3) + (j * 32 + 20)), i * 32 + 20, null);
//
//                        if (tb[i][j].patch()){
//                            graphics2D.drawImage(patch, (((int) context.getScreenInfo().getWidth()/3) + (j * 32 + 20)), i * 32 + 20, null);
//                        }
//                        if (tb[i][j].button()){
//                            graphics2D.drawImage(button, (((int) context.getScreenInfo().getWidth()/3) + (j * 32 + 20)), i * 32 + 20, null);
//                        }
//                        if (tb[i][j].player1() != null){
//                            graphics2D.drawImage(player1, (((int) context.getScreenInfo().getWidth()/3) + (j * 32 + 20)), i * 32 + 20, null);
//                        }
//                        if (tb[i][j].player2() != null){
//                            graphics2D.drawImage(player2, (((int) context.getScreenInfo().getWidth()/3) + (j * 32 + 20)), i * 32 + 20, null);
//                        }
//                    }
//                }

                for (int j = 0; j < 54; j++){
                        graphics2D.drawImage(outlineSquare, (j * 32 + 20), (int) context.getScreenInfo().getHeight()/2, null);

                        if (timeBoard.cells.get(j).patch()){
                            graphics2D.drawImage(patch, (j * 32 + 20), (int) context.getScreenInfo().getHeight()/2, null);
                        }
                        if (timeBoard.cells.get(j).button()){
                            graphics2D.drawImage(button, (j * 32 + 20), (int) context.getScreenInfo().getHeight()/2, null);
                        }
                        if (timeBoard.cells.get(j).player1() != null){
                            graphics2D.drawImage(player1, (j * 32 + 20), (int) context.getScreenInfo().getHeight()/2, null);
                        }
                        if (timeBoard.cells.get(j).player2() != null){
                            graphics2D.drawImage(player2, (j * 32 + 20), (int) context.getScreenInfo().getHeight()/2, null);
                        }
                    }
            });
        });
    }

    public static BufferedImage fileToImage(String path, int w, int h){
        BufferedImage img;
        try (var input = Files.newInputStream(Path.of(path))) {
            img = ImageIO.read(input);
            img = scale(img, w, h);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public static BufferedImage scale(BufferedImage src, int w, int h)
    {
        BufferedImage img =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
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
