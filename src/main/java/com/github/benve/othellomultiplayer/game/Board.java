package com.github.benve.othellomultiplayer.game;

import java.util.UUID;


/**
 *
 * @author lemad85
 */
public class Board {

    UUID [][] board;
    int height;
    int width;
    
    public Board(){
        board = new UUID[10][10];
        height = 10;
        width = 10;
        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                board[i][j] = null;
    }
            
    public Board(int x, int y){
        width = x;
        height = y;
        board = new UUID[x][y];
        for(int i=0;i<x;i++)
            for(int j=0;j<y;j++)
                board[i][j] = null;        
    }
    
    public UUID getStatus(int x,int y){
        return board[x][y];
    }
    
    public void setStatus(int x, int y, UUID playerID){
        board[x][y] = playerID;
    }
    

    public boolean compareStatus(int x,int y, UUID playerID){
        boolean result = true;
        if (playerID.compareTo(board[x][y]) !=0)
            result = false;
        return result;
    }
    
    /*
     * colonizeBoard: prende in input due interi che rappresentano la coordinata di una casella e
     * ritorna un booleano che indica se e' possibile colonizzare quella casella in quanto adiacente
     * a una delle pedine del giocatore
     */

    public boolean canColonize(int x1, int y1, UUID currPlayer){
        boolean result = true;
        int hi,hj,wi,wj,pedine;
        pedine = 0;
        if(this.getStatus(x1, y1) == null){
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
                   if(this.board[i][j] != null && currPlayer.toString().equals((this.board[i][j]).toString()))
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
     * getSingleReversi: prende in input una coordinata e lo UUID del giocatore e una
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

    public void getSingleReversi(int x1, int y1, UUID currPlayer, int[][] mosseReversi){
        //int[][] mosseReversi = new int[height][width];
        int i,j,dist;
        boolean find;
        
        for(i=0;i<height;i++)
            for(j=0;j<width;j++)
                mosseReversi[i][j] = -1;
        
        mosseReversi[x1][y1] = 0;

        if(board[x1][y1] != null)
            return;

        //nord: y fissa x decrescente da x1-1 a 0
        find = false;
        dist = 0;

        for(i=x1-1;i>=0;i--)
            if(board[i][y1] != null){
                if(board[i][y1].compareTo(currPlayer) != 0){
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
                mosseReversi[x1-1-i][y1] = 0;

        //sud: y fissa x cresente da x1+1 a height-1
        find = false;
        dist = 0;

        for(i=x1+1;i<height;i++)
            if(board[i][y1] != null){
                if(board[i][y1].compareTo(currPlayer) != 0){
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
                mosseReversi[x1+1+i][y1] = 0;

        //est: x fissa y crescente da y1+1 a width-1

        find = false;
        dist = 0;
        for(i=y1+1;i<width;i++)
            if(board[x1][i] != null){
                if(board[x1][i].compareTo(currPlayer) != 0){
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
                mosseReversi[x1][y1+1+i] = 0;

        //ovest: x fissa y decrescente da y1-1 a 0

        find = false;
        dist = 0;
        for(i=y1-1;i>=0;i--)
            if(board[x1][i] != null){
                if(board[x1][i].compareTo(currPlayer) != 0){
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
                mosseReversi[x1][y1-1-i] = 0;
        
        //nord-est: x decrescente da x1-1 a 0 y crescente da y1+1 a width-1
        find=false;
        dist=0;

        i = x1-1; j = y1+1;
        
        while(i>=0 && j < width){
            if(board[i][j] != null){
                if(board[i][j].compareTo(currPlayer) != 0){
                    find = false;dist++;
                    i--;j++;
                }
                else{
                    find = true;System.out.println("nord-est");break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1-1-i][y1+1+i] = 0;


        //sud-est: x crescente da x1+1 a height-1 y crescente da y1+1 a width-1
        find=false;
        dist=0;

        i = x1+1; j = y1+1;
        while(i<height && j < width){
            if(board[i][j] != null){
                if(board[i][j].compareTo(currPlayer) != 0){
                    find = false;dist++;
                    i++;j++;
                }
                else{
                    find = true;System.out.println("sud-est");break;
                }
            }else{
                dist = 0; find = false;
                break;
            }            
        }
        
        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1+1+i][y1+1+i] = 0;
     
        //sud-ovest: x crescente da x1+1 a height-1 y decrescente da y1-1 a 0
        find=false;
        dist=0;

        i = x1+1; j = y1-1;
        while(i<height && j >= 0){
            if(board[i][j] != null){
                if(board[i][j].compareTo(currPlayer) != 0){
                    find = false;dist++;
                    i++;j--;
                }
                else{
                    find = true;System.out.println("sud-ovest");break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1+1+i][y1-1-i] = 0;

        //nord-ovest: x decrescente da x1-1 a 0 y decrescente da y1-1 a 0
        find=false;
        dist=0;

        i = x1-1; j = y1-1;
        while(i >= 0 && j >= 0){
            if(board[i][j] != null){
                if(board[i][j].compareTo(currPlayer) != 0){
                    find = false;dist++;
                    i--;j--;
                }
                else{
                    find = true;System.out.println("nord-ovest");break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[x1-1-i][y1-1-i] = 0;
     
        return;
    }


    /*
     * hasReversi: Prende in input lo UUID del giocatore e ritorna un booleano che indica se sulla tavolozza
     * e' possibile effettuare una mossa di reversi. Viene effettuata una scansione delle caselle vuote della
     * tavolozza e in corrispondenza anche di una sola mossa di reversi viene ritornato true;
     * */

    public boolean hasReversi(UUID currentPlayer){
        boolean result = false;
        int [][] reversi = new int[height][width];
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                if(this.board[i][j] == null){
                    this.getSingleReversi(i, j, currentPlayer, reversi);
                    if(this.canReversi(reversi)){
                        result = true;
                        System.out.println("x: "+i+" y: "+j);
                        break;
                        //return result;
                    }
                }
            }

        return result;
    }


    /*
     * getAllReversi:: Prend in input lo UUID del giocatore e ritrona una matrice all'interno della quale
     * e' presente la mappa di tutte le possibili caselle sulle quali il giocatore puo' effettuare una mossa
     * di reversi.
     *
     * il significato dei valori della matrice e' il seguente
     *  0       Sulla casella e' possibile una mossa di reversi valida
     *  -1      Sulla casella non e' possibile effettuare una mossa di reversi
     *
     */

    public void getAllReversi(int[][] mosseReversi, UUID currPlayer){
        int[][] caselleMossa = new int[height][width];

        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                if(this.board[i][j] == null){
                    this.getSingleReversi(i, j, currPlayer, caselleMossa);
                    if(this.canReversi(caselleMossa)){
                        mosseReversi[i][j] = 0;
                    }else
                        mosseReversi[i][j] = -1;
                }else
                    mosseReversi[i][j] = -1;
            }
    }

    /*
     * canReversi: prende in input una matrice mossa risultato di getSingleReversi e valuta se il risultato
     * al suo interno contiene una mossa reversi valida
     * 
     */

    public boolean canReversi(int[][] mossaReversi){
        boolean result;
        if(this.sommatoria(mossaReversi) > ((height*width*-1)+1))
            result = true;
        else
            result = false;
        return result;

    }

    /*
     *  moveReversi: metodo che data la matrice di una mossa e il giocatore che deve giocare cambia lo
     *  stato della tavolozza mettendo i nuovi valori alle caselle corrette. 
     */
    public void moveReversi(int[][] mossaReversi, UUID currentPlayer){
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                if(mossaReversi[i][j] != -1){
                    this.setStatus(i, j, currentPlayer);
                }
            }
    }

    public void colonizeField(int x1, int y1, UUID currenPlayer){
        int[][] mossaColonizza = new int[height][width];
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                mossaColonizza[i][j] = -1;
        mossaColonizza[x1][y1] = 0;
        this.moveReversi(mossaColonizza, currenPlayer);
    }

   public boolean hasColonize(UUID currentPlayer){
       boolean result = false;
       for(int i=0;i<height;i++)
           for(int j=0;j<width;j++)
               if(this.canColonize(i, j, currentPlayer)){
                        result = true;
                        System.out.println("x: "+i+" y: "+j);
                        break;
               }
       return result;
   }

   public void getAllColonize(int[][] allColonize,UUID currentPlayer){
       for(int i=0;i<height;i++)
           for(int j=0;j<width;j++)
               if(this.canColonize(i, j, currentPlayer))
                   allColonize[i][j] = 0;
               else
                   allColonize[i][j] = -1;                   
   }

   public void printBoard(){
        for(int i=0;i<height;i++){
            System.out.flush();
            System.out.print("|");
            for(int j=0;j<width;j++){
                System.out.flush();
                if(board[i][j]!= null)
                   System.out.print(board[i][j].toString().charAt(0) +"|");
                else
                    System.out.print(" |");
            }
            System.out.print("\n");
        }
    }

    private int sommatoria(int[][] mossa){
        int total = 0;
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                total += mossa[i][j];
        return total;
    }
}
