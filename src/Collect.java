public class Collect {
    private int id;
    private int machineId;          //van lijst van machines die elk id hebben

    private Machine machine;

    public Collect(int id, int machineId, Machine machine) {
        this.id = id;
        this.machineId = machineId;
        this.machine = machine;
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

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "id=" + id +
                ", machineId=" + machineId +
                ", machine=" + machine +
                '}';
    }
}
