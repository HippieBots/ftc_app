/**
 * Created by pflores on 11/21/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

public abstract class AutonomousBase extends LinearOpMode  {
    protected Robot robot;

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
        if (angle > STUPID_TURN_THRESHOLD) {
            return LUCDACRIS_TURN_SPEED;
        }
        if (angle > FAST_TURN_THRESHOLD) {
            return FAST_TURN_SPEED;
        }
        return SAFE_TURN_SPEED;
    }
    private static final double MAX_HEADING_SLOP = Math.PI / 50.0;

    private void turnToAngleDeg(double degrees) throws InterruptedException {
        while(opModeIsActive()) {
            double diff = angleDifference(robot.getHeadingDeg(), degrees);
            if (MAX_HEADING_SLOP >= Math.abs(diff)) break;
            double speed = speedForTurnDistance(diff);
            robot.drive(0.0, 0.0, diff > 0 ? -speed : speed);
            idle();
        }
        robot.stopDriveMotors();
    }
    protected void turnDeg(double degrees) throws InterruptedException {
        robot.resetGyro();
        turnToAngleDeg(degrees);
    }
    protected void driveDirectionTiles(double directionDegrees, double tiles) throws InterruptedException {
        driveDirectionTiles(directionDegrees, tiles, .35);
    }
    protected void driveDirectionTiles(double directionDegrees, double tiles, double power) throws InterruptedException {
        robot.setPowerNew(power);
        robot.encoderDriveTiles(directionDegrees, tiles);
        while (opModeIsActive() && robot.driveMotorsBusy()) {
            //robot.updateSensorTelemetry();
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