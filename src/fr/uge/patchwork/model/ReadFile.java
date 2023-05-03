package fr.uge.patchwork.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe dédiée à la lecture d'un fichier contenant toutes les pièces du jeu à interpréter.
 * @param path : chemin du fichier à lire
 */
public record ReadFile(Path path) {
    /**
     * Lit le fichier et retourne une liste de pièces.
     * @return : une liste de pièces
     * @throws FileNotFoundException : si le fichier n'est pas trouvé
     */
    public PieceList read() throws FileNotFoundException {
        var pieces = new PieceList();
        File file = new File(path.toUri());
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] data = line.split(" ");
            var schema = new ArrayList<ArrayList<Boolean>>();

            while (data[0].equals("o") || data[0].equals("x")) {
                var row = new ArrayList<Boolean>();
                for (String datum : data) {
                    if (datum.equals("x")) {
                        row.add(true);
                    } else {
                        row.add(false);
                    }
                }
                schema.add(row);

                line = sc.nextLine();
                data = line.split(" ");
            }

            if(isParsableToInt(data[0])){
                int cost = Integer.parseInt(data[0]);
                int time = Integer.parseInt(data[1]);
                int button = Integer.parseInt(data[2]);

                pieces.addPiece(new Piece(schema, cost, time, button));
            }
        }
        return pieces;
    }

    /**
     * Vérifie si une chaîne de caractères est convertible en entier.
     * @param s : chaîne de caractères à vérifier
     * @return : true si la chaîne est convertible en entier, false sinon.
     */
    public boolean isParsableToInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
