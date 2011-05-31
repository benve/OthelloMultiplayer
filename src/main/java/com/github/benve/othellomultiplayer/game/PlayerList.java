package com.github.benve.othellomultiplayer.game;

import java.io.Serializable;
import java.util.Iterator;
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
    public int getPosition(Player p1){
        return this.indexOf(p1);
    }

    public Player getNext(Player p1){
        return this.get(this.indexOf(p1)+1);
    }

    public void removeElementByPosition(int position){
        this.remove(position);
    }

    public void removeElementByReference(Player p1){
        int position = this.getPosition(p1);
        this.remove(position);
    }

}
