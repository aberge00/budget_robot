package com.sabotage.opmodes.autonomous.steps;

import com.sabotage.robot.Robot;

public interface StepInterface {


    boolean isStepDone();

    void runStep();

    boolean isAborted();

    void setRobot(Robot robot);

    String getLogKey();

}
