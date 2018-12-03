import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blocks Oppo")


public class Blocks_Oppo extends AutonomousBase {


    @Override
    public void runOpMode() throws InterruptedException {

//        public void initialize(hardwareMap, telemetry) {
//            robot = new Robot(hardwareMap, telemetry);
        //       }
        initialize(hardwareMap, telemetry);



        while (!isStarted()) {
            telemetry.addData("Time", getRuntime());
            telemetry.update();
        }

        waitForStart();

        runLiftMotor(151);
        robot.drive (Math.PI / 2,.8,0);
        sleep(200);
        driveDirectionTiles(0,2.5, .5);
        robot.tiltUp();
        sleep(2200);
        driveDirectionTiles(Math.PI, 1.55,0.5);
        turnRad(45*Math.PI / 100 );
        driveDirectionTiles(0, 2.5, 0.8);
        turnRad(Math.PI / 20);
        robot.drive (Math.PI / 2,.8,0);
        sleep(1500);
        robot.drive (3*Math.PI / 2,.8,0);
        sleep(300);
        turnRad(Math.PI/28);
        driveDirectionTiles(0,0.7,0.6);
        robot.stopDown();
        robot.tiltDown();
        sleep(1000);
    }
}

