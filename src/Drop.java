public class Drop {
    private int id;
    private int machineId;          //van lijst van machines die elk id hebben
    private int locationId;

    public Drop(int id, int machineId, int locationId) {
        this.id = id;
        this.machineId = machineId;
        this.locationId = locationId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
