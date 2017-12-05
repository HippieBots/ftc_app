import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * Created by pflores on 11/9/17.
 */

@Autonomous(name = "Red Far")


public class RedFar extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final Robot robot = new Robot(hardwareMap, telemetry);
        waitForStart();

        robot.grabBlock();
        robot.lifterUp();
        sleep(300);
        robot.lifterStop();
        robot.drive(0, .5, 0.0);
        sleep(800);
        robot.drive(3 * Math.PI / 2, .6, 0.0);
        sleep(1150);
        robot.drive(0, .3, 0.0);
        sleep(800);
        robot.dropBlock();
        robot.drive(Math.PI, .3, 0.0);
        sleep(400);
        robot.lifterDown();
        sleep(150);
        robot.lifterStop();
        robot.grabBlock();
        robot.drive(0, .3, 0.0);
        sleep(900);
        robot.drive(Math.PI, .3, 0.0);
        sleep(350);




        robot.stopDriveMotors();
    }
}