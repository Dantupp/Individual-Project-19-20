package danieltupper2.com.mobilelanguagelearner;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Location.class,Marker.class,Vocab.class},version=1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract MarkerDao MarkerDao();
    public abstract LocationDao LocationDao();
    public abstract VocabDao VocabDao();

    public synchronized static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,AppDatabase.class,"AppRelationalDatabase.db").createFromAsset("AppRelationalDatabase.db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
