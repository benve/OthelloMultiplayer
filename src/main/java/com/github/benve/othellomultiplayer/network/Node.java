package com.github.benve.othellomultiplayer.network;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Giacomo Benvenuti
 */
public class Node implements RemoteNode {

    private RemoteNode next;

    public void setNext(String nextIP, int nextPort) throws RemoteException, NotBoundException {
        // http://java.sun.com/docs/books/tutorial/rmi/overview.html
        Registry registry = LocateRegistry.getRegistry(nextIP, nextPort);
        this.next = (RemoteNode) registry.lookup("Node");
    }

    /**
     *
     * @param lport Porta su cui sta in ascolto il nodo
     */
    public Node(int lport) throws RemoteException, AlreadyBoundException {
        RemoteNode node = (RemoteNode) UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(lport);
        registry.bind("Node", node);

    }

    public void prova(String s) throws RemoteException {
        next.msg(s);
    }


    public void msg(String s) {
        System.out.println(s);

    }



}
