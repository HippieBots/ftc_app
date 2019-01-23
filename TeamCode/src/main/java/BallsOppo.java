import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


//@Autonomous(name = "Balls Oppo")


public class BallsOppo extends AutonomousBase {


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

        runLiftMotor(150);
        driveDirectionTiles(Math.PI / 2, 0.2, .8);
        driveDirectionTiles(0, 1.4, .5);
        driveDirectionTiles(Math.PI,0.5, 0.5);
        turnRad(44*Math.PI/100);
        driveDirectionTiles(0, 2.6, .9);
        turnRad(25*Math.PI/100);
        driveDirectionTiles(Math.PI / 2, 0.3,.8);
        driveDirectionTiles(0, 1.6,.7);
        //robot.tiltUp();
        sleep(2200);
        driveDirectionTiles(Math.PI, 2,.9);
        driveDirectionTiles(3*Math.PI/2, 0.2,0.8);
        turnRad(28*Math.PI/100);
        driveDirectionTiles(0, 4.3, 0.8);
        turnRad(32*Math.PI/100);

    }

}

