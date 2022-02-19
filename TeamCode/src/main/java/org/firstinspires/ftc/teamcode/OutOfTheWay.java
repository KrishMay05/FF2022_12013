package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name="GET OUT", group="AR/AB")
public class OutOfTheWay extends OpMode{
    MecanumDrive robot = new MecanumDrive();
    //    Intake intake = new Intake();
    MotorControl motor = new MotorControl();
    Servos servo = new Servos();

    private int stateMachineFlow;
    private double waitTime;
    private ElapsedTime runtime = new ElapsedTime();
    public void init(){
        msStuckDetectInit = 11500;
        msStuckDetectLoop = 10000;

        //Maps all the variables to the correct corresponding  hardware part on the robot
        robot.init(hardwareMap);
//        intake.init(hardwareMap);
        motor.init(hardwareMap);
        servo.init(hardwareMap);

        stateMachineFlow = 0;
    }
    public void loop(){
        switch(stateMachineFlow){
            case 0:
//                robot.sideDrive(.8,4);
//                waitTime = .5;
//                runtime.reset();
//                while (waitTime > runtime.time());
                robot.frontLinearDrive(1,-80);
                waitTime = 10;
                runtime.reset();
                while (waitTime > runtime.time());
                stateMachineFlow++;
                break;
            default:
//                robot.frontLinearDrive(.7,25);
                stop();
        }
    }
}
