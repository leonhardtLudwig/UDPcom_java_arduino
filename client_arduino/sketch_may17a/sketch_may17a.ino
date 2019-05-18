#include "TimeLib.h"
#include <Ethernet.h>
#include <EthernetUDP.h>
#include <SPI.h>
int lux;
byte mac[] = {
  0xBE, 0xEF, 0xBE, 0xEF, 0xBE, 0xEF
};
IPAddress server(192,168,8,25);
IPAddress ip(192, 168, 8, 193);
unsigned int localPort = 9876;
EthernetUDP udp;
void setup() {
 Ethernet.begin(mac,ip);
 delay(100);
 Serial.begin(9600);
 delay(100);
 udp.begin(localPort);
 bool received = false;
 char buf[20];
 buf[19]='\0';
 do{
  udp.beginPacket(server,localPort);
  udp.write("GETDATE");
  udp.endPacket();
  int pkg_size = udp.parsePacket();
  received = pkg_size!=0;
  delay(1000);
 }while(!received);
 udp.read(buf,UDP_TX_PACKET_MAX_SIZE);
 String date(buf);
 setTime(date.substring(11,13).toInt(),date.substring(14,16).toInt(),date.substring(17,19).toInt(),date.substring(0,2).toInt(),date.substring(3,5).toInt(),date.substring(6,10).toInt());
}
int temp_lux = 0;
void loop() {
  char date[20];
  sprintf(date,"%02d/%02d/%04d_%02d:%02d:%02d",day(),month(),year(),hour(),minute(),second());
  char info[36];
  sprintf(info,"Date: %s, Lux: %03d",date,(++temp_lux)%999);
  udp.beginPacket(server,localPort);
  udp.write(info);
  udp.endPacket();
  delay(2000);
}
