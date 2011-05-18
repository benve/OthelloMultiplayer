package com.github.benve.othellomultiplayer.game;

/**
 *
 * @author lemad85
 */
public class Board {

    public int [][] board;
    int height;
    int width;
    
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
    
    /*
     * colonizeBoard: prende in input due interi che rappresentano la coordinata di una casella e
     * ritorna un booleano che indica se e' possibile colonizzare quella casella in quanto adiacente
     * a una delle pedine del giocatore
     */

    public boolean canColonize(int x1, int y1, int currPlayer){
        boolean result = true;
        int hi,hj,wi,wj,pedine;
        pedine = 0;
        if(this.getStatus(x1, y1) == -1){
            if(x1==0){
                hi = 0;hj= x1+1;
            }else if(x1==this.height-1){
                hi = x1-1;hj = x1;
            }else{
                hi = x1-1;hj = x1+1;
            }
            
            if(y1==0){
                wi = 0;wj= y1+1;
            }else if(y1==this.width-1){
                wi = y1-1;wj = y1;
            }else{
                wi = y1-1;wj = y1+1;
            }
            
            for(int i = hi;i<= hj; i++)
                for(int j = wi; j<=wj; j++){
                   if(this.board[i][j] != -1 && currPlayer == this.board[i][j])
                       pedine++;
                }
            if(pedine==0)
                //this.setStatus(x1, y1, currPlayer);
            //else
                result = false;
        }else
            result = false;
        
        return result;
    }

    /*
     * getSingleReversi: prende in input una coordinata e lo int del giocatore e una
     * matrice sulla quale verranno salvate le pedine che posso mangiare con quella mossa.
     *
     * Es.
     * Stato blobale
     * <pre>
     * 0 0 0 0
     * 0 1 0 0
     * 0 0 2 0
     * 0 0 0 0
     * </pre>
     *
     * Matrice risultato
     * <pre>
     * -1 -1 -1 -1
     * -1 -1 -1 -1
     * -1 -1 0 -1
     * -1 -1- -1 0
     * </pre>
     *
     */

