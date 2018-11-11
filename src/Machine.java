public class Machine {
    private int id;
    private int machineTypeId;      //TODO nog denken over id of machine meegeven
    private int locationid;
    private MachineType machineType;

    public Machine(int id, int machineTypeId, int locationid, MachineType machineType) {
        this.id = id;
        this.machineTypeId = machineTypeId;
        this.locationid = locationid;
        this.machineType = machineType;



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

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", machineTypeId=" + machineTypeId +
                ", locationid=" + locationid +
                ", machineType=" + machineType +
                '}';
    }
}
