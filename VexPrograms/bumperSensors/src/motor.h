#include <qeservo.h>

#define MAX_MOTOR_FORWARD 255
#define MAX_MOTOR_REVERSE 0
#define MOTOR_ZERO_SPEED 127
#define MAX_SPEED 127
#define OFFSET 127
#define MAX_SPEED_SETTING 1

CQEServo &motor = CQEServo::GetRef();
unsigned int leftMotorPort;
unsigned int rightMotorPort;

void setMotor(unsigned int port, unsigned char velocity);
void driveForward(unsigned char speed_setting);
void driveBackward(unsigned char speed_setting);
void turnRight(unsigned char speed_setting);
void turnLeft(unsigned char speed_setting);
void stop();

