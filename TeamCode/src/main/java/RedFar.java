import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;



/**
 * Created by pflores on 11/9/17.
 */

@Autonomous(name = "Red Far")


public class RedFar extends AutonomousBase {


    @Override
    public void runOpMode() throws InterruptedException {

//        public void initialize(hardwareMap, telemetry) {
//            robot = new Robot(hardwareMap, telemetry);
//        }
        initialize(hardwareMap, telemetry);


        while (! isStarted()) {
            telemetry.addData("Time", getRuntime());
            telemetry.update();
        }

        waitForStart();
        {





        }
        driveDirectionTiles(Math.PI,  1.3, 0.5);


    }


    //robot.grabBlock();
    //robot.lifterUp();
    //sleep(300);
    //robot.lifterStop();
    //robot.drive(0, .5, 0.0);
    //sleep(800);
    //robot.drive(3 * Math.PI / 2, .6, 0.0);
    //sleep(1150);
    //robot.drive(0, .3, 0.0);
    //sleep(800);
    //robot.dropBlock();
    //robot.drive(Math.PI, .3, 0.0);
    //sleep(400);
    //robot.lifterDown();
    //sleep(150);
    //robot.lifterStop();
    //robot.grabBlock();
    //robot.drive(0, .3, 0.0);
    //sleep(900);
    //robot.drive(Math.PI, .3, 0.0);
    //sleep(350);




    //robot.stopDriveMotors();

}
