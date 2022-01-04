package org.firstinspires.ftc.teamcode;

import android.widget.Spinner;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
/*
This entire program acts like a psuedo-main class, Put all code during TeleOp in this class.
*/

@TeleOp(name="Mecanum_Control_V2", group="Testier")
public class MecanumControlV2 extends OpMode {

    MecanumDrive robot    = new MecanumDrive();//Initializes motors for drive
    //    Motors shooter       = new Motors();
//    Intake  intake        = new Intake();
    Servos spinny = new Servos(); // Output servo - Reece's box
    MotorControl Motor = new MotorControl(); // Initializes all motors, attachment intake

    double driveSpeed;
    double turnSpeed;
    double direction;
    double shooterPower = -.5;//Normally -1 but with start and back buttons as boosts it needs to be decreased

    boolean isSpinnerOn  = false;
    boolean isSpinnerOf = true;
    boolean isIntakeOn  = false;
    boolean isIntakeOf = true;
    boolean wasPowerIncreased;
    boolean wasPowerDecreased;
    double spinnerChange = .07; //5 increments of change in power of shooter, with given code at bottom(Lower=-1;Upper=-.65)

    boolean highGoalMode = true;
    boolean powerShotMode = false;
    boolean autoPower = true;
    boolean manualPower = false;

//    private ElapsedTime period  = new ElapsedTime();
//    private double runtime = 0;

