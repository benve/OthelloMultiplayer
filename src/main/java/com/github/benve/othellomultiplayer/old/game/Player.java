package com.github.benve.othellomultiplayer.old.game;

import com.github.benve.othellomultiplayer.old.network.Node;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.util.List;

/**
 *
 * @author ledonne
 */
public class Player extends Node implements Serializable {
      private String p_name;

      public List<Player> players;

      public Player(String name) throws IOException, AlreadyBoundException {
          super();
          p_name = new String(name);

      }

      public String getName(){
          return p_name.toString();
      }

}