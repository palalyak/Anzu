import java.text.ParseException;
import java.util.*;


public class Main {

    public static void main(String[] args) throws ParseException {
        ParkingLotManager manager = new ParkingLotManager();
        List<String[]> logs = new ArrayList<>();
        logs.add(new String[]{"11349", "MOTORCYCLE", "2020-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11349", "MOTORCYCLE", "2020-01-01T18:10:00.000Z", "EXIT"});
        logs.add(new String[]{"11351", "MOTORCYCLE", "2020-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11350", "MOTORCYCLE", "2020-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11354", "TRUCK", "2020-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11354", "TRUCK", "2020-01-03T14:00:00.000Z", "EXIT"});
//        logs.add(new String[]{"11355", "CAR", "2022-01-01T15:00:00.000Z", "ENTRANCE"});

        manager.processLogs(logs);

        for (VehicleLog log : manager.getVehicleLogList()) {
            System.out.println(log);
        }
        List<VehicleLog> validLogs = manager.getVehicleLogList();

        manager.isNightParking(validLogs);
        manager.numberOfVehicles(validLogs);
        manager.countVehiclesByType(validLogs);
        manager.findBusiestHour(validLogs);
    }


}
