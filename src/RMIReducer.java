import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIReducer {
    public static void main(String[] args) {
        Registry r = null;

        try {
            r = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            Reducer reducer = new Reducer();
            assert r != null;
            r.bind("reducer", reducer);
            System.out.println("Reducer server ready");
        } catch (Exception e) {
            System.out.println("Reducer server main " + e.getMessage());
        }
    }
}