    /*
    This is like a psuedo-main class
     */
    @Override
    public void init() {
        robot.init(hardwareMap);
        Motor.init(hardwareMap);
        spinny.init(hardwareMap);
//        grabber.init(hardwareMap);

        msStuckDetectInit = 18000;
        msStuckDetectLoop = 18000;

        telemetry.addData("Hello","be ready");
        telemetry.addData("Loop_Timeout",msStuckDetectLoop);
        telemetry.update();
        //What happens on startup, maps all the variables to respective hardware device
    }
    @Override
    public void loop() {
        telemetry.addData("isSpinnerOn", isSpinnerOn);
        telemetry.addData("highGoalMode", highGoalMode);
        telemetry.addData("Automatic Power", autoPower);
        telemetry.addData("Spinner Power", Motor.rotaterPower());
        telemetry.addData("Intake Power", Motor.intakeOnePower());
        //telemetry.addData("Drive Speed",driveSpeed);
        //telemetry.addData("Direction",direction);
        //telemetry.addData("Turn Speed", turnSpeed);
        //telemetry.addData("LB",robot.getLBencoder());
        //telemetry.addData("RB",robot.getRBencoder());
        //telemetry.addData("LF",robot.getLFencoder());
        //telemetry.addData("RF",robot.getRFencoder());
        telemetry.update();

        //Speed control (turbo/slow mode) and direction of stick calculation
        if (gamepad1.left_bumper) {
            driveSpeed = Math.hypot(-gamepad1.left_stick_x, -gamepad1.left_stick_y);
            direction = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
            turnSpeed = gamepad1.right_stick_x;
        }else if (gamepad1.right_bumper) {
            driveSpeed = Math.hypot(-gamepad1.left_stick_x, -gamepad1.left_stick_y)*.4;
            direction = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
            turnSpeed = gamepad1.right_stick_x*.4;
        }else {
            driveSpeed = Math.hypot(-gamepad1.left_stick_x, -gamepad1.left_stick_y)*.7;
            direction = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
            turnSpeed = gamepad1.right_stick_x*.7;
        }
        //set power and direction of drive motors
        if (turnSpeed == 0) {
            robot.MecanumController(driveSpeed,direction,0);
        }

        //control of turning
        if (gamepad1.right_stick_x != 0 && driveSpeed == 0) {
            robot.leftFront.setPower(-turnSpeed);
            robot.leftBack.setPower(-turnSpeed);
            robot.rightFront.setPower(turnSpeed);
            robot.rightBack.setPower(turnSpeed);
        }


        //DRIVE CODE ^^^^^^^^^



        //Turn Spinner on
        if (gamepad1.x && !isSpinnerOn) {
            isSpinnerOn = true;
        }else if (!gamepad1.x && isSpinnerOn) {
            isSpinnerOf = false;
        }
        //Turn Spinner off
        if (gamepad1.x && !isSpinnerOf) {
            isSpinnerOn = false;
        }else if (!gamepad1.x && !isSpinnerOn) {
            isSpinnerOf = true;
        }

        // TOGGLE FOR DUCK SPINNER ^^^^



        if (gamepad1.a && !isIntakeOn) {
            isIntakeOn = true;
        }else if (!gamepad1.a && isIntakeOn) {
            isIntakeOf = false;
        }
        //Turn Output off
        if (gamepad1.a && !isIntakeOf) {
            isIntakeOn = false;
        }else if (!gamepad1.a && !isIntakeOn) {
            isIntakeOf = true;
        }


        // TOGGLE FOR INTAKE ^^^^^^



//test 
        //Set power of the intake
        double intakePower = .75;
        if(isIntakeOn){
            if(gamepad1.b){
                Motor.intakeOnePower(intakePower);
            }
            else {
                Motor.intakeOnePower(-intakePower);
            }
        }
        else{
            Motor.intakeOnePower(0);
        }

        //CHANGE DIRECTION OF INTAKE - hold b reverses intake, toggling a turns on/off the intake

//        if (isSpinnerOn) {
//            if (isSpinnerOn) {
//                Motor.rotaterPower(shooterPower);
//            } else {
//                Motor.rotaterPower(0);
//            }
//
//        } else {
//            Motor.rotaterPower(0);
//        }

        //toggle for spinner, already exists above in different form^^^


        //Change spinner power
        if(gamepad1.guide){
            shooterPower*=-1;
        }
        if(gamepad1.start&&isSpinnerOn){
            Motor.rotaterPower(shooterPower*1.2);
        }
        else if(gamepad1.back&&isSpinnerOn){
            Motor.rotaterPower(shooterPower*8);
        }
        else if(isSpinnerOn){
            Motor.rotaterPower(shooterPower);
        }
        else{
            Motor.rotaterPower(0);
        }
//        if (gamepad2.dpad_up) {
//            wasPowerIncreased = true;
//        }else if (!gamepad2.dpad_up && wasPowerIncreased) {
//            shooterPower -= spinnerChange;
//            if (shooterPower <= -1.01) {
//                shooterPower = -.65;
//            }
//            wasPowerIncreased = false;
//        }
//        if (gamepad2.dpad_down) {
//            wasPowerDecreased = true;
//        }else if (!gamepad2.dpad_down && wasPowerDecreased) {
//            shooterPower += spinnerChange;
//            if (shooterPower >= -.62) {
//                shooterPower = -1;
//            }
//            wasPowerDecreased = false;
//        }


        //CHANGE THE SPINNER POWER, uncommented code boosts/ slows/ reverses spinner, commented code uses dpad of controller 2 to cycle through power, cant reverse power




        //Vertical Lift Movement
        if(gamepad1.dpad_up){
            Motor.VertLift.setPower(.4);
        }
        else if(gamepad1.dpad_down){
            Motor.VertLift.setPower(-.4);
        }
        else{
            Motor.VertLift.setPower(0);
        }
        //Horizontal Slide Movement
        if(gamepad1.dpad_up){
            Motor.HorzLift.setPower(.4);
        }
        else if(gamepad1.dpad_down){
            Motor.HorzLift.setPower(-.4);
        }
        else{
            Motor.HorzLift.setPower(0);
        }

        //VERTICAL AND HORIZONTAL SLIDE MOVEMENT USING DPAD OF CONTROLLER 1^^^^

        //Control intake
//        intake.intakePower(-gamepad2.left_stick_y);

        //Control shooter servo
//        if (gamepad2.left_bumper) {
//            shooter.shooterSwitch.setPosition(.63);
//        }else {
//            shooter.shooterSwitch.setPosition(1);
//        }

        //Control Output Servos, initial middle and final positions
        int servopos = 0;
        if(gamepad1.y&&servopos<2){
            servopos++;
        }

        switch (servopos){
            case 0:
                spinny.changePos(.1);
                break;
            case 1:
                spinny.changePos(.55);
            case 2:
                spinny.changePos(.85);
                if(gamepad1.y){
                    servopos=0;
                }
        }
//        if (gamepad2.x) {
//            spinny.changePos(0);
//        }
//        else if(gamepad2.y){
//            spinny.changePos(.55);
//        }
//        else if (gamepad2.b) {
//            spinny.changePos(.85);
//        }

        //Control grabber servo
//        if (gamepad1.b) {
//            spinny.gripperPosition(.77);

//        }else if (gamepad1.dpad_up) {
//            spinny.gripperPosition(0);
//        }

        //CHANGES SERVO POSITION, uncommented code cycles through positions using the y button, the commented code uses 3 buttons to change the position
    }
}