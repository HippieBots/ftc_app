import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

    @Autonomous(name = "Blocks")


    public class Blocks extends AutonomousBase {


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
            driveDirectionTiles(0,2.25, .5);
            robot.tiltUp();
            sleep(2200);
            driveDirectionTiles(Math.PI, 1.40,0.5);
            turnRad(3*Math.PI / 2 );
            driveDirectionTiles(0, 2.1, 0.5);
            turnRad(75*Math.PI/40);
            driveDirectionTiles(0,0.9,0.6);
            robot.stopDown();
            robot.tiltDown();
            sleep(1000);
        }
    }


