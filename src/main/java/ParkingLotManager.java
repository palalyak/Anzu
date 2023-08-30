import java.util.*;
import java.util.stream.Collectors;

public class ParkingLotManager {
    public int isNightParking(List<VehicleLog> validLogs) {
        int count = 0;
        Map<Integer, List<VehicleLog>> vehicleLogsMap = LogManager.groupLogsBy("vehicleId", validLogs);

        for (List<VehicleLog> logs : vehicleLogsMap.values()) {
            boolean hasEntrance = false;
            boolean hasExit = false;
            for (VehicleLog log : logs) {
                if ("ENTRANCE".equals(log.getActionType())) {
                    hasEntrance = true;
                } else if ("EXIT".equals(log.getActionType())) {
                    hasExit = true;
                }

                if (hasEntrance && hasExit && haveDifferentDays(logs.get(0).getTimestamp(), log.getTimestamp())) {
                    if (isBefore23_30_00(logs.get(0).getTimestamp()) && isAfter05_59_59(log.getTimestamp())) {
                        count++;
                    }
                    hasEntrance = false;
                    hasExit = false;
                    break;
                }
            }
        }
        LogManager.printLogs(String.valueOf(count), "The number of vehicles in the night parking: ");
        return count;
    }

    public Map<String, Integer> numberOfVehicles(List<VehicleLog> logs) {
        Map<String, Integer> countMap = new HashMap<>();

        Map<Integer, List<VehicleLog>> vehicleLogsMap = LogManager.groupLogsBy("vehicleId", logs);

        for (List<VehicleLog> vehicleLogs : vehicleLogsMap.values()) {
            for (VehicleLog log : vehicleLogs) {
                if ("ENTRANCE".equals(log.getActionType())) {
                    String vehicleType = log.getVehicleType();
                    int allowedHours = getHoursAllowed(vehicleType);

                    Optional<VehicleLog> exitLog = vehicleLogs.stream()
                            .filter(exit -> "EXIT".equals(exit.getActionType()))
                            .findFirst();

                    if (exitLog.isPresent()) {
                        long entranceTimeMillis = log.getTimestamp().getTime();
                        long exitTimeMillis = exitLog.get().getTimestamp().getTime();
                        long hoursSpent = (exitTimeMillis - entranceTimeMillis) / (60 * 60 * 1000);

                        if (hoursSpent > allowedHours) {
                            countMap.put(vehicleType, countMap.getOrDefault(vehicleType, 0) + 1);
                        }
                    }
                }
            }
        }

        String result = countMap.entrySet().stream()
                .map(entry -> entry.getKey() + " : " + entry.getValue())
                .collect(Collectors.joining("\n"));
        LogManager.printLogs(result, "Number of vehicles that spent more than allowed hours:");
        return countMap;
    }

    private int getHoursAllowed(String vehicleType) {
        switch (vehicleType) {
            case "TRUCK":
                return 3;
            case "MOTORCYCLE":
                return 1;
            case "CAR":
                return 2;
            default:
                return 0;
        }
    }

    public void countVehiclesByType(List<VehicleLog> logs) {
        Map<String, Integer> vehicleTypeCountMap = new HashMap<>();
        Map<Integer, List<VehicleLog>> typeLogsMap = LogManager.groupLogsBy("vehicleType", logs);

        for (List<VehicleLog> typeLogs : typeLogsMap.values()) {
            for (VehicleLog log : typeLogs) {
                if ("ENTRANCE".equals(log.getActionType())) {
                    String vehicleId = log.getVehicleId();
                    String vehicleType = log.getVehicleType();

                    Optional<VehicleLog> exitLog = typeLogs.stream()
                            .filter(exit -> "EXIT".equals(exit.getActionType()) && exit.getVehicleId().equals(vehicleId))
                            .findFirst();

                    if (exitLog.isEmpty()) {
                        vehicleTypeCountMap.put(vehicleType, vehicleTypeCountMap.getOrDefault(vehicleType, 0) + 1);
                    }
                }
            }
        }

        String result = vehicleTypeCountMap.entrySet().stream()
                .map(entry -> entry.getKey() + " : " + entry.getValue())
                .collect(Collectors.joining("\n"));
        LogManager.printLogs(result, "Number of vehicles for each type currently in the parking lot:");
    }

    public void findBusiestHour(List<VehicleLog> logs) {
        Map<Integer, Integer> hourCountMap = new HashMap<>();
        int maxCount = 0;
        Set<Integer> busiestHours = new HashSet<>();
        Map<Integer, List<VehicleLog>> hourLogsMap = LogManager.groupLogsBy("Hour", logs);

        for (List<VehicleLog> hourLogs : hourLogsMap.values()) {
            for (VehicleLog log : hourLogs) {
                if ("ENTRANCE".equals(log.getActionType())) {
                    Date timestamp = log.getTimestamp();
                    String hourStr = LogManager.getFormattedDate("HH").format(timestamp);
                    int hour = Integer.parseInt(hourStr);

                    int count = hourCountMap.getOrDefault(hour, 0) + 1;
                    hourCountMap.put(hour, count);

                    if (count > maxCount) {
                        maxCount = count;
                        busiestHours.clear();
                        busiestHours.add(hour);
                    } else if (count == maxCount) {
                        busiestHours.add(hour);
                    }
                }
            }
        }
        if (!busiestHours.isEmpty()) {
            for (int hour : busiestHours) {
                LogManager.printLogs(hour + ":00", "The busiest hour(s) on the parking lot:");
            }
        } else {
            LogManager.printLogs("!!! ", "No data available to determine the busiest hour.");
        }
    }

    private static boolean haveDifferentDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        return !(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));
    }

    private static boolean isBefore23_30_00(Date timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour < 23 || minute <= 29;
    }

    private static boolean isAfter05_59_59(Date timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return hour > 5 || (hour == 5 && minute == 59 && second == 59);
    }

}