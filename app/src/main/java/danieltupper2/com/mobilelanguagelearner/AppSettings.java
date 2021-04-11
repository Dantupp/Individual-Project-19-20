package danieltupper2.com.mobilelanguagelearner;

public class AppSettings {
    private static AppSettings INSTANCE;
    private int difficulty = 1;

    public static AppSettings getInstance() {
        if(INSTANCE == null){
            INSTANCE = new AppSettings();
        }
        return INSTANCE;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
