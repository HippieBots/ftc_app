import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * Created by pflores on 11/9/17.
 */

@Autonomous(name = "Blue Close")


public class BlueClose extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final Robot robot = new Robot(hardwareMap);
        waitForStart();

        robot.grabBlock();
        robot.lifterUp();
        sleep(300);
        robot.lifterStop();
        robot.drive(Math.PI / 2, .6, 0.0);
        sleep(3000);
        robot.drive(0, .3, 0.0);
        sleep(400);
        robot.dropBlock();
        robot.drive(Math.PI, .3, 0.0);
        sleep(100);
        robot.stopDriveMotors();
    }
}
