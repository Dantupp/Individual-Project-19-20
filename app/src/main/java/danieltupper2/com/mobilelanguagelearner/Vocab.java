package danieltupper2.com.mobilelanguagelearner;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity (primaryKeys = {"LocationID","English"})
public class Vocab {
    @NonNull private int LocationID;
    @NonNull private String English;
    @NonNull private String Hungarian;
    @NonNull private int Difficulty;

    public Vocab(int LocationID, String English, String Hungarian, int Difficulty){
        this.LocationID = LocationID;
        this.English = English;
        this.Hungarian = Hungarian;
        this.Difficulty = Difficulty;
    }

    public int getLocationID() {
        return LocationID;
    }

    public int getDifficulty() {
        return Difficulty;
    }

    public String getEnglish() {
        return English;
    }

    public String getHungarian() {
        return Hungarian;
    }
}
