package dataclasses;

public class Collect {
    private int id;
    private Machine machine;

    public Collect(int id, Machine machine) {
        this.id = id;
        this.machine = machine;
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



    @Override
    public String toString() {
        return "dataclasses.Collect{" +
                "id=" + id +
                ", machine=" + machine +
                '}';
    }
}
