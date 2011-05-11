package com.github.benve.othellomultiplayer.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

/**
 *
 * @author Giacomo Benvenuti
 */
public class Node implements RemoteNode {

    //Nodo successivo nella rete
    private RemoteNode next;

    private Registry registry;

    public final int port;

    public int getPort() { return this.port; }

    //ID univoco del nodo
    public static final UUID uuid = UUID.randomUUID();

    public void setNext(String nextIP, int nextPort) throws RemoteException, NotBoundException {
        // http://java.sun.com/docs/books/tutorial/rmi/overview.html
        Registry registry = LocateRegistry.getRegistry(nextIP, nextPort);
        this.next = (RemoteNode) registry.lookup("Node");
    }

    public RemoteRegistration getRegistration(String rhost, int rport) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(rhost, rport);
        RemoteRegistration reg = (RemoteRegistration) registry.lookup("Reg");
        return reg;
    }

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

    /**
     * Crea il servizio di registrazione e lo mette in ascolto
     * @param maxplayer
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    public RemoteRegistration initRegistration(int maxplayer) throws RemoteException, AlreadyBoundException, MaxPlayerException {
        Registration reg = new Registration(maxplayer);
        this.registry.bind("Reg", reg);

        return reg;
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


    public void msg(String s) {
        System.out.println(s);

    }



}
