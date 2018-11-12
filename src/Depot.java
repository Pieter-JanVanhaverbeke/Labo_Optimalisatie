public class Depot {
    private int id;
    private int locationid;

    private Location location;

    public Depot(int id, int locationid, Location location) {
        this.id = id;
        this.locationid = locationid;

        this.location = location;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id=" + id +
                ", locationid=" + locationid +
                ", location=" + location +
                '}';
    }
}
