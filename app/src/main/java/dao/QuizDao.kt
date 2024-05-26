package dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizapp.QuizModel



@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizModel)

    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): List<QuizModel>



    // Add other methods for database operations as needed
}

