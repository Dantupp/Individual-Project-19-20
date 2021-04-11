package danieltupper2.com.mobilelanguagelearner;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkerDao {
    @Query("SELECT * FROM Marker")
    List<Marker> getAll();

    @Query("SELECT * FROM Marker WHERE LocationID = :LocationID")
    Marker getMarkerFromID(Integer LocationID);
}
