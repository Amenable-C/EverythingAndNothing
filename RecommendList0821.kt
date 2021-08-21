package org.techtown.testrecyclerview.recommend

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log // Log test
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.techtown.testrecyclerview.*
import org.techtown.testrecyclerview.search.FoodAdapter
import org.techtown.testrecyclerview.search.FoodData

class RecommendList : AppCompatActivity() {
    lateinit var dbHelper : DBHelper
    lateinit var db : SQLiteDatabase
    var foodList = arrayListOf<FoodData>()
    lateinit var recyclerView : RecyclerView
    val displayList = ArrayList<FoodData>()
    val mAdapter = RecommendFoodAdapter(this,foodList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_list)
        dbHelper = DBHelper(this, "food_nutri.db", null, 1)
        db = dbHelper.readableDatabase

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        recyclerView = findViewById(R.id.mRecyclerView)
        fillData()
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        displayList.addAll(foodList)


        mAdapter.setItemClickListner(object : RecommendFoodAdapter.OnItemClickListner {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(applicationContext, RecommendResult::class.java)
                startActivityForResult(intent, 101)
            }

        })
    }
    private fun fillData() {

        /////////// FoodCalculator - START

//        println("현재 몸무게, 목표 몸무게, 키 : ${dbHelper.getColValue(0, "user_info").toInt()}, ${dbHelper.getColValue(1, "user_info").toInt()}, ${dbHelper.getColValue(4, "user_info").toInt()}")
        // 1. 하루 권장 칼로리 계산
        var recommendedKcal : Int = recommendedKcal(
            dbHelper.getColValue(0, "user_info").toInt(),
            dbHelper.getColValue(1, "user_info").toInt(),
            dbHelper.getColValue(4, "user_info").toInt()
        )
//        println("하루 권장 칼로리 : ${recommendedKcal}")
        // 2. "하루 권장 칼로리"를 이용하여 "하루 권장 탄수화물, 단백질, 지방" 계산
        var nutrientRate : Triple<Int, Int, Int> = nutrientRate(dbHelper.getColValue(0, "user_info").toInt(),
            dbHelper.getColValue(1, "user_info").toInt(),
            recommendedKcal)
//        println("하루 권장 탄수화물, 단백질, 지방: ${nutrientRate}")
        // 3. |'하루 권장 탄, 단, 지' - '이미 섭취한 탄, 단, 지'|를 계산 하여 '부족한 탄, 단, 지(%)' 계산 // 임의의 값 넣어놓은 것
        var scarceNutrientList:List<Double> = listOf(rateOfScarceNutrient(100.0, 500.0, 200.0).component1(), rateOfScarceNutrient(100.0, 500.0, 200.0).component2(), rateOfScarceNutrient(100.0, 500.0, 200.0).component3())
//        println("비교를 위한 '부족한 영양소 비율(탄, 단, 지)(%)' $scarceNutrientList")

        // more test
        var name : String
        var cab : Double
        var pro : Double
        var fat : Double
        var priority : Int
        var differenceAndName = mutableMapOf<Double, String>()
        var priorityAndName = mutableMapOf<String, Int>()
        var differenceList = mutableListOf<Double>()

        for (i in 0..90) {

            name = dbHelper.getColValueTest(i, 1, "real_nutri_91")
            cab = dbHelper.getColValueTest(i, 3, "real_nutri_91").toDouble()
            pro = dbHelper.getColValueTest(i, 4, "real_nutri_91").toDouble()
            fat = dbHelper.getColValueTest(i, 5, "real_nutri_91").toDouble()
            priority = dbHelper.getColValueTest(i, 6, "real_nutri_91").toInt()
            listOf(rateOfScarceNutrient(100.0, 500.0, 200.0).component1(), rateOfScarceNutrient(100.0, 500.0, 200.0).component2(), rateOfScarceNutrient(100.0, 500.0, 200.0).component3())
            var foodNutrientList = listOf(cab, pro, fat, name)
            var foodNutrientListrate :List<Double> = listOf(rateOfScarceNutrient(cab, pro, fat).component1(), rateOfScarceNutrient(cab, pro, fat).component2(), rateOfScarceNutrient(cab, pro, fat).component3())
//            println(foodNutrientList)


            differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientListrate), foodNutrientList[3].toString())
            priorityAndName.put(foodNutrientList[3].toString(), priority)
            differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientListrate))
        }
//        println(differenceAndName)
        differenceList.sort() // 차이값 오름차순 정렬


        dbHelper = DBHelper(this, "food_nutri.db", null, 1)
        db = dbHelper.readableDatabase

        // Priority 고려 (START)
        var times: Int = 0
        var foodName : String
        var Top5Food: Array<String> = arrayOf()
        while(true){
            for(pri in 5 downTo 0) {
                for (i in 0..4) {
                    if (priorityAndName.get(differenceAndName.get(differenceList[i])) == pri) {
                        foodName = differenceAndName.get(differenceList[i]).toString()
                        Top5Food = Top5Food.plus(foodName)
                        times = times + 1
                        if (times == 6)
                            break;
                    }
                }
                if (times == 6)
                    break;
            }
            if (times == 6)
                break;
        }
        // Priority 고려 (FINISH)

        for(i in 0..5) {
            var cursor: Cursor = db.rawQuery("SELECT * FROM real_nutri_91", null)
            while (cursor.moveToNext()) {
//                if (cursor.getString(1) == differenceAndName.get(differenceList[i])) // origin
                if (cursor.getString(1) == Top5Food[i])
                    foodList.add(
                        FoodData(
                            cursor.getString(1),
                            cursor.getString(2).toDouble(),
                            100,
                            cursor.getString(3).toDouble(),
                            cursor.getString(4).toDouble(),
                            cursor.getString(5).toDouble()
                        )
                    )
            }
            cursor.close()
        }

        db.close()

        ///////////  FoodCalculator - FINISH


//            foodList.add(FoodData("gimchi",1000,100,100,100,100))
//            foodList.add(FoodData("bob",1000,100,100,100,100))
//            foodList.add(FoodData("banana",1000,100,100,100,100))
//            foodList.add(FoodData("salad",1000,100,100,100,100))
//            foodList.add(FoodData("bulgogi",1000,100,100,100,100))
//            foodList.add(FoodData("chicken",1000,100,100,100,100))
//            foodList.add(FoodData("pizza",2000,100,100,100,100))
//            foodList.add(FoodData("bread",1000,100,100,100,100))
//            foodList.add(FoodData("meat",1000,100,100,100,100))
    }
}