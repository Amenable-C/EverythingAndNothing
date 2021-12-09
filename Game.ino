unsigned long time_previous;
unsigned long time_current;

// HandOut 함수를 위한 전역 변수
int SpeakerPin = A0; // 스피커 연결 핀

// RockScissorsPaper 함수를 위한 전역 변수
int Flex11 = A1; // Flex Sensors 연결된 아날로그 핀
int Flex12 = A2;
int Flex13 = A3;
int Flex21 = A4;
int Flex22 = A5;
int Flex23 = A6;

void setup() {

}

void loop() {
  int GameNumer = ChooseTheGame(); // 게임 3개 중 하나 선택(태영), 0:가위바위보, 1:묵찌빠, 2:무궁화 꽃

  switch(GameNumber){
    case 0 : GameStart1(); //가위바위보 게임 시작
    break;
    
    case 1 : GameStart2();
    break;
    
    case 2 : GameStart3();
    break;
  }
}

void GameStart1(){
  time_previous = millis();
  StartTune(); // 게임 시작 음 재생
  while(1){
    int game_speed = SetTheSpeed(); // 게임 속도 조절
    time_current = millis();
    if(time_current - time_previous > 10000){ // 게임 속도 조절 시간 : 10초
      game_speed = SetTheSpeed(); // 게임 속도 조절
      break;
    }
    HandOut(game_speed); // 게임 속도 어느 정도인지 알 수 있게 음 계속 재생
  }

  while(Score1 < 3 || Score2 < 3){
    RockScissorsPaper();
  }

  EndTune(); // 게임 종료 음 재생
  FinalResult(Score1, Score2); // 최종 승자 정하기
}

void GameStart2(){
  time_previous = millis();
  StartTune(); // 게임 시작 음 재생
  while(1){
    int game_speed = SetTheSpeed(); // 게임 속도 조절
    time_current = millis();
    if(time_current - time_previous > 10000){ // 게임 속도 조절 시간 : 10초
      game_speed = SetTheSpeed(); // 게임 속도 조절
      break;
    }
    HandOut(game_speed); // 게임 속도 어느 정도인지 알 수 있게 음 계속 재생
  }

  while(Score1 < 3 || Score2 < 3){
    MukChiPa();
  }
  
  EndTune(); // 게임 종료 음 재생
  FinalResult(Score1, Score2); // 최종 승자 정하기
}

void GameStart3(){
  // 무궁화 꽃이 피었습니다 게임 함수
}
