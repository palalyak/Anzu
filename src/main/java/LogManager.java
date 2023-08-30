import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LogManager {

    public static List<VehicleLog> sortLogBy(String key, List<VehicleLog> logs) {
        Comparator<VehicleLog> comparator = getComparatorByKey(key);
        List<VehicleLog> sortLogs = logs.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        printLogs(sortLogs, key);
        return sortLogs;
    }

    public static List<VehicleLog> findLogBy(String key, String value, List<VehicleLog> logs) {
        List<VehicleLog> groupLogs = logs.stream()
                .filter(log -> matchesKey(log, key, value))
                .collect(Collectors.toList());
        printLogs(groupLogs, key, value);
        return groupLogs;
    }

    public static Map<Integer, List<VehicleLog>> groupLogsBy(String key, List<VehicleLog> logs) {
        Map<String, List<VehicleLog>> logGroups = new HashMap<>();

        for (VehicleLog log : logs) {
            String value = getValueForKey(log, key);
            logGroups.computeIfAbsent(value, k -> new ArrayList<>()).add(log);
        }

        Map<Integer, List<VehicleLog>> groupedLogs = new HashMap<>();
        int counter = 1;
        for (List<VehicleLog> group : logGroups.values()) {
            groupedLogs.put(counter++, group);
        }
        String result = groupedLogs.entrySet().stream()
                .map(entry -> entry.getKey() + " group : " + entry.getValue())
                .collect(Collectors.joining("\n"));

        printLogs(result, key);
        return groupedLogs;
    }

    private static boolean matchesKey(VehicleLog log, String key, String value) {
        switch (key) {
            case "VehicleId":
                return log.getVehicleId().equals(value);
            case "VehicleType":
                return log.getVehicleType().equals(value);
            case "Timestamp":
                return log.getTimestamp().equals(parseDate(value));
            case "ActionType":
                return log.getActionType().equals(value);
            default:
                throw new IllegalArgumentException("Invalid key");
        }
    }

    private static Comparator<VehicleLog> getComparatorByKey(String key) {
        switch (key) {
            case "VehicleId":
                return Comparator.comparing(VehicleLog::getVehicleId);
            case "VehicleType":
                return Comparator.comparing(VehicleLog::getVehicleType);
            case "Timestamp":
                return Comparator.comparing(VehicleLog::getTimestamp);
            case "ActionType":
                return Comparator.comparing(VehicleLog::getActionType);
            default:
                throw new IllegalArgumentException("Invalid sorting key");
        }
    }

    private static String getCallerMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 4) {
            StackTraceElement callerElement = stackTrace[3];
            String callerClassName = callerElement.getClassName();
            String callerMethodName = callerElement.getMethodName();
            return callerClassName + "." + callerMethodName;
        } else {
            return "Unknown Caller";
        }
    }

    private static Date parseDate(String value) {
        try {
            return getFormattedDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }

    private static String getValueForKey(VehicleLog log, String key) {
        switch (key) {
            case "vehicleId":
                return log.getVehicleId();
            case "vehicleType":
                return log.getVehicleType();
            case "actionType":
                return log.getActionType();
            case "timestamp":
                return String.valueOf(log.getTimestamp());
            case "Hour":
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                return hourFormat.format(log.getTimestamp());
            default:
                throw new IllegalArgumentException("Invalid key");
        }
    }

    public static void printLogs(List<VehicleLog> logs, String...key) {
        String callerMethodName = getCallerMethodName();
        System.out.printf("========================= LOGS %s ==> %s =====================%n",
                callerMethodName.toUpperCase().toUpperCase(), Arrays.toString(key).toUpperCase());

        int count = 1;
        for (VehicleLog log : logs) {
            System.out.println(count++ + ") " + log);
        }
        System.out.println(" ");
    }

    public static void printLogs(String value, String key) {
        String callerMethodName = getCallerMethodName();
        System.out.printf("========================= LOGS %s ==> %s =====================%n",
                callerMethodName.toUpperCase().toUpperCase(), key.toUpperCase());
        System.out.println(value);
        System.out.println(" ");
    }

    public static SimpleDateFormat getFormattedDate(String pattern) {
        return new SimpleDateFormat(pattern);
    }

}
