package com.github.benve.othellomultiplayer.gui;

import com.github.benve.othellomultiplayer.game.Board;
import com.github.benve.othellomultiplayer.game.BoardLogic;
import com.github.benve.othellomultiplayer.game.PlayerList;
import com.github.benve.othellomultiplayer.network.Node;
import com.github.benve.othellomultiplayer.utils.NetUtils;
import processing.core.PApplet;
import processing.core.PFont;

public class Gui extends PApplet {

    //color(random(0, 255), random(0, 255), random(0, 255));
    public int color(float[] color) {
        //return color(random(0, 255), random(0, 255), random(0, 255));
        return color(color[0], color[1], color[2]);
    }

    final BoardLogic logic = BoardLogic.getInstance();

    //lato in caselle della scacchiera
    int bSize;
    //Lato in pixel della casella
    int lato;

    int nplayers;

    //Vincitore, -1 se ancora non impostato
    int winner = -1;


    PlayerList pls = null;
    Board board = null;

    //Giocatore del turno corrente
    int currP = 0;

    //Cornice intorno al campo di gioco
    int cornice = 10;

    public void setup() {
        //Grandezza finestra
        size(450+cornice*2, 450+cornice*2);
        frameRate(10);
        background(100);

        smooth();

        PFont myFont = loadFont("Ziggurat-HTF-Black-32.vlw");
        textFont(myFont, 32);

        ellipseMode(CORNER);

    }

    public void draw() {

        background(0);

        stroke(255);

        if (winner > -1) {
            fill(color(pls.get(winner).c));
            text("The Winner is \n"+winner+" !!", height/2, width/2);

        } else if (board != null) {

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
                            stroke(color(pls.get(currP).c));
                            noFill();
                            strokeWeight(3);
                            ellipse((i * lato)+5, (j * lato)+5, lato-10, lato-10);
                            //Label
                            fill(color(pls.get(currP).c));
                            text(currP, (i*lato)+lato/2, (j*lato)+lato/2);
                        } else if (colonize != null && colonize[j][i]) {
                            stroke(color(pls.get(currP).c));
                            noFill();
                            strokeWeight(3);
                            rect((i * lato)+5, (j * lato)+5, lato-10, lato-10);
                            //Label
                            fill(color(pls.get(currP).c));
                            text(currP, (i*lato)+lato/2, (j*lato)+lato/2);
                        }
                    } else {//Cassella con una pedina
                        fill(color(pls.get(board.getStatus(j, i)).c));
                        noStroke();
                        ellipse((i * lato)+5, (j * lato)+5, lato-10, lato-10);

                        //label player
                        fill(0);
                        text(currP, (i*lato)+lato/2, (j*lato)+lato/2);
                    }
                }
            }
        } else {//Iniziallizzazione Board
            if (node != null && node.b != null) {
                board = node.b;
                pls = node.allPlayer;
                nplayers = pls.size();
                bSize = board.side;
                lato = height / bSize;
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
                    case 'a':
                        board.setStatus(mouseY / lato, mouseX / lato, currP);
                        break;
                    case 'd':
                        board.setStatus(mouseY / lato, mouseX / lato, -1);
                        break;
                }
            }
        }
    }

    public void mouseClicked() {

        int nx = mouseX / lato;
        int ny = mouseY / lato;

        print(nx+"."+ny+" ");flush();

        boolean[][] allr = logic.getSingleReversi(board, ny, nx, currP);
        if (allr[ny][nx]) {
            for (int r = 0; r < allr.length; r++) {
                println();
                for (int c = 0; c < allr[r].length; c++) {
                    print((allr[r][c] ? "T" : "_") + "\t");
                    if (allr[r][c])
                        board.setStatus(r, c, currP);
                }
            }
            println();
            currP = (currP + 1) % pls.size();
            println("Reversi! Giocatore: " + currP);
        } else if (logic.canColonize(board, ny, nx, currP)) {
            board.setStatus(ny, nx, currP);
            currP = (currP + 1) % pls.size();
            println("Colonize! Giocatore: " + currP);
        } //else println("Mossa non consentita");

        winner = logic.getWinner(board);
    }

    static Node node;


    static public void main(String args[]) {

        try {

            boolean isServer = false;
            BoardLogic bl1 = BoardLogic.getInstance();
            Board b1;
            int port;

            NetUtils n = NetUtils.getInstance();

            //n.getPublicIP().toString());
            if (args.length >= 3) {
                port = Integer.parseInt(args[0]);

                System.out.println(port + "\t" + n.getHostAddress());

                node = new Node(port, Integer.parseInt(args[1]));
                if (Integer.parseInt(args[2]) == 1) {
                    isServer = true;
                }

                node.initializeNode(isServer);

                System.out.println(node.me.getPort() + "\t" + node.me.getUuid() + "|" + node.me.getPort());

                if (isServer) {
                    node.registerToGame(isServer, 0);
                } else {
                    node.registerToGame(isServer, 1234);
                }

                PApplet.main(new String[]{"--bgcolor=#DFDFDF", "com.github.benve.othellomultiplayer.gui.Gui"});


                if (node.allPlayer.getPosition(node.me) == 0) {
                    node.startGame();
                    //node.actionToken(node.me.getUuid());
                }


            } else {
                System.out.println("Servono 3 parametri: porta numerogiocatori 1\n" +
                        "con 1 viene istanziato il registro dei giocatori");

            }

        } catch (Throwable e) {
            //Exception Funnel
            e.printStackTrace();
        }

    }
}
