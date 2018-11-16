public class Machine {
    private int id;
    private int machineTypeId;      //TODO nog denken over id of machine meegeven

    private Location location;

    private int volume;
    private int servicetime;
    private String name;


    public Machine() {
        this.id = -1;
        this.machineTypeId = 0;
        this.location = null;
    }

    public Machine(int id, int machineTypeId, Location location, MachineType machineType) {
        this.id = id;
        this.machineTypeId = machineTypeId;
        this.location = location;

        this.volume = machineType.getVolume();
        this.servicetime = machineType.getServicetime();
        this.name = machineType.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMachineTypeId() {
        return machineTypeId;
    }

    public void setMachineTypeId(int machineTypeId) {
        this.machineTypeId = machineTypeId;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getServicetime() {
        return servicetime;
    }

    public void setServicetime(int servicetime) {
        this.servicetime = servicetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", machineTypeId=" + machineTypeId +
                ", location=" + location +
                ", volume=" + volume +
                ", servicetime=" + servicetime +
                ", name='" + name + '\'' +
                '}';
    }
}
