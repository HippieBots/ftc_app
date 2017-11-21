import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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

@Autonomous(name = "Blue Close")


public class BlueClose extends AutonomousBase {

    public double ADJ =0.0;

    protected void adjustDriveDistance(final RelicRecoveryVuMark v) {
        if (RelicRecoveryVuMark.LEFT == v) {
            ADJ = -0.25;

        } else if (RelicRecoveryVuMark.RIGHT == v) {
            ADJ = 0.25;
        }
        ADJ = 0.0;
    }
    @Override
    public void runOpMode() throws InterruptedException {

        final Robot robot = new Robot(hardwareMap, telemetry);
//        public void initialize(hardwareMap, telemetry) {
//            robot = new Robot(hardwareMap, telemetry);
//        }
        //initialize(hardwareMap, telemetry);

        final VisionTargets vt = new VisionTargets();
        vt.initFrontCamera(this);

        while (! isStarted()) {
            vt.loop();
            telemetry.addData("Time", getRuntime());
            telemetry.addData("Vision", vt.getCurrentVuMark());
            telemetry.update();
        }

        waitForStart();

        final RelicRecoveryVuMark target = vt.getCurrentVuMark();
        vt.close();
        //get Jewel
        robot.grabBlock();
        robot.lifterUp();
        sleep(300);
        robot.lifterStop();
        driveDirectionTiles(0,(1.5+ADJ),0.5);
        turnDeg(-90);
        driveDirectionTiles(0,.5,0.5);
        robot.dropBlock();
        driveDirectionTiles(180,.5,0.5);
        robot.grabBlock();




//          OLD CODE
//        robot.grabBlock();
//        robot.lifterUp();
//        sleep(300);
//        robot.lifterStop();
//        robot.drive(Math.PI / 2, .6, 0.0);
//        sleep(2200);
//        robot.drive(0, .3, 0.0);
//        sleep(800);
//        robot.dropBlock();
//        robot.drive(Math.PI, .3, 0.0);
//        sleep(300);
//        robot.lifterDown();
//        sleep(100);
//        robot.lifterStop();
//        robot.grabBlock();
//        robot.drive(0, .3, 0.0);
//        sleep(900);
//        robot.drive(Math.PI, .3, 0.0);
//        sleep(250);
//
//        robot.stopDriveMotors();
    }
}
