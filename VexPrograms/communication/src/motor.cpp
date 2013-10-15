#include "motor.h"

void setMotor(unsigned int port, unsigned char velocity)
{
    motor.SetCommand(port, velocity);
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
    setMotor(leftMotorPort, -speed + OFFSET);
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

void stop()
{
    motor.Disable(leftMotorPort);
    motor.Disable(rightMotorPort);
}
