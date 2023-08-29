import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ParkingLotManager {
    private List<VehicleLog> vehicleLogList;

    public ParkingLotManager() {
        vehicleLogList = new ArrayList<>();
    }


    public void processLogs(List<String[]> logs) {
        logs.sort(Comparator.comparing(log -> log[0]));
        for (String[] logData : logs) {
            if (isValidLog(logData)) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date timestamp = dateFormat.parse(logData[2]);

                    VehicleLog vehicleLog = new VehicleLog(logData[0], logData[1], timestamp, logData[3]);
                    vehicleLogList.add(vehicleLog);
                } catch (ParseException e) {
                    System.err.println("Error parsing timestamp: " + logData[2]);
                }
            }
        }
    }

    private boolean isValidLog(String[] logData) {
        if (logData.length < 4) {
            return false;
        }

        String timestampStr = logData[2];
        if (timestampStr.length() != 24 || !timestampStr.endsWith("Z")) {
            return false;
        }

        String regex = "^(0{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z|" +
                "\\d{4}-0{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z|" +
                "\\d{4}-\\d{2}-0{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z|" +
                "0{4}-0{2}-0{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z)$";

        if (timestampStr.matches(regex)) {
            return false;
        }

        Date timestamp;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            timestamp = dateFormat.parse(timestampStr);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public int isNightParking(List<VehicleLog> validLogs) {
        int count = 0;
        Map<String, List<VehicleLog>> vehicleLogsMap = validLogs.stream()
                .collect(Collectors.groupingBy(VehicleLog::getVehicleId));

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
        System.out.println("The number of vehicles staying overnight in the parking lot.: " + count);
        return count;
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

    public List<VehicleLog> getVehicleLogList() {
        return vehicleLogList;
    }


    public Map<String, Integer> numberOfVehicles(List<VehicleLog> logs) {
        Map<String, Integer> countMap = new HashMap<>();

        for (VehicleLog log : logs) {
            if ("ENTRANCE".equals(log.getActionType())) {
                String vehicleType = log.getVehicleType();
                int allowedHours = getHoursAllowed(vehicleType);

                Optional<VehicleLog> exitLog = logs.stream()
                        .filter(exit -> "EXIT".equals(exit.getActionType()) && exit.getVehicleId().equals(log.getVehicleId()))
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

        System.out.println("Number of vehicles that spent more than allowed hours:");
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

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

        for (VehicleLog log : logs) {
            if ("ENTRANCE".equals(log.getActionType())) {
                String vehicleId = log.getVehicleId();
                String vehicleType = log.getVehicleType();

                Optional<VehicleLog> exitLog = logs.stream()
                        .filter(exit -> "EXIT".equals(exit.getActionType()) && exit.getVehicleId().equals(vehicleId))
                        .findFirst();

                if (exitLog.isPresent()) {
                    continue;
                }
                vehicleTypeCountMap.put(vehicleType, vehicleTypeCountMap.getOrDefault(vehicleType, 0) + 1);
            }
        }

        System.out.println("Number of vehicles for each type currently in the parking lot:");
        for (Map.Entry<String, Integer> entry : vehicleTypeCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }


    public void findBusiestHour(List<VehicleLog> logs) {
        Map<Integer, Integer> hourCountMap = new HashMap<>();
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

        int maxCount = 0;
        Set<Integer> busiestHours = new HashSet<>();

        for (VehicleLog log : logs) {
            if ("ENTRANCE".equals(log.getActionType())) {
                Date timestamp = log.getTimestamp();
                String hourStr = hourFormat.format(timestamp);
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

        if (!busiestHours.isEmpty()) {
            System.out.println("The busiest hour(s) on the parking lot:");
            for (int hour : busiestHours) {
                System.out.println(hour + ":00");
            }
        } else {
            System.out.println("No data available to determine the busiest hour.");
        }
    }

}