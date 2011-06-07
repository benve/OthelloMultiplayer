package com.github.benve.othellomultiplayer.game;

import java.io.Serializable;

/**
 *
 * @author lemad85
 */
public class Board implements Serializable{

    public int [][] board;
    public final int height;
    public final int width;

    //Solo per test
    protected void setBoard(int [][] b) {
        assert b.length == width;
        assert b[0].length == height;
        board = b;
    }
    
    public Board(){
        board = new int[10][10];
        height = 10;
        width = 10;
        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                board[i][j] = -1;
    }
            
    public Board(int x, int y){
        width = x;
        height = y;
        board = new int[x][y];
        for(int i=0;i<x;i++)
            for(int j=0;j<y;j++)
                board[i][j] = -1;
    }
    
    public int getStatus(int x,int y){
        return board[x][y];
    }
    
    public void setStatus(int x, int y, int playerID){
        board[x][y] = playerID;
    }
    

    public boolean compareStatus(int x,int y, int playerID){
        boolean result = true;
        if (playerID != board[x][y])
            result = false;
        return result;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }


}
