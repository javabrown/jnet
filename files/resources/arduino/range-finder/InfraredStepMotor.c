#include <IRremote.h>
#include <Stepper.h>

int RECV_PIN = 12;
IRrecv irrecv(RECV_PIN);
decode_results results;

int led = 13;

const int stepsPerRevolution = 200;  // change this to fit the number of steps per revolution
Stepper myStepper(stepsPerRevolution, 8, 9, 10, 11);
int stepCount = 0;

void setup()
{
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
  pinMode(led, OUTPUT);

}

void loop() {

  if (irrecv.decode(&results)) {
    Serial.println(results.value, HEX);
    doIRAction();
    irrecv.resume(); // Receive the next value
  }
  delay(10);
}


void doIRAction(){
  switch(results.value)
  {
    case 0xFF6897:
       toggleLed();
       break;

    default:
      Serial.println(" Other button");
  }

  startStepMoter();
}

void startStepMoter(){
    myStepper.setSpeed(60);
    myStepper.step(500);
}

void toggleLed(){
  int ledStatus = 0;
  ledStatus = digitalRead(led);

  if(ledStatus == 0){
    Serial.println("Turn on Led");
    digitalWrite(led, HIGH);

    startStepMoter();
  }

  if(ledStatus == 1){
    Serial.println("Turn on Led");
    digitalWrite(led, LOW);
  }
}

