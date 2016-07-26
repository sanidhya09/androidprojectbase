package xicom.com.baselibrary.locations;

public class LocationConfig {
    public long interval;
    public int priority;

    public LocationConfig setInterval(long interval) {
        this.interval = interval;
        return this;
    }

    public LocationConfig setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}