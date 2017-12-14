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

@Autonomous(name = "Blue Far")


public class BlueFar extends AutonomousBase {

    public double ADJ =0.0;

    protected double adjustDriveDistance(final RelicRecoveryVuMark v) {
        if (RelicRecoveryVuMark.LEFT == v) {
           return ADJ = -0.25;

        } else if (RelicRecoveryVuMark.RIGHT == v) {
            return ADJ = 0.25;
        }
    else {

            return ADJ = 0.0;
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {

//        public void initialize(hardwareMap, telemetry) {
//            robot = new Robot(hardwareMap, telemetry);
//        }
        initialize(hardwareMap, telemetry);

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
        sleep(350);
        robot.lifterStop();
        driveDirectionTiles(0,  1.1, 0.5);
        driveDirectionTiles((Math.PI)/2, 1+ADJ, .5);
        robot.drive (0,.3,0);
        sleep(1000);
        robot.dropBlock();
        driveDirectionTiles(Math.PI, .5,.5);
        robot.lifterDown();
        sleep(100);
        robot.lifterStop();
        robot.grabBlock();
        robot.drive (0,.3,0);
        sleep(1200);
        driveDirectionTiles(Math.PI, .25,.5);


    }
}