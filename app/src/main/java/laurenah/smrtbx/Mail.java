package laurenah.smrtbx;

public class Mail {

    public String time, key;

    public Mail (String time, String key) {
        this.time = time;
        this.key = key;
    }

    public Mail() {

    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "time='" + time + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
