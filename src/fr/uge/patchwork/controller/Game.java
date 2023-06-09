package fr.uge.patchwork.controller;

import fr.uge.patchwork.model.*;
import fr.uge.patchwork.view.ConsoleView;
import fr.uge.patchwork.view.GUIView;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import fr.umlv.zen5.Event;
import fr.umlv.zen5.KeyboardKey;

/**
 * Class containing all controller methods of the game.
 */
public interface Game {

    /**
     * Controller method to start the game loop with the necessary parameters.
     * @throws IOException if there is an error while reading the file
     * @throws FontFormatException if there is an error while loading the font
     * @throws InterruptedException if there is an error while loading the font
     */
    static void start() throws IOException, FontFormatException, InterruptedException {
        /* Creating the game with the necessary parameters */

        /* Creation of player boards */
        PlayerBoard playerBoard1 = new PlayerBoard();
        PlayerBoard playerBoard2 = new PlayerBoard();
        /* Creation of players */
        Player player1 = new Player(1, playerBoard1);
        Player player2 = new Player(2, playerBoard2);
        /* Creation of the game board */
        TimeBoard timeBoard = new TimeBoard(player1, player2);
        /* Storage of players in a Map */
        Map<Integer, Player> players = Map.of(player1.getId(), player1, player2.getId(), player2);
        /* Creation of a list of pieces */
        PieceSet pieceSet = new PieceSet();

        /* Asking the user which version of the game he wants to play */
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez votre version de jeu (1 pour la version de base, 2 pour la version complète ou 3 pour la version personnalisée)");
        String gameVersion = sc.nextLine();

        /* Initialization of the list of pieces depending on the version chosen */
        pieceSet.init(gameVersion);

        /* Asking the user which graphics mode he wants to play */
        sc = new Scanner(System.in);
        System.out.println("Choisissez votre version de jeu (1 pour la version GUI ou 2 pour la version CONSOLE)");
        String input = sc.nextLine();
        GameMode mode;

        /* GameMode selection */
        if(input.equals("1")){
            mode = GameMode.GUI;
        } else if(input.equals("2")){
            mode = GameMode.CONSOLE;
        } else {
            mode = null;
            System.out.println("Vous n'avez pas choisi une version de jeu valide.");
            System.exit(0);
        }

        System.out.print("\nLANCEMENT DU JEU EN VERSION " + gameVersion + ". Système graphique choisi " + mode + ".\n\n");

        /* Starting the game loop with window */
        if(mode == GameMode.GUI) {
            Application.run(Color.BLACK, context -> {
                try {
                    GUIView.statusView(context, timeBoard, players.get(1), players.get(2), playerBoard1, playerBoard2);
                } catch (IOException | FontFormatException e) {
                    throw new RuntimeException(e);
                }

                /* Starting game loop */
                while (timeBoard.endGame()) {
                    try {
                        Game.progress(context, pieceSet, players, timeBoard, gameVersion, mode);
                    } catch (IOException | FontFormatException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        GUIView.statusView(context, timeBoard, players.get(1), players.get(2), playerBoard1, playerBoard2);
                        GUIView.turnView(context, timeBoard.turnOf());
                    } catch (IOException | FontFormatException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Game.end(context, players, mode);
                } catch (IOException | FontFormatException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        /* Starting game loop on console */
        else {
            while (timeBoard.endGame()) {
                Game.progress(null, pieceSet, players, timeBoard, gameVersion, mode);
            }

            Game.end(null, players, mode);
        }

        sc.close();
    }


    /**
     * Controller method to display the status of the game.
     * @param context zen5 context
     * @param player1 player 1
     * @param player2 player 2
     * @param pieceList list of pieces
     * @param timeBoard game board
     * @param mode game mode
     * @throws IOException if an I/O error occurs
     * @throws FontFormatException if the font is not valid
     * @throws InputMismatchException if the input is not valid
     */
    static void status(ApplicationContext context, Player player1, Player player2, PieceSet pieceList,
                              TimeBoard timeBoard, GameMode mode) throws IOException, FontFormatException {
        /* Retrieving the ID of the player who must play */
        int idPlayerPrior = timeBoard.turnOf();

        if(mode == GameMode.GUI) {
            /* Displaying the player who must play */
            GUIView.turnView(context, idPlayerPrior);
        } else {
            /* Getting playable pieces */
            ArrayList<Piece> playablePieces = pieceList.nextPieces();
            /* Displaying the player who must play */
            ConsoleView.statusView(player1, player2, timeBoard, idPlayerPrior, playablePieces);
        }
    }

    /**
     * Controller method to manage the game loop.
     * @param context zen5 context
     * @param pieceList list of pieces
     * @param players Map of players by ID
     * @param timeBoard game board
     * @param gameVersion version of the game
     * @param mode game mode
     * @throws IOException if an I/O error occurs
     * @throws FontFormatException if the font is not valid
     * @throws InterruptedException if the thread is interrupted
     * @throws InputMismatchException if the input is not valid
     */
    static void progress(ApplicationContext context, PieceSet pieceList, Map<Integer, Player> players,
                            TimeBoard timeBoard, String gameVersion, GameMode mode) throws IOException,
            FontFormatException, InterruptedException {
        int idPlayerPrior = timeBoard.turnOf();

        if(mode == GameMode.GUI) {
            Event event;

            Game.status(context, players.get(1), players.get(2), pieceList, timeBoard, mode);

            /* Asking the player if they want to buy a piece */
            event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(5000);

            if (event == null || !event.getAction().equals(Event.Action.KEY_PRESSED)) {
                Game.overtake(context, players, timeBoard, idPlayerPrior, mode);
            } else {
                if (event.getKey().equals(KeyboardKey.O)) {             /* Oui */
                    Game.buy(context, pieceList, players, timeBoard, idPlayerPrior, mode);
                } else if (event.getKey().equals(KeyboardKey.N)) {      /* Non */
                    Game.overtake(context, players, timeBoard, idPlayerPrior, mode);
                } else {
                    Game.overtake(context, players, timeBoard, idPlayerPrior, mode);
                }
            }
        }
        else {
            Game.status(null, players.get(1), players.get(2), pieceList, timeBoard, mode);

            /* Asking the player if they want to buy a piece */
            ConsoleView.askingForPiece();

            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();

            if (str.equals("oui")) {
                Game.buy(null, pieceList, players, timeBoard, idPlayerPrior, mode);
            } else {
                Game.overtake(null, players, timeBoard, idPlayerPrior, mode);
            }
        }

        if (gameVersion.equals("2")) {
            if (players.get(idPlayerPrior).getBoard().isSpecialPieceEarnable() && timeBoard.isSpecialPieceAvailable()) {
                players.get(idPlayerPrior).setSpecialPiece(true);
                timeBoard.setSpecialPieceAvailable(false);
                if(mode == GameMode.CONSOLE)
                    System.out.println("Le joueur " + idPlayerPrior + " a gagné la tuile spéciale. (7x7)");
                else
                    GUIView.specialPiece(context, idPlayerPrior);
            }
        }
    }

    /**
     * Controller method to manage the case where the player wants to buy a piece.
     * @param context       zen5 context
     * @param pieceList     list of pieces
     * @param players       Map of players by ID
     * @param timeBoard     game board
     * @param idPlayerPrior ID of the player who must play
     * @param mode          game mode
     * @throws IOException         if an I/O error occurs
     * @throws FontFormatException if the font format is not supported
     * @throws InterruptedException if the thread is interrupted
     */
    static void buy(ApplicationContext context, PieceSet pieceList, Map<Integer, Player> players,
                            TimeBoard timeBoard, int idPlayerPrior, GameMode mode) throws IOException,
                            FontFormatException, InterruptedException {
        if(mode == GameMode.GUI) {
            int idPiece = 0;
            int firstPlace = (int) context.getScreenInfo().getWidth() * 5 / 13;
            int x, y, px = 0, max = 0;
            ArrayList<Integer> positions = new ArrayList<>();
            ArrayList<Piece> playablePieces = pieceList.nextPieces();
            ArrayList<ArrayList<Boolean>> schema;
            ArrayList<ArrayList<ArrayList<Boolean>>> playablePiecesBooleans = new ArrayList<>();

            for (Piece playablePiece : playablePieces) {
                playablePiecesBooleans.add(playablePiece.schema());
            }

            GUIView.playablePiecesView(context, playablePieces, playablePiecesBooleans);

            /* Asking the player which piece they want to buy */
            positions.add((int) context.getScreenInfo().getWidth() * 5 / 13);

            for (ArrayList<ArrayList<Boolean>> playablePiece : playablePiecesBooleans) {
                schema = playablePiece;

                for (ArrayList<Boolean> booleans : schema) {
                    for (boolean ignored : booleans) {
                        px += 30;
                    }

                    if (px > max) {
                        max = px;
                    }

                    px = 0;
                }

                positions.add(firstPlace += max + 50);
            }

            Event event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(5000);

            while (event.getLocation() == null) {
                event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(5000);
            }

            px = (int) event.getLocation().getX();

            if (px < positions.get(1)) {
                idPiece = 1;
            } else if (px >= positions.get(1) && px < positions.get(2)) {
                idPiece = 2;
            } else if (px >= positions.get(2)) {
                idPiece = 3;
            }

            while (playablePieces.get(idPiece - 1).cost() > players.get(idPlayerPrior).getButtons()) {
                /* Asking the player if he wants to buy another piece */
                event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(5000);

                while (event.getLocation() == null) {
                    event = context.pollOrWaitEvent(30000);
                    context.pollOrWaitEvent(5000);
                }

                px = (int) event.getLocation().getX();

                if (px < positions.get(1)) {
                    idPiece = 1;
                } else if (px >= positions.get(1) && px < positions.get(2)) {
                    idPiece = 2;
                } else if (px >= positions.get(2)) {
                    idPiece = 3;
                }
            }

            /* Display the piece he wants to buy and ask him if he wants to rotate, invert or validate */
            GUIView.statusView(context, timeBoard, players.get(1), players.get(2), players.get(1).getBoard(),
                    players.get(2).getBoard());
            GUIView.turnView(context, idPlayerPrior);
            GUIView.currentPieceView(context, playablePiecesBooleans.get(idPiece - 1));

            event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(5000);

            while (event.getKey() == null || !event.getKey().equals(KeyboardKey.V)) {
            		if(event.getKey() != null) {
	                if (event.getKey().equals(KeyboardKey.R)) {
	                    /* Replacement of the piece by the rotate piece */
	                    playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).rotate());
	                    schema = playablePieces.get(idPiece - 1).schema();
	                    GUIView.currentPieceView(context, schema);
	                    GUIView.turnView(context, idPlayerPrior);
	                } else if (event.getKey().equals(KeyboardKey.I)) {
	                    /* Replacement of the piece with the invert piece */
	                    playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).invert());
	                    schema = playablePieces.get(idPiece - 1).schema();
	                    GUIView.currentPieceView(context, schema);
	                    GUIView.turnView(context, idPlayerPrior);
	                }
               } 

                event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(5000);
            }

            /* Asking the user where he wants to place the piece on his board */
            schema = playablePieces.get(idPiece - 1).schema();

            GUIView.validatedPieceView(context, schema);
            GUIView.turnView(context, idPlayerPrior);

            event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(5000);

            int[] position = GUIView.askPosition(event, context, idPlayerPrior);
            x = position[0];
            y = position[1];

            /* If the player bought the piece, we move him on the time board. We also remove the piece from the list */
            if (players.get(idPlayerPrior).buyPiece(playablePieces.get(idPiece - 1), x, y)) {
                int buttonsCrossed, patchesEarned;

                /* Predicting the movement of the player depending on the piece he bought */
                Map<String, Integer> movement = timeBoard.predictMovement(players.get(idPlayerPrior), playablePieces.get(0).time());

                /* The player gets the number of he has on his board for each button cell he crossed */
                buttonsCrossed = timeBoard.nbButton(movement.get("start"), movement.get("end"));

                /* The player earns the patches he has passed */
                patchesEarned = timeBoard.nbPatch(movement.get("start"), movement.get("end"));
                if (buttonsCrossed > 0 || patchesEarned > 0) {
                    reward(context, players, buttonsCrossed, patchesEarned, idPlayerPrior, mode);
                }

                /* Moving the player and getting the number of buttons passed and then adding the buttons won */
                timeBoard.movePlayer(players.get(idPlayerPrior), playablePieces.get(idPiece - 1).time());
                pieceList.removePiece(idPiece - 1);

                GUIView.statusView(context, timeBoard, players.get(1), players.get(2), players.get(1).getBoard(),
                        players.get(2).getBoard());
            } else {
                overtake(context, players, timeBoard, idPlayerPrior, mode);
            }
        }
        else {
            String str;
            int idPiece;
            int x, y;
            ArrayList<Piece> playablePieces = pieceList.nextPieces();

            /* Asking the player which piece they want to buy */
            ConsoleView.whichPiece();
            Scanner sc = new Scanner(System.in);
            idPiece = sc.nextInt();

            while (playablePieces.get(idPiece - 1).cost() > players.get(idPlayerPrior).getButtons()) {
                /* Asking the player if he wants to buy another piece */
                ConsoleView.cantBuyPiece();
                idPiece = sc.nextInt();
            }

            /* Display the piece he wants to buy and ask him if he wants to rotate, invert or validate */
            ConsoleView.actionsOnPiece(playablePieces, idPiece);

            str = sc.nextLine();

            while(!str.equals("validate")) {
                if (str.equals("rotate")) {
                    /* Replacement of the piece by the rotate piece */
                    playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).rotate());
                } else if (str.equals("invert")) {
                    /* Replacement of the piece with the invert piece */
                    playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).invert());
                } else {
                    ConsoleView.notValid();
                }

