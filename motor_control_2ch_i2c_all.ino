#define debug 1

#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>


/////////////////// Encoder Driver ////////////////////////
#define ENC1_ADD 41
#define ENC2_ADD 42

signed long encoder1count = 0;
signed long encoder2count = 0;


/////////////////// Steering Servo Control ///////////////////
#define RC_SERVO_PIN 29 ////////////////////////이거 나중에 다 바꿔주기////////////////////////////
#define NEURAL_ANGLE 90 // 서보의 중립 (0~180) //90으로 놓고 조립
#define LEFT_STEER_ANGLE -15
#define RIGHT_STEER_ANGLE 15 // 나중에 차량에 맞게 바꿔야 함 // 한계치보다 조금씩 적게 해야함

#include <Servo.h>
Servo Steeringservo;
int Steering_Angle = NEURAL_ANGLE; //중립

void steering_control(){ // 최대, 최소에서 더 안 돌아가게 정의
  if(Steering_Angle <= LEFT_STEER_ANGLE + NEURAL_ANGLE)
    Steering_Angle = LEFT_STEER_ANGLE + NEURAL_ANGLE;
  if(Steering_Angle >= RIGHT_STEER_ANGLE + NEURAL_ANGLE)
    Steering_Angle = RIGHT_STEER_ANGLE + NEURAL_ANGLE;
  Steeringservo.write(Steering_Angle);
  Serial.println(Steering_Angle);
}


/////////////////// Sonar Sensor Setup ///////////////////
#include <NewPing.h>

#define TRIGGER_PIN  3  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 150 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.

int sonar_data = 10; // 통신을 할때 float로 하기에는 조금 어려워서, int로 설정

float sonar_sensor_read(void){
  float distance = sonar.ping_cm();
  if(distance == 0.0)
    distance = MAX_DISTANCE; // MAX를 넘어가면 0이 나오는데 이거를 MAX로 한다는거
  return distance;
}




/////////////////// initEncoders() 함수 ////////////////////////
// setup에서 이 함수가 실행
void initEncoders() {
  // Set slave selects as outputs
  pinMode(ENC1_ADD, OUTPUT);
  pinMode(ENC2_ADD, OUTPUT);
  
  // Raise select pins
  // Communication begins when you drop the individual select signsl
  digitalWrite(ENC1_ADD,HIGH);
  digitalWrite(ENC2_ADD,HIGH);
  
  SPI.begin(); // spi 통신을 위해 통신 포트 초기화
  
  // Initialize encoder 1
  //    Clock division factor: 0
  //    Negative index input
  //    free-running count mode
  //    x4 quatrature count mode (four counts per quadrature cycle)
  // NOTE: For more information on commands, see datasheet
  digitalWrite(ENC1_ADD,LOW);        // Begin SPI conversation
  SPI.transfer(0x88);                       // Write to MDR0
  SPI.transfer(0x03);                       // Configure to 4 byte mode
  digitalWrite(ENC1_ADD,HIGH);       // Terminate SPI conversation 

  // Initialize encoder 2
  //    Clock division factor: 0
  //    Negative index input
  //    free-running count mode
  //    x4 quatrature count mode (four counts per quadrature cycle)
  // NOTE: For more information on commands, see datasheet
  digitalWrite(ENC2_ADD,LOW);        // Begin SPI conversation
  SPI.transfer(0x88);                       // Write to MDR0
  SPI.transfer(0x03);                       // Configure to 4 byte mode
  digitalWrite(ENC2_ADD,HIGH);       // Terminate SPI conversation 

  
}

long readEncoder(int encoder_no) 
{
  
  // Initialize temporary variables for SPI read
  unsigned int count_1, count_2, count_3, count_4;
  long count_value;  
  
  
    digitalWrite(ENC1_ADD + encoder_no-1,LOW);      // Begin SPI conversation
   // digitalWrite(ENC4_ADD,LOW);      // Begin SPI conversation
    SPI.transfer(0x60);                     // Request count
    count_1 = SPI.transfer(0x00);           // Read highest order byte
    count_2 = SPI.transfer(0x00);           
    count_3 = SPI.transfer(0x00);           
    count_4 = SPI.transfer(0x00);           // Read lowest order byte
    digitalWrite(ENC1_ADD+encoder_no-1,HIGH);     // Terminate SPI conversation 
    //digitalWrite(ENC4_ADD,HIGH);      // Begin SPI conversation
// Calculate encoder count
  count_value= ((long)count_1<<24) + ((long)count_2<<16) + ((long)count_3<<8 ) + (long)count_4;
  
  return count_value;
}


