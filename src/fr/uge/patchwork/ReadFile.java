package fr.uge.patchwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public record ReadFile(Path path) {
    //fonction qui lit un fichier txt et renvoie un ArrayList de piece
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

    public boolean isParsableToInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
