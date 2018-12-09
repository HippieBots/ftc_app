import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Balls Full")


public class BallsFull extends AutonomousBase {


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

        runLiftMotor(152);
        driveDirectionTiles(Math.PI / 2, 0.2, .8);
        //robot.drive (Math.PI / 2,.6,0);
        //sleep(200);
        driveDirectionTiles(0, 0.7, .5);
        turnRad(44*Math.PI/100);
        driveDirectionTiles(0, 2.3, .7);
        turnRad(20*Math.PI/100);
        //driveDirectionTiles(Math.PI / 2, 0.3,.8);
        robot.drive (Math.PI / 2,.5,0);
        sleep(1000);
        robot.drive (3*Math.PI / 2,.7,0);
        sleep (350);
        driveDirectionTiles(0, 1.5,.7);
        robot.tiltUp();
        sleep(2200);
        driveDirectionTiles(Math.PI, 3.0,.9);
        //driveDirectionTiles(3*Math.PI/2, 0.5, 0.9);
        robot.drive (3*Math.PI / 2,.9,0);
        sleep(400);
        turnRad(110*Math.PI/100);
        driveDirectionTiles(0, 0.2,0.7);
        robot.stopDown();
        robot.tiltDown();
        sleep(1000);


    }

}
