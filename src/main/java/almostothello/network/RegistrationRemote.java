package almostothello.network;

import almostothello.game.Player;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 13.19
 */
public interface RegistrationRemote extends Remote {
    List<Player> register(Player pplay) throws MaxPlayerException, RemoteException, CantAddPlayerException;

    void instaceRegistration() throws RemoteException, AlreadyBoundException, UnknownHostException, SocketException;
}
