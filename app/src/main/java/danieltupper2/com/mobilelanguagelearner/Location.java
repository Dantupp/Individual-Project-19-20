package danieltupper2.com.mobilelanguagelearner;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"ID"})
public class Location {
    @NonNull private int ID;
    @NonNull private double Lat;
    @NonNull private double Lng;

    public Location(int ID, double Lat, double Lng){
        this.ID = ID;
        this.Lat = Lat;
        this.Lng = Lng;
    }

    public double getLng() {
        return Lng;
    }

    public double getLat() {
        return Lat;
    }

    public int getID() {
        return ID;
    }
}
