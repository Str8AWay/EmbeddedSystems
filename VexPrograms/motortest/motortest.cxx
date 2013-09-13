#include <qeservo.h>
#include <stdio.h>
#include <stdlib.h>
#include <TIME.h>

int main(int argc, char **argv)
{

  // get motor reference
  CQEServo *motor = CQEServo::GetPtr();
  printf("running away\n");

  // move back and forth
	  motor->SetCommand(0, 0);
	  motor->SetCommand(1,255);
	  sleep(5);
  	  motor->SetCommand(0, 255);
  	  motor->SetCommand(1,0);
  	  sleep(5);
  	  motor->SetCommand(0, 0);
  	  motor->SetCommand(1, 0);
  	  sleep(5);
  	  motor->SetCommand(0,255);
  	  motor->SetCommand(1,255);
  	  sleep(5);
  	  motor->Disable(0);
  	  motor->Disable(1);
  	  motor->Release();
}

