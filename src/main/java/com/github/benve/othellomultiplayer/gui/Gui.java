package com.github.benve.othellomultiplayer.gui;

import com.github.benve.othellomultiplayer.game.Board;
import com.github.benve.othellomultiplayer.game.BoardLogic;
import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.game.PlayerList;
import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.network.Node;
import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


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

    int players = 3;

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
        createMessageBox();
    }

    public void draw() {

        background(100);

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
                bSize = board.side;
                lato = height / bSize;
            }
        }
    }

    /*public void keyPressed() {
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
    }*/

    public void mouseClicked() {

        if (node != null && node.b != null && (pls.get(board.currP).getUuid() == node.me.getUuid())) {

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

    int messageBoxResult = -1;
    ControlGroup messageBox;
    String messageBoxString = "";

    void createMessageBox() {
        // create a group to store the messageBox elements
        messageBox = controlP5.addGroup("messageBox", width / 2 - 150, 100, 300);
        messageBox.setBackgroundHeight(200);
        messageBox.setBackgroundColor(color(0, 100));
        messageBox.hideBar();

        // add a TextLabel to the messageBox.
        Textlabel l = controlP5.addTextlabel("messageBoxLabel", "Parametri di gioco:", 20, 20);
        l.moveTo(messageBox);

        // add a textfield-controller with named-id inputbox
        // this controller will be linked to function inputbox() below.
        Textfield name = controlP5.addTextfield("name", 20, 40, 260, 20);
        //name.captionLabel().setVisible(false);
        name.moveTo(messageBox);
        name.setColorForeground(color(20));
        name.setColorBackground(color(20));
        name.setColorActive(color(100));

        RadioButton radio = controlP5.addRadioButton("radioButton",20,90);
        radio.moveTo(messageBox);
        radio.setColorForeground(color(120));
        radio.setColorActive(color(255));
        radio.setColorLabel(color(255));
        radio.setItemsPerRow(2);
        radio.setSpacingColumn(70);
        radio.setNoneSelectedAllowed(false);

        Toggle t1 = radio.addItem("new_game",1);
        t1.setState(true);
        t1.captionLabel().setColorBackground(color(80));
        t1.captionLabel().style().movePadding(2,0,-1,2);
        t1.captionLabel().style().moveMargin(-2,0,0,-3);
        t1.captionLabel().style().backgroundWidth = 46;

        Toggle t2 = radio.addItem("join_game",0);
        t2.captionLabel().setColorBackground(color(80));
        t2.captionLabel().style().movePadding(2,0,-1,2);
        t2.captionLabel().style().moveMargin(-2,0,0,-3);
        t2.captionLabel().style().backgroundWidth = 46;

        Textfield port = controlP5.addTextfield("port", 20, 120, 260, 20);
        port.moveTo(messageBox);
        port.setColorForeground(color(20));
        port.setColorBackground(color(20));
        port.setColorActive(color(100));
        port.hide();

        Slider pslider = controlP5.addSlider("players", 2, 8, 3, 20, 120, 260, 20);
        pslider.captionLabel().setVisible(false);
        pslider.moveTo(messageBox);
        pslider.setNumberOfTickMarks(7);
        pslider.setSliderMode(Slider.FLEXIBLE);
        //pslider.hide();

        // add the OK button to the messageBox.
        // the name of the button corresponds to function buttonOK
        // below and will be triggered when pressing the button.
        controlP5.Button b1 = controlP5.addButton("buttonOK", 0, 65, 200, 80, 24);
        b1.moveTo(messageBox);
        b1.setColorBackground(color(40));
        b1.setColorActive(color(20));
        // by default setValue would trigger function buttonOK,
        // therefore we disable the broadcasting before setting
        // the value and enable broadcasting again afterwards.
        // same applies to the cancel button below.
        b1.setBroadcast(false);
        b1.setValue(1);
        b1.setBroadcast(true);
        b1.setCaptionLabel("OK");
        // centering of a label needs to be done manually
        // with marginTop and marginLeft
        //b1.captionLabel().style().marginTop = -2;
        //b1.captionLabel().style().marginLeft = 26;

        // add the Cancel button to the messageBox.
        // the name of the button corresponds to function buttonCancel
        // below and will be triggered when pressing the button.
        controlP5.Button b2 = controlP5.addButton("buttonCancel", 0, 155, 200, 80, 24);
        b2.moveTo(messageBox);
        b2.setBroadcast(false);
        b2.setValue(0);
        b2.setBroadcast(true);
        b2.setCaptionLabel("Cancel");
        b2.setColorBackground(color(40));
        b2.setColorActive(color(20));
        //b2.captionLabel().toUpperCase(false);
        // centering of a label needs to be done manually
        // with marginTop and marginLeft
        //b2.captionLabel().style().marginTop = -2;
        //b2.captionLabel().style().marginLeft = 16;
    }

    public void controlEvent(ControlEvent theEvent) {
        if(
                controlP5.controller("players") != null &&
                controlP5.controller("port") != null &&
                theEvent.isGroup() &&
                theEvent.group().name().equals("radioButton")

                ) {
            if (theEvent.group().value() == 0) {
                controlP5.controller("players").hide();
                controlP5.controller("port").show();
            } else {
                controlP5.controller("players").show();
                controlP5.controller("port").hide();
            }
        }

    }

    // function buttonOK will be triggered when pressing
// the OK button of the messageBox.
    void buttonOK(int theValue) {
        println("a button event from button OK.");
        messageBoxString = ((Textfield) controlP5.controller("inputbox")).getText();
        messageBoxResult = theValue;

         try {
        if (((Toggle)controlP5.controller("new_game")).getState()) {//Server registrazione

                node = new Node(1234, players);

                node.initializeNode(true);

                node.registerToGame(true, 0);



        } else {
            int p = Integer.parseInt(((Textfield)controlP5.controller("port")).getText());
            node = new Node(p);

            node.initializeNode(false);

            node.registerToGame(false, 1234);

        }

         } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (AlreadyBoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnknownHostException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SocketException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (MaxPlayerException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotBoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        messageBox.hide();

            if (node.allPlayer.getPosition(node.me) == 0) {
                    node.startGame();
                    //node.actionToken(node.me.getUuid());
            }

    }


    // function buttonCancel will be triggered when pressing
// the Cancel button of the messageBox.
    void buttonCancel(int theValue) {
        println("a button event from button Cancel.");
        messageBoxResult = theValue;
        messageBox.hide();
    }

    // inputbox is called whenever RETURN has been pressed
// in textfield-controller inputbox
    void name(String theString) {
        println("got something from the inputbox : " + theString);
        messageBoxString = theString;
        //messageBox.hide();
    }

    static Node node;


    static public void main(String args[]) {

        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "com.github.benve.othellomultiplayer.gui.Gui"});

    }
}
