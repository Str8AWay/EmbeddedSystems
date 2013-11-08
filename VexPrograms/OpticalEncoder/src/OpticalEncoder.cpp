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
#include <9302hw.h>
#include <sonar.h>
#include <sonarcont.h>
#include <pthread.h>

using namespace std;

unsigned int leftWheelCount = 0;
unsigned int rightWheelCount = 0;

void cycleCount(unsigned int, timeval*, void*) {
	leftWheelCount++;
	printf("leftWheelCounta = %u\n", leftWheelCount);
}

pthread_t tid[2];

CSonar sonar = CSonar::CSonar(12, 13);

//volatile unsigned short *ReadEncoder;
//CQEGpioInt &io = CQEGpioInt::GetRef(); // get i/o reference
//C9302Hardware &hardware = C9302Hardware::GetRef();
//
//void InitEncoders(unsigned char num_of_enc) {
//
//	// Init Encoder
//	if (hardware.GetBitstreamMajorVersion()!=0xa0) {
//		throw std::runtime_error("Wrong FPGA bitstream version");
//	}
//
//	ReadEncoder = hardware.m_fpga.Ushort(0x500);
//
//	unsigned int iodir = io.GetDataDirection();
//	switch (num_of_enc) {
//	case 0:
//		break;
//	case 1:
//		iodir &= ~0x0003;
//		break;
//	default:
//		iodir & -~0x00ff;
//	}
//	io.SetDataDirection(iodir);
//}

//CQEGpioInt &io = CQEGpioInt::GetRef(); // get i/o reference

void sonarCallback(unsigned int port, int inches) {
	printf("distance %d\n", inches);
}

void* sonarFire(void *arg) {
	while (1) {
		sonar.Fire(false);
		usleep(20000);
	}
	return NULL;
}

int main() {
	//	io.SetDataDirection(0xFFFF); //bitmap of I/O ports (pin 10 is output, rest are inputs)
	//	io.SetData(0x0000);
	//	io.ResetDataBit(0);
	//	io.SetDataDirection(0x0000); //bitmap of I/O ports (pin 10 is output, rest are inputs)
	//	CQEAnalog &analog = CQEAnalog::GetRef();
	//
	//	if(io.SetInterrupt(8, true) == -1){
	//			printf("Set Optical Sensor failed");
	//		}
	//
	//	if(io.SetInterruptMode(8, QEG_INTERRUPT_NEGEDGE) == -1){
	//			printf("set interrupt Optical Sensor mode failed");
	//		}
	//
	//	if(io.RegisterCallback(8, 0, cycleCount) == -1){
	//			printf("register Optical Sensor call back failed");
	//		}
	//	int y  = 0;
	//	InitEncoders(1);
	//	unsigned short initial = ReadEncoder[0];
	//
	//	short previous = 0;

	//	volatile unsigned int data, prev;

	pthread_create(&tid[0], NULL, &sonarFire, NULL);
	sonar.RegisterCallback(sonarCallback);

	int i = 0;
	while (1) {
		//printf("num %d\n", i++);
		//printf("distance = %d\n", sonar.GetVal());
		//sleep(1);
		//		short position = ReadEncoder[0];
		//		if (position != previous)
		//		{
		//			printf("position %d\n", position);
		//			previous = position;
		//		}
		//
		//				data = io.GetData();
		//				if (prev != data) {
		//					printf("digital data 0x%04X\n", data);
		//				}
		//				prev = data;
		//		int voltage = analog.GetADVoltage(15);
		//		printf("analog value %d\n", voltage);
		//		if (analog.GetADVoltage(15) > 2000) {
		//			leftWheelCount++;
		//			while(analog.GetADVoltage(15) > 2000) {
		//				continue;
		//			}
		//		}
		//		if (y > 1000) {
		//			printf("leftWheelCount = %u\n", leftWheelCount);
		//			y = 0;
		//		}
		//		y++;
	}

}

