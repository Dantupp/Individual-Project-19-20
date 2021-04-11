package danieltupper2.com.mobilelanguagelearner;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"LocationID"})
public class Marker {
    @NonNull private int LocationID;
    @NonNull private String PlaceName;

    public Marker(int LocationID, String PlaceName){
        this.LocationID = LocationID;
        this.PlaceName = PlaceName;
    }

    public int getLocationID() {
        return LocationID;
    }

    public String getPlaceName() {
        return PlaceName;
    }

}