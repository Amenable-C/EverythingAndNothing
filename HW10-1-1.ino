#include <SoftwareSerial.h>
SoftwareSerial mySerial(4, 5);
unsigned long time_previous, time_current;

void setup() {
  mySerial.begin(9600);
  Serial.begin(9600);
  time_previous = millis();
}

void loop() {
  int reading = analogRead(54); 
  float voltage = reading * 5.0 / 1024.0;
  
  float temp_C = voltage * 100;
  
  time_current = millis();
  if(time_current - time_previous >= 1000){
    time_previous = time_current;
    
    Serial.print(voltage);
    Serial.print(" V : ");
    //Serial.print(temp_C);
    mySerial.print(temp_C);
    Serial.print(temp_C);
    mySerial.write('\n');
    Serial.print(" C");
    Serial.println();
  }
}
