/* FTC  */

package com.sabotage.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.sabotage.robot.Robot;
import com.sabotage.opmodes.autonomous.steps.StepInterface;

import java.util.List;


public abstract class BaseRescueOp extends OpMode {


    protected List<StepInterface> stepList = null;

    private static final String KEY = "Step_BaseRescueOp";

    private Robot robot = null;
    private int delayUntilLoopCount = 0;
    private int activeStepNumber = 0;

    private boolean init_HardwarePositions;

    private long MAX_RUN_TIME_MILLI_SECONDS = 31 * 1000;

    private Long startTimeMilliSeconds = null;

    private boolean rescueIsAborted = false;

    protected abstract void init_defineRescueAsStepList();

    @Override
    public void init() {

        init_defineRescueAsStepList();
        init_readRobotHardwareMap();


        init_calibrateGyroSensors();

    }

    private void init_calibrateGyroSensors() {


//        robot.gyroSensor.calibrate();

    }


    private void init_readRobotHardwareMap() {

        robot = new Robot();
        robot.telemetry = this.telemetry;

        robot.motorRight = hardwareMap.dcMotor.get("motorRight");
        robot.motorLeft = hardwareMap.dcMotor.get("motorLeft");


    }

    private void initializeHardwarePositions() {

        if(init_HardwarePositions == false) {


            init_HardwarePositions = true;

        }
    }

    @Override
    public void loop() {

        robot.loopCounter = robot.loopCounter + 1;

        initStartTime();

        if (isWaiting() || isCalibratingGyroSensor()) {
            Log.i(KEY, "isWaiting:" + isWaiting() + " isCalibratingGyroSensor:" + isCalibratingGyroSensor());

            return;
        }


        initializeHardwarePositions();

//        runDebrisPickupToEject();
        logIt();
        sendTelemetry();

        performRescue();

    }


    private boolean isWaiting() {
        return this.delayUntilLoopCount > robot.loopCounter;
    }

    private boolean isCalibratingGyroSensor() {

//        return (robot.gyroSensor.isCalibrating());
        return false;
    }

    private void initStartTime() {

        if (this.startTimeMilliSeconds == null) {
            this.startTimeMilliSeconds = System.currentTimeMillis();
        }
    }


    @Override
    public void stop() {


        sendTelemetry();
        robot.motorRight.setPower(0);
        robot.motorLeft.setPower(0);
    }

    private void sendTelemetry() {
        robot.telemetry.addData("Status1", "Rescue Step:" + this.activeStepNumber + " Loop:" + robot.loopCounter);

    }


    private void performRescue() {

        if (isRescueDone() || this.rescueIsAborted) {

            celebrate();

        } else {

            StepInterface activeStep = stepList.get(activeStepNumber);
            activeStep.setRobot(robot);
            activeStep.runStep();

            if (activeStep.isAborted()) {

                rescueIsAborted = true;
                return;
            }


            if (activeStep.isStepDone()) {
                activeStepNumber = activeStepNumber + 1;
                setLoopDelay();
            }

        }


    }


    private void celebrate() {

        robot.telemetry.addData("CELEBRATE with Happy Dance", this.activeStepNumber + " Loop:" + robot.loopCounter);
        robot.telemetry.addData("DURATION", (System.currentTimeMillis() / 1000 - this.startTimeMilliSeconds / 1000));
        Log.i(KEY + "_END", "DURATION: " + (System.currentTimeMillis() / 1000 - this.startTimeMilliSeconds / 1000));
        stop();

    }

    private void logIt() {

        StringBuilder sb = new StringBuilder();
        sb.append("LoopCount:" + robot.loopCounter + " Step:" + activeStepNumber);
//        sb.append(" Gyro Rotation:" + robot.gyroSensor.getIntegratedZValue());
        Log.i(KEY, sb.toString());

    }


    private boolean isRescueAborted() {

        return false;
    }

    private boolean isRescueDone() {


        return isAutoTimeOver() || activeStepNumber >= stepList.size();
    }

    private boolean isAutoTimeOver() {

        if (System.currentTimeMillis() >= this.startTimeMilliSeconds + MAX_RUN_TIME_MILLI_SECONDS) {

            Log.i(KEY + "_TOUT", "DURATION: " + (System.currentTimeMillis() / 1000 - this.startTimeMilliSeconds / 1000));

            return true;
        }
        return false;
    }


    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + 10;
    }

}
