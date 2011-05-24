package com.github.benve.othellomultiplayer;

import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.network.Node;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 13.25
 * To change this template use File | Settings | File Templates.
 */
public class gMain {

    public gMain(){}


    public static void main(String[] args) throws IOException, AlreadyBoundException, MaxPlayerException, NotBoundException {
        Node mySelf;
        boolean isServer = false;
        List<Player> plist = new ArrayList();
        if(args.length >= 3){
            mySelf = new Node(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            if(Integer.parseInt(args[2]) == 1){
                isServer = true;
            }

            mySelf.initializeNode(isServer);

            if(isServer)
                mySelf.registerToGame(isServer,0);
            else
                mySelf.registerToGame(isServer,1234);

            for(int i=0;i<255; i++)
                System.out.println(mySelf.askForIp());
        } else {
            System.out.println("Servono 3 parametri: porta numerogiocatori 1\n" +
                    "con 1 viene istanziato il registro dei giocatori");

        }

    }
}
