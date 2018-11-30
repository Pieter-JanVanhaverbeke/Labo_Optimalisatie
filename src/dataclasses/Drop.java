package dataclasses;

public class Drop {
    private int id;
    private int machineTypeId;          //van lijst van machines die elk id hebben

    private Machine machine;
    Location location;

    public Drop(int id, int machineTypeId, Location location) {
        this.id = id;
        this.machineTypeId = machineTypeId;
        this.machine = null;
        this.location = location;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public int getMachineTypeId() {
        return machineTypeId;
    }

    public void setMachineTypeId(int machineTypeId) {
        this.machineTypeId = machineTypeId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
