import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReducerMapperInterface extends Remote {
    void calculateStatistics(String mapperId, String storageId, int fileCount) throws RemoteException;
}
