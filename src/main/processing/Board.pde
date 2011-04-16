class Board {
  
  //-1 è casella vuota
  int[][] b;

  int bSize;

  //Riferimento giocatori
  private ArrayList<Player> players;

  public Board(int bSize, ArrayList<Player> p) {
    //Inizializzo scacchiera vuota
    b = new int[bSize][bSize];
    for (int i = 0; i < bSize; i++) {
      for (int j = 0; j < bSize; j++) {
        b[i][j] = -1;
      }
    }
    players = p;
    this.bSize = bSize;
  }

  private boolean valid = false;

  private boolean isV(int xi,int yj,int player) {
    //println(xi+" "+yj);
    if (
      xi < 0 ||
      xi == bSize ||
      yj < 0 ||
      yj == bSize
    ) {
     valid = false;
     return true; 
    }
    if(isEmpty(xi,yj)) { 
      valid = false; 
      return true;
    }
    if(b[xi][yj] != player) { 
      valid = true;
    } 
    else { 
      return true;
    }
    return false;
  }
  
  public byte isValid(int x, int y, int player) {
    return isValid(x,y,player,false);
  }

  /**
   Controlla che nella data casella il giocatore possa mettere una pedina
   */
  public byte isValid(int x, int y, int player, boolean eat) {
    if (!isEmpty(x,y)) {
      return VOID;
    }
    
    byte ret = VOID;

    //print("Sud-Est ");
    int xi = x+1;
    int yj = y+1;
    valid = false;//True se valido
    while (yj <= bSize && xi <= bSize) {
      if (isV(xi, yj, player)) {
        break;
      }
      yj++;
      xi++;
    }
    if (valid) {//è una mossa valida
      ret |= SE;
      if (eat) {//Mangio
        println(x+""+y+" - "+xi+" "+yj);
        while (xi != x) {
          xi--;
          yj--;
          b[xi][yj] = player;
          println("Mangio "+xi+" "+yj);
        }   
      }
    }
    

    //print("Sud-Ovest ");
    xi = x-1;
    yj = y+1;
    valid = false;//True se valido
    while (yj <= bSize && xi >= -1) {
      if (isV(xi, yj, player)) {
        break;
      }
      yj++;
      xi--;
    }
    if (valid) {
      ret |= SW;
      if (eat) {//Mangio
        while (xi != x) {
          xi++;
          yj--;
          b[xi][yj] = player;
        }   
      }
    }

    //print("Nord-Est ");
    xi = x+1;
    yj = y-1;
    valid = false;//True se valido
    while (yj >= -1 && xi <= bSize) {
      if (isV(xi, yj, player)) {
        break;
      }
      yj--;
      xi++;
    }
    if (valid) {
      ret |= NE;
      if (eat) {//Mangio
        while (xi != x) {
          xi--;
          yj++;
          b[xi][yj] = player;
        }   
      }
    }

    //print("Nord-Ovest ");
    xi = x-1;
    yj = y-1;
    valid = false;//True se valido
    while (yj >= -1 && xi >= -1) {
      if (isV(xi, yj, player)) {
        break;
      }
      yj--;
      xi--;
    }
    if (valid) {
      ret |= NW;
      if (eat) {//Mangio
        while (xi != x) {
          xi++;
          yj++;
          b[xi][yj] = player;
        }   
      }
    }

    //print("Nord ");
    yj = y-1;
    valid = false;//True se valido
    while (yj >= -1) {
      if (isV(x, yj, player)) {
        break;
      }
      yj--;
    }
    if (valid) {
      ret |= N;
      if (eat) {//Mangio
        while (yj != y) {
          yj++;
          b[x][yj] = player;
        }   
      }
    }

    //print("Sud ");
    yj = y+1;
    valid = false;//True se valido
    while (yj <= bSize) {
      if (isV(x, yj, player)) {
        break;
      }
      yj++;
    }
    if (valid) {
      ret |= S;
      if (eat) {//Mangio
        while (yj != y) {
          yj--;
          b[x][yj] = player;
        }   
      }
    }

    //print("Ovest ");
    xi = x-1;
    valid = false;//True se valido
    while (xi >= -1) {
      if (isV(xi, y, player)) {
        break;
      }
      xi--;
    }
    if (valid) {
      ret |= W;
      if (eat) {//Mangio
        while (xi != x) {
          xi++;
          b[xi][y] = player;
        }   
      }
    }

    //print("Est ");
    xi = x+1;
    valid = false;//True se valido
    while (xi <= bSize) {
      if (isV(xi, y, player)) {
        break;
      }
      xi++;
    }
    if (valid) {
      ret |= E;
      if (eat) {//Mangio
        while (xi != x) {
          xi--;
          b[xi][y] = player;
        }   
      }
    }

    return ret;
  }
  
  /**
   Aggiunge una pedina nel gioco
   il player deve essere positivo
   ritorna false se non è possibile aggiungere la pedina
   */
  public boolean put(int x, int y, int player) {
    assert(player >= 0);
    if (isValid(x,y, player) != VOID) {
      b[x][y] = player;
      return true;
    } 
    else {
      return false;
    }
  }

  public boolean isEmpty(int x, int y) {
    if (b[x][y] == -1) {
      return true;
    } 
    else {
      return false;
    }
  }

  //é comodo poter ottenere il colore della pedina
  public color getC(int x, int y) {
    return players.get(b[x][y]).c;
  }
}

