import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by pflores on 11/6/17.
 */

@Autonomous(name = "Parking")
public class ParkingAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final Robot robot = new Robot(hardwareMap);
        waitForStart();

        robot.drive(0, .6, 0.0);
        sleep(1000);
        robot.stopDriveMotors();
    }
}
