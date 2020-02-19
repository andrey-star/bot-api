package ru.ok.botapi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMICommentInterface extends Remote {

    List<RMIComment> getComments(List<Pair> postIds) throws RemoteException;

}
