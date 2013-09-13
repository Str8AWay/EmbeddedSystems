//============================================================================
// Name        : bumperSensors.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
// references: http://www.vexforum.com/wiki/index.php/VEXpro_Programming_Resources
// 			   http://www.vexforum.com/wiki/Bumper_Switch
//============================================================================

#include <qeservo.h>
#include <stdio.h>
#include <stdlib.h>
#include <TIME.h>
#include <qegpioint.h>
#include <keypad.h>

int main(int argc, char **argv){
  CQEServo *motor = CQEServo::GetPtr();  // get motor reference
  CQEGpioInt &io = CQEGpioInt::GetRef(); // get i/o reference
  CKeypad &key = CKeypad::GetRef(); // get key reference

  bool button = true; //button switch high normally, low when pressed

  io.SetDataDirection(0x00200); //bitmap of I/O ports (pin 10 is output, rest are inputs)
  io.ResetDataBit(9); //Resets output pin 10, port 9

  printf("Bump!\n");

  while(!key.KeyCancel()){ //cancel key  on vex will stop operation :)
	  motor->SetCommand(0, 0); //moving forwards
	  motor->SetCommand(1,255);

	  button = (io.GetData()& 0x8000); //port 15, pin 16

	  if(!button){//switch press is true, pin 16 low
		io.SetData(9); //pin 10 on logic high
		sleep(5);
		motor->SetCommand(0, 255); //back off
		motor->SetCommand(1,0);
		sleep(5);
		motor->SetCommand(0, 0);// turn
		motor->SetCommand(1, 0);
		sleep(5);
	  }

	  else io.ResetDataBit(9); //switch press is false, pin 16 high, reset pin 10 to logic low
  }

  motor->Release();
  io.Release();
  key.Release();
}
