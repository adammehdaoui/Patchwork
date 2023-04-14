package fr.uge.patchwork;

import java.util.ArrayList;

public class TimeBoard {

    private final ArrayList<Cell> cells;
    final static int nbCases = 54;

    public TimeBoard() {
        cells = new ArrayList<>();

        for(int i=0; i<nbCases; i++){
            cells.add(new Cell());

            if(i%6==5 && i>5){
                cells.get(i).setButton(true);
            }
        }
        cells.get(5).setButton(true);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

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

        return sb.toString();
    }

}
