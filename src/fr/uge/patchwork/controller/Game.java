package fr.uge.patchwork.controller;

import fr.uge.patchwork.model.*;
import fr.uge.patchwork.view.View;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import fr.umlv.zen5.Event;
import fr.umlv.zen5.KeyboardKey;

import static java.lang.Thread.sleep;

/**
 * Class containing all controller methods of the game.
 */
public interface Game {

    /**
     * Controller method to start the game loop with the necessary parameters.
     */
    static void start() throws IOException {
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
        System.out.println("Choisissez votre version de jeu (1 ou 2)");
        String gameVersion = sc.nextLine();

        /* Initialization of the list of pieces depending on the version chosen */
        pieceSet.init(gameVersion);

        System.out.print("\nLANCEMENT DU JEU EN VERSION " + gameVersion + ".\n\n");

        Application.run(Color.BLACK, context -> {
            try {
                View.statusView(context, timeBoard, playerBoard1, playerBoard2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (FontFormatException e) {
                throw new RuntimeException(e);
            }

            /* Starting game loop */
            while(!timeBoard.endGame()){
                try {
                    Game.progress(context, pieceSet, players, timeBoard, gameVersion);
                } catch (IOException | FontFormatException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    View.statusView(context, timeBoard, playerBoard1, playerBoard2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (FontFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Game.end(players);

        sc.close();

        System.out.println("FIN DU JEU.");
    }


    /**
     * Controller method to display the status of the game.
     * @param player1 player 1
     * @param player2 player 2
     * @param pieceList list of pieces
     * @param timeBoard game board
     */
    static void status(ApplicationContext context, Player player1, Player player2, PieceSet pieceList,
                              TimeBoard timeBoard) throws IOException, FontFormatException {
        /* Display the status of the game */
        System.out.println(player1);
        System.out.println(timeBoard);
        System.out.println(player2);

        /* Retrieving the ID of the player who must play */
        int idPlayerPrior = timeBoard.turnOf();

        /* Displaying the player who must play */
        View.turnView(context, idPlayerPrior);

        System.out.println("C'est au tour du joueur " + idPlayerPrior + "\n");

        /* Displaying playable pieces */
        ArrayList<Piece> playablePieces = pieceList.nextPieces();
        System.out.println("Les pièces jouables sont : \n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Pièce " + i + " : \n" + playablePieces.get(i));
        }
    }

    /**
     * Controller method to manage the game loop.
     * @param pieceList list of pieces
     * @param players Map of players by ID
     * @param timeBoard game board
     * @param gameVersion version of the game
     */
    static void progress(ApplicationContext context, PieceSet pieceList, Map<Integer, Player> players,
                            TimeBoard timeBoard, String gameVersion) throws IOException, FontFormatException, InterruptedException {
        int idPlayerPrior = timeBoard.turnOf();
        Event event;
        System.out.println("\n========== TOUR SUIVANT ==========\n");

        Game.status(context, players.get(1), players.get(2), pieceList, timeBoard);

        /* Asking the player if they want to buy a piece */
        System.out.println("Voulez-vous acheter une pièce ? (oui/non)");

        event = context.pollOrWaitEvent(30000);
        context.pollOrWaitEvent(5000);

        if (event == null || !event.getAction().equals(Event.Action.KEY_PRESSED)) {
            System.out.println("Aucune action effectuée. On passe le tour.");
            Game.overtake(context, players, timeBoard, idPlayerPrior);
        } else {
            if (event.getKey().equals(KeyboardKey.O)) {
                Game.buy(context, pieceList, players, timeBoard, idPlayerPrior);
            } else if (event.getKey().equals(KeyboardKey.N)) {
                Game.overtake(context, players, timeBoard, idPlayerPrior);
            } else {
                System.out.println("Aucune action effectuée. On passe le tour.");
                Game.overtake(context, players, timeBoard, idPlayerPrior);
            }
        }

        /*
        Version dans la console
        Scanner sc = new Scanner(System.in);

        String str = sc.nextLine();

        if (str.equals("oui")) {
            Game.buy(pieceList, players, timeBoard, idPlayerPrior);
        } else {
            Game.overtake(players, timeBoard, idPlayerPrior);
        }
        */

        if(gameVersion.equals("2")){
            if(players.get(1).getBoard().isSpecialPieceEarnable() && timeBoard.isSpecialPieceAvailable()){
                players.get(1).setSpecialPiece(true);
                timeBoard.setSpecialPieceAvailable(false);
                System.out.println("Le joueur 1 a gagné la tuile spéciale.");
            }
            if(players.get(2).getBoard().isSpecialPieceEarnable() && timeBoard.isSpecialPieceAvailable()){
                players.get(2).setSpecialPiece(true);
                timeBoard.setSpecialPieceAvailable(false);
                System.out.println("Le joueur 2 a gagné la tuile spéciale.");
            }
        }
    }

    /**
     * Controller method to manage the case where the player wants to buy a piece.
     * @param pieceList     list of pieces
     * @param players       Map of players by ID
     * @param timeBoard     game board
     * @param idPlayerPrior ID of the player who must play
     */
    static void buy(ApplicationContext context, PieceSet pieceList, Map<Integer, Player> players,
                            TimeBoard timeBoard, int idPlayerPrior) throws IOException, FontFormatException, InterruptedException {
        int idPiece = 0;
        int firstPlace = (int)context.getScreenInfo().getWidth()*5/13;
        int x, y, px = 0, max = 0;
        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<Piece> playablePieces = pieceList.nextPieces();
        ArrayList<ArrayList<Boolean>> schema;
        ArrayList<ArrayList<ArrayList<Boolean>>> playablePiecesBooleans = new ArrayList<>();

        for (Piece playablePiece : playablePieces) {
            playablePiecesBooleans.add(playablePiece.schema());
        }

        View.playablePiecesView(context, playablePiecesBooleans);

        /* Asking the player which piece they want to buy */
        System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");

        positions.add((int)context.getScreenInfo().getWidth()*5/13);

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

        while(event.getLocation() == null){
            event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(5000);
        }

        px = (int)event.getLocation().getX();

        if(px < positions.get(1)){
            System.out.println("Pièce 1");
            idPiece = 1;
        }
        else if(px >= positions.get(1) && px < positions.get(2)){
            System.out.println("Pièce 2");
            idPiece = 2;
        }
        else if(px >= positions.get(2)){
            System.out.println("Pièce 3");
            idPiece = 3;
        }

        while(playablePieces.get(idPiece - 1).cost() > players.get(idPlayerPrior).getButtons()){
            /* Asking the player if he wants to buy another piece */
            System.out.println("Vous n'avez pas assez de boutons pour acheter cette pièce");
            System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");

            event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(5000);

            while(event.getLocation() == null){
                event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(5000);
            }

            px = (int)event.getLocation().getX();

            if(px < positions.get(1)){
                System.out.println("Pièce 1");
                idPiece = 1;
            }
            else if(px >= positions.get(1) && px < positions.get(2)){
                System.out.println("Pièce 2");
                idPiece = 2;
            }
            else if(px >= positions.get(2)){
                System.out.println("Pièce 3");
                idPiece = 3;
            }
        }

        /* Display the piece he wants to buy and ask him if he wants to rotate, invert or validate */
        System.out.println("Vous avez choisi la pièce : \n" + playablePieces.get(idPiece - 1));

        View.statusView(context, timeBoard, players.get(1).getBoard(), players.get(2).getBoard());
        View.currentPieceView(context, playablePiecesBooleans.get(idPiece - 1));

        System.out.println("Que voulez-vous en faire ? (actions : rotate/invert/validate)");
        /* str = sc.nextLine(); */

        event = context.pollOrWaitEvent(30000);
        context.pollOrWaitEvent(5000);

        while(event.getKey() == null || !event.getKey().equals(KeyboardKey.V)){
            if(event.getKey() != null && event.getKey().equals(KeyboardKey.R)){
                /* Replacement of the piece by the rotate piece */
                playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).rotate());
                View.statusView(context, timeBoard, players.get(1).getBoard(), players.get(2).getBoard());

                schema = playablePieces.get(idPiece - 1).schema();
                View.currentPieceView(context, schema);
            }
            else if(event.getKey() != null && event.getKey().equals(KeyboardKey.I)){
                /* Replacement of the piece with the invert piece */
                playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).invert());
                View.statusView(context, timeBoard, players.get(1).getBoard(), players.get(2).getBoard());
                schema = playablePieces.get(idPiece - 1).schema();
                View.currentPieceView(context, schema);
            }
            else{
                System.out.println("Vous n'avez pas choisi une action valide");
            }

            System.out.println("Votre pièce courante : \n" + playablePieces.get(idPiece - 1));
            System.out.println("Que voulez-vous en faire ? (rotate/invert/validate)");

            event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(5000);
            /* str = sc.nextLine(); */
        }

        /* Asking the user where he wants to place the piece on his board */
        System.out.println("Veuillez choisir une position pour votre pièce ? (ligne colonne)");

        event = context.pollOrWaitEvent(30000);
        context.pollOrWaitEvent(5000);

        /* Version dans la console
        x = sc.nextInt();
        y = sc.nextInt();
        sc.nextLine();
         */

        if(idPlayerPrior == 1) {
            while (event == null || event.getLocation() == null
                    || event.getLocation().getX() < 65
                    || event.getLocation().getX() > 65 + 9 * 32
                    || event.getLocation().getY() < 0
                    || event.getLocation().getY() > 9 * 32) {

                System.out.println("Vous n'avez pas choisi de position, veuillez réessayer (ligne colonne)");
                event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(3000);
            }

            x = (int)(event.getLocation().getY() / 32);
            y = (int)(event.getLocation().getX() - 65) / 32;
        }
        else{

            while (event == null || event.getLocation() == null
                    || event.getLocation().getX() < context.getScreenInfo().getWidth() / 1.3
                    || event.getLocation().getX() > context.getScreenInfo().getWidth() / 1.3 + 9 * 32
                    || event.getLocation().getY() < 0
                    || event.getLocation().getY() > 9 * 32) {

                System.out.println("Vous n'avez pas choisi de position, veuillez réessayer (ligne colonne)");
                event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(3000);
            }

            x = (int)(event.getLocation().getY() / 32);
            y = (int)(event.getLocation().getX() - (context.getScreenInfo().getWidth()/1.3)) / 32;
        }

        /* If the player bought the piece, we move him on the time board. We also remove the piece from the list */
        if(players.get(idPlayerPrior).buyPiece(playablePieces.get(idPiece - 1), x, y)){
            int buttonsCrossed, patchesEarned;

            /* Predicting the movement of the player depending on the piece he bought */
            Map<String, Integer> movement = timeBoard.predictMovement(players.get(idPlayerPrior), playablePieces.get(0).time());

            /* The player gets the number of he has on his board for each button cell he crossed */
            buttonsCrossed = timeBoard.nbButton(movement.get("start"), movement.get("end"));
            players.get(idPlayerPrior).addButtons(buttonsCrossed);

            /* The player earns the patches he has passed */
            patchesEarned = timeBoard.nbPatch(movement.get("start"), movement.get("end"));
            if(patchesEarned > 0){
                reward(context, players, buttonsCrossed, patchesEarned, idPlayerPrior);
            }

            /* Moving the player and getting the number of buttons passed and then adding the buttons won */
            timeBoard.movePlayer(players.get(idPlayerPrior), playablePieces.get(idPiece - 1).time());
            pieceList.removePiece(idPiece - 1);

            System.out.println("Le joueur " + idPlayerPrior + " a acheté une pièce et l'a placé sur son plateau." +
                    " Il a avancé de " + playablePieces.get(idPiece - 1).time() + " cases.");

            View.statusView(context, timeBoard, players.get(1).getBoard(), players.get(2).getBoard());
        } else {
            System.out.println("Vous ne pouvez pas placer cette pièce à cet endroit. Vous passez donc votre tour.");

            overtake(context, players, timeBoard, idPlayerPrior);
        }
    }

    /**
     * Controller method to manage the case where the player wants to pass.
     * @param players       players Map by ID
     * @param timeBoard     game board
     * @param idPlayerPrior ID of the player who must play
     */
    static void overtake(ApplicationContext context, Map<Integer, Player> players, TimeBoard timeBoard, int idPlayerPrior) throws IOException, FontFormatException, InterruptedException {
        int buttonsCrossed, patchesEarned;
        int distance;

        if(timeBoard.isInFront() == idPlayerPrior || timeBoard.isInFront() == 0){
            distance = 1;
            Map<String, Integer> movement = timeBoard.predictMovement(players.get(idPlayerPrior), distance);

            /* The player gets the number of he has on his board for each button cell he crossed */
            buttonsCrossed = timeBoard.nbButton(movement.get("start"), movement.get("end"));
            players.get(idPlayerPrior).addButtons(buttonsCrossed);
            /* The player earns the patches he has passed */
            patchesEarned = timeBoard.nbPatch(movement.get("start"), movement.get("end"));

            /* The player earns the patches he has passed */
            if(buttonsCrossed > 0 || patchesEarned > 0){
                reward(context, players, buttonsCrossed, patchesEarned, idPlayerPrior);
            }

            System.out.println("Le joueur " + idPlayerPrior + " était en tête, il a donc avancé de " + distance + " case.");
        }
        else {
            /* Predicting movement before cleaning the board */
            distance = timeBoard.distance() + 1;
            Map<String, Integer> movement = timeBoard.predictMovement(players.get(idPlayerPrior), distance);

            /* The player gets the number of he has on his board for each button cell he crossed */
            buttonsCrossed = timeBoard.nbButton(movement.get("start"), movement.get("end"));
            players.get(idPlayerPrior).addButtons(buttonsCrossed);
            /* The player earns the patches he has passed */
            patchesEarned = timeBoard.nbPatch(movement.get("start"), movement.get("end"));

            /* The player earns the patches he has passed */
            if(buttonsCrossed > 0 || patchesEarned > 0){
                reward(context, players, buttonsCrossed, patchesEarned, idPlayerPrior);
            }

            System.out.println("Le joueur " + idPlayerPrior + " a décidé de passer son tour. Il a donc dépassé son adversaire en parcourant "
                    + distance + " cases.");
        }

        /* Winning the distance in buttons */
        players.get(idPlayerPrior).addButtons(distance);
        /* Moving the player in front of his opponent */
        timeBoard.movePlayer(players.get(idPlayerPrior), distance);
    }

    /**
     * Controller method to manage the case where the player is rewarded with patches.
     * @param players players Map by ID
     * @param buttonsCrossed number of buttons crossed
     * @param patchesEarned number of patches earned
     * @param idPlayerPrior ID of the player who must play
     */
    static void reward(ApplicationContext context, Map<Integer, Player> players, int buttonsCrossed, int patchesEarned, int idPlayerPrior) throws IOException, FontFormatException, InterruptedException {
        int x;
        int y;

        /* For each button cells the player crossed, he earns his number of buttons in patches he has on his board*/
        for (int i = 0; i < buttonsCrossed; i++) {
            players.get(idPlayerPrior).addButtons(players.get(idPlayerPrior).buttonsToEarn());
            System.out.println("Le joueur " + idPlayerPrior + " a gagné " + players.get(idPlayerPrior).buttonsToEarn()
                    + " bouton(s) en passant sur une case bouton.");
        }

        /* If the player has passed patches, we ask him where he wants to place them */
        for (int i = 0; i < patchesEarned; i++) {
            View.winPatchView(context);
            System.out.println("Vous avez gagné un patch spécial 1x1 en passant sur une case dédiée ! Veuillez choisir "
                    + "où la placer (ligne colonne).");

            Event event = context.pollOrWaitEvent(30000);
            context.pollOrWaitEvent(3000);

            if(idPlayerPrior == 1) {
                while (event == null || event.getLocation() == null
                        || event.getLocation().getX() < 65
                        || event.getLocation().getX() > 65 + 9 * 32
                        || event.getLocation().getY() < 0
                        || event.getLocation().getY() > 9 * 32) {

                    System.out.println("Vous n'avez pas choisi de position, veuillez réessayer (ligne colonne)");
                    event = context.pollOrWaitEvent(30000);
                    context.pollOrWaitEvent(3000);
                }

                x = (int)(event.getLocation().getY() / 32);
                y = (int)(event.getLocation().getX() - 65) / 32;
            }
            else{
                while (event == null || event.getLocation() == null
                        || event.getLocation().getX() < context.getScreenInfo().getWidth() / 1.3
                        || event.getLocation().getX() > context.getScreenInfo().getWidth() / 1.3 + 9 * 32
                        || event.getLocation().getY() < 0
                        || event.getLocation().getY() > 9 * 32) {

                    System.out.println("Vous n'avez pas choisi de position, veuillez réessayer (ligne colonne)");
                    event = context.pollOrWaitEvent(30000);
                    context.pollOrWaitEvent(3000);
                }

                x = (int)(event.getLocation().getY() / 32);
                y = (int)(event.getLocation().getX() - context.getScreenInfo().getWidth()*1/3) / 32;
            }

            /*
            Version en ligne de commande
            while(!validIntegers){
                try {
                    x = sc.nextInt();
                    y = sc.nextInt();
                    sc.nextLine();
                    validIntegers = true;
                } catch (InputMismatchException e){
                    System.out.println("Les entiers entrés ne sont pas valides, veuillez essayer à nouveau (ligne colonne)");
                    sc.nextLine();
                }
            } */

            var schema = new ArrayList<ArrayList<Boolean>>();
            var row = new ArrayList<Boolean>();
            row.add(true);
            schema.add(row);
            var square = new Piece(schema, 0, 0, 0);

            while(!players.get(idPlayerPrior).buyPiece(square, x, y)){
                event = context.pollOrWaitEvent(30000);
                context.pollOrWaitEvent(3000);

                System.out.println("Vous ne pouvez pas placer la pièce à ces positions, veuillez choisir d'autres coordonnées (ligne colonne)");

                if(idPlayerPrior == 1) {
                    while (event == null || event.getLocation() == null || event.getLocation().getX() < 65
                            || event.getLocation().getX() > 65 + 9 * 32 || event.getLocation().getY() < 0
                            || event.getLocation().getY() > 9 * 32) {

                        System.out.println("Vous n'avez pas choisi de position, veuillez réessayer (ligne colonne)");
                        event = context.pollOrWaitEvent(30000);
                        context.pollOrWaitEvent(3000);
                    }

                    x = (int)(event.getLocation().getY() / 32);
                    y = (int)(event.getLocation().getX() - 65) / 32;
                }
                else{
                    while (event == null || event.getLocation() == null
                            || event.getLocation().getX() < context.getScreenInfo().getWidth()*1/3
                            || event.getLocation().getX() > context.getScreenInfo().getWidth()*1/3 + 9 * 32
                            || event.getLocation().getY() < 0 || event.getLocation().getY() > 9 * 32) {

                        System.out.println("Vous n'avez pas choisi de position, veuillez réessayer (ligne colonne)");
                        event = context.pollOrWaitEvent(30000);
                        context.pollOrWaitEvent(3000);
                    }

                    x = (int)(event.getLocation().getY() / 32);
                    y = (int)(event.getLocation().getX() - context.getScreenInfo().getWidth()*1/3) / 32;
                }
            }
        }
    }

    /**
     * Controller method to display the winner of the game.
     * @param players players Map by ID
     */
    static void end(Map<Integer, Player> players){
        int scorePlayer1 = players.get(1).score();
        int scorePlayer2 = players.get(2).score();

        if(scorePlayer1 > scorePlayer2){
            System.out.println("\nLe joueur 1 a gagné avec " + scorePlayer1 + " points contre " + scorePlayer2 +
                    " points.");
        } else if(scorePlayer1 < scorePlayer2){
            System.out.println("\nLe joueur 2 a gagné avec " + scorePlayer2 + " points contre " + scorePlayer1 +
                    " points.");
        } else {
            System.out.println("\nÉgalité parfaite entre les deux joueurs avec " + scorePlayer1 + " points.");
        }
    }

}
