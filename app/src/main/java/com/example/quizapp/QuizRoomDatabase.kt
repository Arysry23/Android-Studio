import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizapp.QuizModel
import dao.QuizDao

@Database(entities = [QuizModel::class], version = 1, exportSchema = false)
abstract class QuizRoomDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao


    companion object {
        @Volatile
        private var INSTANCE: QuizRoomDatabase? = null

        fun getDatabase(context: Context): QuizRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizRoomDatabase::class.java,
                    "quiz_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
