package com.github.benve.othellomultiplayer.game;

import com.github.benve.othellomultiplayer.utils.NetUtils;

import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

//import static com.github.benve.othellomultiplayer.utils.NetUtils.getHostAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Gabriele Ledonne
 * Date: 20/05/11
 * Time: 10.39
 * Realizzazione della comunicazione attraverso una nuova struttura di classi di comunicazione (ma con logica
 * sostanzialmente uguale)
 */
public class Player implements Serializable{
    private String name;

    /**
     * Colore
     */
    public float[] c = new float[]{(float) Math.random() * 255,(float) Math.random() * 255,(float) Math.random() * 255};

    /**
     * Porta sulla quale il giocatore sta in ascolto
     */
    private int port;

    /**
     * Id univoco del giocatore
     */
    private int uuid;

    /**
     * IP locale del giocatore
     */
    private String ipAddress;

    /**
     * Crea un nuovo giocatore con nome ipmacchina:porta
     * @param port porta su cui sta in ascolto il registro RMI
     * @throws UnknownHostException non è possibile ottenere l'ip corrente
     */
    public Player(int port) throws UnknownHostException, SocketException {
        this(NetUtils.getInstance().getHostAddress()+":"+port, port);

    }

    /**
     * Crea un nuovo giocatore
     * @param name nome del giocatore
     * @param port porta su cui sta in ascolto il registro RMI
     * @throws UnknownHostException non è possibile ottenere l'ip corrente
     */
    public Player(String name, int port) throws UnknownHostException, SocketException {
        this.name = name;
        this.port = port;
        this.ipAddress = NetUtils.getInstance().getHostAddress();
        this.uuid = UUID.randomUUID().hashCode();
    }


    public String getName(){
        return this.name.toString();
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
