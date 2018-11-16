public class Collect {
    private int id;
    private Machine machine;

    //TODO boolean collect
    public Collect(int id, Machine machine) {
        this.id = id;
        this.machine = machine;
        boolean collect;
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
        return "Collect{" +
                "id=" + id +
                ", machine=" + machine +
                '}';
    }
}
