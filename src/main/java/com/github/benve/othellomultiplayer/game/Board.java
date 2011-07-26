package com.github.benve.othellomultiplayer.game;

import java.io.Serializable;

/**
 *
 * @author lemad85
 */
public class Board implements Serializable {

    public int [][] board;
    public final int side;

    /**
     * Giocatore corrente
     */
    public int currP = 0;

    //Solo per test
    protected void setBoard(int [][] b) {
        assert b.length == side;
        assert b[0].length == side;
        board = b;
    }
    
    public Board(){
        side = 4;
        board = new int[side][side];

        for(int i=0;i<side;i++)
            for(int j=0;j<side;j++)
                board[i][j] = -1;
    }
            
    public Board(int side){
        this.side = side;
        board = new int[side][side];
        for(int i=0;i<side;i++)
            for(int j=0;j<side;j++)
                board[i][j] = -1;
    }

    public void initRandomBoard(PlayerList playerList) {
           //Aggiungo giocatori dandogli pedine casuali
        for (int i = 0; i < playerList.size(); i++) {
                int x = (int) (Math.random()*(side - 1));
                int y = (int) (Math.random()*(side - 1));
                while (board[x][y] != -1) {
                    x = (int) (Math.random()*(side - 1));
                    y = (int) (Math.random()*(side - 1));
                }
                board[x][y] = playerList.get(i).getUuid();
        }
    }
    
    public int getStatus(int r,int c){
        return board[r][c];
    }
    
    public void setStatus(int r, int c, int playerID){
        board[r][c] = playerID;
    }
    

    public boolean compareStatus(int r,int c, int playerID){
        boolean result = true;
        if (playerID != board[r][c])
            result = false;
        return result;
    }

    public int getRow(){
        return this.side;
    }

    public int getColumn(){
        return this.side;
    }


}
