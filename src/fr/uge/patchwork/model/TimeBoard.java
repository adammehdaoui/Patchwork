package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class dedicated to the representation of the game board ("TimeBoard").
 */
public class TimeBoard {

    private final ArrayList<Cell> cells;
    final static int nbCases = 54;

    /* Storing here the last player who played */
    private final Player priorityPlayer;

    /**
     * Constructor of the class TimeBoard.
     * @param player1 : player 1
     * @param player2 : player 2
     */
    public TimeBoard(Player player1, Player player2) {
        cells = new ArrayList<>();

        /* Creating the cells and placing the buttons every 6 cells */
        for(int i=0; i<nbCases; i++){
            cells.add(new Cell());

            if(i % 6 == 5){
                cells.get(i).setButton(true);
            }
        }

        /* Positioning the players on the first cell of the TimeBoard (beginning of the game) */
        cells.get(0).setPlayer(player1);
        cells.get(0).setPlayer(player2);

        priorityPlayer = player1;
    }

    /**
     * Returns the priority player (the last player who played).
     * @return : priority player
     */
    public Player getPriorityPlayer(){
        return priorityPlayer;
    }

    /**
     * Moves the player according to the number of cells indicated.
     * @param player : player to move
     * @param move : number of cells to move
     * @return : number of buttons between the old and the new position of the player
     * @throws IllegalArgumentException : argument player not found in the main board
     */
    public int movePlayer(Player player, int move) throws IllegalArgumentException {
        int index = -1;
        int moveTo;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).getPlayer(player) != null){
                index = i;
                break;
            }
        }

        if(index<0){
            throw new IllegalArgumentException("Le joueur n'a pas été trouvé dans le board principal");
        }

        cells.get(index).freePlayer(player);

        if(index+move >= nbCases){
            moveTo=nbCases-1;
        }
        else{
            moveTo=index+move;
        }

        cells.get(moveTo).setPlayer(player);

        return nbButton(index, moveTo);
    }

    /**
     * Returns the number of buttons between two cells.
     * @param start : starting cell
     * @param end : ending cell
     * @return : number of buttons between the two cells
     */
    public int nbButton(int start, int end){
        int res = 0;
        for(int i=start; i<end; i++){
            if(cells.get(i).button()){
                res++;
            }
        }
        return res;
    }

    /**
     * Returns the distance between the two players.
     * @return : distance between the two players
     */
    public int distance(){
        int posP1 = 0;
        int posP2 = 0;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1() != null){
                posP1 = i;
            }
            if(cells.get(i).player2() != null){
                posP2 = i;
            }
        }

        return Math.abs(posP2 - posP1);
    }

    /**
     * Returns true if the two players are on the last cell.
     * @return : true if the two players are on the last cell
     */
    public boolean endGame(){
        return cells.get(nbCases - 1).player1() != null && cells.get(nbCases - 1).player2() != null;
    }

    /**
     * Returns the player who must play.
     * @return : player who must play
     */
    public int turnOf(){
        int posP1 = 0;
        int posP2 = 0;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1() != null){
                posP1 = i;
            }
            if(cells.get(i).player2() != null){
                posP2 = i;
            }
        }

        if(posP1 == posP2){
            return priorityPlayer.getId();
        }
        else if(posP1 < posP2){
            return 1;
        }
        else{
            return 2;
        }
    }

    @Override
    public String toString(){
        int i=1;
        StringBuilder sb = new StringBuilder();

        sb.append("TimeBoard :\n------------------------------\n");

        for(Cell cell : cells){
            sb.append("|");

            if(cell.button()){
                sb.append("°");
            }
            else{
                sb.append(" ");
            }

            if(cell.player1() != null){
                sb.append("1");
            }
            else{
                sb.append(" ");
            }

            if(cell.player2() != null){
                sb.append("2");
            }
            else{
                sb.append(" ");
            }

            sb.append("|");

            if(i%6 == 0 && i!=0){
                sb.append("\n").append("------------------------------").append("\n");
            }

            i++;
        }

        return sb.toString();
    }

}
