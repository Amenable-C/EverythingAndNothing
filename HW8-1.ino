int pin_button = 14;
boolean state = true;

byte patterns[] = {
  0xFC, 0x60, 0xDA, 0xF2, 0x66, 0xB6, 0xBE, 0xE4, 0xFE, 0xE6 //이 값들을 byte로 나타내면 숫자로 표시된다는거
};
int digit_select_pin[] = {67, 68, 69};
int segment_pin[] = {58, 59, 60, 61, 62, 63, 64, 65};

int count = 0;
int SEGMENT_DELAY = 5; // 숫자 표시 사이의 시간 간격
unsigned long time_previous, time_current;

void setup() {
  pinMode(pin_button, INPUT);
  Serial.begin(9600);
  // put your setup code here, to run once:
  for(int i = 0; i < 3; i++){
    pinMode(digit_select_pin[i], OUTPUT);
  }
  for(int i = 0; i < 8; i++){
    pinMode(segment_pin[i], OUTPUT);
  }

  time_previous = millis();
}

// 해당 자리에 숫자 하나를 표시하는 함수
void show_digit(int pos, int number){ //(위치, 출력할 숫자)
  for(int i = 0; i < 3; i++){
    if(i + 1 == pos)
      digitalWrite(digit_select_pin[i], LOW);
    else
      digitalWrite(digit_select_pin[i], HIGH);
  }

  for(int i = 0; i < 8; i++){
    boolean on_off = bitRead(patterns[number], 7-i); // 2진수를 각 자리수마다 읽는거
    digitalWrite(segment_pin[i], on_off); // 총 7개를 키고 끌건지 선택
  }
}

// 4자리 7세그먼트 표시 장치에 3자리 숫자를 표시하는 함수
void show_4_digit(int number){
  number = number % 1000; //3자리로 제한
  int hundreads = number / 100;
  number = number % 100;
  int tens = number / 10;
  int ones = number % 10;

  show_digit(1, hundreads);
  delay(SEGMENT_DELAY);
  show_digit(2, tens);
  delay(SEGMENT_DELAY);
  show_digit(3, ones);
  delay(SEGMENT_DELAY);
}

void loop() {
  // put your main code here, to run repeatedly:
  time_current = millis();
  if(count == 0){
    count = 1000;
  }
  // 증가
  if(digitalRead(pin_button)){
    state = !state;
    delay(50);
  }
  if(state){
    if(time_current - time_previous >= 1000){
      time_previous = time_current;
      count++;
    }
  }
  
  // 감소
  if(!state){
    if(time_current - time_previous >= 1000){
      time_previous = time_current;
      count--;
    }
  }
  Serial.println(state); // 버튼 상태 확인

  show_4_digit(count);
}
