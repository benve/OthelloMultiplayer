package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Giacomo Benvenuti
 */
public interface RemoteNode extends Remote {

    public void msg(String s) throws RemoteException;

    List<Player> register(Player pplay) throws RemoteException, MaxPlayerException;

    List<Player> getPlayerList() throws RemoteException;

}
