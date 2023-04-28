package fr.uge.patchwork.model;

import java.util.ArrayList;

public record Piece(ArrayList<ArrayList<Boolean>> schema, int cost, int time, int button) {

    /* Compte le nombre de valeurs à true dans la pièce : permet de déterminer sa forme
    et les positions qu'elle peut prendre */
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (ArrayList<Boolean> booleans : schema) {
            for (boolean aBoolean : booleans) {
                sb.append(aBoolean ? "■" : "□");
            }
            sb.append("\n");
        }

        sb.append("cost: ").append(cost).append("\n");
        sb.append("time: ").append(time).append("\n");
        sb.append("button: ").append(button).append("\n");

        return sb.toString();
    }

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
}
