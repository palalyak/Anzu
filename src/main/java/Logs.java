import java.util.ArrayList;
import java.util.List;

public class Logs {
    private static List<String[]> logs;

    public Logs() {
    }

    public static List<VehicleLog> getLogs() {
        logs = new ArrayList<>();
        logs.add(new String[]{"MOTORCYCLE", "2023-01-01T18:10:00.000Z", "EXIT"});
        logs.add(new String[]{"11349", "MOTORCYCLE", "3019-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11351", "MOTORCYCLE", "2021-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11350", "MOTORCYCLE", "2015-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11350", "MOTORCYCLE", "2015-01-01T14:00:00.000Z", "EXIT"});
        logs.add(new String[]{"11354", "TRUCK", "2014-01-01T14:00:00.000Z", "ENTRANCE"});
        logs.add(new String[]{"11354", "TRUCK", "2016-01-03T14:00:00.000Z", "EXIT"});

        LogValidator logValidator = new LogValidator();
        logValidator.processLogs(logs);
        return LogValidator.getVehicleLogList();
    }
}
