public class Machine {
    private int id;
    private int machineTypeId;      //TODO nog denken over id of machine meegeven

    private MachineType machineType;
    private Location location;
    private boolean collect;


    public Machine() {
        this.id = -1;
        this.machineTypeId = 0;
        this.machineType = null;
        this.location = null;
        this.collect = false;
    }

    public Machine(int id, int machineTypeId, Location location, MachineType machineType) {
        this.id = id;
        this.machineTypeId = machineTypeId;
        this.machineType = machineType;
        this.location = location;
        this.collect = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMachineTypeId() {
        return machineTypeId;
    }

    public void setMachineTypeId(int machineTypeId) {
        this.machineTypeId = machineTypeId;
    }

    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }


    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", machineTypeId=" + machineTypeId +
                ", machineType=" + machineType +
                ", location=" + location +
                '}';
    }
}
