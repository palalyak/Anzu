import java.util.List;


public class Main {

    public static void main(String[] args) {
        ParkingLotManager manager = new ParkingLotManager();
        List<VehicleLog> logs = LogManager.getValidLogs();

        manager.isNightParking(logs);
        manager.numberOfVehicles(logs);
        manager.countVehiclesByType(logs);
        manager.findBusiestHour(logs);

        LogManager.sortLogBy("VehicleId", logs);
        LogManager.sortLogBy("VehicleType", logs);
        LogManager.sortLogBy("Timestamp", logs);
        LogManager.sortLogBy("ActionType", logs);

        LogManager.findLogBy("VehicleType", "MOTORCYCLE", logs);
        LogManager.findLogBy("VehicleId", "11354", logs);
        LogManager.findLogBy("Timestamp", "2014-01-01T14:00:00.000Z", logs);
        LogManager.findLogBy("ActionType", "ENTRANCE", logs);

        LogManager.groupLogsBy("vehicleType", logs);
        LogManager.groupLogsBy("vehicleId", logs);
        LogManager.groupLogsBy("timestamp", logs);
        LogManager.groupLogsBy("actionType", logs);
    }
}
