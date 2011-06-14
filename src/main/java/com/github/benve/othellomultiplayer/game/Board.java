package com.github.benve.othellomultiplayer.game;

import java.io.Serializable;

/**
 *
 * @author lemad85
 */
public class Board implements Serializable{

    public int [][] board;
    public final int row;
    public final int column;

    /**
     * Giocatore corrente
     */
    public int currP;

    //Solo per test
    protected void setBoard(int [][] b) {
        assert b.length == column;
        assert b[0].length == row;
        board = b;
    }
    
    public Board(){
        board = new int[10][10];
        row = 10;
        column = 10;
        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                board[i][j] = -1;
    }
            
    public Board(int x, int y){
        column = x;
        row = y;
        board = new int[x][y];
        for(int i=0;i<x;i++)
            for(int j=0;j<y;j++)
                board[i][j] = -1;
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
        return this.row;
    }

    public int getColumn(){
        return this.column;
    }


}
