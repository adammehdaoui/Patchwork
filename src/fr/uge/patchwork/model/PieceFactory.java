package fr.uge.patchwork.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class dedicated to the reading of a file containing all the pieces of the game to interpret.
 * @param path : path of the file to read
 */
public record PieceFactory(Path path) {

    /**
     * Constructor of the class.
     * @param path path of the file to read
     */
    public PieceFactory {
        Objects.requireNonNull(path);
    }

    /**
     * Read the file and return a list of pieces.
     * @param pieces list of pieces to fill
     * @throws IOException error while reading the file
     */
    public void read(PieceSet pieces) throws IOException {
        String line;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path.toString());

        if (inputStream == null) {
            throw new IOException("File could not been opened");
        }

        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String[] data = scanner.nextLine().split(" ");
            var schema = new ArrayList<ArrayList<Boolean>>();

            while(data[0].equals("o") || data[0].equals("x")){
                var row = new ArrayList<Boolean>();
                for (String datum : data) {
                    if (datum.equals("x")) {
                        row.add(true);
                    } else {
                        row.add(false);
                    }
                }
                schema.add(row);

                line = scanner.nextLine();
                data = line.split(" ");
            }

            if(isParsableToInt(data[0])){
                int cost = Integer.parseInt(data[0]);
                int time = Integer.parseInt(data[1]);
                int button = Integer.parseInt(data[2]);

                pieces.addPiece(new Piece(schema, cost, time, button));
            }
        }
    }

    /**
     * Check if a string is convertible to integer.
     * @param s string to check
     * @return true if the string is convertible to integer, false otherwise
     */
    public boolean isParsableToInt(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
