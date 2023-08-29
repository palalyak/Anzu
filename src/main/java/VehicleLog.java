import java.util.Date;

public class VehicleLog {
    private String vehicleId;
    private String vehicleType;
    private Date timestamp;
    private String actionType;

    public VehicleLog(String vehicleId, String vehicleType, Date timestamp, String actionType) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.timestamp = timestamp;
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "VehicleLog{" +
                "vehicleId='" + vehicleId + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", timestamp=" + timestamp +
                ", actionType='" + actionType + '\'' +
                '}';
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getActionType() {
        return actionType;
    }
}
