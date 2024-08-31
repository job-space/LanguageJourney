package com.example.dictionaryapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.ImageButton





class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val queryW = "CREATE TABLE words ( id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, translation TEXT, favorite BOOLEAN, level INT)"
        db!!.execSQL(queryW)

        val queryN = "CREATE TABLE notification ( id INTEGER PRIMARY KEY AUTOINCREMENT, status BOOLEAN DEFAULT 1)"
        db.execSQL(queryN)

        // Adding a default string
        val insertDefault = "INSERT INTO notification (status) VALUES (1)"
        db.execSQL(insertDefault)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS words")
        db.execSQL("DROP TABLE IF EXISTS notification")
        onCreate(db)
    }

    // Notifications
    fun statusCheck(): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT status FROM notification", null)

        var status = false
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) > 0
        }

        cursor.close()
        db.close()
        return status
    }


    // Update the status in the database
    fun statusUpdate(st: Boolean): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("status", if (st) 1 else 0) // 1 for true, 0 for false
        }

        // Update the row with id=1 (if it is the only one)
        val rowsAffected = db.update("notification", values, "id = ?", arrayOf("1"))

        db.close()

        return rowsAffected > 0 // Return true if at least one line has been updated
    }






    // Filling the database with words
    fun addWords(word: Words) { ///////////////////////////////
        val values = ContentValues()
        values.put("word", word.word)
        values.put("translation", word.translation)
        values.put("favorite", word.favorite)
        values.put("level", word.level)

        val db = this.writableDatabase
        db.insert("words", null, values)

        db.close()
    }

    // Fill in the database
    fun dictionaryFull() {
        val wordsList = listOf(
            // Easy level (1)
            WordEntry("Goodbye", "До побачення", 1),
            WordEntry("Please", "Будь ласка", 1),
            WordEntry("Thank you", "Дякую", 1),
            WordEntry("Yes", "Так", 1),
            WordEntry("No", "Ні", 1),
            WordEntry("Hello", "Привіт", 1),
            WordEntry("Morning", "Ранок", 1),
            WordEntry("Evening", "Вечір", 1),
            WordEntry("Night", "Ніч", 1),
            WordEntry("Cat", "Кіт", 1),
            WordEntry("Dog", "Собака", 1),
            WordEntry("Fish", "Риба", 1),
            WordEntry("Bird", "Птах", 1),
            WordEntry("Red", "Червоний", 1),
            WordEntry("Blue", "Синій", 1),
            WordEntry("Green", "Зелений", 1),
            WordEntry("Yellow", "Жовтий", 1),
            WordEntry("Black", "Чорний", 1),
            WordEntry("White", "Білий", 1),
            WordEntry("Purple", "Фіолетовий", 1),
            WordEntry("Orange", "Помаранчевий", 1),
            WordEntry("Brown", "Коричневий", 1),
            WordEntry("Happy", "Щасливий", 1),
            WordEntry("Sad", "Сумний", 1),
            WordEntry("Angry", "Злий", 1),
            WordEntry("Tired", "Втомлений", 1),
            WordEntry("Hungry", "Голодний", 1),
            WordEntry("Thirsty", "Спраглий", 1),
            WordEntry("Hot", "Гарячий", 1),
            WordEntry("Cold", "Холодний", 1),
            WordEntry("Rain", "Дощ", 1),
            WordEntry("Snow", "Сніг", 1),
            WordEntry("Wind", "Вітер", 1),
            WordEntry("Sun", "Сонце", 1),
            WordEntry("Moon", "Місяць", 1),
            WordEntry("Star", "Зірка", 1),
            WordEntry("Sky", "Небо", 1),
            WordEntry("Tree", "Дерево", 1),
            WordEntry("Flower", "Квітка", 1),
            WordEntry("Grass", "Трава", 1),
            WordEntry("Leaf", "Листок", 1),
            WordEntry("Water", "Вода", 1),
            WordEntry("Milk", "Молоко", 1),
            WordEntry("Juice", "Сік", 1),
            WordEntry("Coffee", "Кава", 1),
            WordEntry("Tea", "Чай", 1),
            WordEntry("Bread", "Хліб", 1),
            WordEntry("Butter", "Масло", 1),
            WordEntry("Cheese", "Сир", 1),
            WordEntry("Egg", "Яйце", 1),
            WordEntry("Chicken", "Курка", 1),
            WordEntry("Fish", "Риба", 1),
            WordEntry("Beef", "Яловичина", 1),
            WordEntry("Pork", "Свинина", 1),
            WordEntry("Apple", "Яблуко", 1),
            WordEntry("Banana", "Банан", 1),
            WordEntry("Orange", "Апельсин", 1),
            WordEntry("Grape", "Виноград", 1),
            WordEntry("Lemon", "Лимон", 1),
            WordEntry("Peach", "Персик", 1),
            WordEntry("Cherry", "Вишня", 1),
            WordEntry("Strawberry", "Полуниця", 1),
            WordEntry("Potato", "Картопля", 1),
            WordEntry("Tomato", "Помідор", 1),
            WordEntry("Carrot", "Морква", 1),
            WordEntry("Onion", "Цибуля", 1),
            WordEntry("Garlic", "Часник", 1),
            WordEntry("Cucumber", "Огірок", 1),
            WordEntry("Pepper", "Перець", 1),
            WordEntry("Salt", "Сіль", 1),
            WordEntry("Sugar", "Цукор", 1),
            WordEntry("Honey", "Мед", 1),
            WordEntry("Rice", "Рис", 1),
            WordEntry("Pasta", "Паста", 1),
            WordEntry("Soup", "Суп", 1),
            WordEntry("Sandwich", "Бутерброд", 1),
            WordEntry("Salad", "Салат", 1),
            WordEntry("Pizza", "Піца", 1),
            WordEntry("Hamburger", "Гамбургер", 1),
            WordEntry("Sausage", "Ковбаса", 1),
            WordEntry("Ice cream", "Морозиво", 1),
            WordEntry("Cake", "Торт", 1),
            WordEntry("Cookie", "Печиво", 1),
            WordEntry("Chocolate", "Шоколад", 1),

            // Intermediate level (2)
            WordEntry("Family", "Сім'я", 2),
            WordEntry("Friend", "Друг", 2),
            WordEntry("Love", "Любов", 2),
            WordEntry("Peace", "Мир", 2),
            WordEntry("Joy", "Радість", 2),
            WordEntry("Happiness", "Щастя", 2),
            WordEntry("Sadness", "Смуток", 2),
            WordEntry("Anger", "Гнів", 2),
            WordEntry("Fear", "Страх", 2),
            WordEntry("Surprise", "Сюрприз", 2),
            WordEntry("Disgust", "Огида", 2),
            WordEntry("Trust", "Довіра", 2),
            WordEntry("Hope", "Надія", 2),
            WordEntry("Faith", "Віра", 2),
            WordEntry("Pride", "Гордість", 2),
            WordEntry("Shame", "Сором", 2),
            WordEntry("Guilt", "Вина", 2),
            WordEntry("Regret", "Жаль", 2),
            WordEntry("Nostalgia", "Ностальгія", 2),
            WordEntry("Empathy", "Співчуття", 2),
            WordEntry("Sympathy", "Симпатія", 2),
            WordEntry("Compassion", "Співчуття", 2),
            WordEntry("Gratitude", "Вдячність", 2),
            WordEntry("Patience", "Терпіння", 2),
            WordEntry("Courage", "Мужність", 2),
            WordEntry("Wisdom", "Мудрість", 2),
            WordEntry("Knowledge", "Знання", 2),
            WordEntry("Skill", "Навичка", 2),
            WordEntry("Talent", "Талант", 2),
            WordEntry("Ability", "Здатність", 2),
            WordEntry("Strength", "Сила", 2),
            WordEntry("Weakness", "Слабкість", 2),
            WordEntry("Beauty", "Краса", 2),
            WordEntry("Ugly", "Потворний", 2),
            WordEntry("Dirty", "Брудний", 2),
            WordEntry("Clean", "Чистий", 2),
            WordEntry("Soft", "М'який", 2),
            WordEntry("Hard", "Твердий", 2),
            WordEntry("Heavy", "Важкий", 2),
            WordEntry("Light", "Легкий", 2),
            WordEntry("Bright", "Яскравий", 2),
            WordEntry("Dark", "Темний", 2),
            WordEntry("Loud", "Гучний", 2),
            WordEntry("Quiet", "Тихий", 2),
            WordEntry("Fast", "Швидкий", 2),
            WordEntry("Slow", "Повільний", 2),
            WordEntry("Old", "Старий", 2),
            WordEntry("Young", "Молодий", 2),
            WordEntry("Rich", "Багатий", 2),
            WordEntry("Poor", "Бідний", 2),
            WordEntry("Strong", "Сильний", 2),
            WordEntry("Weak", "Слабкий", 2),
            WordEntry("Brave", "Хоробрий", 2),
            WordEntry("Coward", "Боягуз", 2),
            WordEntry("Smart", "Розумний", 2),
            WordEntry("Stupid", "Тупий", 2),
            WordEntry("Kind", "Добрий", 2),
            WordEntry("Mean", "Злий", 2),
            WordEntry("Generous", "Щедрий", 2),
            WordEntry("Greedy", "Жадібний", 2),
            WordEntry("Honest", "Чесний", 2),
            WordEntry("Dishonest", "Нечесний", 2),
            WordEntry("Loyal", "Вірний", 2),
            WordEntry("Disloyal", "Невірний", 2),
            WordEntry("Brilliant", "Блискучий", 2),
            WordEntry("Dull", "Тьмяний", 2),
            WordEntry("Calm", "Спокійний", 2),
            WordEntry("Nervous", "Нервовий", 2),
            WordEntry("Polite", "Ввічливий", 2),
            WordEntry("Rude", "Грубий", 2),
            WordEntry("Confident", "Впевнений", 2),
            WordEntry("Shy", "Сором'язливий", 2),
            WordEntry("Friendly", "Дружелюбний", 2),
            WordEntry("Unfriendly", "Недружелюбний", 2),
            WordEntry("Creative", "Креативний", 2),
            WordEntry("Boring", "Нудний", 2),
            WordEntry("Lazy", "Лінивий", 2),
            WordEntry("Hardworking", "Працьовитий", 2),
            WordEntry("Patient", "Терплячий", 2),
            WordEntry("Impatient", "Нетерплячий", 2),
            WordEntry("Careful", "Обережний", 2),
            WordEntry("Careless", "Необережний", 2),
            WordEntry("Responsible", "Відповідальний", 2),
            WordEntry("Irresponsible", "Безвідповідальний", 2),
            WordEntry("Adventurous", "Пригодницький", 2),
            WordEntry("Cautious", "Обачний", 2),
            WordEntry("Determined", "Рішучий", 2),
            WordEntry("Indecisive", "Нерішучий", 2),
            WordEntry("Ambitious", "Амбіційний", 2),
            WordEntry("Unambitious", "Неамбіційний", 2),
            WordEntry("Optimistic", "Оптимістичний", 2),
            WordEntry("Pessimistic", "Песимістичний", 2),

            // Advanced level (3)
            WordEntry("Responsibility", "Відповідальність", 3),
            WordEntry("Determination", "Рішучість", 3),
            WordEntry("Compassionate", "Співчутливий", 3),
            WordEntry("Imagination", "Уява", 3),
            WordEntry("Independence", "Незалежність", 3),
            WordEntry("Encouragement", "Задоволення", 3),
            WordEntry("Philosophy", "Філософія", 3),
            WordEntry("Psychology", "Психологія", 3),
            WordEntry("Sociology", "Соціологія", 3),
            WordEntry("Literature", "Література", 3),
            WordEntry("Constitution", "Конституція", 3),
            WordEntry("Government", "Уряд", 3),
            WordEntry("International", "Міжнародний", 3),
            WordEntry("Community", "Спільнота", 3),
            WordEntry("Technology", "Технологія", 3),
            WordEntry("Transportation", "Транспорт", 3),
            WordEntry("Environmental", "Екологічний", 3),
            WordEntry("Agriculture", "Сільське господарство", 3),
            WordEntry("Development", "Розвиток", 3),
            WordEntry("Investment", "Інвестиції", 3),
            WordEntry("Economy", "Економіка", 3),
            WordEntry("Finance", "Фінанси", 3),
            WordEntry("Commerce", "Торгівля", 3),
            WordEntry("Enterprise", "Підприємництво", 3),
            WordEntry("Manufacturing", "Виробництво", 3),
            WordEntry("Construction", "Будівництво", 3),
            WordEntry("Communication", "Комунікація", 3),
            WordEntry("Education", "Освіта", 3),
            WordEntry("Employment", "Зайнятість", 3),
            WordEntry("Healthcare", "Охорона здоров'я", 3),
            WordEntry("Intelligence", "Інтелект", 3),
            WordEntry("Investigation", "Розслідування", 3),
            WordEntry("Laboratory", "Лабораторія", 3),
            WordEntry("Mathematics", "Математика", 3),
            WordEntry("Philosophy", "Філософія", 3),
            WordEntry("Physiology", "Фізіологія", 3),
            WordEntry("Professional", "Професійний", 3),
            WordEntry("Revolution", "Революція", 3),
            WordEntry("Statistics", "Статистика", 3),
            WordEntry("Technology", "Технологія", 3),
            WordEntry("Theoretical", "Теоретичний", 3),
            WordEntry("Volunteer", "Волонтер", 3),
            WordEntry("Weather", "Погода", 3),
            WordEntry("Workshop", "Майстерня", 3),
            WordEntry("Engineering", "Інженерія", 3),
            WordEntry("Experiment", "Експеримент", 3),
            WordEntry("Innovation", "Інновація", 3),
            WordEntry("Observation", "Спостереження", 3),
            WordEntry("Organization", "Організація", 3),
            WordEntry("Participation", "Участь", 3),
            WordEntry("Pharmacy", "Аптека", 3),
            WordEntry("Pollution", "Забруднення", 3),
            WordEntry("Population", "Населення", 3),
            WordEntry("Recycling", "Переробка", 3),
            WordEntry("Resources", "Ресурси", 3),
            WordEntry("Responsibility", "Відповідальність", 3),
            WordEntry("Sustainable", "Сталий", 3),
            WordEntry("Therapy", "Терапія", 3),
            WordEntry("Tradition", "Традиція", 3),
            WordEntry("Transformation", "Трансформація", 3),
            WordEntry("Transportation", "Транспорт", 3),
            WordEntry("Unemployment", "Безробіття", 3),
            WordEntry("Urbanization", "Урбанізація", 3),
            WordEntry("Vaccination", "Вакцинація", 3),
            WordEntry("Wilderness", "Дикість", 3),
            WordEntry("Zoology", "Зоологія", 3),
            WordEntry("Aerospace", "Аерокосмічний", 3),
            WordEntry("Biochemistry", "Біохімія", 3),
            WordEntry("Biomedical", "Біомедичний", 3),
            WordEntry("Cybersecurity", "Кібербезпека", 3),
            WordEntry("Ecosystem", "Екосистема", 3),
            WordEntry("Electromagnetism", "Електромагнетизм", 3),
            WordEntry("Genetics", "Генетика", 3),
            WordEntry("Geology", "Геологія", 3),
            WordEntry("Nanotechnology", "Нанотехнологія", 3),
            WordEntry("Neuroscience", "Нейронаука", 3),
            WordEntry("Nuclear", "Ядерний", 3),
            WordEntry("Oceanography", "Океанографія", 3),
            WordEntry("Optics", "Оптика", 3),
            WordEntry("Quantum", "Квантовий", 3),
            WordEntry("Radiation", "Радіація", 3),
            WordEntry("Renewable", "Відновлюваний", 3),
            WordEntry("Robotics", "Робототехніка", 3),
            WordEntry("Seismology", "Сейсмологія", 3),
            WordEntry("Spacecraft", "Космічний апарат", 3),
            WordEntry("Telecommunications", "Телекомунікації", 3),
            WordEntry("Thermodynamics", "Термодинаміка", 3)
        )

        for ((word_v, translation_v, level) in wordsList) {
            val word = Words(0, word_v, translation_v, false, level)  // id = 0, will be automatically assigned to
            addWords(word)
        }
    }



    // Changes the favourite status to the opposite
    fun changeFavorite(id: Int, starButton: ImageButton): Boolean {
        val query = "SELECT favorite FROM words WHERE id = ?"
        val db = this.readableDatabase
        var favorite: Boolean = false

        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        if (cursor.moveToFirst()) {
            favorite = cursor.getInt(cursor.getColumnIndexOrThrow("favorite")) > 0
        }
        cursor.close()

        // Change the favourite value to the opposite
        val newFavorite = if (favorite) 0 else 1

        // Update values in the database
        val updateQuery = "UPDATE words SET favorite = ? WHERE id = ?"
        val statement = db.compileStatement(updateQuery)
        statement.bindLong(1, newFavorite.toLong())
        statement.bindLong(2, id.toLong())
        statement.executeUpdateDelete()

        // Update the image on the starButton
        if (newFavorite == 1) {
            starButton.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            starButton.setImageResource(android.R.drawable.btn_star_big_off)
        }

        db.close()
        return !favorite // Returns an already valid, changed value
    }








    // Returns a list of all words
    @SuppressLint("Range")
    fun getAllWords(): List<Words> {
        val wordsList = mutableListOf<Words>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM words ORDER BY word ASC", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val word = cursor.getString(cursor.getColumnIndex("word"))
                val translation = cursor.getString(cursor.getColumnIndex("translation"))
                val favorite = cursor.getInt(cursor.getColumnIndex("favorite")) > 0
                val level = cursor.getInt(cursor.getColumnIndex("level")) /////////
                wordsList.add(Words(id, word, translation, favorite, level)) //////
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return wordsList
    }

    // Returns a list of all words for the test
    @SuppressLint("Range")
    fun getWordsForTest(level: String): List<Words> {

        val wordsList = mutableListOf<Words>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM words WHERE level = ${level.toInt()}", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val word = cursor.getString(cursor.getColumnIndex("word"))
                val translation = cursor.getString(cursor.getColumnIndex("translation"))
                val favorite = cursor.getInt(cursor.getColumnIndex("favorite")) > 0
                val level = cursor.getInt(cursor.getColumnIndex("level")) /////////
                wordsList.add(Words(id, word, translation, favorite, level)) //////
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return wordsList
    }

    // Returns a list of selected words
    fun getFavoriteWords(): List<Words>  {
        val wordsList = mutableListOf<Words>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM words WHERE favorite = 1 ORDER BY word ASC", null) // In SQLite, true is represented as 1

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val word = cursor.getString(cursor.getColumnIndexOrThrow("word"))
                val translation = cursor.getString(cursor.getColumnIndexOrThrow("translation"))
                val favorite = cursor.getInt(cursor.getColumnIndexOrThrow("favorite")) > 0
                val level = cursor.getInt(cursor.getColumnIndexOrThrow("level")) //////
                wordsList.add(Words(id, word, translation, favorite, level)) /////////
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return wordsList
    }

    // Returns a list of words by the selected letter
    fun getSearchWordsLetter(letter: String): List<Words> {
        val wordsList = mutableListOf<Words>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM words WHERE word LIKE ? ORDER BY word ASC", arrayOf("$letter%")) // Select words that start with a specific letter

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val word = cursor.getString(cursor.getColumnIndexOrThrow("word"))
                val translation = cursor.getString(cursor.getColumnIndexOrThrow("translation"))
                val favorite = cursor.getInt(cursor.getColumnIndexOrThrow("favorite")) > 0
                val level = cursor.getInt(cursor.getColumnIndexOrThrow("level")) //////
                wordsList.add(Words(id, word, translation, favorite, level)) ////
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return wordsList
    }


    // Returns a list of words by the entered letters in real time
    fun getSearchWords(letter: String): List<Words> {
        val wordsList = mutableListOf<Words>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM words WHERE word LIKE ? ORDER BY word ASC", arrayOf("%$letter%")) // Select words that contain certain characters

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val word = cursor.getString(cursor.getColumnIndexOrThrow("word"))
                val translation = cursor.getString(cursor.getColumnIndexOrThrow("translation"))
                val favorite = cursor.getInt(cursor.getColumnIndexOrThrow("favorite")) > 0
                val level = cursor.getInt(cursor.getColumnIndexOrThrow("level"))
                wordsList.add(Words(id, word, translation, favorite, level)) ///////
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return wordsList
    }







}

