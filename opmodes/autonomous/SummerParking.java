/* FTC */

package com.sabotage.opmodes.autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.sabotage.opmodes.autonomous.BaseRescueOp;
import com.sabotage.opmodes.autonomous.steps.StepInterface;
import com.sabotage.opmodes.autonomous.steps.Step_Straight;

import java.util.ArrayList;


public class SummerParking extends BaseRescueOp {


    protected void init_defineRescueAsStepList() {


        // Here are our steps (in order) that make up our rescue plan.
        stepList = new ArrayList<StepInterface>();

//        stepList.add(new Step_TurnLeft(90));
        stepList.add(new Step_Straight(6000, DcMotor.Direction.FORWARD));
        stepList.add(new Step_Straight(3000, DcMotor.Direction.REVERSE));


//        stepList.add(new Step_TurnRight(0));
//        stepList.add(new Step_Straight(600, DcMotor.Direction.FORWARD));
//        stepList.add(new Step_TurnRight(180));
//        stepList.add(new Step_FindColorLine(Robot.ColorEnum.BLUE));
//        stepList.add(new Step_Straight(75, DcMotor.Direction.FORWARD));
//        stepList.add(new Step_UnloadClimbers());

//        stepList.add(new Step_ReCalibrateGyroIfNeeded());
//        stepList.add(new Step_Straight(1400, DcMotor.Direction.REVERSE));
//        stepList.add(new Step_ControlDebris(Robot.ControlDebrisEnum.EJECT));
//        stepList.add(new Step_TurnRight(40));
//        stepList.add(new Step_Straight(4200, DcMotor.Direction.REVERSE));
//        stepList.add(new Step_FindColorLine(Robot.ColorEnum.BLUE));
//
//
//        stepList.add(new Step_TurnRight(80));
//        stepList.add(new Step_Straight(500, DcMotor.Direction.REVERSE));
//        stepList.add(new Step_TurnRight(10));
//        stepList.add(new Step_FindColorLine(Robot.ColorEnum.WHITE));
//        stepList.add(new Step_Straight(400, DcMotor.Direction.REVERSE));
//        stepList.add(new Step_TurnRight(90));
//        stepList.add(new Step_Straight(200, DcMotor.Direction.FORWARD));
//        stepList.add(new Step_ControlDebris(Robot.ControlDebrisEnum.PICK_UP));
//        stepList.add(new Step_TrackWhiteLine(Robot.ColorEnum.BLUE));
//        stepList.add(new Step_UnloadClimbers());

        // NEW, park robot away from beacon.
//        stepList.add(new Step_Straight(400, DcMotor.Direction.FORWARD));
//        stepList.add(new Step_TurnRight(160));
//        stepList.add(new Step_Straight(1800, DcMotor.Direction.REVERSE));

       // try to obstruct our opponent
//        stepList.add(new Step_Straight(400, DcMotor.Direction.FORWARD));
//        stepList.add(new Step_TurnLeft(45));
//        stepList.add(new Step_Straight(5000, DcMotor.Direction.REVERSE));













    }


}
