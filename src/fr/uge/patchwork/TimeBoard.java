package fr.uge.patchwork;

import java.util.ArrayList;

public class TimeBoard {

    private final ArrayList<Cell> cells;
    final static int nbCases = 54;

    public TimeBoard() {
        cells = new ArrayList<>();

        for(int i=0; i<nbCases; i++){
            cells.add(new Cell());

            if(i%6==0 && i!=0){
                cells.get(i).setButton(true);
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("--".repeat(cells.size()+1)).append("\n");

        sb.append("|");

        for(Cell cell : cells){
            if(cell.button()){
                sb.append("O");
            }
            else{
                sb.append(" ");
            }

            sb.append("|");
        }



        sb.append("\n").append("--".repeat(cells.size()+1));

        return sb.toString();
    }

}
