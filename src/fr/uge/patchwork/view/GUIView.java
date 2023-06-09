package fr.uge.patchwork.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import fr.uge.patchwork.model.Piece;
import fr.uge.patchwork.model.Player;
import fr.uge.patchwork.model.PlayerBoard;
import fr.uge.patchwork.model.TimeBoard;
import javax.imageio.ImageIO;
import fr.umlv.zen5.*;

/**
 * Class dedicated to the graphic representation of the game.
 */
public final class GUIView {

    /**
     * Method that returns a BufferedImage from a file.
     * @param context context of the game
     * @param timeBoard time board of the game
     * @param p1 player 1
     * @param p2 player 2
     * @param player1Board player 1 board
     * @param player2Board player 2 board
     * @throws IOException if there is an error while reading the file
     * @throws FontFormatException if there is an error while loading the font
     */
    public static void statusView(ApplicationContext context, TimeBoard timeBoard, Player p1, Player p2,
                           PlayerBoard player1Board, PlayerBoard player2Board) throws IOException,
                                  FontFormatException {
        BufferedImage filledSquare = fileToImage("Board/filledSquare.png", 30, 30);
        BufferedImage outlineSquare = fileToImage("Board/outlineSquare.png", 30, 30);
        BufferedImage outlineSquareTB = fileToImage("Board/outlineSquare.png", 60, 60);
        BufferedImage button = fileToImage("Board/button.png", 30, 30);
        BufferedImage miniButton = fileToImage("Board/button.png", 15, 15);
        BufferedImage patch = fileToImage("Board/patch.png", 30, 30);

        BufferedImage player1 = fileToImage("Player/player1.png", 15, 15);
        BufferedImage player2 = fileToImage("Player/player2.png", 14, 14);
        BufferedImage tagPlayer1 = fileToImage("Player/tagPlayer1.png", 45, 45);
        BufferedImage tagPlayer2 = fileToImage("Player/tagPlayer2.png", 45, 45);

        Path path = Path.of("Font/Montserrat/static/Montserrat-Black.ttf");
        InputStream fontStream = GUIView.class.getClassLoader().getResourceAsStream(path.toString());
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));

        int width = (int)context.getScreenInfo().getWidth();
        int height = (int)context.getScreenInfo().getHeight();

        context.renderFrame(graphics2D -> {
            clearView(context);
            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(font.deriveFont(20f));

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
                        graphics2D.drawImage(filledSquare, (int) (context.getScreenInfo().getWidth() / 1.3 + j * 32),
                                i * 32, null);
                    }
                }
            }

            int xDrawSquare;
            int yDrawSquare;
            int cellInd;

            /* Drawing the time board */
            for (i = 0; i < 7; i++) {
                for (j = 0; j < 8; j++) {
                    xDrawSquare = (int) ((context.getScreenInfo().getWidth() / 2) + i * 80 - 280);
                    yDrawSquare = (int) ((context.getScreenInfo().getHeight() / 2) + j * 60 - 120);
                    cellInd = j * 7 + i;


                    if(cellInd < timeBoard.cells.size()) {
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
            }

            i = 0;
            int x = 65;
            int y = 300;

            graphics2D.drawString(String.valueOf(p1.getButtons()), x - 30, y + 30);
            while(i < p1.getButtons()) {
                graphics2D.drawImage(miniButton, x, y, null);

                if((i + 1)%6 == 0 && i!=0){
                    x = 65;
                    y += 15;
                }
                else{
                    x += 15;
                }

                i++;
            }

            i = 0;
            x = width - 300;
            y = 300;

            graphics2D.drawString(String.valueOf(p2.getButtons()), x - 30, y + 30);
            while(i < p2.getButtons()) {
                graphics2D.drawImage(miniButton, x, y, null);

                if((i + 1)%6 == 0 && i!=0){
                    x = width - 300;
                    y += 15;
                }
                else{
                    x += 15;
                }

                i++;
            }

            graphics2D.drawString("O : ACHETER UNE PIÈCE" , width*5/6 - 50, height*11/20);
            graphics2D.drawString("N : PASSER LE TOUR", width*5/6 - 50, height*12/20);
        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param context the context of the game
     * @param playablePieces the list of the playable pieces
     * @param playablePiecesBoolean the list of the playable pieces in boolean
     * @throws IOException if the file of the image is not found
     * @throws FontFormatException if the font is not found
     */
    public static void playablePiecesView(ApplicationContext context, ArrayList<Piece> playablePieces,
                                   ArrayList<ArrayList<ArrayList<Boolean>>> playablePiecesBoolean)
            throws IOException, FontFormatException {
        var filledSquare = fileToImage("Board/filledSquare.png", 30, 30);

        Path path = Path.of("Font/Montserrat/static/Montserrat-Black.ttf");
        InputStream fontStream = GUIView.class.getClassLoader().getResourceAsStream(path.toString());
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));

        int width = (int)context.getScreenInfo().getWidth();
        int height = (int)context.getScreenInfo().getHeight();

        context.renderFrame(graphics2D -> {
            ArrayList<ArrayList<Boolean>> schema;
            int i=0;
            int firstPlace = width*5/13;
            int px = 0;
            int py = 100;
            int max = 0;

            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(width/3, 0, width/3, height/3);

            for (ArrayList<ArrayList<Boolean>> playablePiece : playablePiecesBoolean) {
                schema = playablePiece;

                for (ArrayList<Boolean> booleans : schema) {
                    for (boolean aBoolean : booleans) {
                        if (aBoolean) {
                            graphics2D.drawImage(filledSquare, firstPlace + px, py, null);
                        }
                        px += 30;
                    }

                    if (px > max) {
                        max = px;
                    }

                    px = 0;
                    py += 30;
                }

                graphics2D.setColor(Color.WHITE);
                graphics2D.setFont(font.deriveFont(20f));
                graphics2D.drawString("Cost: " + playablePieces.get(i).cost() , firstPlace + px, 240);
                graphics2D.drawString("Time: " + playablePieces.get(i).time(), firstPlace + px, 270);
                graphics2D.drawString("Button: " + playablePieces.get(i).button(), firstPlace + px, 300);

                py = 100;
                firstPlace += max + 50;
                i++;
            }

        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param context the context of the game
     * @param idPlayerPrior the id of the player who has the priority
     * @throws IOException if the file of the image is not found
     * @throws FontFormatException if the font is not found
     */
    public static void turnView(ApplicationContext context, int idPlayerPrior) throws IOException, FontFormatException {
        Path path = Path.of("Font/Montserrat/static/Montserrat-Black.ttf");
        InputStream fontStream = GUIView.class.getClassLoader().getResourceAsStream(path.toString());
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));

        int width = (int)context.getScreenInfo().getWidth();
        int height = (int)context.getScreenInfo().getHeight();

        context.renderFrame(graphics2D -> {
            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(font.deriveFont(20f));

            if (idPlayerPrior == 1) {
                graphics2D.drawString("TOUR DU JOUEUR 1", width*5/6, height*19/20);
            }
            else {
                graphics2D.drawString("TOUR DU JOUEUR 2", width*5/6, height*19/20);
            }
        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param context the context of the game
     * @throws IOException if the file of the image is not found
     * @throws FontFormatException if the font is not found
     */
    public static void winPatchView(ApplicationContext context) throws IOException, FontFormatException {
        Path path = Path.of("Font/Montserrat/static/Montserrat-Black.ttf");
        InputStream fontStream = GUIView.class.getClassLoader().getResourceAsStream(path.toString());
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));

        int width = (int)context.getScreenInfo().getWidth();
        int height = (int)context.getScreenInfo().getHeight();

        context.renderFrame(graphics2D -> {
            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(font.deriveFont(20f));
            graphics2D.drawString("VOUS AVEZ GAGNÉ UNE TUILE SPÉCIALE", width * 5/14, height*19/20);
            graphics2D.drawString("CLIQUEZ SUR UNE CASE POUR LA PLACER", 20, height*19/20);
        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param context the context of the game
     * @param currentPiece the current piece of the player
     * @throws IOException if the file of the image is not found
     * @throws FontFormatException if the font is not found
     */
    public static void currentPieceView(ApplicationContext context, ArrayList<ArrayList<Boolean>> currentPiece)
            throws IOException, FontFormatException {
        var filledSquare = fileToImage("Board/filledSquare.png", 30, 30);

        Path path = Path.of("Font/Montserrat/static/Montserrat-Black.ttf");
        InputStream fontStream = GUIView.class.getClassLoader().getResourceAsStream(path.toString());
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));

        int width = (int)context.getScreenInfo().getWidth();
        int height = (int)context.getScreenInfo().getHeight();

        context.renderFrame(graphics2D -> {
            ArrayList<ArrayList<Boolean>> schema;
            int firstPlace = width*5/13;
            int px = 0;
            int py = 100;
            int max = 0;

            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(width/3, 0, width/3, height/3);

            schema = currentPiece;

            for (ArrayList<Boolean> booleans : schema) {
                for (boolean aBoolean : booleans) {
                    if (aBoolean) {
                        graphics2D.drawImage(filledSquare, firstPlace + px, py, null);
                    }
                    px += 30;
                }

                if (px > max) {
                    max = px;
                }

                px = 0;
                py += 30;
            }

            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(font.deriveFont(20f));
            graphics2D.drawString("VOTRE PIÈCE", width/2, 150);
            graphics2D.drawString("I:INVERT; R:ROTATE; V:VALIDER", 30, height*19/20);
        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param context the context of the game
     * @param currentPiece the current piece of the player
     * @throws IOException if the file of the image is not found
     * @throws FontFormatException if the font is not found
     */
    public static void validatedPieceView(ApplicationContext context, ArrayList<ArrayList<Boolean>> currentPiece)
            throws IOException, FontFormatException {
        BufferedImage filledSquare = fileToImage("Board/filledSquare.png", 30, 30);

        Path path = Path.of("Font/Montserrat/static/Montserrat-Black.ttf");
        InputStream fontStream = GUIView.class.getClassLoader().getResourceAsStream(path.toString());
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));

        int width = (int)context.getScreenInfo().getWidth();
        int height = (int)context.getScreenInfo().getHeight();

        context.renderFrame(graphics2D -> {
            ArrayList<ArrayList<Boolean>> schema;
            int firstPlace = width*5/13;
            int px = 0;
            int py = 100;
            int max = 0;

            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, height * 3/4, width/3 - 50, height/3);

            schema = currentPiece;

            for (ArrayList<Boolean> booleans : schema) {
                for (boolean aBoolean : booleans) {
                    if (aBoolean) {
                        graphics2D.drawImage(filledSquare, firstPlace + px, py, null);
                    }
                    px += 30;
                }

                if (px > max) {
                    max = px;
                }

                px = 0;
                py += 30;
            }

            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(font.deriveFont(20f));
            graphics2D.drawString("CLIQUER POUR PLACER LA PIÈCE", 30, height*19/20);
        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param context the context of the game
     */
    public static void clearView(ApplicationContext context) {
        context.renderFrame(graphics2D -> {
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, (int)context.getScreenInfo().getWidth(), (int)context.getScreenInfo().getHeight());
        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param context the context of the game
     * @param scorePlayer1 the score of the player 1
     * @param scorePlayer2 the score of the player 2
     * @throws IOException if the file of the image is not found
     * @throws FontFormatException if the font is not found
     */
    public static void endView(ApplicationContext context, int scorePlayer1, int scorePlayer2) throws IOException, FontFormatException {
        Path path = Path.of("Font/Montserrat/static/Montserrat-Black.ttf");
        InputStream fontStream = GUIView.class.getClassLoader().getResourceAsStream(path.toString());
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));

        int width = (int)context.getScreenInfo().getWidth();
        int height = (int)context.getScreenInfo().getHeight();

        clearView(context);

        context.renderFrame(graphics2D -> {
           graphics2D.setColor(Color.WHITE);
           graphics2D.setFont(font.deriveFont(20f));

           if(scorePlayer1 > scorePlayer2){
               graphics2D.drawString("LE JOUEUR 1 A GAGNÉ AVEC "+scorePlayer1+" POINTS", width/3 + 30, height/2);
           }
           else if(scorePlayer1 < scorePlayer2){
               graphics2D.drawString("LE JOUEUR 2 A GAGNÉ AVEC "+scorePlayer1+" POINTS", width/3 + 30, height/2);
           }
           else {
               graphics2D.drawString("LES DEUX JOUEURS TERMINENT À ÉGALITÉ", width/3 + 50, height/2);
           }

        });
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param path the path of the image
     * @param w the width of the image
     * @param h the height of the image
     * @return the image object
     */
    public static BufferedImage fileToImage(String path, int w, int h) {
    	BufferedImage img;
    	
    	try (var input = GUIView.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null){
                throw new IllegalArgumentException("file not found! " + path);
            }

            img = ImageIO.read(input);
    	    img = scale(img, w, h);
    	} catch (IOException e) {
    	    throw new RuntimeException(e);
    	}
    	return img;
    }

    /**
     * Display the view of the game when the player has to choose a piece to buy
     * @param src the image object
     * @param w the width of the image
     * @param h the height of the image
     * @return the image object
     */
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
