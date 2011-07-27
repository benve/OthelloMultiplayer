package com.github.benve.othellomultiplayer.game;

import com.github.benve.othellomultiplayer.network.MaxPlayerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean canColonize(Board b, int r, int c, int currPlayer){
        boolean result = true;
        int hi,hj,wi,wj,pedine;
        pedine = 0;
        if(b.getStatus(r, c) == -1){
            if(r==0){
                hi = 0;hj= r+1;
            }else if(r==b.getRow()-1){
                hi = r-1;hj = r;
            }else{
                hi = r-1;hj = r+1;
            }

            if(c==0){
                wi = 0;wj= c+1;
            }else if(c==b.getColumn()-1){
                wi = c-1;wj = c;
            }else{
                wi = c-1;wj = c+1;
            }

            for(int i = hi;i<= hj; i++)
                for(int j = wi; j<=wj; j++){
                   if(b.getStatus(i,j) != -1 && currPlayer == b.getStatus(i,j))
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
     * matrice sulla quale verranno salvate le pedine che posso mangiare con quella mossa
     * e se possono essere mangiate pedine la mossa stessa
     */
    public boolean[][] getSingleReversi(Board b, int r, int c, int currPlayer){
        boolean[][] mosseReversi = new boolean[b.getRow()][b.getColumn()];
        int i,j,dist;
        boolean find, gFind;
        gFind = false;

        for(i=0;i<b.getRow();i++)
            for(j=0;j<b.getColumn();j++)
                mosseReversi[i][j] = false;

        //mosseReversi[r][c] = true;

        if(b.getStatus(r,c) != -1)
            return mosseReversi;

        //nord: y fissa x decrescente da r-1 a 0
        find = false;
        dist = 0;

        for(i=r-1;i>=0;i--)
            if(b.getStatus(i,c) != -1){
                if(b.getStatus(i,c) != currPlayer){
                    dist++;find = false;
                }else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0;find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r-1-i][c] = true;

        //sud: y fissa x cresente da r+1 a row-1
        find = false;
        dist = 0;

        for(i=r+1;i<b.getRow();i++)
            if(b.getStatus(i,c) != -1){
                if(b.getStatus(i,c) != currPlayer){
                    dist++;find = false;
                }else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0;find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r+1+i][c] = true;

        //est: x fissa y crescente da c+1 a column-1

        find = false;
        dist = 0;
        for(i=c+1;i<b.getColumn();i++)
            if(b.getStatus(r,i) != -1){
                if(b.getStatus(r,i) != currPlayer){
                    find = false;dist++;
                }
                else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0;find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r][c+1+i] = true;

        //ovest: x fissa y decrescente da c-1 a 0

        find = false;
        dist = 0;
        for(i=c-1;i>=0;i--)
            if(b.getStatus(r,i) != -1){
                if(b.getStatus(r,i) != currPlayer){
                    find = false;dist++;
                }
                else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0; find = false;
                break;
            }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r][c-1-i] = true;

        //nord-est: x decrescente da r-1 a 0 y crescente da c+1 a column-1
        find=false;
        dist=0;

        i = r-1; j = c+1;

        while(i>=0 && j < b.getColumn()){
            if(b.getStatus(i,j) != -1){
                if(b.getStatus(i,j) != currPlayer){
                    find = false;dist++;
                    i--;j++;
                }
                else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r-1-i][c+1+i] = true;


        //sud-est: x crescente da r+1 a row-1 y crescente da c+1 a column-1
        find=false;
        dist=0;

        i = r+1; j = c+1;
        while(i<b.getRow() && j < b.getColumn()){
            if(b.getStatus(i,j) != -1){
                if(b.getStatus(i,j) != currPlayer){
                    find = false;dist++;
                    i++;j++;
                }
                else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r+1+i][c+1+i] = true;

        //sud-ovest: x crescente da r+1 a row-1 y decrescente da c-1 a 0
        find=false;
        dist=0;

        i = r+1; j = c-1;
        while(i<b.getRow() && j >= 0){
            if(b.getStatus(i,j) != -1){
                if(b.getStatus(i,j) != currPlayer){
                    find = false;dist++;
                    i++;j--;
                }
                else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r+1+i][c-1-i] = true;

        //nord-ovest: x decrescente da r-1 a 0 y decrescente da c-1 a 0
        find=false;
        dist=0;

        i = r-1; j = c-1;
        while(i >= 0 && j >= 0){
            if(b.getStatus(i,j) != -1){
                if(b.getStatus(i,j) != currPlayer){
                    find = false;dist++;
                    i--;j--;
                }
                else{
                    if(dist>0){
                        find = true;gFind =true;
                    }break;
                }
            }else{
                dist = 0; find = false;
                break;
            }
        }

        if(find)
            for(i=0;i<dist;i++)
                mosseReversi[r-1-i][c-1-i] = true;

        mosseReversi[r][c] = gFind;

        return mosseReversi;
    }


    /*
     * hasReversi: Prende in input lo int del giocatore e ritorna un booleano che indica se sulla tavolozza
     * e' possibile effettuare una mossa di reversi. Viene effettuata una scansione delle caselle vuote della
     * tavolozza e in corrispondenza anche di una sola mossa di reversi viene ritornato true;
     * */
    public boolean hasReversi(Board b, int currentPlayer){

        for(int i=0;i<b.getRow();i++)
            for(int j=0;j<b.getColumn();j++){
                if(b.getStatus(i,j) == -1) {
                    boolean [][] reversi = this.getSingleReversi(b, i, j, currentPlayer);
                    if(this.canReversi(b.getRow(), b.getColumn(), reversi)) {
                        return true;
                    }
                }
            }

        return false;
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
    public boolean[][] getAllReversi(Board b, int currPlayer){
        boolean[][] mosseReversi = new boolean[b.getRow()][b.getColumn()];
        for(int i=0;i<b.getRow();i++)
            for(int j=0;j<b.getColumn();j++){
                if(b.getStatus(i,j) == -1){
                    mosseReversi[i][j] = this.canReversi(b.getRow(), b.getColumn(), this.getSingleReversi(b, i, j, currPlayer));
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
        result = this.orMatrice(height, width, mossaReversi);
        return result;

    }

    /*
     *  moveReversi: metodo che data la matrice di una mossa e il giocatore che deve giocare cambia lo
     *  stato della tavolozza mettendo i nuovi valori alle caselle corrette.
     */
    public void moveReversi(Board b, boolean[][] mossaReversi, int currentPlayer){
        for(int i=0;i<b.getRow();i++)
            for(int j=0;j<b.getColumn();j++){
                if(mossaReversi[i][j]){
                    b.setStatus(i, j, currentPlayer);
                }
            }
    }

    public void colonizeField(Board b, int r, int c, int currenPlayer){
        b.setStatus(r,c,currenPlayer);
    }

   public boolean hasColonize(Board b, int currentPlayer){
       boolean result = false;
       for(int i=0;i<b.getRow();i++)
           for(int j=0;j<b.getColumn();j++)
               if(this.canColonize(b, i, j, currentPlayer)){
                        result = true;
                        break;
               }
       return result;
   }

   public boolean[][] getAllColonize(Board b, int currentPlayer){
       boolean[][] allColonize = new boolean[b.getRow()][b.getColumn()];
       for(int i=0;i<b.getRow();i++)
           for(int j=0;j<b.getColumn();j++)
               allColonize[i][j] = this.canColonize(b, i, j, currentPlayer);

       return allColonize;
   }

   public void printBoard(Board b){
        for(int i=0;i<b.getRow();i++){
            System.out.flush();
            System.out.print("|");
            for(int j=0;j<b.getColumn();j++){
                System.out.flush();
                if(b.getStatus(i,j)!= -1)
                   System.out.print(b.getStatus(i,j)+"|");
                else
                   System.out.print(" |");
            }
            System.out.print("\n");
        }
    }

    /**
     * Ritorna il giocatore che ha più pedine
     * @param b
     * @return il giocatore che vince o -1 se non è finito il gioco
     */
    public int getWinner(Board b) {
        HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();

        if(PlayerList.getInstance().size() == 1){
            return PlayerList.getInstance().get(0).getUuid();
        }

        for(int r=0;r<b.getRow();r++) {
            for(int c=0;c<b.getColumn();c++) {
                int p = b.getStatus(r,c);
                if (p == -1) return -1;
                Integer ra = count.get(p);
                if (ra == null)
                    ra = 0;
                ra = ra + 1;
                count.put(b.getStatus(r,c), ra);
            }
        }

        int max = 0;
        int winner = -1;
        for (Map.Entry<Integer, Integer> en : count.entrySet()) {
            if (en.getValue() > max) {
                max = en.getValue();
                winner = en.getKey();
            }

        }

        int nw = 0;//numero di vincitori, se > 1 patta
        for ( Integer val : count.values()) {
            if (val >= max) nw++;
            if (nw >= 2) return -2;
        }

        return winner;
    }


    public int[] getdraw(Board b) {
        HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();

        PlayerList plist = PlayerList.getInstance();
        int[] drawPlayer = new int[plist.size()];

        if(PlayerList.getInstance().size() == 1){
            return null;
        }

        for(int r=0;r<b.getRow();r++) {
            for(int c=0;c<b.getColumn();c++) {
                int p = b.getStatus(r,c);
                if (p != -1){
                    Integer ra = count.get(p);
                    if (ra == null)
                        ra = 0;
                    ra = ra + 1;
                    count.put(b.getStatus(r,c), ra);
                }
            }
        }



        int max = 0;
        int nd = 0;

        for (Map.Entry<Integer, Integer> en : count.entrySet()) {
            if (en.getValue() > max) {
                max = en.getValue();
            }

        }

        for(int i=0;i<plist.size();i++){
            if(max == count.get(plist.get(i).getUuid())){
                drawPlayer[nd] = plist.get(i).getUuid();
                nd++;
            }
        }

        return drawPlayer;
    }


    /*
    Si effettua una valutazione cortocircuitata dell'or sulla matrice in modo tale che alla prima
    occorrenza di un true la funzione possa terminare la valutazione
     */
    public boolean orMatrice(int height, int width, boolean[][] mossa){
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                if(mossa[i][j])
                    return true;
        return false;
    }
}

