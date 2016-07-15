package com.sabotage.opmodes.autonomous.steps;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.sabotage.robot.Robot;


public class Step_Straight implements StepInterface {

    private final Integer distanceEncoderCounts;
    private final DcMotor.Direction direction;
    private final Robot.MotorPowerEnum motorPowerEnum = Robot.MotorPowerEnum.FTL;

    private Robot robot;

    private boolean initializedMotors = false;
    private boolean encodersReset = false;

    private int delayUntilLoopCount = 0;
    private final int DONE_TOLERANCE = 600;
    private static final double MOTOR_POWER_BALANCE_FACTOR = 1.0;

    //constructor
    public Step_Straight(Integer distanceEncoderCounts, DcMotor.Direction direction) {
        this.distanceEncoderCounts = distanceEncoderCounts + DONE_TOLERANCE;
        this.direction = direction;
    }


    @Override
    public String getLogKey() {
        return "Step_Straight";
    }

    @Override
    public void runStep() {


        resetEncodersAndSetMotorDirectionOnlyOnce();

        if (isStillWaiting()) {
            return;
        }


        initializeMotors();


        robot.motorRight.setPower(determinePower() * MOTOR_POWER_BALANCE_FACTOR);
        robot.motorLeft.setPower(determinePower());
        Log.w(getLogKey(), "remaining:" + rightRemainingDistance() + " motor power..." + robot.motorRight.getPower() + "    " + robot.loopCounter);

        logIt("Loop:");
    }


    private double determinePower() {


        int remainingDistance = rightRemainingDistance();

        if (remainingDistance > 1500) {

            return this.motorPowerEnum.getValue();
        }

        if (remainingDistance < 200) {

            return 0.1;
        }


        return this.motorPowerEnum.getValue() * rightRemainingDistance() / 1500;

    }


    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }

    private void resetEncodersAndSetMotorDirectionOnlyOnce() {

        if (encodersReset == false) {
            Log.w(getLogKey(), "resetEncodersAndSetMotorDirectionOnlyOnce..." + robot.loopCounter);
            robot.motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            robot.motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

            if (direction.equals(DcMotor.Direction.REVERSE)) {

                robot.motorLeft.setDirection(DcMotor.Direction.FORWARD);
                robot.motorRight.setDirection(DcMotor.Direction.REVERSE);


            } else {

                robot.motorLeft.setDirection(DcMotor.Direction.REVERSE);
                robot.motorRight.setDirection(DcMotor.Direction.FORWARD);
            }

            setLoopDelay();
            encodersReset = true;
        }

    }


    private void initializeMotors() {


        if (initializedMotors == false) {

            Log.w(getLogKey(), "initializeMotors..." + robot.loopCounter);

            robot.motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            robot.motorLeft.setTargetPosition(distanceEncoderCounts);

            robot.motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);


            robot.motorRight.setTargetPosition(distanceEncoderCounts);


            robot.motorRight.setPower(this.motorPowerEnum.getValue() * MOTOR_POWER_BALANCE_FACTOR);
            robot.motorLeft.setPower(this.motorPowerEnum.getValue());

            initializedMotors = true;
            setLoopDelay();


            StringBuilder sb = new StringBuilder();
            sb.append("initializeMotors" + robot.loopCounter);
            sb.append(" , CurrentPosition:" + robot.motorLeft.getCurrentPosition() + " , " + robot.motorRight.getCurrentPosition());
            sb.append(" ,TargetPosition:" + robot.motorLeft.getTargetPosition() + " , " + robot.motorRight.getTargetPosition());
            sb.append(" , Delta:" + (rightRemainingDistance() - getLeftRemainingPosition()));
            sb.append(" , LeftRemaining :" + getLeftRemainingPosition());
            sb.append(" , RightRemaining:" + rightRemainingDistance());


            Log.i(getLogKey(), sb.toString());

        }
    }

    private int rightRemainingDistance() {

        return Math.abs(robot.motorRight.getTargetPosition() - robot.motorRight.getCurrentPosition());
    }


    private int getLeftRemainingPosition() {

        return Math.abs(robot.motorLeft.getTargetPosition() - robot.motorLeft.getCurrentPosition());
    }


    @Override
    public boolean isStepDone() {

        if (isStillWaiting() || encodersReset == false || initializedMotors == false) {
            return false;
        }


        if (isMotorLeftDone()) {

            logIt("isStepDone:");

            robot.motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            robot.motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

            robot.motorRight.setPower(0);
            robot.motorLeft.setPower(0);

            return true;
        }

        return false;
    }

    private boolean isMotorLeftDone() {

        return Math.abs(getLeftRemainingPosition()) <= DONE_TOLERANCE;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + robot.loopCounter);
        sb.append(" , RightRemaining:" + (robot.motorRight.getTargetPosition() - robot.motorRight.getCurrentPosition()));
        sb.append(" , Delta:" + (rightRemainingDistance() - getLeftRemainingPosition()));
        sb.append(" , LeftRemaining :" + (robot.motorLeft.getTargetPosition() - robot.motorLeft.getCurrentPosition()));
        sb.append(" , LeftCurrent :" + (robot.motorLeft.getCurrentPosition()) + " rightCurrent:" + robot.motorRight.getCurrentPosition());
        Log.i(getLogKey(), sb.toString());

    }


    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }

    @Override
    public boolean isAborted() {
        return false;
    }


    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }


}
