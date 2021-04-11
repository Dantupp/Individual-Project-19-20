package danieltupper2.com.mobilelanguagelearner;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VocabDao {
    @Query("SELECT * FROM Vocab")
    List<Vocab> getAll();

    @Query("SELECT * FROM Vocab WHERE LocationID = :LocationID")
    List<Vocab> getVocabFromID(Integer LocationID);
}
