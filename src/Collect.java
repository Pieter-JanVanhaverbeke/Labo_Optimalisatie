public class Collect {
    private int id;
    private int machineId;          //van lijst van machines die elk id hebben

    public Collect(int id, int machineId) {
        this.id = id;
        this.machineId = machineId;
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

    @Override
    public String toString() {
        return "Collect{" +
                "id=" + id +
                ", machineId=" + machineId +
                '}';
    }
}
