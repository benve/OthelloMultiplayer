package com.github.benve.othellomultiplayer.gui;

import com.github.benve.othellomultiplayer.game.Board;
import processing.core.*;
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class Gui extends PApplet {


class Player {
 
 int index;
 
 int c;
 
 public Player (int idx) {
  index = idx;
  c = color(
    random(0,255),
    random(0,255),
    random(0,255)
  ); 
 }
  
}

//lato il caselle della scacchiera
final int bSize = 8;
//Lato in pixel della casella
int ablato;

int nplayers = 4;

final ArrayList<Player> pls = new ArrayList<Player>(nplayers);
final Board board = new Board(bSize, bSize);

//Giocatore del turno corrente
int currP = 0;

public void setup() {
    //Grandezza finestra
  size(400, 400);
  frameRate(20);
  
  ablato = height/bSize;  
  smooth();

  //PFont myFont = loadFont("ArialMT-48.vlw");
  //textFont(myFont, 12);

  ellipseMode(CORNER);

  //Aggiungo giocatori dandogli pedine casuali
  for (int i = 0; i < nplayers; i++) {
    pls.add(i, new Player(i));
    for (int j = 0; j < (10 - nplayers); j++) {
      int x = PApplet.parseInt(random(0,bSize-1));
      int y = PApplet.parseInt(random(0,bSize-1));
      while (board.board[x][y] != -1) {
        x = PApplet.parseInt(random(0,bSize-1));
        y = PApplet.parseInt(random(0,bSize-1));
      }
      board.board[x][y] = i;
    }
  }
  
}

public void draw() {

  background(0);

  stroke(255);

  //Disegno righe
  for (int i = 0; i < bSize; i++) {
    line(0, i*ablato, width, i*ablato);
  }
  //Disegno colonne
  for (int i = 0; i < bSize; i++) {
    line(i*ablato, 0, i*ablato, height);
  }

  //Disegno pedine
  for (int i = 0; i < bSize; i++) {
    for (int j = 0; j < bSize; j++) {
      //println("Casella "+i+" "+j);
      if (board.getStatus(i,j) == -1) {
         /*if (board.isValid(i, j, currP) != VOID) {//E' una mossa consentita per il giocatore
           fill(pls.get(currP).c);
           text(currP, (i*ablato)+ablato/2, (j*ablato)+ablato/2);
         } */
        
      } else {//Cassella con una pedina
        fill(pls.get(board.getStatus(i,j)).c);
        noStroke();
        ellipse(i*ablato, j*ablato, ablato, ablato);
        
        //label player
        //fill(0);
        //text(board.getStatus(i,j), (i*ablato)+ablato/2, (j*ablato)+ablato/2);
      
      }
    }
  }
}

public void keyPressed() {
  if (key == CODED) {
    println(keyCode);
  } 
  else {
    int intk = -1;
    try {
      intk = Integer.parseInt(key + "");

      println("Cambio Giocatore: "+intk);
      currP = intk;
    } 
    catch (NumberFormatException e) {
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

  int nx = mouseX / ablato; 
  int ny = mouseY / ablato;

/*  if (board.isValid(nx, ny, currP) != VOID) {
    byte ret = board.isValid(nx, ny, currP, true);
    board.put(nx,ny,currP);
    println(binary(ret));
    currP = (currP + 1) %  pls.size();
   println("Cambio Giocatore: "+currP); 
  } else {
   println("Mossa non consentita"); 
  }*/
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "Gui" });
  }
}
