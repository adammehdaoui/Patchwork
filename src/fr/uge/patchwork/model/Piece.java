package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class dedicated to the representation of a piece (or a "patch" of the game).
 * @param schema : diagram of the piece
 * @param cost : cost of the piece
 * @param time : time to place the piece
 * @param button : presence of a button on the piece
 */
public record Piece(ArrayList<ArrayList<Boolean>> schema, int cost, int time, int button) {

    /**
     * Count the number of "true" in the piece's diagram.
     * @return : number of "true" in the piece's diagram
     */
    public int countTrue(){
        int res = 0;
        for (ArrayList<Boolean> booleans : schema) {
            for (boolean aBoolean : booleans) {
                if(aBoolean){
                    res++;
                }
            }
        }
        return res;
    }

    /**
     * Returns a new piece corresponding to the rotation of the current piece.
     * @return : a new piece corresponding to the rotation of the current piece
     */
    public Piece rotate() {
        ArrayList<ArrayList<Boolean>> newSchema = new ArrayList<>();
        for (int i = 0; i < schema.get(0).size(); i++) {
            ArrayList<Boolean> newLine = new ArrayList<>();
            for (int j = schema.size() - 1; j >= 0; j--) {
                newLine.add(schema.get(j).get(i));
            }
            newSchema.add(newLine);
        }
        return new Piece(newSchema, cost, time, button);
    }

    /**
     * Returns a new piece corresponding to the inversion of the current piece.
     * @return : a new piece corresponding to the inversion of the current piece
     */
    public Piece invert() {
        ArrayList<ArrayList<Boolean>> newSchema = new ArrayList<>();
        for (ArrayList<Boolean> booleans : schema) {
            ArrayList<Boolean> newLine = new ArrayList<>();
            for (int j = booleans.size() - 1; j >= 0; j--) {
                newLine.add(booleans.get(j));
            }
            newSchema.add(newLine);
        }
        return new Piece(newSchema, cost, time, button);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (ArrayList<Boolean> booleans : schema) {
            for (boolean aBoolean : booleans) {
                sb.append(aBoolean ? "■" : "□");
            }
            sb.append("\n");
        }

        sb.append("coût : ").append(cost).append("\n");
        sb.append("temps : ").append(time).append("\n");
        sb.append("bouton(s) : ").append(button).append("\n");

        return sb.toString();
    }
}
