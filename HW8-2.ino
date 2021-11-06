#include <LiquidCrystal.h>

// 핀 번호 (RS, E< DB4, DB5, DB6, DB7)
LiquidCrystal lcd(44, 45, 46, 47, 48, 49); // LCD 연결

byte user[8] = {
  B01000,
  B00011,
  B00100,
  B01000,
  B01000,
  B01000,
  B00100,
  B00011,
};
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  lcd.createChar(0, user);
  lcd.begin(16, 2); // LCD 크기설정
  lcd.clear(); // LCD 화면 지우기
}

void readTemperature(){
  //온도 센서 읽기
  int reading = analogRead(55);
  //ADC 반환값을 전합으로 변환
  float voltage = reading * 5.0 / 1024.0;
  float temp_C = voltage * 100; // 이게 온도
  
}

void readIlluminance(){
  int reading = analogRead(56);
  float voltage = reading * 5.0 / 1024.0;
  
}

void loop() {
  // 온도 센서 읽기
  int reading1 = analogRead(55);
  // ADC 반환값을 전합으로 변환
  float voltage1 = reading1 * 5.0 / 1024.0;
  float temp_C = voltage1 * 100; // 이게 온도
  
  // 조도 센서 읽기
  int reading2 = analogRead(56);
  float voltage2 = reading2 * 5.0 / 1024.0;

  lcd.setCursor(0,0);
  lcd.print("Temp: ");
  lcd.print(temp_C);
  lcd.print(" ");
  lcd.write(byte(0));
  
  lcd.setCursor(0,1);
  lcd.print("Light: ");
  lcd.print(voltage2);
  delay(1000);
}
