package com.github.benve.othellomultiplayer.gui;

import com.github.benve.othellomultiplayer.game.Board;
import com.github.benve.othellomultiplayer.game.BoardLogic;
import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.game.PlayerList;
import com.github.benve.othellomultiplayer.network.Node;
import com.github.benve.othellomultiplayer.utils.NetUtils;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;


public class Gui extends PApplet {

    //color(random(0, 255), random(0, 255), random(0, 255));
    public int color(float[] color) {
        //return color(random(0, 255), random(0, 255), random(0, 255));
        return color(color[0], color[1], color[2]);
    }

    ControlP5 controlP5;

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

    //Cornice intorno al campo di gioco
    int cornice = 10;

    public void setup() {
        //Grandezza finestra
        size(350+cornice*2, 350+cornice*2);
        frameRate(10);
        background(100);

        smooth();

        PFont myFont = loadFont("Ziggurat-HTF-Black-32.vlw");
        textFont(myFont, 32);

        ellipseMode(CORNER);

        controlP5 = new ControlP5(this);
        controlP5 = new ControlP5(this);
        controlP5.addTextfield("Il tuo nome",100,100,100,30);

    }

    void controlEvent(ControlEvent event) {
        println(event.controller().stringValue());
    }

    public void draw() {

        background(0);

        stroke(255);

        if (winner > -1) {
            fill(color(pls.getByUUID(winner).c));
            text("The Winner is \n"+winner+" !!", height/2, width/4);

        } else if (board != null) {

            if (board.currP >= pls.size()) {//currP è troppo grande per crash dell'ultimo nodo di pls
                board.currP = board.currP % pls.size();
            }


            boolean[][] reversi = null;
            boolean[][] colonize = null;

            Player player = pls.get(board.currP);

            if (logic.hasReversi(board, player.getUuid())) {
                reversi = logic.getAllReversi(board, player.getUuid());
            } else if (logic.hasColonize(board, player.getUuid())) {
                colonize = logic.getAllColonize(board, player.getUuid());
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
                        if (player.getUuid() == node.me.getUuid()) {
                            if (reversi != null && reversi[j][i]) {//Mossa possibile
                                stroke(color(player.c));
                                noFill();
                                strokeWeight(3);
                                ellipse((i * lato) + 5, (j * lato) + 5, lato - 10, lato - 10);
                                //Label
                                fill(color(player.c));
                                text(board.currP, (i * lato) + lato / 2, (j * lato) + lato / 2);
                            } else if (colonize != null && colonize[j][i]) {//Casella che posso colonizzare
                                stroke(color(player.c));
                                noFill();
                                strokeWeight(3);
                                rect((i * lato) + 5, (j * lato) + 5, lato - 10, lato - 10);
                                //Label
                                fill(color(player.c));
                                text(board.currP, (i * lato) + lato / 2, (j * lato) + lato / 2);
                            }
                        }
                    } else {//Cassella con una pedina
                        Player owner = pls.getByUUID(board.getStatus(j, i));
                        String text = "";
                        if(owner == null) {
                            fill(200);
                            text = "X";//non stampa il teschietto: provare a cambiare il font
                        } else {
                            fill(color(owner.c));
                            text = ""+board.currP;
                        }

                        noStroke();
                        ellipse((i * lato) + 5, (j * lato) + 5, lato - 10, lato - 10);

                        //label player
                        fill(0);
                        text(text, (i * lato) + lato / 2, (j * lato) + lato / 2);
                    }
                }
            }
            if (reversi == null && colonize == null) {
                board.currP = (board.currP + 1) % pls.size();
                println("Non ho mosse possibili, next Giocatore: " + board.currP);
                node.sendToken(board.currP);
                //TODO:Se non ci sono mosse possibili forse qualcuno ha vinto :(
                //if (winner == -1)
                //    winner = logic.getWinner(board);
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
                board.currP = intk % nplayers;
            } catch (NumberFormatException e) {
            }
            if (intk == -1) {
                switch (key) {
                    case 'p':
                        println("Ciao "+board.currP+"  UUID:"+pls.get(board.currP).getUuid()+"  ME:"+node.me.getUuid());
                        break;
                    case 'a':
                        board.setStatus(mouseY / lato, mouseX / lato, node.me.getUuid());
                        node.sendMove(mouseY / lato, mouseX / lato, node.me.getUuid());
                        break;
                    case 'd':
                        board.setStatus(mouseY / lato, mouseX / lato, -1);
                        node.sendMove(mouseY / lato, mouseX / lato, -1);
                        break;
                }
            }
        }
    }

    public void mouseClicked() {

        if (pls.get(board.currP).getUuid() == node.me.getUuid()) {

            int nx = mouseX / lato;
            int ny = mouseY / lato;

            print(nx + "." + ny + " ");
            flush();

            boolean[][] allr = logic.getSingleReversi(board, ny, nx, node.me.getUuid());
            if (allr[ny][nx]) {
                for (int r = 0; r < allr.length; r++) {
                    println();
                    for (int c = 0; c < allr[r].length; c++) {
                        print((allr[r][c] ? "T" : "_") + "\t");
                        if (allr[r][c]) {
                            board.setStatus(r, c, node.me.getUuid());
                            node.sendMove(r,c,node.me.getUuid());
                        }
                    }
                }
                println();
                board.currP = (board.currP + 1) % pls.size();
                println("Reversi! Giocatore: " + board.currP);
                node.sendToken(board.currP);

            } else if (logic.canColonize(board, ny, nx, node.me.getUuid())) {
                board.setStatus(ny, nx, node.me.getUuid());
                node.sendMove(ny,nx,node.me.getUuid());
                board.currP = (board.currP + 1) % pls.size();
                println("Colonize! Giocatore: " + board.currP);
                node.sendToken(board.currP);
            } //else println("Mossa non consentita");

            winner = logic.getWinner(board);

        }
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