void clearEncoderCount(int encoder_no) {
    
  // Set encoder1's data register to 0
  digitalWrite(ENC1_ADD+encoder_no-1,LOW);      // Begin SPI conversation  
  // Write to DTR
  SPI.transfer(0x98);    
  // Load data
  SPI.transfer(0x00);  // Highest order byte
  SPI.transfer(0x00);           
  SPI.transfer(0x00);           
  SPI.transfer(0x00);  // lowest order byte
  digitalWrite(ENC1_ADD+encoder_no-1,HIGH);     // Terminate SPI conversation 
  
  delayMicroseconds(100);  // provides some breathing room between SPI conversations
  
  // Set encoder1's current data register to center
  digitalWrite(ENC1_ADD+encoder_no-1,LOW);      // Begin SPI conversation  
  SPI.transfer(0xE0);    
  digitalWrite(ENC1_ADD+encoder_no-1,HIGH);     // Terminate SPI conversation 
}
/////////////////// Motor Driver ////////////////////////
#define STBY1  39
#define STBY2  40

#define MOTOR1_EN1 32
#define MOTOR1_EN2 31
#define MOTOR1_PWM 8

#define MOTOR2_EN1 33
#define MOTOR2_EN2 34
#define MOTOR2_PWM 7

int Motor_Speed; 


void motor1_control(int dir, int speed)
{
  digitalWrite(STBY1, HIGH);
   switch(dir)
   {
      case  1: digitalWrite(MOTOR1_EN1, HIGH); ////////////////////핀 어떻게 꼽혀있는지 보고 옆에 숫자 수정
               digitalWrite(MOTOR1_EN2, LOW);
               analogWrite(MOTOR1_PWM, speed);
               break; 
      
      case -1:    
               digitalWrite(MOTOR1_EN1, LOW);
               digitalWrite(MOTOR1_EN2, HIGH);
               analogWrite(MOTOR1_PWM, speed);      
               break;

      case 0 :
               digitalWrite(MOTOR1_EN1, LOW);
               digitalWrite(MOTOR1_EN2, LOW);
               analogWrite(MOTOR1_PWM, 0);
               break;
      default :                  
               digitalWrite(MOTOR1_EN1, LOW);
               digitalWrite(MOTOR1_EN2, LOW);
               analogWrite(MOTOR1_PWM, 0);
               break;     
   }
}
void motor2_control(int dir, int speed)
{
  digitalWrite(STBY1, HIGH);
   switch(dir)
   {
      case  1: digitalWrite(MOTOR2_EN1, HIGH);
               digitalWrite(MOTOR2_EN2, LOW);
               analogWrite(MOTOR2_PWM, speed);
               break; 
      
      case -1:    
               digitalWrite(MOTOR2_EN1, LOW);
               digitalWrite(MOTOR2_EN2, HIGH);
               analogWrite(MOTOR2_PWM, speed);      
               break;

      case 0 :
               digitalWrite(MOTOR2_EN1, LOW);
               digitalWrite(MOTOR2_EN2, LOW);
               analogWrite(MOTOR2_PWM, 0);
               break;
      default :                  
               digitalWrite(MOTOR2_EN1, LOW);
               digitalWrite(MOTOR2_EN2, LOW);
               analogWrite(MOTOR2_PWM, 0);
               break;     
   }
}

/////////////////// I2C 통신 ///////////////////
#include <Wire.h>
int sensor_flag = 0;

// 여기는 받는거 // angle, speed, sonar
void receiveEvent(int howMany){
  unsigned char a[7]; //여기에 데이터 저장 
  // 통신프로토콜 정의 // '#'(start) + '제어 byte' + '2byte' + '*'
  a[0] = Wire.read(); // '#' = start
  a[1] = Wire.read(); // control = 제어->어떤 기능할건지
  a[2] = Wire.read(); // data // about angle
  a[3] = Wire.read(); // data // about angle
  a[4] = Wire.read(); // data // about speed
  a[5] = Wire.read(); // data (총 데이터는 4byte로 보낼 수 있음) // about speed
  a[6] = Wire.read(); // '*' = end
  // 여기까지 값을 읽어 오는거
  // steering and motor control
  if((a[0] == '#') && (a[1] == 'C' ) && (a[6] == '*')){
    Steering_Angle = a[2] * 256 + a[3]; // steering angle 0~180 // ROS에서 아두이노로 값을 준다는거
    //Steering_Angle = 0; // test
    steering_control(); // 여기 함수로 가서 차량에 맞게 값 입력
    
    if((a[4]&0x80 >> 7) == 1){ // check MSB bit is 1 -> negative
      Motor_Speed = ((255 - a[4]) + (256 - a[5])) * -1; // 여기 두 개 -> 값을 정수 속도 값
    }
    else{
      Motor_Speed = a[4]*256 + a[5]; // 여기 두 개 -> 값을 정수 속도 값
    }
    //Motor_Speed = 30; // test
    // 그에 따른 전진, 후진, 멈춤
    if(Motor_Speed > 0)
      motor1_control(1, Motor_Speed);
    else if(Motor_Speed < 0)
      motor1_control(-1, -Motor_Speed);
    else
      motor1_control(0, 0);
  }

  // sonar sensor control
  if((a[0] == '#') && (a[1] == 'S' ) && (a[6] == '*')){
    sensor_flag = 1; // 값 자체를 받을 수는 없나?... 값을 받아야 하는데?
  }
}

