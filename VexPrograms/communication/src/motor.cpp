#include "motor.h"

void setMotor(unsigned int port, unsigned char velocity)
{
    motor.SetCommand(port, velocity);
}

void drive(char leftVel, char rightVel)
{
    motor.SetCommand(leftMotorPort, -leftVel + OFFSET);
    motor.SetCommand(rightMotorPort, rightVel + OFFSET);
}

void driveForward(unsigned char speed_setting)
{
    unsigned char speed;
    switch (speed_setting) {
        case MAX_SPEED_SETTING:
            speed = MAX_SPEED;
            break;
        case MEDIUM_SPEED_SETTING:
			speed = MEDIUM_SPEED;
			break;
        default:
            speed = 0;
    }
    setMotor(leftMotorPort, -(speed - 11) + OFFSET);
    setMotor(rightMotorPort, speed + OFFSET);
}

void driveBackward(unsigned char speed_setting)
{
    unsigned char speed;
    switch (speed_setting) {
        case MAX_SPEED_SETTING:
            speed = MAX_SPEED;
            break;
        case MEDIUM_SPEED_SETTING:
       			speed = MEDIUM_SPEED;
       			break;
        default:
            speed = 0;
    }
    setMotor(leftMotorPort, speed + OFFSET);
    setMotor(rightMotorPort, -speed + OFFSET);
}

void turnRight(unsigned char speed_setting)
{
    unsigned char speed;
    switch (speed_setting) {
        case MAX_SPEED_SETTING:
            speed = MAX_SPEED;
            break;
        case MEDIUM_SPEED_SETTING:
       			speed = MEDIUM_SPEED;
       			break;
        default:
            speed = 0;
    }
    setMotor(leftMotorPort, -speed + OFFSET);
    setMotor(rightMotorPort, -speed + OFFSET);
}

void turnLeft(unsigned char speed_setting)
{
    unsigned char speed;
    switch (speed_setting) {
        case MAX_SPEED_SETTING:
            speed = MAX_SPEED;
            break;
        case MEDIUM_SPEED_SETTING:
       			speed = MEDIUM_SPEED;
       			break;
        default:
            speed = 0;
    }
    setMotor(leftMotorPort, speed + OFFSET);
    setMotor(rightMotorPort, speed + OFFSET);
}

void driveToBearing(int bearing)
{
    char left, right;
    if (bearing < BEARING_FREEDOM) {
        left = MAX_SPEED;
        right = (90 + bearing)*MAX_SPEED/90;
    } else if (bearing > BEARING_FREEDOM) {
        left = (90 - bearing)*MAX_SPEED/90;
        right = MAX_SPEED;
    } else {
        left = MAX_SPEED;
        right = MAX_SPEED;
    }
    drive(left, right);
}

void stop()
{
    motor.Disable(leftMotorPort);
    motor.Disable(rightMotorPort);
}
