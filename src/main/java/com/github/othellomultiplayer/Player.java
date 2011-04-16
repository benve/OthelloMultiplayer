package com.github.benve.othellomultiplayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.UUID;
/**
 *
 * @author ledonne
 */
public class Player implements PlayerInterface{
      private String p_name;
      private UUID p_uuid;
      private String p_url;
      private String p_port;

      public Player(){
          p_name = null;
          p_uuid = null;
          p_url = null;
          p_port = null;
      }

      public Player(String name,String url,String port){
          p_name = new String(name);
          p_uuid = UUID.randomUUID();
          p_url = new String(url);
          p_port = new String(url);
      }

      public String getName(){
          return p_name.toString();
      }

      public String getUUID(){
           return p_uuid.toString();
      }

      public String getUrl(){
          return p_url.toString();
      }

      public String getPort(){
          return p_port.toString();
      }
}
