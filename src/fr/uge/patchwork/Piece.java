package fr.uge.patchwork;

import java.util.ArrayList;

public record Piece(ArrayList<ArrayList<Boolean>> schema, int cost, int time, int button) {

    //compte le nombre de true dans la piece (son nombre de carré en gros)
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
