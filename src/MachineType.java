public class MachineType {
    private int id;
    private int volume;
    private int servicetime;
    private String name;

    public MachineType(int id, int volume, int servicetime, String name) {
        this.id = id;
        this.volume = volume;
        this.servicetime = servicetime;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "MachineType{" +
                "id=" + id +
                ", volume=" + volume +
                ", servicetime=" + servicetime +
                ", name='" + name + '\'' +
                '}';
    }
}
