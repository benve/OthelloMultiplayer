package com.github.benve.othellomultiplayer.old.network;

import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.old.game.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Giacomo Benvenuti
 * Date: 4/30/11
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface RemoteRegistration extends Remote {
    List<Player> register(Player pplay) throws RemoteException, MaxPlayerException;

    List<Player> getPlayerList() throws RemoteException;
}
