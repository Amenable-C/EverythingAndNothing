package com.example.myapplication

import java.util.*

val menuList = mapOf(1 to "참깨라면", 2 to "햄버거", 3 to "콜라", 4 to "핫바", 5 to "초코우유");
val priceList = mapOf("1" to 1000, "2" to 1500, "3" to 800, "4" to 1200, "5" to 1500);
fun printMenu() {
    println("========== MENU ==========")
    println("| (1) 참깨라면    - 1,000 원   |")
    println("| (2) 햄버거      - 1,500 원   |")
    println("| (3) 콜라       -   800 원   |")
    println("| (4) 핫바       - 1,200 원   |")
    println("| (5) 초코우유    - 1,500 원   |")
    println("Choose menu:")
}

fun getChange(menuPrice : Int, inputMoney : Int){
    println("감사합니다. 잔돈반환 : ${inputMoney - menuPrice}")
}

fun getCoin() : Int{
    var getMoney : Int = 0
    var check : Int = 1
    var input : String ? = null

    // input이 모두 숫자로 표현된 정확한 돈의 형식인지 체크하는 것.
    while(check > 0){
        check = 0
        println("Insert Coin")
        input = readLine()
        // 입력된 것에 문자가 있지 않은지 확인.
        if(input?.length == 0){
            check++;
        }else{
            for(i in input!!){
                if(i.toInt() < 48 || i.toInt() > 57){
                    check++
                }
            }
            // 오직 숫자로만 이루어진 경우, 다음 단계로 이동
            if(check == 0) {
                getMoney = input.toInt()
                break
            }
        }
        println("돈을 넣지 않았습니다")
        println("다시 돈을 넣어주세요")
    }
    println("${getMoney} 원을 넣었습니다.")
    return getMoney
}

fun getMenu(menuNum : Int){
    // map을 이용하여 선택된 메뉴 보여주기
    println("${menuList.get(menuNum)}이 선택되었습니다.")
}

fun getPrice(menuNum : String) : Int{
    // map을 이용하여 선택한 메뉴의 가격정보 가져오기.
    // menuNum 이미 null 검증 완료
    return priceList.get(menuNum)!!.toInt();
}

fun main(){
    // 메뉴 출력
    printMenu()

    // 입력 받기
    val sc:Scanner = Scanner(System.`in`)
    var inputString : String? = sc.nextLine()

    // 엘비스 연산자를 이용하여 null 처리
    var price : Int? = priceList.get(inputString)?.toInt() ?: 0;

    // null이 아닐때까지 계속해서 입력받기.
    while(price == 0){
        println("아무것도 선택되지 않았습니다.")
        println("다시 선택해주세요.")
        printMenu()
        inputString = sc.nextLine()
        price = priceList.get(inputString)?.toInt() ?: 0;
    }

    // 위에서 null 검증 완료 // map에 접근하기 위한 형식변환
    var menuNum : Int = inputString!!.toInt();
    // getMenu -> 메뉴 선택하고 반환하기.
    getMenu(menuNum)

    // getPrice -> 선택한 메뉴의 가격정보 가져오기.
    var menuPrice : Int = getPrice(inputString)
    // getCoin -> 돈 넣기.
    var inputMoney : Int = getCoin()

    var checkChange : Int? = null
    var change : Int = inputMoney - menuPrice
    // 금액이 부족할 시, null 반환
    if(change >= 0){
        checkChange = change
    }

    // null을 이용하여 계산.
    if(checkChange == null){
        println("현금이 부족합니다.")
    }else{
        getChange(menuPrice, inputMoney)
    }
}