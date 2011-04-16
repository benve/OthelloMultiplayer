static final byte N = byte(unbinary("00000001"));
static final byte NE = byte(unbinary("00000010"));
static final byte E = byte(unbinary("00000100"));
static final byte SE = byte(unbinary("00001000"));
static final byte S = byte(unbinary("00010000"));
static final byte SW = byte(unbinary("00100000"));
static final byte W = byte(unbinary("01000000"));
static final byte NW = byte(unbinary("10000000"));
static final byte VOID = byte(unbinary("00000000"));

final int bSize = 8;
//Lato in pixel della casella
int ablato;

int nplayers = 4;

final ArrayList<Player> pls = new ArrayList<Player>(nplayers);
final Board board = new Board(bSize, pls);

//Giocatore del turno corrente
int currP = 0;

void setup() {
  size(240, 240);
  frameRate(20);
  
  ablato = height/bSize;  
  smooth();

  PFont myFont = loadFont("AndaleMono-12.vlw");
  textFont(myFont, 12);

  ellipseMode(CORNER);

  //Aggiungo giocatori dandogli pedine casuali
  for (int i = 0; i < nplayers; i++) {
    pls.add(i, new Player(i));
    for (int j = 0; j < (10 - nplayers); j++) {
      int x = int(random(0,bSize-1));
      int y = int(random(0,bSize-1));
      while (board.b[x][y] != -1) {
        x = int(random(0,bSize-1));
        y = int(random(0,bSize-1));
      }
      board.b[x][y] = i;
    }
  }
  
}

void draw() {

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
      if (board.isEmpty(i,j)) {
         if (board.isValid(i, j, currP) != VOID) {//E' una mossa consentita per il giocatore
           fill(pls.get(currP).c);
           text(currP, (i*ablato)+ablato/2, (j*ablato)+ablato/2);    
         }
        
      } else {//Cassella con una pedina
        fill(board.getC(i,j));
        noStroke();
        ellipse(i*ablato, j*ablato, ablato, ablato);
        
        //label player
        fill(0);
        text(board.b[i][j], (i*ablato)+ablato/2, (j*ablato)+ablato/2);
      
      }
    }
  }
}

void keyPressed() {
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

void mousePressed() {

  int nx = mouseX / ablato; 
  int ny = mouseY / ablato;

  if (board.isValid(nx, ny, currP) != VOID) {
    byte ret = board.isValid(nx, ny, currP, true);
    board.put(nx,ny,currP);
    println(binary(ret));
    currP = (currP + 1) %  pls.size();
   println("Cambio Giocatore: "+currP); 
  } else {
   println("Mossa non consentita"); 
  }
}

