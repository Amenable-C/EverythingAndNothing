int endLine = 30;

int trigPin = 3;
int echoPin = 2;

int flex_1 = A0;
int flex_2 = A1;

int buttons = {14, 15, 16, 17, 18, 19}; // 여섯개 쭉 깔아놓기 
void setup() {
  Serial.begin(9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  for(int i = 0; i < 4; i++){
    pinMode(buttons[i], INPUT);
  }

  int flex_1_pos;
  int flex_2_pos_new;
  int flex_2_pos_previous;

  gameStart(); // 게임 시작
}

void loop() {
  digitalWrite(trigPin, HIGH);
  delay(10);
  digitalWrite(trigPin, LOW);
  float duration = pulseIn(echoPin, HIGH);

  float distance = duration * 340 / 10000 / 2;

  // (4-1) 구현
  if(distance > 90){
    soundForShark(10); // 상어 브금 속도 1배속
  }
  else if(distance > 30){
    soundForShark(20); // 상어 브금 속도 2 배속
  }
  else if(distance > 10){
    soundForShark(30); // 상어 브금 속도 3배속
  }
  else{ // 거리가 10보다 작다는 말 // 그냥 손 뻗으면 바로 버튼이 닫는 거리
    soundForShark(100); // 이렇게 해서 Player2에게 바로 앞에 버튼이 있다는것을 알려줌?
  }

  // (6) 구현
  boolean state1 = digitalRead(buttons[0]);
  boolean state2 = digitalRead(buttons[1]);
  boolean state3 = digitalRead(buttons[2]);
  boolean state4 = digitalRead(buttons[3]);
  boolean state5 = digitalRead(buttons[4]);
  boolean state6 = digitalRead(buttons[5]);

  if(state1 || state2 || state3 || state4 || state5 || state6){ //하나라도 눌러진 경우
    Player2Win();
    delay(5000);
    soundForRestart(); 
  }

  // 
  flex_1_pos = analogRead(flex_1);
  flex_2_pos_previous = flex_2_pos_new;
  flex_2_pos_new = analogRead(flex_2);
  

  // (2-1) 구현
  if(flex_1_pos < 40){// 손이 다 펴졌을떄
    flex_2_pos_new = analogRead(flex_2);
    // (5) 구현
     if(abs(flex_2_pos_new - flex_2_pos_previous) > 10){// 움직였다는 말
      Player1Win();
      delay(5000);
      soundForRestart(); // 게임이 다시 진행된다는거
     }
  }
  
  

}
