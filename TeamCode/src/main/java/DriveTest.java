import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
/**
 * Created by pflores on 11/25/17.
 */

@Autonomous(name = "OneTile")
public class DriveTest extends AutonomousBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initialize(hardwareMap, telemetry);
        waitForStart();
        driveDirectionTiles(Math.PI/2, 1.0, 0.5);
    }
}
