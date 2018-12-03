import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

//don't use this was for a workshop!!!!
public abstract class AutoBase extends LinearOpMode {
    protected RobotWorkshop robot = null;

    protected void initialize(final HardwareMap hardwareMap) {
        robot = new RobotWorkshop(hardwareMap);
    }

    protected void driveTime(final double seconds,
                             final double leftPower,
                             final double rightPower) {
        final double timeAtStart = getRuntime();
        robot.drive(leftPower, rightPower);
        while(opModeIsActive() && seconds > (getRuntime() - timeAtStart)) {
            // NOTHING!!\

        }
        robot.drive(0,0);
    }
    protected void driveDistance(final double inches, double speed) {
        final double WHEEL_RADIUS_IN = 4.0 * Math.PI;
        final double TICKS_PER_REVOLUTION = 1120.0;
        final double GEAR_RATIO = 1.0 / 1.0;
        final double TICKS_PER_INCH = (TICKS_PER_REVOLUTION / GEAR_RATIO)
                /WHEEL_RADIUS_IN;
        robot.setMotorRunmode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setMotorRunmode(DcMotor.RunMode.RUN_TO_POSITION);
        final int ticks = (int)(inches * TICKS_PER_INCH);
        robot.setEncoderTargets(ticks, ticks);
        robot.drive(speed,speed);
        while(opModeIsActive() && robot.motorsAreBusy()) {
            sleep(1);
        }
        robot.drive( 0, 0);

    }
    protected void driveWheel(final double angle, final double speed) {
        robot.resetGyro();
        if (angle > 0.0) {
            robot.drive(speed, -speed);
        }else {
            robot.drive(-speed, speed);
        }
        while (opModeIsActive() && Math.abs(angle) > Math.abs(robot.getRotationZ())){
        }robot.drive(0,0);
    }
}