public class Depot {
    private int id;
    private int locationid;

    public Depot(int id, int locationid) {
        this.id = id;
        this.locationid = locationid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id=" + id +
                ", locationid=" + locationid +
                '}';
    }
}
