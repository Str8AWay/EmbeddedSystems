//============================================================================
// Name        : OpticalEncoder.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description :C++, Ansi-style
//============================================================================
#include <arpa/inet.h>
#include <stdio.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <qegpioint.h>
#include <qeanalog.h>

using namespace std;

unsigned int leftWheelCount = 0;
unsigned int rightWheelCount = 0;


void cycleCount(unsigned int, timeval*, void*){
	leftWheelCount++;
	printf("leftWheelCounta = %u\n", leftWheelCount);
}

int main() {
//	CQEGpioInt &io = CQEGpioInt::GetRef(); // get i/o reference
//	io.SetData(0);
//	io.SetDataDirection(0x0000); //bitmap of I/O ports (pin 10 is output, rest are inputs)
	CQEAnalog &analog = CQEAnalog::GetRef();

//	if(io.SetInterrupt(8, true) == -1){
//			printf("Set Optical Sensor failed");
//		}

//	if(io.SetInterruptMode(8, QEG_INTERRUPT_NEGEDGE) == -1){
//			printf("set interrupt Optical Sensor mode failed");
//		}
//
//	if(io.RegisterCallback(8, 0, cycleCount) == -1){
//			printf("register Optical Sensor call back failed");
//		}
	int y  = 0;
	while(1){
		int voltage = analog.GetADVoltage(15);
		printf("analog value %d\n", voltage);
		if (analog.GetADVoltage(15) > 2000) {
			leftWheelCount++;
			while(analog.GetADVoltage(15) > 2000) {
				continue;
			}
		}
		if (y > 1000) {
			printf("leftWheelCount = %u\n", leftWheelCount);
			y = 0;
		}
		y++;
	}

}
