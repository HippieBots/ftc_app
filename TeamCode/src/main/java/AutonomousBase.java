/**
 * Created by pflores on 11/21/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

//This is the actual AutoBase!!!!!!
public abstract class AutonomousBase extends LinearOpMode  {
    protected Robot robot;
    //this is for 40 motor//private static final double TICKS_PER_REV = 1120;
    //this is for the 5.2:1 motor//private static final double TICKS_PER_REV = 145.6;
    private static final double TICKS_PER_REV = 537.6;
    private static final double MM_PER_REV = 8.0;
    private static final double TICKS_PER_REV_TILT = 1680;//change to actual?(maybe)
    private static final double MM_PER_REV_TILT = 8.0;//change to actual

    void  runLiftMotor (final double mm) {
        final DcMotor la = robot.getLiftMotor();
        final int ticksToLand = (int) Math.floor(TICKS_PER_REV * (mm / MM_PER_REV));
        la.setMode(DcMotor.RunMode. STOP_AND_RESET_ENCODER);
        la.setTargetPosition(ticksToLand);
        la.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        la.setPower(1.0);
        while (opModeIsActive()&& la.isBusy()) {
            sleep(0);
        }
        la.setPower(0.0);
        la.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }



    void  tiltOut (final double mm) {
        final DcMotor tiltLeft = robot.getTiltMotorL();
        final DcMotor tiltRight = robot.getTiltMotorR();
        final int ticksToOut = (int) Math.floor(TICKS_PER_REV_TILT * (mm / MM_PER_REV_TILT));
        tiltLeft.setMode(DcMotor.RunMode. STOP_AND_RESET_ENCODER);
        tiltRight.setMode(DcMotor.RunMode. STOP_AND_RESET_ENCODER);
        tiltLeft.setTargetPosition(ticksToOut);
        tiltRight.setTargetPosition(-ticksToOut);
        tiltLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tiltRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tiltLeft.setPower(0.68);
        tiltRight.setPower(0.68);
        while (opModeIsActive() && tiltLeft.isBusy() && tiltRight.isBusy()) {
            sleep(0);
        }
        tiltLeft.setPower(0.0);
        tiltRight.setPower(0.0);
        tiltLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tiltRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



    }


    public void initialize(HardwareMap hm, Telemetry telemetry) {
    robot = new Robot(hm, telemetry);
    }

    public void onStart() {
        robot.onStart();
    }

    public void onStop() {
        robot.onStop();
    }

    private static final double SAFE_TURN_SPEED = .3;
    private static final double FAST_TURN_SPEED = .5;
    private static final double LUCDACRIS_TURN_SPEED = .7;
    private static final int FAST_TURN_THRESHOLD = 30;
    private static final int STUPID_TURN_THRESHOLD = 60;
    private static final double LUDACRIS_TURN_THRESHOLD = Math.PI / 3.0;

    private double angleDifference(double from, double to) {
        if (from < 0) from += 2*Math.PI;
        if (to < 0) to += 2*Math.PI;

        double diff = to - from;

        if (diff < -Math.PI) {
            diff += 2*Math.PI;
        } else if (diff > Math.PI) {
            diff = - (2*Math.PI - diff);
        }

        return diff;
    }

    private static double speedForTurnDistance(double angle) {
        angle = Math.abs(angle);
        if (angle > LUDACRIS_TURN_THRESHOLD) {
            return LUCDACRIS_TURN_SPEED;
        }
        if (angle > FAST_TURN_THRESHOLD) {
            return FAST_TURN_SPEED;
        }
        return SAFE_TURN_SPEED;
    }

    private static final double MAX_HEADING_SLOP = Math.PI / 60.0;

    private void turnToAngleRad(double radians) throws InterruptedException {
        while(opModeIsActive()) {
            robot.loop();
            double diff = angleDifference(robot.getHeadingRadians(), radians);
            if (MAX_HEADING_SLOP >= Math.abs(diff)) break;
            double speed = speedForTurnDistance(diff);
            robot.drive(0.0, 0.0, diff > 0 ? -speed : speed);
            idle();
        }
        robot.stopDriveMotors();
    }

    protected void turnRad(double radians) throws InterruptedException {
        robot.resetGyro();
        turnToAngleRad(radians);
    }

    protected void driveDirectionTiles(double directionRadians, double tiles, double power) throws InterruptedException {
        robot.setEncoderDrivePower(power);
        robot.encoderDriveTiles(directionRadians, tiles);
        while (opModeIsActive() && robot.driveMotorsBusy()) {
            robot.loop();
            telemetry.update();
            idle();
        }
        robot.stopDriveMotors();
        robot.resetDriveMotorModes();
        robot.clearEncoderDrivePower();
    }

    protected void jumpToTeleop() {
        jumpToTeleop("TELEOP");
    }

    protected void jumpToTeleop(final String teleop) {
        final OpModeManagerImpl ommi = ((OpModeManagerImpl) internalOpModeServices);
        if (null != ommi) {
            ommi.initActiveOpMode(teleop);
        }
    }

}
