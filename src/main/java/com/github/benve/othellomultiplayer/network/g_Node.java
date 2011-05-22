package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.g_Player;
import com.sun.org.apache.xpath.internal.operations.Mult;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 10.51
 * To change this template use File | Settings | File Templates.
 */
public class g_Node extends UnicastRemoteObject implements g_Node_Remote {

    private g_Player me;
    private Registry registry;
    public List<g_Player> allPlayer;
    private int maxplayer;
    private g_Registration reg1;

    public g_Node(int n_port) throws RemoteException, AlreadyBoundException {
        super();
        me = new g_Player(n_port);
        allPlayer = new ArrayList<g_Player>();
        maxplayer = 3;
    }

    public g_Node(String Name) throws IOException {
        super();
        ServerSocket ss = new ServerSocket(0);
        int freeport = ss.getLocalPort();
        ss.close();

        System.out.println("porta:"+freeport);

        me = new g_Player(Name,"localhost",freeport);
        allPlayer = new ArrayList<g_Player>();
        maxplayer = 4;
    }

    public g_Node(int n_port,int n_player) throws RemoteException, AlreadyBoundException {
        super();
        me = new g_Player(n_port);
        allPlayer = new ArrayList<g_Player>();
        maxplayer = n_player;
    }

    public g_Node(String Name, int port, int n_player) throws IOException {
        super();
        me = new g_Player(Name,"localhost",port);
        allPlayer = new ArrayList<g_Player>();
        maxplayer = n_player;
    }

    public void initializeNode(boolean server) throws RemoteException, AlreadyBoundException {
        Registry register;
        register = LocateRegistry.createRegistry(this.me.getPort());
        this.registry = register;
        this.registry.bind("g_Node",this);

        if(server)
            this.setRegister();

    }

    private void setRegister() throws RemoteException, AlreadyBoundException {
        reg1 = new g_Registration(me.getPort(),this.maxplayer);
        reg1.instaceRegistration();
    }

    public void registerToGame(boolean server,int rPort) throws MaxPlayerException, RemoteException, NotBoundException {
        int regPort;
        if(server)
            regPort = this.me.getPort();
        else
            regPort = rPort;

        Registry register = LocateRegistry.getRegistry(regPort);
        this.registry = register;
        g_Registration_Remote r_reg =  (g_Registration_Remote) this.registry.lookup("g_Reg");

        r_reg.register(this.me);
        this.allPlayer = r_reg.getPlayerList();

    }

    public String askForIp() throws RemoteException, NotBoundException {
        int position = 0;
        for(int i=0;i<this.allPlayer.size();i++){
            if(me.getUuid().equals(allPlayer.get(i).getUuid())){
                System.out.println("Trovato!"+i);
                position = i;
                break;
            }
        }

        Registry register = LocateRegistry.getRegistry(allPlayer.get((position+1)%maxplayer).getPort());

        g_Node_Remote gRem = (g_Node_Remote) register.lookup("g_Node");
        return gRem.replyForIp();


    }

    public String replyForIp() throws RemoteException{
        return (me.getIpAddress()+":"+me.getPort()).toString();//me.getUuid().toString();
    }

}