// 여기는 주는거 // sonar, encoder
void requestEvent(){
  unsigned char s[8] = {0, };
  encoder1count = readEncoder(1);
  s[0] = '#';
  s[1] = (sonar_data&0xff00) >> 8;
  s[2] = (sonar_data&0x00ff);
  s[3] = (encoder1count&0xff000000) >> 24; //encoder MSB 8bit
  s[4] = (encoder1count&0x00ff0000) >> 16;
  s[5] = (encoder1count&0x0000ff00) >> 8;  
  s[6] = (encoder1count&0x000000ff);  // encoder LSB 8bit
  s[7] = '*';
  Wire.write(s, 8); // respond
  sensor_flag = 0; // 이게 먼지 다시 체크 // 느릴수도 있다고 이렇게 sensor_flag를 쓴다고 하는데 머지??
}


/////////////////// setup() 함수 ////////////////////////
void setup() 
{
  // 통신 부분
  Wire.begin(5);  // I2C bus #5 // 젯슨 나노에서 확인하면 5번 BUS를 사용해서 통신하는거 확인가능
  Wire.onRequest(requestEvent);  // register events
  Wire.onReceive(receiveEvent);
  

  Serial.begin(115200);      // Serial com for data output
  
  initEncoders();       Serial.println("Encoders Initialized...");  
  clearEncoderCount(1);  Serial.println("Encoder[1] Cleared...");
  clearEncoderCount(2);  Serial.println("Encoder[2] Cleared..."); //2번은 안 필요한거 같은데??
  
 
  pinMode(STBY1, OUTPUT); pinMode(STBY2, OUTPUT);
  pinMode(MOTOR1_EN1, OUTPUT);   pinMode(MOTOR1_EN2, OUTPUT);   pinMode(MOTOR1_PWM, OUTPUT);
  pinMode(MOTOR2_EN1, OUTPUT);   pinMode(MOTOR2_EN2, OUTPUT);   pinMode(MOTOR2_PWM, OUTPUT);
  
  Steeringservo.attach(RC_SERVO_PIN);
  Steeringservo.write(Steering_Angle); // 이게 원래 있던거 // 주석 풀기 // 이게 값을 받아와서 작동하는거
  // Steering_Angle = 30; // 이거 내가 그냥 임의의 값 넣은거 // 근데 원래는 90도 인데 왜 우리꺼는 30도가 거의 중립이지?
  // Steeringservo.write(Steering_Angle); // 중립 맞출려고 내가 넣은거
  // 일단 맨처음에 중립 서보모터에 맞춰서 각 조절해야함.
  delay(1000);
  
}

void loop() {
   encoder1count = readEncoder(1);  encoder2count = readEncoder(2); 
   Steering_Angle = 90; // test
   //Serial.print(Steering_Angle);
   steering_control(); // 여기 함수로 가서 차량에 맞게 값 입력
   Motor_Speed = 60;
   if(Motor_Speed > 0)
      motor1_control(1, Motor_Speed);
    else if(Motor_Speed < 0)
      motor1_control(-1, -Motor_Speed);
    else
      motor1_control(0, 0);
   
   //motor1_control(1, 30);
//   delay(2000);

//   
//   Serial.print("Enc1: "); Serial.print(encoder1count); Serial.print(" "); 
//   Serial.print("Enc2: "); Serial.println(encoder2count); 


 ////////////////////////////// sonar체크 할려고 위에꺼 주석처리
 sonar_data = sonar_sensor_read() * 10; // *10을 해서 mm단위가 나옴
 if(debug == 1){
  /////// Sonar data ///////
  Serial.print("Sonar : ");
  Serial.print(sonar_data);
  Serial.print("cm");

  /////// Steering Servo ///////  
  Serial.print("  Steering Angle : ");
  Serial.print(Steering_Angle);

  /////// encoder ///////
  Serial.print("  Encoder Pos : ");
  Serial.print(encoder1count);

  /////// Motor PWM ///////
  Serial.print("  Motor PWM : ");
  Serial.println(Motor_Speed);
 }
 
 
}
