package danieltupper2.com.mobilelanguagelearner;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Query("SELECT * FROM Location WHERE ID = :ID")
    Location getLocationFromID(Integer ID);
}
