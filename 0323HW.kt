package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.Nullable

class MainActivity : AppCompatActivity() {
    var edit1 : EditText? = null;
    var edit2 : EditText? = null;
    lateinit var btnAdd : Button;
    lateinit var btnSub : Button;
    lateinit var btnMul : Button;
    lateinit var btnDiv : Button;
    lateinit var btnRemain : Button;
    lateinit var btnChange : Button;
    lateinit var textResult : TextView
    lateinit var textResult1 : EditText
    lateinit var textResult2 : EditText
    lateinit var num1 : String;
    lateinit var num2 : String;
    var count : Int = 0
    var result : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "${count}회 계산기"


        edit1 = findViewById<EditText>(R.id.Edit1)
        edit2 = findViewById<EditText>(R.id.Edit2)




        btnAdd = findViewById<Button>(R.id.BtnAdd)
        btnSub = findViewById<Button>(R.id.BtnSub)
        btnMul = findViewById<Button>(R.id.BtnMul)
        btnDiv = findViewById<Button>(R.id.BtnDiv)
        btnRemain = findViewById<Button>(R.id.BtnRemain)
        btnChange = findViewById<Button>(R.id.BtnChange)
        textResult = findViewById<TextView>(R.id.TextResult)
        textResult1 = findViewById<EditText>(R.id.Edit1)
        textResult2 = findViewById<EditText>(R.id.Edit2)



        btnAdd.setOnClickListener{
            try{
                num1 = edit1!!.text.toString()
                num2 = edit2!!.text.toString()
                result = Integer.parseInt(num1) + Integer.parseInt(num2)
                textResult.text = "계산 결과 : " + result.toString()
                count++
                setTitle("${count} 회 계산기")
                textResult1.text.clear()
                textResult1.text.insert(0, result.toString())
                textResult2.text.clear()
            }catch (e : NumberFormatException){
                textResult.text = "값을 모두 입력하세요."
            }
            false
        }

        btnSub.setOnClickListener{
            try {
                num1 = edit1!!.text.toString()
                num2 = edit2!!.text.toString()
                result = Integer.parseInt(num1) - Integer.parseInt(num2)
                textResult.text = "계산 결과 : " + result.toString()
                count++
                setTitle("${count} 회 계산기")
                textResult1.text.clear()
                textResult1.text.insert(0, result.toString())
                textResult2.text.clear()

            }catch (e : NumberFormatException){
                textResult.text = "값을 모두 입력하세요."
            }
            false
        }

        btnMul.setOnClickListener{
            try{
                num1 = edit1!!.text.toString()
                num2 = edit2!!.text.toString()
                result = Integer.parseInt(num1) * Integer.parseInt(num2)
                textResult.text = "계산 결과 : " + result.toString()
                count++
                setTitle("${count} 회 계산기")
                textResult1.text.clear()
                textResult1.text.insert(0, result.toString())
                textResult2.text.clear()
            }catch (e : NumberFormatException){
                textResult.text = "값을 모두 입력하세요."
            }
            false
        }

        btnDiv.setOnClickListener{
            try{
                num1 = edit1!!.text.toString()
                num2 = edit2!!.text.toString()
                result = Integer.parseInt(num1) / Integer.parseInt(num2)
                textResult.text = "계산 결과 : " + result.toString()
                count++
                setTitle("${count} 회 계산기")
                textResult1.text.clear()
                textResult1.text.insert(0, result.toString())
                textResult2.text.clear()
            }catch (e : NumberFormatException){
                textResult.text = "값을 모두 입력하세요."
            }
            false
        }

        btnRemain.setOnClickListener{
            try {
                num1 = edit1!!.text.toString()
                num2 = edit2!!.text.toString()
                result = Integer.parseInt(num1) % Integer.parseInt(num2)
                textResult.text = "계산 결과 : " + result.toString()
                count++
                setTitle("${count} 회 계산기")
                textResult1.text.clear()
                textResult1.text.insert(0, result.toString())
                textResult2.text.clear()
            }catch (e : NumberFormatException){
                textResult.text = "값을 모두 입력하세요."
            }
            false
        }

        btnChange.setOnClickListener{
            try {
                num1 = edit1!!.text.toString()
                num2 = edit2!!.text.toString()

                textResult1.text.clear()
                textResult1.text.insert(0, num2.toString())

                textResult2.text.clear()
                textResult2.text.insert(0, num1.toString())
            }catch (e : NumberFormatException){
                textResult.text = "값을 모두 입력하세요."
            }
            false
        }

    }
}