package com.github.benve.othellomultiplayer.game;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Gabriele Ledonne
 * Date: 20/05/11
 * Time: 10.39
 * Realizzazione della comunicazione attraverso una nuova struttura di classi di comunicazione (ma con logica
 * sostanzialmente uguale)
 */
public class Player implements Serializable{
    private String p_name;

    private int port;

    private int uuid;

    private String ipAddress;

    public Player(int port) {
        this.port = port;
        this.ipAddress = "localhost";
        this.p_name = this.ipAddress+":"+this.port;
        this.uuid = UUID.randomUUID().hashCode();
    }

    public Player(String Name, String host, int port) {
        this.port = port;
        this.ipAddress = host;
        this.p_name = Name;
        this.uuid = UUID.randomUUID().hashCode();
    }

    public Player(String Name, int port){
        this.p_name = Name;
        this.port = port;
        this.ipAddress = "localhost:"+this.port;
        this.uuid = UUID.randomUUID().hashCode();
    }

    public String getName(){
        return this.p_name.toString();
    }

    public String getIpAddress(){
        return this.ipAddress.toString();
    }

    public int getUuid(){
        return this.uuid;
    }

    public int getPort(){
        return this.port;
    }



}
