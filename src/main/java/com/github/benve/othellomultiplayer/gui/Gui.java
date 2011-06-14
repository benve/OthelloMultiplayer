package com.github.benve.othellomultiplayer.gui;

import com.github.benve.othellomultiplayer.game.Board;
import com.github.benve.othellomultiplayer.game.BoardLogic;
import processing.core.*;

import java.util.*;

public class Gui extends PApplet {


    class Player {

        int index;

        int c;

        public Player(int idx) {
            index = idx;
            c = color(
                    random(0, 255),
                    random(0, 255),
                    random(0, 255)
            );
        }

    }

    final BoardLogic logic = BoardLogic.getInstance();

    //lato in caselle della scacchiera
    final int bSize = 8;
    //Lato in pixel della casella
    int lato;

    int nplayers = 4;

    final ArrayList<Player> pls = new ArrayList<Player>(nplayers);
    final Board board = new Board(bSize, bSize);

    //Giocatore del turno corrente
    int currP = 0;

    //Cornice intorno al campo di gioco
    int cornice = 10;

    public void setup() {
        //Grandezza finestra
        size(400+cornice*2, 400+cornice*2);
        frameRate(10);

        lato = height / bSize;
        smooth();

        //PFont myFont = loadFont("ArialMT-48.vlw");
        //textFont(myFont, 12);

        ellipseMode(CORNER);

        //Aggiungo giocatori dandogli pedine casuali
        for (int i = 0; i < nplayers; i++) {
            pls.add(i, new Player(i));
            for (int j = 0; j < (6 - nplayers); j++) {
                int x = PApplet.parseInt(random(0, bSize - 1));
                int y = PApplet.parseInt(random(0, bSize - 1));
                while (board.board[x][y] != -1) {
                    x = PApplet.parseInt(random(0, bSize - 1));
                    y = PApplet.parseInt(random(0, bSize - 1));
                }
                board.board[x][y] = i;
            }
        }

    }

    public void draw() {

        background(0);

        stroke(255);

        if (board != null) {

            boolean[][] reversi = null;
            boolean[][] colonize = null;

            if (logic.hasReversi(board, currP)) {
                reversi = logic.getAllReversi(board, currP);
            } else if (logic.hasColonize(board, currP)) {
                colonize = logic.getAllColonize(board, currP);
            }

            //Disegno righe
            for (int y = 0; y < bSize; y++) {
                line(0, y * lato, width, y * lato);
            }
            //Disegno colonne
            for (int x = 0; x < bSize; x++) {
                line(x * lato, 0, x * lato, height);
            }

            //Disegno pedine
            for (int i = 0; i < bSize; i++) {
                for (int j = 0; j < bSize; j++) {
                    //println("Casella "+i+" "+j);
                    if (board.getStatus(j, i) == -1) {
                        if (reversi != null && reversi[j][i]) {
                            stroke(pls.get(currP).c);
                            noFill();
                            strokeWeight(3);
                            ellipse((i * lato)+5, (j * lato)+5, lato-10, lato-10);
                        } else if (colonize != null && colonize[j][i]) {
                            stroke(pls.get(currP).c);
                            noFill();
                            strokeWeight(3);
                            rect((i * lato)+5, (j * lato)+5, lato-10, lato-10);
                        }
                        /*if (board.isValid(i, j, currP) != VOID) {//E' una mossa consentita per il giocatore
                         fill(pls.get(currP).c);
                         text(currP, (i*lato)+lato/2, (j*lato)+lato/2);
                       } */

                    } else {//Cassella con una pedina
                        fill(pls.get(board.getStatus(j, i)).c);
                        noStroke();
                        ellipse((i * lato)+5, (j * lato)+5, lato-10, lato-10);

                        //label player
                        //fill(0);
                        //text(board.getStatus(i,j), (i*lato)+lato/2, (j*lato)+lato/2);

                    }
                }
            }

        }
    }

    public void keyPressed() {
        if (key == CODED) {
            println(keyCode);
        } else {
            int intk = -1;
            try {
                intk = Integer.parseInt(key + "");

                println("Cambio Giocatore: " + intk);
                currP = intk % nplayers;
            } catch (NumberFormatException e) {
            }
            if (intk == -1) {
                switch (key) {
                    case 'p':
                        println("Ciao!");
                        break;
                }
            }
        }
    }

    public void mousePressed() {

        int nx = mouseX / lato;
        int ny = mouseY / lato;

        if (logic.canColonize(board,ny,nx,currP)) {
            board.setStatus(ny, nx, currP);
            currP = (currP + 1) %  pls.size();
            println("Cambio Giocatore: "+currP);
        } else {
            boolean[][] allr = logic.getAllReversi(board, currP);
            if (allr[ny][nx]) {
                board.setStatus(ny, nx, currP);
                currP = (currP + 1) %  pls.size();
                println("Cambio Giocatore: "+currP);
            } else println("Mossa non consentita");
        }


    }

    static public void main(String args[]) {
        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "com.github.benve.othellomultiplayer.gui.Gui"});
    }
}
