import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Balls")


public class Balls extends AutonomousBase {


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

        runLiftMotor(-152);
        driveDirectionTiles(Math.PI/2,0.2, .25);
        driveDirectionTiles(0,1, .5);




    }
}