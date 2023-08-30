import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        ParkingLotManager manager = new ParkingLotManager();
        List<String[]> logs = new ArrayList<>();
        logs.add(new String[]{"11349", "MOTORCYCLE", "2019-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11349", "MOTORCYCLE", "2023-01-01T18:10:00.000Z", "EXIT"});
        logs.add(new String[]{"11351", "MOTORCYCLE", "2021-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11350", "MOTORCYCLE", "2015-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11350", "MOTORCYCLE", "2015-01-01T14:00:00.000Z", "EXIT"});
        logs.add(new String[]{"11354", "TRUCK", "2014-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11354", "TRUCK", "2016-01-03T14:00:00.000Z", "EXIT"});

        manager.processLogs(logs);
        List<VehicleLog> validLogs = manager.getVehicleLogList();

        manager.isNightParking(validLogs);
        manager.numberOfVehicles(validLogs);
        manager.countVehiclesByType(validLogs);
        manager.findBusiestHour(validLogs);

        LogManager.sortLogBy("VehicleId", validLogs);
        LogManager.sortLogBy("VehicleType", validLogs);
        LogManager.sortLogBy("Timestamp", validLogs);
        LogManager.sortLogBy("ActionType", validLogs);

        LogManager.findLogBy("VehicleType", "MOTORCYCLE", validLogs);
        LogManager.findLogBy("VehicleId", "11354", validLogs);
        LogManager.findLogBy("Timestamp", "2014-01-01T14:00:00.000Z", validLogs);
        LogManager.findLogBy("ActionType", "ENTRANCE", validLogs);

        LogManager.groupLogsBy("vehicleType", validLogs);
        LogManager.groupLogsBy("vehicleId", validLogs);
        LogManager.groupLogsBy("timestamp", validLogs);
        LogManager.groupLogsBy("actionType", validLogs);
    }
}
