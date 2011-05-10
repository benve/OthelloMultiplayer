package com.github.benve.othellomultiplayer.game;

import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.network.Node;
import com.github.benve.othellomultiplayer.network.Registration;
import com.github.benve.othellomultiplayer.network.RemoteRegistration;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;
/**
 *
 * @author ledonne
 */
public class Player extends Node {
      private String p_name;

      public List<Player> players;

      public Player(String name) throws IOException, AlreadyBoundException {
          super();
          p_name = new String(name);

      }

      public void register(int mapplayer) throws AlreadyBoundException, MaxPlayerException, RemoteException {
        RemoteRegistration reg = this.initRegistration(mapplayer);

        players = reg.register(this);
      }

      public void register(String rhost, int rport) throws MaxPlayerException, RemoteException, NotBoundException {
        RemoteRegistration reg = this.getRegistration(rhost, rport);

        players = reg.register(this);
      }

      public String getName(){
          return p_name.toString();
      }

}