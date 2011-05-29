package com.github.benve.othellomultiplayer.game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: giacomo
 * Date: 5/24/11
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerList extends LinkedList<Player> implements List<Player>{

    /**
     * Dato un giocatore ritorna il next della lista
     * @param myP giocatore corrente
     * @return giocatore successivo, null se non trovo il giocatore corrente
     */
    public Player nextP(Player myP) {

        for (int i = 0; i < (this.size()-1); i++) {//Scorro tutti tranne l'ultimo
            if (this.get(i).getUuid() == myP.getUuid()) {
                return this.get(i+1);
            }
        }
        //Se sono l'ultimo torno in testa alla lista
        if (this.get(this.size()-1).getUuid() == myP.getUuid()) {
            return this.get(0);
        }

        return null;
    }

}
