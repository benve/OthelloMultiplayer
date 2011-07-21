package com.github.benve.othellomultiplayer;

import com.github.benve.othellomultiplayer.game.Board;
import com.github.benve.othellomultiplayer.game.BoardLogic;
import com.github.benve.othellomultiplayer.gui.Gui;
import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.network.Message;
import com.github.benve.othellomultiplayer.network.Node;
import com.github.benve.othellomultiplayer.utils.NetUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 13.25
 * To change this template use File | Settings | File Templates.
 */
public class gMain {

    public gMain(){}


    public static void main(String[] args) throws IOException, AlreadyBoundException, MaxPlayerException, NotBoundException, InterruptedException {
        Node mySelf;
        boolean isServer = false;
        BoardLogic bl1 = BoardLogic.getInstance();
        Board b1;
        int port;



        NetUtils n = NetUtils.getInstance();

        //n.getPublicIP().toString());
        if(args.length == 1){
            mySelf = new Node("Server",1234,2);
            mySelf.initializeNode(true);
            mySelf.registerToGame(true,n.getHostAddress());
        }else{
            mySelf = new Node("Client");
            mySelf.initializeNode(false);
            mySelf.registerToGame(false,"192.168.1.203");
        }

        Message msg = new Message(mySelf.me.getUuid());

        for(int i=0;i<25;i++){
            Thread.sleep(1000);
            mySelf.startBroadcast(msg);
        }

    }
}
