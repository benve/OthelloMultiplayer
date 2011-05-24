package com.github.benve.othellomultiplayer.old.network;

import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.old.game.Player;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Giacomo Benvenuti
 */
public class Node implements RemoteNode, Serializable {

    //Nodo successivo nella rete
    private RemoteNode next;

    private Registry registry;

    private Registration reg = null;

    public final int port;

    public int getPort() { return this.port; }

        /**
     * Creo un server su una porta libera
     * @throws IOException
     * @throws AlreadyBoundException
     */
    public Node() throws IOException, AlreadyBoundException {

        //Ottengo una porta ip libera
        //Il Socket è usato esclusivamente per ottenere una porta libera, non per comunicare
        ServerSocket ss = new ServerSocket(0);
        int freeport = ss.getLocalPort();
        ss.close();

        this.port = freeport;

        initServer(freeport);
    }

    /**
     *
     * @param lport Porta su cui sta in ascolto il nodo
     */
    public Node(int lport) throws RemoteException, AlreadyBoundException {
        initServer(lport);

        this.port = lport;
    }

    //ID univoco del nodo
    public static final UUID uuid = UUID.randomUUID();

    public void setNext(String nextIP, int nextPort) throws RemoteException, NotBoundException {
        // http://java.sun.com/docs/books/tutorial/rmi/overview.html
        Registry registry = LocateRegistry.getRegistry(nextIP, nextPort);
        this.next = (RemoteNode) registry.lookup("Node");
    }

    private void initServer(int lport) throws RemoteException, AlreadyBoundException {
        RemoteNode node = (RemoteNode) UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(lport);
        this.registry = registry;

        registry.bind("Node", node);
    }

    public void prova(String s) throws RemoteException {
        next.msg(s);
    }

    @Override
    public void msg(String s) {
        System.out.println(s);

    }

    public void initReg(int maxplayer) {
        this.reg = new Registration(maxplayer);
    }

    @Override
    public List<Player> register(Player pplay) throws RemoteException, MaxPlayerException {
        if (reg != null) {
            return reg.register(pplay);
        } else if(next != null) {
            return next.register(pplay);
        }

        return null;
    }

    @Override
    public List<Player> getPlayerList() throws RemoteException {
        assert (reg != null);
        return reg.getPlayerList();
    }


}
