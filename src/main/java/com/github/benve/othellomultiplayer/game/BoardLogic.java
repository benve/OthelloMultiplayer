package com.github.benve.othellomultiplayer.game;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 31/05/11
 * Time: 10.22
 * To change this template use File | Settings | File Templates.
 */
public class BoardLogic {
    private static BoardLogic instance;

    private BoardLogic(){}

    public static BoardLogic getInstance(){
        if(instance == null)
            instance = new BoardLogic();
        return instance;
    }

    /*
     * colonizeBoard: prende in input due interi che rappresentano la coordinata di una casella e
     * ritorna un booleano che indica se e' possibile colonizzare quella casella in quanto adiacente
     * a una delle pedine del giocatore
     */

    public boolean canColonize(Board b1, int x1, int y1, int currPlayer){
        boolean result = true;
        int hi,hj,wi,wj,pedine;
        pedine = 0;
        if(b1.getStatus(x1, y1) == -1){
            if(x1==0){
                hi = 0;hj= x1+1;
            }else if(x1==b1.getHeight()-1){
                hi = x1-1;hj = x1;
            }else{
                hi = x1-1;hj = x1+1;
            }

            if(y1==0){
                wi = 0;wj= y1+1;
            }else if(y1==b1.getWidth()-1){
                wi = y1-1;wj = y1;
            }else{
                wi = y1-1;wj = y1+1;
            }

            for(int i = hi;i<= hj; i++)
                for(int j = wi; j<=wj; j++){
                   if(b1.getStatus(i,j) != -1 && currPlayer == b1.getStatus(i,j))
                       pedine++;
                }
            if(pedine==0)
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
     * f f f f
     * f f f f
     * f f t f
     * f f f t
     * </pre>
     *
     */

    public boolean[][] getSingleReversi(Board b1, int x1, int y1, int currPlayer){
        boolean[][] mosseReversi = new boolean[b1.getHeight()][b1.getWidth()];
        int i,j,dist;
        boolean find;

        for(i=0;i<b1.getHeight();i++)
            for(j=0;j<b1.getWidth();j++)
                mosseReversi[i][j] = false;

        mosseReversi[x1][y1] = true;

        if(b1.getStatus(x1,y1) != -1)
            return mosseReversi;

        //nord: y fissa x decrescente da x1-1 a 0
        find = false;
        dist = 0;

        for(i=x1-1;i>=0;i--)
            if(b1.getStatus(i,y1) != -1){
                if(b1.getStatus(i,y1) != currPlayer){
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

        for(i=x1+1;i<b1.getHeight();i++)
            if(b1.getStatus(i,y1) != -1){
                if(b1.getStatus(i,y1) != currPlayer){
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
        for(i=y1+1;i<b1.getWidth();i++)
            if(b1.getStatus(x1,i) != -1){
                if(b1.getStatus(x1,i) != currPlayer){
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
            if(b1.getStatus(x1,i) != -1){
                if(b1.getStatus(x1,i) != currPlayer){
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

        while(i>=0 && j < b1.getWidth()){
            if(b1.getStatus(i,j) != -1){
                if(b1.getStatus(i,j) != currPlayer){
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
        while(i<b1.getHeight() && j < b1.getWidth()){
            if(b1.getStatus(i,j) != -1){
                if(b1.getStatus(i,j) != currPlayer){
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
        while(i<b1.getHeight() && j >= 0){
            if(b1.getStatus(i,j) != -1){
                if(b1.getStatus(i,j) != currPlayer){
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
            if(b1.getStatus(i,j) != -1){
                if(b1.getStatus(i,j) != currPlayer){
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

    public boolean hasReversi(Board b1, int currentPlayer){
        boolean result = false;
        boolean [][] reversi = new boolean[b1.getHeight()][b1.getWidth()];
        for(int i=0;i<b1.getHeight();i++)
            for(int j=0;j<b1.getWidth();j++){
                if(b1.getStatus(i,j) == -1){
                    reversi = this.getSingleReversi(b1, i, j, currentPlayer);
                    if(this.canReversi(b1.getHeight(), b1.getWidth(), reversi)){
                        result = true;
                        return result;
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
     *  true       Sulla casella e' possibile una mossa di reversi valida
     *  false      Sulla casella non e' possibile effettuare una mossa di reversi
     *
     */

    public boolean[][] getAllReversi(Board b1, int currPlayer){
        boolean[][] mosseReversi = new boolean[b1.getHeight()][b1.getWidth()];
        for(int i=0;i<b1.getHeight();i++)
            for(int j=0;j<b1.getWidth();j++){
                if(b1.getStatus(i,j) == -1){
                    if(this.canReversi(b1.getHeight(), b1.getWidth(), this.getSingleReversi(b1, i, j, currPlayer))){
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

    public boolean canReversi(int height, int width, boolean[][] mossaReversi){
        boolean result;
        if(this.orMatrice(height, width, mossaReversi))
            result = true;
        else
            result = false;
        return result;

    }

    /*
     *  moveReversi: metodo che data la matrice di una mossa e il giocatore che deve giocare cambia lo
     *  stato della tavolozza mettendo i nuovi valori alle caselle corrette.
     */
    public void moveReversi(Board b1, boolean[][] mossaReversi, int currentPlayer){
        for(int i=0;i<b1.getHeight();i++)
            for(int j=0;j<b1.getWidth();j++){
                if(mossaReversi[i][j]){
                    b1.setStatus(i, j, currentPlayer);
                }
            }
    }

    public void colonizeField(Board b1, int x1, int y1, int currenPlayer){
        b1.setStatus(x1,y1,currenPlayer);
    }

   public boolean hasColonize(Board b1, int currentPlayer){
       boolean result = false;
       for(int i=0;i<b1.getHeight();i++)
           for(int j=0;j<b1.getWidth();j++)
               if(this.canColonize(b1, i, j, currentPlayer)){
                        result = true;
                        break;
               }
       return result;
   }

   public boolean[][] getAllColonize(Board b1, int currentPlayer){
       boolean[][] allColonize = new boolean[b1.getHeight()][b1.getWidth()];
       for(int i=0;i<b1.getHeight();i++)
           for(int j=0;j<b1.getWidth();j++)
               if(this.canColonize(b1, i, j, currentPlayer))
                   allColonize[i][j] = true;
               else
                   allColonize[i][j] = false;

       return allColonize;
   }

   public void printBoard(Board b1){
        for(int i=0;i<b1.getHeight();i++){
            System.out.flush();
            System.out.print("|");
            for(int j=0;j<b1.getWidth();j++){
                System.out.flush();
                if(b1.getStatus(i,j)!= -1)
                   System.out.print(b1.getStatus(i,j)+"|");
                else
                   System.out.print(" |");
            }
            System.out.print("\n");
        }
    }


    /*
    Si effettua una valutazione cortocircuitata dell'or sulla matrice in modo tale che alla prima
    occorrenza di un true la funzione possa terminare la valutazione
     */
    private boolean orMatrice(int height, int width, boolean[][] mossa){
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                if(mossa[i][j])
                    return true;
        return false;
    }
}

