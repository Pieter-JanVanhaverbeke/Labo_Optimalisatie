package tvh.dataClasses;

public class Location {

    private long lon;
    private long lat;
    private String name;
    private Type type;

    public Location(long lon, long lat, String name, Type type) {
        this.lon = lon;
        this.lat = lat;
        this.name = name;
        this.type = type;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type{
        LOCATION,
        DEPOT;
    }
}
