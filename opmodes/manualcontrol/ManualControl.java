/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.sabotage.opmodes.manualcontrol;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


public class ManualControl extends OpMode {


    private static final String KEY = "Manual";

    private DcMotor motorRight;
    private DcMotor motorLeft;

    double[] scaleDriveArray = {0.0, 0.06, 0.09, 0.10, 0.22, 0.25, 0.31, 0.34,
            0.40, 0.45, 0.50, 0.50, 0.55, 0.60, 0.80, 0.90, 1.00};
    private int loopCounter = 0;


    public ManualControl() {
    }


    @Override
    public void init() {


        this.motorLeft = hardwareMap.dcMotor.get("motorLeft");
        this.motorRight = hardwareMap.dcMotor.get("motorRight");
        this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorRight.setDirection(DcMotor.Direction.FORWARD);

    }


    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        loopCounter = loopCounter + 1;


        driver_controlDriveMotors();

    }

    private void driver_controlDriveMotors() {

        // clip the rightDrive/leftDrive values so that the values never exceed +/- 1
        float rightDriveInput = gamepad1.right_stick_y;
        float leftDriveInput = gamepad1.left_stick_y;


        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        float rightDriveOutput = (float) scaleOutput(rightDriveInput, scaleDriveArray);
        float leftDriveOutput = (float) scaleOutput(leftDriveInput, scaleDriveArray);


        // write the values to the motors
        this.motorRight.setPower(rightDriveInput);
        this.motorLeft.setPower(leftDriveInput);


        Log.i(KEY + "_DRIVE", "Input leftDrive: " + String.format("%.2f", leftDriveInput) + " rightDrive: " + String.format("%.2f", rightDriveInput));
        //Log.i(KEY + "_DRIVE", "Output leftDrive: " + String.format("%.2f", leftDriveOutput) + " rightDrive: " + String.format("%.2f", rightDriveOutput));


    }
    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

        telemetry.addData("TextStop", "***Stop happened**" + loopCounter);

    }

    /*
     * This method scales the joystick inputValue so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleOutput(double inputValue, double[] scaleArray) {


        // get the corresponding index for the scaleOutput array.
        int index = (int) (inputValue * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (inputValue < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
