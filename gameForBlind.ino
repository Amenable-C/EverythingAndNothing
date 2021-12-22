#include <pitches.h>
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>

unsigned long time_previous;
unsigned long time_current;

// UART 통신
SoftwareSerial mySerial(4, 5);

int sharkNoteDuration = 1; // 빠르면 숫자가 올라가야 하는건가???
int buttons[] = {14, 15, 16, 17}; // 여섯개 쭉 깔아놓기 

int Game1_button = 31; //가위바위보 버튼
int Game2_button = 32; //묵찌빠 버튼
int Game3_button = 33; //무궁화꽃이피었습니다 버튼

int game_speed_rsp; // 게임 속도 조절

int GameNumber = 0;
boolean previous_state1 = false;
boolean previous_state2 = false;
boolean previous_state3 = false;

int count = 0; // 묵찌빠 게임 횟수

int Card1[] = {0}; // 첫번째 참가자의 패(1:Rock, 2:Scissors, 3:Paper)
int Card2[] = {0}; // 두번째 참가자의 패(1:Rock, 2:Scissors, 3:Paper)


// 결승점
int endLine = 30;

// 초음파 센서
int trigPin = 3;
int echoPin = 2;

// HandOut 함수를 위한 전역 변수
int SpeakerPin = A0; // 스피커 연결 핀
int Melody[] = { // 가위바위보 낼 타이밍 알려주는 음
  NOTE_C4, NOTE_C4, NOTE_C4, NOTE_C5
};
int NoteDuration = 2; // 2분음표

// RockScissorsPaper 함수를 위한 전역 변수
int Flex11 = A1; // Flex Sensors 연결된 아날로그 핀
int Flex12 = A2;
int Flex13 = A3;
int Flex21 = A4;
int Flex22 = A5;
int Flex23 = A6;
#define MAX 600 // Flex Sensors 최댓값. 실험을 통해 정하기
#define MID 100 // Flex Sensors 중간값. 실험을 통해 정하기
#define MIN 0 // Flex Sensors 최솟값. 실험을 통해 정하기

// GuessRSP 함수를 위한 전역 변수
int Card; // 참가자의 패

// WinnerOfTheRound 함수를 위한 전역 변수
#define WIN 10
#define TIE 15
#define LOSE 20
int outcome; // 첫번째 참가자의 승패

// Vibration 함수를 위한 전역 변수
int Vib1 = 11; // 진동모터 핀
int Vib2 = 12;

// KeepOrStopMukchipa 함수를 위한 전역변수
int KOS = 0; // 0이면 계속, 1이면 멈춤.

// ScoreBoard 함수를 위한 전역 변수
int Score1 = 0;
int Score2 = 0;
// 핀 번호 (RS, E, DB4, DB5, DB6, DB7)
LiquidCrystal lcd(44, 45, 46, 47, 48, 49);

