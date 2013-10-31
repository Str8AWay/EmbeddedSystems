#include <qeservo.h>

#define MAX_MOTOR_FORWARD 255
#define MAX_MOTOR_REVERSE 0
#define MOTOR_ZERO_SPEED 127
#define MAX_SPEED 127
#define MEDIUM_SPEED 50
#define OFFSET 127
#define MAX_SPEED_SETTING 1
#define MEDIUM_SPEED_SETTING 2
#define BEARING_FREEDOM 0

static CQEServo &motor = CQEServo::GetRef();
static unsigned int leftMotorPort = 0;
static unsigned int rightMotorPort = 1;

void setMotor(unsigned int port, unsigned char velocity);
void drive(char leftVel, char rightVel);
void driveForward(unsigned char speed_setting);
void driveBackward(unsigned char speed_setting);
void turnRight(unsigned char speed_setting);
void turnLeft(unsigned char speed_setting);
void driveToBearing(int bearing);
void stop();
