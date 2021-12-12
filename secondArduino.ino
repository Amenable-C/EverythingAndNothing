#include <pitches.h>
int NoteDuration = 4;
int SpeakerPin = A0; // 스피커 연결 핀
int sharkMelody[] = {
  NOTE_E4, NOTE_F4, NOTE_E4, NOTE_F4,
  NOTE_E4, NOTE_F4, NOTE_E4, NOTE_F4
}

void soundForShark(){ // 이 함수가 실행될 동안 메인에서는 넘어가는건가?... 안 넘어갈거 같은데...
  for(int thisNote = 0; thisNote<sizeof(sharkMelody)/sizeof(int); thisNote++){
    // 음표 길이를 시간으로 변환
    int NoteLength = 1000 / NoteDuration;
    // 단음 재생
    tone(SpeakerPin, sharkMelody[thisNote], NoteLength);
    delay(NoteLength);
    noTone((SpeakerPin); // 현재 음 재생 중지
  }
}

void setup() {
  Serial.begin(9600);
  Serial1.begin(9600);
}


void loop() {
  char buffer[128];
  int len = 0;
  while(true){
    while(Serial1.available()){
      char data = Serial1.read();
      if(data == '\n'){
        buffer[len] = '\n';
        String in_str = buffer;
        Serial.println("Distance : " + in_str);
        // 여기 부터 소리 출력
        int distance = in_str.toInt();
        if(distance > 90){
          NoteDuration = 1;
          soundForShark();
        }
        else if(distance > 30){
          NoteDuration = 4;
          soundForShark();
        }
        else{
          NoteDuration = 8;
          soundForShark();
        }
        //
        len = 0;
        break;
      }
      buffer[len++] = data;
    }
  }
}