void setup() {
  Serial.begin(9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  for(int i = 0; i < 4; i++){
    pinMode(buttons[i], INPUT);
  }

  int Player1Hand;
  int Player2HandOrigin;
  int Player2HandNew;

  boolean state1 = digitalRead(buttons[0]);
  boolean state2 = digitalRead(buttons[1]);
  boolean state3 = digitalRead(buttons[2]);
  boolean state4 = digitalRead(buttons[3]);
  /*boolean state5 = digitalRead(buttons[4]);
  boolean state6 = digitalRead(buttons[5]);*/

  outcome = TIE;

  mySerial.begin(9600);
  time_previous = millis();
  
  //lcd.createChar(0, colon1); // 사용자 정의 문자1 생성
  //lcd.createChar(1, colon2); // 사용자 정의 문자2 생성
  lcd.begin(16, 2); // LCD 초기화
  lcd.clear();
  lcd.print("Player1 : Player2"); // 문자열 출력
  lcd.setCursor(7, 1);
  lcd.print(":");
  lcd.setCursor(0, 1);
  lcd.print(Score1);
  lcd.setCursor(8, 1);
  lcd.print(Score2);
}

int ChooseTheGame() { //버튼 3개로 해서 누르면 게임 시작하도록
  boolean game_state1 = digitalRead(Game1_button);
  boolean game_state2 = digitalRead(Game2_button);
  boolean game_state3 = digitalRead(Game3_button);

  while(game_state1 == 0 && game_state2 == 0 && game_state3 == 0){
    game_state1 = digitalRead(Game1_button);
    game_state2 = digitalRead(Game2_button);
    game_state3 = digitalRead(Game3_button);
  
  }
  
  if(game_state1){
    if(previous_state1==false){ //1번 버튼을 누르면 가위바위보 게임 시작
      GameNumber = 1;
      return 1;
    }
  }
  else previous_state1 = false; 

  if(game_state2){
    if(previous_state2==false){ //2번 버튼을 누르면 묵찌빠 게임 시작
      GameNumber = 2;
      return 2;
    }
  }
  else previous_state2 = false;

  if(game_state3){
    if(previous_state3==false){ //3번 버튼을 누르면 무궁화꽃이피었습니다 게임 시작
      GameNumber = 3;
      return 3;
    }
  }
  else previous_state3 = false;
  
}

int RSP_melody[] = { // 가위바위보(시작, 패 낼때) 멜로디, 0은 쉼표
  NOTE_E4, 0, NOTE_E4, NOTE_D4, NOTE_E4, 0, NOTE_D4, 0, 
  NOTE_E4, NOTE_D4, NOTE_C4, NOTE_D4, NOTE_E4, 0, 0, 0, 0, 0
};

void StartTune1() {
  for (int thisNote = 0; thisNote < sizeof(RSP_melody) / sizeof(int); thisNote++) {
    // 음표 길이를 시간으로 변환
    int noteLength = 250;
    // 단음 재생
    tone(SpeakerPin, RSP_melody[thisNote], noteLength);
    delay(noteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지  
  }
}

int Game2_melody[] = { // 묵찌빠 게임 시작 멜로디, 0은 쉼표
  NOTE_D4, 0, NOTE_D4, 0, NOTE_G4
};

void StartTune2() {
  for (int thisNote = 0; thisNote < sizeof(Game2_melody) / sizeof(int); thisNote++) {
    // 음표 길이를 시간으로 변환
    int noteLength = 250;
    // 단음 재생
    tone(SpeakerPin, Game2_melody[thisNote], noteLength);
    delay(noteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
  return;
}

int Game3_melody[] = { // 무궁화꽃이피었습니다(시작, 진행) 멜로디, 0은 쉼표
  NOTE_E4, NOTE_A4, NOTE_A4, 0, NOTE_A4, 0, NOTE_G4, 0, NOTE_A4, NOTE_A4, NOTE_E4, NOTE_E4, NOTE_G4, 0
};

void StartTune3() {
  for (int thisNote = 0; thisNote < sizeof(Game3_melody) / sizeof(int); thisNote++) {
    // 음표 길이를 시간으로 변환
    int noteLength = 250;
    // 단음 재생
    tone(SpeakerPin, Game3_melody[thisNote], noteLength);
    delay(noteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
};

int SetTheSpeed(){
  int reading = analogRead(A8);
  int level = 0;
  int noteLength = 500;

  level = map(reading, 0, 1023, 0, 4);

  if(level==0) noteLength = 150;
  if(level==1) noteLength = 200;
  if(level==2) noteLength = 250;
  if(level==3) noteLength = 300;
  if(level==4) noteLength = 350;
  
  return noteLength;
}

int Count3_melody[] = { // 321카운트 멜로디, 0은 쉼표
  NOTE_E4, 0, 0, 0, NOTE_E4, 0, 0, 0, NOTE_E4, 0, 0, 0, NOTE_C5
};

void HandOut2(int game_speed) {
  for (int thisNote = 0; thisNote < sizeof(Count3_melody) / sizeof(int); thisNote++) {
    // 3초 카운트 3번 재생
    tone(SpeakerPin, Count3_melody[thisNote], game_speed);
    delay(game_speed);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
}

int Quit_melody[] = { // 게임 종료 멜로디, 0은 쉼표
  NOTE_C5, 0, 0, NOTE_G4, 0, 0, NOTE_E4, 0, 0, NOTE_C4
};

void EndTune() {
  for (int thisNote = 0; thisNote < sizeof(Quit_melody) / sizeof(int); thisNote++) {
    // 음표 길이를 시간으로 변환
    int noteLength = 250;
    // 2번 재생
    tone(SpeakerPin, Quit_melody[thisNote], noteLength);
    delay(noteLength);
    tone(SpeakerPin, Quit_melody[thisNote], noteLength);
    delay(noteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
}

void RSP(int game_speed) {//가위바위보 패 내기 안내음
  for (int thisNote = 0; thisNote < sizeof(RSP_melody) / sizeof(int); thisNote++) {
    // 음표 길이를 시간으로 변환
    int noteLength = game_speed;
    // 단음 재생
    tone(SpeakerPin, RSP_melody[thisNote], noteLength);
    delay(noteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
}

void MJP(int game_speed) {//묵찌빠 패 내기 안내음
  for (int thisNote = 0; thisNote < sizeof(Count3_melody) / sizeof(int); thisNote++) {
    // 음표 길이를 시간으로 변환
    int noteLength = game_speed;
    // 단음 재생
    tone(SpeakerPin, Count3_melody[thisNote], noteLength);
    delay(noteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
}

void Mugunghwa() {//무궁화꽃이피었습니다  안내음
  for (int thisNote = 0; thisNote < sizeof(Game3_melody) / sizeof(int); thisNote++) {
    // 음표 길이를 시간으로 변환
    int noteLength = 4;
    // 단음 재생
    tone(SpeakerPin, Game3_melody[thisNote], noteLength);
    delay(noteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
}

void HandOut(){ // 스피커를 통해 언제 자신의 패를 내는지 알려주는 함수
  for(int thisNote = 0; thisNote<sizeof(Melody)/sizeof(int); thisNote++){
    // 음표 길이를 시간으로 변환
    int NoteLength = 1000 / NoteDuration;
    // 단음 재생
    tone(SpeakerPin, Melody[thisNote], NoteLength);
    delay(NoteLength);
    noTone(SpeakerPin); // 현재 음 재생 중지
  }
  while(true); // 1회 재생 후 대기
}

void RockScissorsPaper(){ // 가위바위보 게임 함수
  RSP(game_speed_rsp);
  
  int Flex11Val; // Flex Sensors 값, 중지
  int Flex12Val; // 검지
  int Flex13Val; // 엄지
  int Flex21Val; // 중지
  int Flex22Val; // 검지
  int Flex23Val; // 엄지

  Flex11Val = analogRead(Flex11);
  Flex12Val = analogRead(Flex12);
  Flex13Val = analogRead(Flex13);
  Flex21Val = analogRead(Flex21);
  Flex22Val = analogRead(Flex22);
  Flex23Val = analogRead(Flex23);

  int Card1 = 0; // 첫번째 참가자의 패(1:Rock, 2:Scissors, 3:Paper)
  int Card2 = 0; // 두번째 참가자의 패(1:Rock, 2:Scissors, 3:Paper)

  GuessRSP(Flex11Val, Flex12Val, Flex13Val); // 어떤 패를 냈는지 판단
  Card1 = Card;
  GuessRSP(Flex21Val, Flex22Val, Flex23Val);
  Card2 = Card;
  
  Serial.print("첫번째 플레이어 :");
  Serial.println(Card1);
  Serial.print("엄지 : ");
  Serial.println(Flex11Val);
  Serial.print("검지 : ");
  Serial.println(Flex12Val);
  Serial.print("중지 : ");
  Serial.println(Flex13Val);
  Serial.println("");

  Serial.print("두번째 플레이어 :");
  Serial.println(Card2);
  Serial.print("엄지 : ");
  Serial.println(Flex21Val);
  Serial.print("검지 : ");
  Serial.println(Flex22Val);
  Serial.print("중지 : ");
  Serial.println(Flex23Val);
  Serial.println("");

  WinnerOfTheRound(Card1, Card2); // 첫번째 참가자를 기준으로 승패 결정(승,무,패)
  Vibration(outcome); // 승패 결과를 진동으로 알려주기
  ScoreBoard(); // 점수판 수정

  Serial.print("Score 1 : ");
  Serial.println(Score1);
  Serial.print("Score 2 : ");
  Serial.println(Score2);
  Serial.println("");

  return;
}

void GuessRSP(int val1, int val2, int val3){
  // 가위바위보 중 뭘 냈는지 판단하는 함수
  if(val1>=MID & val1<=MAX){ // Rock
    if(val2>=MID & val2<=MAX){
      if(val3>=MID & val3<=MAX){
        Card = 1;
      }
    }
    else if(val2>=MIN & val2<=MID){ // Scissors
      if(val3>=MIN & val3<=MID){
        Card = 2;
      }
    }
  }
  else if(val1>=MIN & val1<=MID){ // Paper
    if(val2>=MIN & val2<=MID){
      if(val3>=MIN & val3<=MID){
        Card = 3;
      }
      else { // Scissors
        Card = 2;
      }
    }
  }
}

void WinnerOfTheRound(int card1, int card2){
  if(card1 == card2){ // 똑같은 거 냈을 때
    outcome = TIE;
  }
  else{ // 다른 거 냈을 때
    if(card1 == 1){ // 바위
      if(card2 == 2){ // 가위
        outcome = WIN;
      }
      else if(card2 == 3){ // 보
        outcome = LOSE;
      }
    }
    else if(card1 == 2){ // 가위
      if(card2 == 1){ // 바위
        outcome = LOSE;
      }
      else if(card2 == 3){ // 보
        outcome = WIN;
      }
    }
    else if(card1 == 3){ // 보
      if(card2 == 1){ // 바위
        outcome = WIN;
      }
      else if(card2 == 2){ // 가위
        outcome = LOSE;
      }
    }
  }
}

void Vibration(int res){
  if(res == 10){ // 첫번째 참가자가 이겼을 때
    analogWrite(Vib1, 100);
    analogWrite(Vib2, 100);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
    analogWrite(Vib1, 100);
    analogWrite(Vib2, 0);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
    analogWrite(Vib1, 100);
    analogWrite(Vib2, 0);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
  }
  else if(res == 15){ // 비겼을 때
    analogWrite(Vib1, 100);
    analogWrite(Vib2, 100);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
    analogWrite(Vib1, 100);
    analogWrite(Vib2, 100);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
  }
  else if(res == 20){ // 첫번째 참가자가 졌을 때
    analogWrite(Vib1, 100);
    analogWrite(Vib2, 100);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 100);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 100);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
  }
}

void ScoreBoard(){ // Text LCD에 점수판 나타내기
  if(outcome == WIN){ // 점수 계산
    Score1++;
  }
  else if(outcome == LOSE){
    Score2++;
  }
}

void FinalResult(int sco1, int sco2){ // 진동모터로 최종 승자 알리기
  if(sco1 == sco2){
    analogWrite(Vib1, 100);
    analogWrite(Vib2, 100);
    delay(500);
    analogWrite(Vib1, 0);
    analogWrite(Vib2, 0);
    delay(1000);
  }
  else{
    if(sco1 > sco2){
      analogWrite(Vib1, 100);
      analogWrite(Vib2, 100);
      delay(500);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 0);
      delay(1000);
      analogWrite(Vib1, 100);
      analogWrite(Vib2, 0);
      delay(500);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 0);
      delay(1000);
      analogWrite(Vib1, 100);
      analogWrite(Vib2, 0);
      delay(500);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 0);
      delay(1000);
    }
    else{
      analogWrite(Vib1, 100);
      analogWrite(Vib2, 100);
      delay(500);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 0);
      delay(1000);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 100);
      delay(500);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 0);
      delay(1000);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 100);
      delay(500);
      analogWrite(Vib1, 0);
      analogWrite(Vib2, 0);
      delay(1000);
    }
  }
}

void MukChiPa(){  
  int Flex11Val; // Flex Sensors 값, 중지
  int Flex12Val; // 검지
  int Flex13Val; // 엄지
  int Flex21Val; // 중지
  int Flex22Val; // 검지
  int Flex23Val; // 엄지

  while(KOS == 0){
    RSP(game_speed_rsp);

    Flex11Val = analogRead(Flex11);
    Flex12Val = analogRead(Flex12);
    Flex13Val = analogRead(Flex13);
    Flex21Val = analogRead(Flex21);
    Flex22Val = analogRead(Flex22);
    Flex23Val = analogRead(Flex23);
    
    GuessRSP(Flex11Val, Flex12Val, Flex13Val); // 어떤 패를 냈는지 판단
    Card1[count] = Card;
    GuessRSP(Flex21Val, Flex22Val, Flex23Val);
    Card2[count] = Card;

    KeepOrStopMukchipa(Card1[count], Card2[count]); // 묵찌빠 게임에서 게임을 계속 할건지 말건지 결정
    ScoreBoard(); // 점수판 수정
  }
}

void KeepOrStopMukchipa(int card1, int card2){ // 묵찌빠 게임 계속 또는 결과 나옴
  if(count == 0){
    KOS = 0; // 묵찌빠 첫 판은 가위바위보니까 계속 함.
  }
  else { // 가위바위보 승패 결정 시부터
    if(Card1[count] != Card2[count]){ // 두 번째에 다른 거 내면 계속 함.
      KOS = 0;
      count++;
    }
    else { // 같은 거 냈을 때
      KOS = 1; // 점수 내야 하니까 멈춤.
      
      if(Card1[count-1] == 1){ // 1번 플레이어가 이전 판에 묵 냄.
        if(Card2[count-1] == 2){ // 2번 플레이어가 이전 판에 찌 냄.
          outcome = WIN; // 1번 플레이어 승리
        }
        else { // 2번 플레이어가 이전 판에 빠 냄.
          outcome = LOSE; // 2번 플레이어 승리
        }
      }
      else if(Card1[count-1] == 2){ // 1번 플레이어가 이전 판에 찌 냄.
        if(Card2[count-1] == 3){ // 2번 플레이어가 이전 판에 빠 냄.
          outcome = WIN;
        }
        else { // 2번 플레이어가 이전 판에 묵 냄
          outcome = LOSE;
        }
      }
      else { // 1번 플레이어가 이전 판에 빠 냄.
        if(Card2[count-1] == 1){ // 2번 플레이어가 이전 판에 묵 냄.
          outcome = WIN;
        }
        else { // 2번 플레이어가 이전 판에 찌 냄.
          outcome = LOSE;
        }
      }
    }
  }
}

void GameStart1(){
  time_previous = millis();
  StartTune1(); // 게임 시작 음 재생
  while(1){
    delay(5000); // 가변저항 설정 전 딜레이값
    game_speed_rsp = SetTheSpeed(); // 게임 속도 조절
    time_current = millis();
    if(time_current - time_previous > 10000){ // 게임 속도 조절 시간 : 10초
      game_speed_rsp = SetTheSpeed(); // 게임 속도 조절
      break;
    }
    HandOut2(game_speed_rsp); // 게임 속도 어느 정도인지 알 수 있게 음 계속 재생
  }

  while(Score1 < 3 && Score2 < 3){
    RockScissorsPaper();
  }

  EndTune(); // 게임 종료 음 재생
  FinalResult(Score1, Score2); // 최종 승자 정하기
}

void GameStart2(){
  time_previous = millis();
  StartTune2(); // 게임 시작 음 재생
  while(1){
    delay(5000);
    game_speed_rsp = SetTheSpeed(); // 게임 속도 조절
    time_current = millis();
    if(time_current - time_previous > 10000){ // 게임 속도 조절 시간 : 10초
      game_speed_rsp = SetTheSpeed(); // 게임 속도 조절
      break;
    }
    HandOut2(game_speed); // 게임 속도 어느 정도인지 알 수 있게 음 계속 재생
  }

  while(Score1 < 3 && Score2 < 3){
    MukChiPa();
  }
  
  EndTune(); // 게임 종료 음 재생
  FinalResult(Score1, Score2); // 최종 승자 정하기
  KOS = 0;
}

void GameStart3(){
  static boolean state11 = digitalRead(buttons[0]);
  static boolean state12 = digitalRead(buttons[1]);
  static boolean state13 = digitalRead(buttons[2]);
  static boolean state14 = digitalRead(buttons[3]);

  int Player1Hand;
  int Player2HandOrigin;
  int Player2HandNew;
  
  StartTune3();
  time_current = millis();
  while(1){
    digitalWrite(trigPin, HIGH);
    delay(10);
    digitalWrite(trigPin, LOW);
    float duration = pulseIn(echoPin, HIGH);
    float distance = duration * 340 / 10000 / 2;
    
    if(time_current - time_previous >= 1000){
      time_previous = time_current;
      
      Serial.print("Distance : ");
      Serial.print(distance);
      mySerial.print(distance);
      mySerial.write('\n');
      Serial.println();
    }

    // loop문이 아닌 while에서도 이게 바로 읽어 들이는지 모르겠네? // 안되면 count로 하기
    if(state11 || state12 || state13 || state14){ // 하나라도 버튼이 눌러진 경우
        outcome = LOSE;
        Vibration(outcome); // 승패 결과를 진동으로 알려주기
        ScoreBoard(); // 점수판 수정
        
        outcome = TIE;
        state11 = false; state12 = false; state13 = false; state14 = false; // 혹시나 해서 다시 리셋
        delay(3000); // 다시 게임 시작 전에 3초 딜레이 
    }
    Player1Hand = analogRead(Flex11);
    Player2HandOrigin = analogRead(Flex22); // 검지 중지 중에서 더 많이 변경되는 것으로 설정하기
    
    // 멈춰야 하는 순간 직전동작이랑 멈춰야 하는 순간 동작이랑 같다고 보기 // 매우 짧은 시간이라서 무방할 듯
    while(Player1Hand < 20){ // 손이 다 펴졌을 때
      Player2HandNew = analogRead(Flex22);
      if(abs(Player2HandOrigin - Player2HandNew) > 10){ // 움직임 감지 체크하여 '10' 수정하기
        outcome = WIN;
        Vibration(outcome);
        ScoreBoard();

        outcome = TIE;
        delay(3000);
      }

      Player1Hand = analogRead(Flex11);
    }
  }
}

void loop() {
  int GameNumber = ChooseTheGame(); // 게임 3개 중 하나 선택(태영), 0:가위바위보, 1:묵찌빠, 2:무궁화 꽃

  switch(GameNumber){
    case 1 : GameStart1(); //가위바위보 게임 시작
    break;
    
    case 2 : GameStart2();
    break;
    
    case 3 : GameStart3();
    break;
  }
}
