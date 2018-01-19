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

    public double ADJ;

    protected double adjustDriveDistance(final RelicRecoveryVuMark v) {
        if (RelicRecoveryVuMark.LEFT == v) {
            return ADJ = -0.45;

        } else if (RelicRecoveryVuMark.RIGHT == v) {
            return ADJ = 0.45;

        } else if (RelicRecoveryVuMark.CENTER == v) {
            return ADJ = 0.0;
        }else{
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

        robot.grabBlock();
        robot.lifterUp();
        sleep(250);
        robot.lifterStop();

        robot.PutArmDown();
        sleep(1000);

        if (robot.isRed()){
            driveDirectionTiles(0,.2,0.1);
            robot.PutArmUp();
            sleep(1000);
            driveDirectionTiles(Math.PI,.2,0.1);

        }else if(!robot.isRed()){
            driveDirectionTiles(Math.PI,.2,0.1);
            robot.PutArmUp();
            sleep(1000);
            driveDirectionTiles(0,.2,0.1);

        }

        driveDirectionTiles(0,(2.15 + adjustDriveDistance(target)),0.6);
        turnRad((Math.PI / 2.0));
        robot.drive (0,.3,0);
        sleep(1000);
        robot.dropBlock();
        robot.drive(Math.PI, .3, 0.0);
        sleep(800);
        robot.lifterDown();
        sleep(150);
        robot.lifterStop();
        robot.grabBlock();
        robot.drive(0, .3, 0.0);
        sleep(1000);
        robot.drive(Math.PI, .3, 0.0);
        sleep(700);
//get more blocks
        /*turnRad((Math.PI / -2.0));
        sleep (100);
        turnRad((Math.PI / -2.0));
        robot.dropBlock();
        driveDirectionTiles(0,2.2, 0.85);
        robot.grabBlock();
        robot.lifterUp();
        sleep(1200);
        robot.lifterStop();
        driveDirectionTiles(Math.PI,1.1, 0.85);
        turnRad((Math.PI / 2.0));
        sleep(100);
        turnRad((Math.PI / 2.0));
        driveDirectionTiles(0,1.1, 0.5);
        robot.drive(0, .3, 0.0);
        sleep(1000);
        robot.lifterDown();
        sleep (400);
        robot.lifterStop();
        sleep (100);
        robot.dropBlock();
        driveDirectionTiles(Math.PI,.5, 0.2);*/







        //driveDirectionTiles(0,.5,0.5);
        //robot.dropBlock();
        //driveDirectionTiles(180,.5,0.5);
        //robot.grabBlock();
        //driveDirectionTiles(0,.5,0.5);
        //driveDirectionTiles(180,.1,0.5);





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