    public boolean[][] getSingleReversi(int x1, int y1, int currPlayer){
        boolean[][] mosseReversi = new boolean[height][width];
        int i,j,dist;
        boolean find;
        
        for(i=0;i<height;i++)
            for(j=0;j<width;j++)
                mosseReversi[i][j] = false;
        
        mosseReversi[x1][y1] = true;

        if(board[x1][y1] != -1)
            return mosseReversi;

        //nord: y fissa x decrescente da x1-1 a 0
        find = false;
        dist = 0;

        for(i=x1-1;i>=0;i--)
            if(board[i][y1] != -1){
                if(board[i][y1] != currPlayer){
                    dist++;find = false;
                }else{
                    find = true;break;
                }
            }else{
                dist = 0;find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1-1-i][y1] = true;

        //sud: y fissa x cresente da x1+1 a height-1
        find = false;
        dist = 0;

        for(i=x1+1;i<height;i++)
            if(board[i][y1] != -1){
                if(board[i][y1] != currPlayer){
                    dist++;find = false;
                }else{
                    find = true;break;
                }
            }else{
                dist = 0;find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1+1+i][y1] = true;

        //est: x fissa y crescente da y1+1 a width-1

        find = false;
        dist = 0;
        for(i=y1+1;i<width;i++)
            if(board[x1][i] != -1){
                if(board[x1][i] != currPlayer){
                    find = false;dist++;
                }
                else{
                    find = true; break;
                }
            }else{
                dist = 0;find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1][y1+1+i] = true;

        //ovest: x fissa y decrescente da y1-1 a 0

        find = false;
        dist = 0;
        for(i=y1-1;i>=0;i--)
            if(board[x1][i] != -1){
                if(board[x1][i] != currPlayer){
                    find = false;dist++;
                }
                else{
                    find = true;break;
                }
            }else{
                dist = 0; find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1][y1-1-i] = true;
        
        //nord-est: x decrescente da x1-1 a 0 y crescente da y1+1 a width-1
        find=false;
        dist=0;

        i = x1-1; j = y1+1;
        
        while(i>=0 && j < width){
            if(board[i][j] != -1){
                if(board[i][j] != currPlayer){
                    find = false;dist++;
                    i--;j++;
                }
                else{
                    find = true;break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1-1-i][y1+1+i] = true;


        //sud-est: x crescente da x1+1 a height-1 y crescente da y1+1 a width-1
        find=false;
        dist=0;

        i = x1+1; j = y1+1;
        while(i<height && j < width){
            if(board[i][j] != -1){
                if(board[i][j] != currPlayer){
                    find = false;dist++;
                    i++;j++;
                }
                else{
                    find = true;break;
                }
            }else{
                dist = 0; find = false;
                break;
            }            
        }
        
        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1+1+i][y1+1+i] = true;
     
        //sud-ovest: x crescente da x1+1 a height-1 y decrescente da y1-1 a 0
        find=false;
        dist=0;

        i = x1+1; j = y1-1;
        while(i<height && j >= 0){
            if(board[i][j] != -1){
                if(board[i][j] != currPlayer){
                    find = false;dist++;
                    i++;j--;
                }
                else{
                    find = true;break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1+1+i][y1-1-i] = true;

        //nord-ovest: x decrescente da x1-1 a 0 y decrescente da y1-1 a 0
        find=false;
        dist=0;

        i = x1-1; j = y1-1;
        while(i >= 0 && j >= 0){
            if(board[i][j] != -1){
                if(board[i][j] != currPlayer){
                    find = false;dist++;
                    i--;j--;
                }
                else{
                    find = true;break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1-1-i][y1-1-i] = true;
     
        return mosseReversi;
    }


    /*
     * hasReversi: Prende in input lo int del giocatore e ritorna un booleano che indica se sulla tavolozza
     * e' possibile effettuare una mossa di reversi. Viene effettuata una scansione delle caselle vuote della
     * tavolozza e in corrispondenza anche di una sola mossa di reversi viene ritornato true;
     * */

    public boolean hasReversi(int currentPlayer){
        boolean result = false;
        boolean [][] reversi = new boolean[height][width];
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                if(this.board[i][j] == -1){
                    reversi = this.getSingleReversi(i, j, currentPlayer);
                    if(this.canReversi(reversi)){
                        result = true;
                        break;
                        //return result;
                    }
                }
            }

        return result;
    }


    /*
     * getAllReversi:: Prend in input lo int del giocatore e ritrona una matrice all'interno della quale
     * e' presente la mappa di tutte le possibili caselle sulle quali il giocatore puo' effettuare una mossa
     * di reversi.
     *
     * il significato dei valori della matrice e' il seguente
     *  0       Sulla casella e' possibile una mossa di reversi valida
     *  -1      Sulla casella non e' possibile effettuare una mossa di reversi
     *
     */

    public boolean[][] getAllReversi(int currPlayer){
        boolean[][] mosseReversi = new boolean[height][width];
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                if(this.board[i][j] == -1){
                    if(this.canReversi(this.getSingleReversi(i, j, currPlayer))){
                        mosseReversi[i][j] = true;
                    }else
                        mosseReversi[i][j] = false;
                }else
                    mosseReversi[i][j] = false;
            }

        return mosseReversi;
    }

    /*
     * canReversi: prende in input una matrice mossa risultato di getSingleReversi e valuta se il risultato
     * al suo interno contiene una mossa reversi valida
     * 
     */

    public boolean canReversi(boolean[][] mossaReversi){
        boolean result;
        if(this.orMatrice(mossaReversi))
            result = true;
        else
            result = false;
        return result;

    }

    /*
     *  moveReversi: metodo che data la matrice di una mossa e il giocatore che deve giocare cambia lo
     *  stato della tavolozza mettendo i nuovi valori alle caselle corrette. 
     */
    public void moveReversi(boolean[][] mossaReversi, int currentPlayer){
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                if(mossaReversi[i][j]){
                    this.setStatus(i, j, currentPlayer);
                }
            }
    }

    public void colonizeField(int x1, int y1, int currenPlayer){
        this.setStatus(x1,y1,currenPlayer);
    }

   public boolean hasColonize(int currentPlayer){
       boolean result = false;
       for(int i=0;i<height;i++)
           for(int j=0;j<width;j++)
               if(this.canColonize(i, j, currentPlayer)){
                        result = true;
                        break;
               }
       return result;
   }

   public boolean[][] getAllColonize(int currentPlayer){
       boolean[][] allColonize = new boolean[height][width];
       for(int i=0;i<height;i++)
           for(int j=0;j<width;j++)
               if(this.canColonize(i, j, currentPlayer))
                   allColonize[i][j] = true;
               else
                   allColonize[i][j] = false;

       return allColonize;
   }

   public void printBoard(){
        for(int i=0;i<height;i++){
            System.out.flush();
            System.out.print("|");
            for(int j=0;j<width;j++){
                System.out.flush();
                if(board[i][j]!= -1)
                   System.out.print(board[i][j]+"|");
                else
                    System.out.print(" |");
            }
            System.out.print("\n");
        }
    }

    private boolean orMatrice(boolean[][] mossa){
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                if(mossa[i][j])
                    return true;
        return false;
    }
}