                ConsoleView.actionsOnPiece(playablePieces, idPiece);

                str = sc.nextLine();
            }

            /* Asking the user where he wants to place the piece on his board */
            ConsoleView.positions();

            x = sc.nextInt();
            y = sc.nextInt();
            sc.nextLine();

            /* If the player bought the piece, we move him on the time board. We also remove the piece from the list */
            if (players.get(idPlayerPrior).buyPiece(playablePieces.get(idPiece - 1), x, y)) {
                int buttonsCrossed, patchesEarned;

                /* Predicting the movement of the player depending on the piece he bought */
                Map<String, Integer> movement = timeBoard.predictMovement(players.get(idPlayerPrior), playablePieces.get(0).time());

                /* The player gets the number of he has on his board for each button cell he crossed */
                buttonsCrossed = timeBoard.nbButton(movement.get("start"), movement.get("end"));

                /* The player earns the patches he has passed */
                patchesEarned = timeBoard.nbPatch(movement.get("start"), movement.get("end"));
                if (buttonsCrossed > 0 || patchesEarned > 0) {
                    reward(null, players, buttonsCrossed, patchesEarned, idPlayerPrior, mode);
                }

                /* Moving the player and getting the number of buttons passed and then adding the buttons won */
                timeBoard.movePlayer(players.get(idPlayerPrior), playablePieces.get(idPiece - 1).time());
                pieceList.removePiece(idPiece-1);

                ConsoleView.playerMove(idPlayerPrior, playablePieces, idPiece);
            } else {
                ConsoleView.cantPlace();

                overtake(null, players, timeBoard, idPlayerPrior, mode);
            }
        }
    }

    /**
     * Controller method to manage the case where the player wants to pass.
     * @param context       zen5 context
     * @param players       players Map by ID
     * @param timeBoard     game board
     * @param idPlayerPrior ID of the player who must play
     * @param mode          game mode
     * @throws IOException          if the font file is not found
     * @throws FontFormatException  if the font is not valid
     */
    static void overtake(ApplicationContext context, Map<Integer, Player> players, TimeBoard timeBoard,
                         int idPlayerPrior, GameMode mode) throws IOException, FontFormatException {
        int buttonsCrossed, patchesEarned;
        int distance;

        if (timeBoard.isInFront() == idPlayerPrior || timeBoard.isInFront() == 0) {
            distance = 1;
            if(mode == GameMode.CONSOLE)
                ConsoleView.wasLeader(idPlayerPrior, distance);
        } else {
            distance = timeBoard.distance() + 1;
            if(mode == GameMode.CONSOLE)
                ConsoleView.wasBehind(idPlayerPrior, distance);
        }

        Map<String, Integer> movement = timeBoard.predictMovement(players.get(idPlayerPrior), distance);

        /* The player gets the number of he has on his board for each button cell he crossed */
        buttonsCrossed = timeBoard.nbButton(movement.get("start"), movement.get("end"));
        for (int i = 0; i < buttonsCrossed; i++) {
            players.get(idPlayerPrior).addButtons(players.get(idPlayerPrior).buttonsToEarn());
        }

        /* The player earns the patches he has passed */
        patchesEarned = timeBoard.nbPatch(movement.get("start"), movement.get("end"));

        /* The player earns the patches he has passed */
        if (buttonsCrossed > 0 || patchesEarned > 0) {
            if (mode == GameMode.GUI)
                reward(context, players, buttonsCrossed, patchesEarned, idPlayerPrior, mode);
            else
                reward(null, players, buttonsCrossed, patchesEarned, idPlayerPrior, mode);
        }

        /* Winning the distance in buttons */
        players.get(idPlayerPrior).addButtons(distance);
        /* Moving the player in front of his opponent */
        timeBoard.movePlayer(players.get(idPlayerPrior), distance);
    }

    /**
     * Controller method to manage the case where the player is rewarded with patches.
     * @param context zen5 context
     * @param players players Map by ID
     * @param buttonsCrossed number of buttons crossed
     * @param patchesEarned number of patches earned
     * @param idPlayerPrior ID of the player who must play
     * @param mode game mode
     * @throws IOException          if the font file is not found
     * @throws FontFormatException  if the font is not valid
     */
    static void reward(ApplicationContext context, Map<Integer, Player> players, int buttonsCrossed, int patchesEarned,
                       int idPlayerPrior, GameMode mode) throws IOException, FontFormatException {
        var schema = new ArrayList<ArrayList<Boolean>>();
        var row = new ArrayList<Boolean>();
        row.add(true);
        schema.add(row);
        var square = new Piece(schema, 0, 0, 0);

        if(mode == GameMode.GUI) {
            int x;
            int y;

            /* For each button cells the player crossed, he earns his number of buttons in patches he has on his board*/
            for (int i = 0; i < buttonsCrossed; i++) {
                players.get(idPlayerPrior).addButtons(players.get(idPlayerPrior).buttonsToEarn());
            }

            /* If the player has passed patches, we ask him where he wants to place them */
            for (int i = 0; i < patchesEarned; i++) {
                GUIView.winPatchView(context);

                Event event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(3000);

                int[] position = GUIView.askPosition(event, context, idPlayerPrior);
                x = position[0];
                y = position[1];



                while (!players.get(idPlayerPrior).buyPiece(square, x, y)) {
                    event = context.pollOrWaitEvent(30000);
                    context.pollOrWaitEvent(3000);

                    position = GUIView.askPosition(event, context, idPlayerPrior);
                    x = position[0];
                    y = position[1];
                }
            }
        }
        else {
            int x = 0;
            int y = 0;

            /* For each button cells the player crossed, he earns his number of buttons in patches he has on his board*/
            for (int i = 0; i < buttonsCrossed; i++) {
                players.get(idPlayerPrior).addButtons(players.get(idPlayerPrior).buttonsToEarn());
                ConsoleView.crossButton(idPlayerPrior, players);
            }

            /* If the player has passed patches, we ask him where he wants to place them */
            for (int i = 0; i < patchesEarned; i++) {
                boolean validIntegers = false;

                ConsoleView.crossPatch();

                Scanner sc = new Scanner(System.in);

                while(!validIntegers){
                    try {
                        x = sc.nextInt();
                        y = sc.nextInt();
                        sc.nextLine();
                        validIntegers = true;
                    } catch (InputMismatchException e){
                        ConsoleView.notValidPositions();
                        sc.nextLine();
                    }
                }

                while (!players.get(idPlayerPrior).buyPiece(square, x, y)) {
                    ConsoleView.cantPlacePatch();
                    x = sc.nextInt();
                    y = sc.nextInt();
                }
            }
        }
    }

    /**
     * Controller method to display the winner of the game.
     * @param context zen5 context
     * @param players players Map by ID
     * @param mode game mode
     * @throws IOException if the file is not found*
     * @throws FontFormatException if the font is not valid
     */
    static void end(ApplicationContext context, Map<Integer, Player> players, GameMode mode) throws IOException,
            FontFormatException, InterruptedException {
        int scorePlayer1 = players.get(1).score();
        int scorePlayer2 = players.get(2).score();

        if(mode == GameMode.GUI) {
            GUIView.endView(context, scorePlayer1, scorePlayer2);
        }
        else {
            ConsoleView.endView(scorePlayer1, scorePlayer2);
        }

        Thread.sleep(5000);
    }

}
