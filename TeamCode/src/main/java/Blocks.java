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
            runLiftMotor(150);
            //runLiftMotor(149);
            //driveDirectionTiles(Math.PI/2,0.2, .4);
            //straffe in seconds
            robot.drive (Math.PI / 2,.8,0);
            sleep(200);
            driveDirectionTiles(0,2.6, .5);
            robot.tiltUp();
            sleep(2200);
            driveDirectionTiles(Math.PI, 1.85,0.5);
            turnRad(3*Math.PI / 2 );
            driveDirectionTiles(0, 2.45, 0.5);
            turnRad(19*Math.PI/10);
            driveDirectionTiles(0,0.4,0.6);
            turnRad(38*Math.PI/20);
            driveDirectionTiles(0,0.4, 0.6);
            robot.stopDown();
            robot.tiltDown();
            sleep(1000);

        }
    }


