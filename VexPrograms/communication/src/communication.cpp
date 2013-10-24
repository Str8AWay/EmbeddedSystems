#include <arpa/inet.h>
#include <stdio.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include "motor.h"
#include <sonar.h>
#include <sonarcont.h>
#include <qegpioint.h>

#define BUFLEN 512
#define PORT 8888

unsigned int leftWheelCount = 0;
unsigned int rightWheelCount = 0;

void diep(char *s) {
	perror(s);
	exit(1);
}

void hitCliff(unsigned int, timeval*, void*){
	stop();
	printf("leftWheelCount = %u\n", leftWheelCount);
}

void cycleCount(unsigned int, timeval*, void*){
	leftWheelCount++;
	printf("leftWheelCounta = %u\n", leftWheelCount);
}


void hitWallBumper(unsigned int, timeval*, void*){

}

int main(void) {
	CQEGpioInt &io = CQEGpioInt::GetRef(); // get i/o reference
	struct sockaddr_in si_me, si_other;
	int s = sizeof(si_other);
	socklen_t slen = sizeof(si_other);
	char buf[BUFLEN];

	//CSonar **sonarArray;
	CSonar sonar = CSonar::CSonar(13,12);
	//sonarArray[0] = &sonar;
	//CSonarController sonarController = CSonarController::CSonarController(1, sonarArray, 50);
	//sonarController.SetFiring(1,sonarArray, 50);
	//io.SetDataDirection(0x1000);

	if(io.SetInterrupt(1, true) == -1){
		printf("Set Optical Sensor failed");
	}

	if(io.SetInterruptMode(1, QEG_INTERRUPT_POSEDGE) == -1){
		printf("set interrupt Optical Sensor mode failed");
	}

	if(io.SetInterrupt(14, true) == -1){
		printf("set Limit Sensor interrupt failed");
	}

	if(io.RegisterCallback(1, 0, cycleCount) == -1){
			printf("register Optical Sensor call back failed");
		}

	if(io.SetInterruptMode(14, QEG_INTERRUPT_POSEDGE) == -1){
		printf("set interrupt Limit Sensor mode failed");
	}

	if(io.RegisterCallback(14, 0, hitCliff) == -1){
		printf("register Limit Sensor call back failed");
	}

	if ((s = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1)
		diep("socket");

	memset((char *) &si_me, 0, sizeof(si_me));
	si_me.sin_family = AF_INET;
	si_me.sin_port = htons(PORT);
	si_me.sin_addr.s_addr = htonl(INADDR_ANY);
	if (bind(s, (struct sockaddr *)&si_me, sizeof(si_me)) == -1)
		diep("bind");

	//int sonarVals;
	while(1) {
		//sonarController.GetVals(sonarVals);
		sonar.Fire(false);
		printf("sonarValue %d\n", sonar.GetVal());
		recvfrom(s, buf, BUFLEN, 0, (struct sockaddr *)&si_other, &slen);
		if(strncmp(buf, "STOP", 4) == 0) {
			stop();
		}
		else if(strncmp(buf, "FORWARDS", 8)== 0){
			driveForward(MAX_SPEED_SETTING);
		}
		else if(strncmp(buf, "BACKWARDS", 9)== 0){
					driveBackward(MAX_SPEED_SETTING);
				}
		else if(strncmp(buf, "LEFT", 4)== 0){
					turnLeft(MAX_SPEED_SETTING);
				}
		else if(strncmp(buf, "RIGHT", 5)== 0){
					turnRight(MAX_SPEED_SETTING);
				}

		printf("Received packet from %s:%d\nData: %s\n\n", inet_ntoa(
				si_other.sin_addr), ntohs(si_other.sin_port), buf);
		bzero(buf, BUFLEN);
	}

	close(s);
	return 0;
}
