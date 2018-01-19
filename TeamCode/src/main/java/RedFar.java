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

    public double ADJ =0.0;

    protected double adjustDriveDistance(final RelicRecoveryVuMark v) {
        if (RelicRecoveryVuMark.LEFT == v) {
            return ADJ = 0.45;

        } else if (RelicRecoveryVuMark.RIGHT == v) {
            return ADJ = -0.6;
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
        robot.PutArmDown();
        sleep(1000);

        if (robot.isRed()){
            driveDirectionTiles(Math.PI,.2,0.1);
            robot.PutArmUp();
            sleep(1000);
            driveDirectionTiles(0,.2,0.1);


        }else if(!robot.isRed()){
            driveDirectionTiles(0,.2,0.1);
            robot.PutArmUp();
            sleep(1000);
            driveDirectionTiles(Math.PI,.2,0.1);


        }
        robot.PutArmUp();
        sleep(1000);
        driveDirectionTiles(Math.PI,  1.3, 0.5);
        driveDirectionTiles((Math.PI)/2, .85+adjustDriveDistance(target), .5);
        turnRad(Math.PI/2);
        turnRad(Math.PI/2);
        robot.drive (0,.3,0);
        sleep(1200);
        robot.dropBlock();
        driveDirectionTiles(Math.PI, .6,.5);
        robot.lifterDown();
        sleep(100);
        robot.lifterStop();
        robot.grabBlock();
        robot.drive (0,.3,0);
        sleep(1400);
        driveDirectionTiles(Math.PI, .4,.5);


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