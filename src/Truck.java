public class Truck {
    //Constanten
    private static final int TRUCK_CAPACITY = 100;
    private static final int TRUCK_WORKING_TIME = 600;

    private int startlocation;            //begin pos meegeven bij constructor
    private int endlocation;

    private int id;
    private String name;
    private int geredenminuten;


    public Truck(int startlocation, int endlocation, int id, String name) {
        this.startlocation = startlocation;
        this.endlocation = endlocation;
        this.id = id;
        this.name = name;
        this.geredenminuten = 0;
    }

    public static int getTruckCapacity() {
        return TRUCK_CAPACITY;
    }

    public static int getTruckWorkingTime() {
        return TRUCK_WORKING_TIME;
    }

    public int getStartlocation() {
        return startlocation;
    }

    public void setStartlocation(int startlocation) {
        this.startlocation = startlocation;
    }

    public int getEndlocation() {
        return endlocation;
    }

    public void setEndlocation(int endlocation) {
        this.endlocation = endlocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeredenminuten() {
        return geredenminuten;
    }

    public void setGeredenminuten(int geredenminuten) {
        this.geredenminuten = geredenminuten;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "startlocation=" + startlocation +
                ", endlocation=" + endlocation +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
