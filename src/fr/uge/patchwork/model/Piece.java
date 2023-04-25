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
}
