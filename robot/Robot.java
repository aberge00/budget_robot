package com.sabotage.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robocol.Telemetry;


public class Robot {

    public int HARDWARE_DELAY = 10;
    public int loopCounter;
    public Telemetry telemetry;


    public DcMotor motorRight;
    public DcMotor motorLeft;

    public enum TurnEnum {

        RIGHT,

        LEFT


    }

    public  enum MotorPowerEnum {

        LowLow(0.1),

        Low(0.2),

        Med(0.6),

        High(0.8),

        FTL(1.0);

        private double motorPower;

        MotorPowerEnum(double motorPower) {
            this.motorPower = motorPower;

        }

        public double getValue() {
            return this.motorPower;

        }

    }


}


