import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LogValidator {
    private static List<VehicleLog> vehicleLogList;

    public LogValidator() {
        vehicleLogList = new ArrayList<>();
    }

    public void processLogs(List<String[]> logs) {
        logs.sort(Comparator.comparing(log -> log[0]));
        for (String[] logData : logs) {
            if (isValidLog(logData)) {
                try {
                    Date timestamp = LogManager.getFormattedDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(logData[2]);
                    VehicleLog vehicleLog = new VehicleLog(logData[0], logData[1], timestamp, logData[3]);
                    vehicleLogList.add(vehicleLog);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Error parsing timestamp: " + logData[2], e);
                }
            }
        }
    }

    public boolean isValidLog(String[] logData) {
        if (logData.length < 4) {
            return false;
        }
        if(!isValidTimestamps(logData)) {
            return false;
        }
        return true;
    }

    private boolean isValidTimestamps(String[] logData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC);
            try {
                Instant instant = Instant.from(formatter.parse(logData[2]));
                int year = instant.atZone(ZoneOffset.UTC).getYear();
                int month = instant.atZone(ZoneOffset.UTC).getMonthValue();
                int day = instant.atZone(ZoneOffset.UTC).getDayOfMonth();
                int hour = instant.atZone(ZoneOffset.UTC).getHour();
                int minutes = instant.atZone(ZoneOffset.UTC).getMinute();
                int seconds = instant.atZone(ZoneOffset.UTC).getSecond();
                int milliseconds = instant.getNano() / 1_000_000;

                boolean isValidYear = year >= 1970 && year <= 2038;
                boolean isValidMonth = month >= 1 && month <= 12;
                boolean isValidDay = day >= 1 && day <= 31;
                boolean isValidHour = hour >= 0 && hour <= 23;
                boolean isValidMinutes = minutes >= 0 && minutes <= 59;
                boolean isValidSeconds = seconds >= 0 && seconds <= 59;
                boolean isValidMilliseconds = milliseconds >= 0 && milliseconds <= 999;

                if (!(isValidYear && isValidMonth && isValidDay && isValidHour && isValidMinutes && isValidSeconds && isValidMilliseconds)) {
                    return false;
                }
            } catch (DateTimeParseException  e) {
                return false;
            }

        return true;
    }

    public static List<VehicleLog> getVehicleLogList() {
        if (vehicleLogList.isEmpty()) {
            throw new IllegalArgumentException("No logs to check");
        }
        return vehicleLogList;
    }


}
