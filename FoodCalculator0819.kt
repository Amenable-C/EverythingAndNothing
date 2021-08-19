package org.techtown.testrecyclerview

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.techtown.testrecyclerview.search.FoodData
import kotlin.math.absoluteValue



fun recommendedKcal(currentWeight: Int, targetWeight: Int, height: Int): Int {
    var standardWeight = (height - 100) * (0.9) * 30
    var forChange:Int = 0
    if(targetWeight > currentWeight) {
        forChange = 300
    }
    else if(targetWeight < currentWeight) {
        forChange = -500
    }



    var Kcal:Int = standardWeight.toInt() + forChange
    return Kcal
}

fun nutrientRate (currentWeight: Int, targetWeight: Int, Kcal: Int): Triple<Int, Int, Int> {
    var pro:Int = 0
    var fat = 0
    var cab = 0
    if(currentWeight == targetWeight){
        pro = (Kcal * (0.3)).toInt()
        fat = (Kcal * (0.2)).toInt()
        cab = (Kcal * (0.5)).toInt()
    }

    else if(currentWeight > targetWeight){
        pro = (Kcal * (0.4)).toInt()
        fat = (Kcal * (0.3)).toInt()
        cab = (Kcal * (0.3)).toInt()
    }

    else {
        pro = (Kcal * (0.4)).toInt()
        fat = (Kcal * (0.2)).toInt()
        cab = (Kcal * (0.4)).toInt()
    }
    return Triple(cab, pro, fat)
}

fun rateOfScarceNutrient (scarceCab:Double, scarcePro:Double, scarceFat: Double): Triple<Double, Double, Double> {
    var total = scarceCab + scarcePro + scarceFat
    var rateCab= scarceCab * 100 / total
    var ratePro = scarcePro * 100 / total
    var rateFat = scarceFat * 100 / total
    return Triple(rateCab, ratePro, rateFat)
}

fun comparingForTheBest (scarceNutrientList:List<Double>, foodNutrientList:List<Double>): Double{
    var difference : Double= 0.0
    for(i: Int in 0..2){
        difference += (scarceNutrientList[i] - foodNutrientList[i]).absoluteValue
    }
    return difference
}

//fun addTheDataToList (scarceNutrientList:List<Int>): Pair<MutableMap<Int, String>, MutableList<Int>> {
//    var differenceAndName = mutableMapOf<Int, String>()
//    var differenceList = mutableListOf<Int>()
    /////// test
//    lateinit var dbHelper : DBHelper
//    lateinit var db : SQLiteDatabase
//    dbHelper = DBHelper(this, "food_nutri.db", null, 1)
//    db = dbHelper.readableDatabase

//    var foodNutrientList = listOf(0, 0, 0, "Init")
//
//    var cursor: Cursor = db.rawQuery("SELECT * FROM real_nutri", null)
//    print(cursor.getString(2))
//    while(cursor.moveToNext()) {
//        println(cursor.getString(2)) // test
//        foodNutrientList = listOf(
//            cursor.getString(2).toInt(),
//            cursor.getString(3).toInt(),
//            cursor.getString(4).toInt(),
//            cursor.getString(1).toString(),
//        )
//        println(foodNutrientList)
//        differenceAndName.put(
//            comparingForTheBest(
//                scarceNutrientList,
//                foodNutrientList.filterIsInstance<Int>()
//            ), foodNutrientList[3].toString()
//        )
//        differenceList.add(
//            comparingForTheBest(
//                scarceNutrientList,
//                foodNutrientList.filterIsInstance<Int>()
//            )
//        )
//    }
//    cursor.close()
//    db.close()

//    var foodNutrientList = listOf(30, 40, 30, "꿩불고기")  // 첫 번째 음식 리스트라고 가정
//    differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()), foodNutrientList[3].toString())
//    differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()))
//    println("This : ${foodNutrientList.filterIsInstance<Int>()}")
//
//    foodNutrientList = listOf(20, 50, 30, "닭갈비") // 두 번째 음식 리스트를 가져온 걸로 가정
//    differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()), foodNutrientList[3].toString())
//    differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()))
//
//    foodNutrientList = listOf(22, 38, 40, "불고기") // 세 번째 음식 리스트를 가져온 걸로 가정
//    differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()), foodNutrientList[3].toString())
//    differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()))
//
//    foodNutrientList = listOf(15, 55, 30, "더덕구이") // 네 번째 음식 리스트를 가져온 걸로 가정
//    differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()), foodNutrientList[3].toString())
//    differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()))
//
//    foodNutrientList = listOf(45, 38, 17, "만둣국") // 다섯 번째 음식 리스트를 가져온 걸로 가정
//    differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()), foodNutrientList[3].toString())
//    differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()))
//
//    foodNutrientList = listOf(45, 17, 38, "된장국") // 여섯 번째 음식 리스트를 가져온 걸로 가정
//    differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()), foodNutrientList[3].toString())
//    differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Int>()))
//
//    differenceList.sort() // 차이값 오름차순 정렬
//    return Pair(differenceAndName, differenceList)
//}