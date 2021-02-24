import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Reducer extends UnicastRemoteObject implements ReducerMapperInterface, ReducerMasterInterface {
    private final StorageReducerInterface sri;

    public Reducer() throws RemoteException, MalformedURLException, NotBoundException {
        sri = (StorageReducerInterface) Naming.lookup("rmi://localhost:2025/storage");
    }

    @Override
    public void calculateStatistics(String mapperId, String reducerId, int fileCount) throws RemoteException {
        LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap = sri.getTimeHarMap();
        if (timeHarMap == null) { return; }

        List<ProcessCombinationModel> combinationsReducer = sri.getCombinationsReducer(mapperId, reducerId);
        if (combinationsReducer == null) { return; }

        for(ProcessCombinationModel combinationInfo : combinationsReducer) {
            boolean resourceFound = false;

            String[] resources = combinationInfo.combination.split(","); // resources of each combination
            for (int i = 0; i < fileCount; i++) { //controlo por run
                for (String combinationResource : resources) {
                    resourceFound = false;
                    for (ResourceInfo comb : timeHarMap.get(combinationResource))
                        if (comb.harRun == i) {
                            resourceFound = true;
                            combinationInfo.resourceLength += comb.resourceLength;
                            break;
                        }
                    if (!resourceFound) {
                        break;
                    }
                }
                if (resourceFound) {
                    combinationInfo.numberOfRuns++;
                }
            }

            combinationInfo.percentage = (float) combinationInfo.numberOfRuns / fileCount;
        }

        sri.saveCombinationsReducer(mapperId, reducerId, combinationsReducer);
    }
}
