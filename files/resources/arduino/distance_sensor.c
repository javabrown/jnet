#include <Bridge.h>
#include <HttpClient.h>

#define trigPin 10
#define echoPin 8
#define soundPin 9

void setup() {
  Serial.begin (9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
}

void loop() {
  float duration, distance;
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);

  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  duration = pulseIn(echoPin, HIGH);
  distance = (duration / 2) * 0.0344 + 1;

  if (distance >= 400 || distance <= 2) {
    Serial.print("Distance = ");
    Serial.println("Out of range");
  }
  else {
    Serial.print("Distance = ");
    Serial.print(distance/2.54);
    Serial.println(" inch");

    if(distance/2.54 < 50){
      beep();
    }

    delay(500);
    //sendToJnet();
  }
  delay(500);
}


void beep(){
  analogWrite(soundPin, 20);      // Almost any value can be used except 0 and 255
                           // experiment to get the best tone
  delay(1000);          // wait for a delayms ms
  analogWrite(soundPin, 0);       // 0 turns it off
  delay(100);          // wait for a delayms ms
}

void sendToJnet(){
  HttpClient client;

  // Make a HTTP request:
  client.get("http://192.168.1.5:2013/mousemove?x=1&y=1");

  while (client.available()) {
    char c = client.read();
    Serial.print(c);
  }

}
