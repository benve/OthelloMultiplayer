package almostothello.game;

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
public class PlayerList extends LinkedList<Player> implements List<Player>, Serializable {

    private static PlayerList players;

    private PlayerList(){
        super();
    }

    public static PlayerList getInstance() {
        if(players ==null)
            players = new PlayerList();
        return players;
    }

    public int getPosition(Player p1){
        for (int i = 0; i < (this.size()); i++) {//Scorro tutti tranne l'ultimo
            if (this.get(i).getUuid() == p1.getUuid()) {
                return i;
            }
        }
        return -1;
    }

    public Player getByUUID(int uuid) {
        for (Player pl : players) {
            if (pl.getUuid() == uuid)
                return pl;
        }
        return null;
    }


    public Player getNext(Player p1){

        int sec = 0;
        while (this.size() <=0 && sec <=10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { e.printStackTrace(); }
            sec++;
        }

        if (this.size() <= 0) return null;

        for (int i = 0; i < (this.size()-1); i++) {//Scorro tutti tranne l'ultimo
            if (this.get(i).getUuid() == p1.getUuid()) {
                return this.get(i+1);
            }
        }
        //Se sono l'ultimo torno in testa alla lista
        if (this.get(this.size()-1).getUuid() == p1.getUuid()) {
            return this.get(0);
        }

        return null;
    }

    public void removeElementByPosition(int position){
        this.remove(position);
    }

    public void removeElementByReference(Player p1){
        int position = this.getPosition(p1);
        this.remove(position);
    }

    public void setColorsByPosition(){
        Player p;
        for(int i=0;i<players.size();i++){
            p = players.get(i);
            p.c = i;
        }

    }

}