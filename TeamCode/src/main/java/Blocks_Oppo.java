import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "Blocks Oppo")

public class Blocks_Oppo extends AutonomousBase {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "AXax5///////AAAAGbN9kxkbgkIGtHCm6mYR5yldmTbfnZz3" +
            "bO9SI/KzKjU+rf9FoudDFqj6CaXe2ZRR/FrfJbufcb0PjwE5Fv/7XmV7t7nTpUUmNMj/AM85QGN8ammim" +
            "64AOTpRbemUwBkyVkZ9yROtZDykH/hECBvAciuXCBdLF2XFHOMzgtnbbKlp1+pvgTs5sPYAAvR40cR5Pz" +
            "tLdjKDZOTEhuhryqguwbhX6xr3H7ylNRaT+CEM38h1tik1SxFaKi4TkyefAKlx3xLG1zRWG95jEn8Ltmn" +
            "YqLoThJy/Wu8LeTFg5cedj0GiIQBSxkV9kvyCvMjyZZNc0yr1XJc7nez7dZRC6kDJg9W4RgtaGrE1DYTRU7/xW55Y";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    enum MineralPosition {
        CENTER, LEFT, RIGHT, UNKNOWN;
    }

    private MineralPosition mineralPosition = MineralPosition.UNKNOWN;

    public void detectMinerals() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() >= 2) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    int count = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        if (2 < ++count) {
                            break;
                        }
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX < 0 && goldMineralX > -2) {
                        mineralPosition = MineralPosition.LEFT;
                        telemetry.addData("Gold Mineral Position", "Left");
                    } else if (goldMineralX > silverMineral1X && silverMineral2X == -1 && silverMineral1X != -1) {
                        mineralPosition = MineralPosition.RIGHT;
                        telemetry.addData("Gold Mineral Position", "Right");
                    } else if (goldMineralX < silverMineral1X && silverMineral2X == -1) {
                        mineralPosition = MineralPosition.CENTER;
                        telemetry.addData("Gold Mineral Position", "Center");
                    } else {
                        mineralPosition = MineralPosition.UNKNOWN;
                        telemetry.addData("Gold Mineral Position", "IDK");
                    }
                }
                telemetry.update();
            }
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.fillCameraMonitorViewParent = false;
        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        tfod.activate();
    }

    @Override
    public void runOpMode() throws InterruptedException {

//        public void initialize(hardwareMap, telemetry) {
//            robot = new Robot(hardwareMap, telemetry);
        //       }
        initialize(hardwareMap, telemetry);
        initVuforia();
        initTfod();
        robot.markerUp();

        while (!isStarted()) {
            detectMinerals();
            telemetry.addData("Mineral", mineralPosition.toString());
            telemetry.addData("Time", getRuntime());
            telemetry.update();
        }



        waitForStart();


        runLiftMotor(91.5);
        //runLiftMotor(149);
        //runLiftMotor(150);
        driveDirectionTiles(Math.PI / 2, 0.2, .8);
        if(mineralPosition ==Blocks_Oppo.MineralPosition.LEFT) {
            driveDirectionTiles(0, 0.4, .5);
            turnRad(16*Math.PI / 100);
            tiltOut(6);
            robot.stopDown();
            robot.harvestIn();
            driveDirectionTiles(0,0.7,0.5);//more into block?
            driveDirectionTiles(Math.PI, 0.7,0.5);//less out?
            robot.tiltUpAuto();
            turnRad(183*Math.PI/100);
            driveDirectionTiles(Math.PI, 0.4,0.7);
            //we score here
            robot.harvestStop();
            robot.tiltDown();
            sleep(800);
            turnRad(17*Math.PI/100);
            driveDirectionTiles(0,1.2,0.5);
            robot.tiltUp();
            sleep(1500);
            driveDirectionTiles(0,1.0,0.5);
            turnRad(160*Math.PI / 100);
            sleep(100);
            robot.drive(3*Math.PI/2, 0.3,0);
            sleep(2400);
            driveDirectionTiles(Math.PI/2,0.25,0.5);
            driveDirectionTiles(0,1.1,0.7);
            robot.markerDown();
            sleep(1000);
            robot.markerUp();
            robot.drive(3*Math.PI/2, 0.5,0);
            sleep(1200);
            driveDirectionTiles(Math.PI / 2, 0.25,0.5);
            driveDirectionTiles(Math.PI, 2.35,0.8);
            driveDirectionTiles(Math.PI, 0.5,0.5);



        } else if (mineralPosition == Blocks_Oppo.MineralPosition.RIGHT) {
            driveDirectionTiles(0, 0.4, .5);
            turnRad(186*Math.PI / 100);
            tiltOut(6);
            robot.stopDown();
            sleep(200);
            robot.harvestIn();
            driveDirectionTiles(0,0.4,0.5);
            driveDirectionTiles(Math.PI, 0.55,0.5);
            robot.tiltUpAuto();
            turnRad(13*Math.PI/100);
            driveDirectionTiles(Math.PI, 0.35,0.7);
            sleep(500);
            //score
            robot.stopTilt();
            robot.harvestStop();
            robot.tiltDown();
            sleep(400);
            turnRad(26.5*Math.PI / 100);
            driveDirectionTiles(0,1,0.5);
            robot.tiltUp();
            sleep(1500);
            driveDirectionTiles(0,1.4,0.5);
            turnRad(146*Math.PI/100);
            sleep(200);
            robot.drive(3*Math.PI/2, 0.3,0);
            sleep(1500);
            driveDirectionTiles(Math.PI/2,0.2,0.5);
            driveDirectionTiles(0,1.7,0.6);
            turnRad(195*Math.PI / 100);
            robot.markerDown();
            sleep(1000);
            robot.markerUp();
            turnRad(5*Math.PI / 100);
            robot.drive(3*Math.PI/2, 0.3,0);
            sleep(1000);
            driveDirectionTiles(Math.PI/2,0.2,0.5);
            driveDirectionTiles(Math.PI, 2.6,0.9);
            driveDirectionTiles(Math.PI, 0.4,0.5);

            /*driveDirectionTiles(0, 1, 0.5);
            turnRad(48*Math.PI / 100);
            driveDirectionTiles(0,1.1,0.5);
            robot.markerDown();
            sleep(1000);
            robot.markerUp();
            driveDirectionTiles(Math.PI,1.05,0.5);
            turnRad(34*Math.PI/100);
            driveDirectionTiles(0,3,0.7);
            turnRad(10*Math.PI / 100);
            robot.drive (Math.PI / 2,.5,0);
            sleep(1000);
            robot.drive (3*Math.PI / 2,.6,0);
            sleep(550);
            turnRad(3*Math.PI/100);
            driveDirectionTiles(0,0.45,0.5);
            robot.stopDown();*/

        } else { //center or unknown
            tiltOut(6);
            robot.stopDown();
            robot.harvestIn();
            driveDirectionTiles(0, 2.8,0.5);
            robot.markerDown();
            sleep(1000);
            robot.markerUp();
            robot.tiltUpAuto();
            driveDirectionTiles(Math.PI, 2.9 ,1);
            //we score here
            robot.tiltDown();
            sleep(800);
            robot.harvestStop();
            driveDirectionTiles(0, 0.7, .5);
            robot.tiltUp();
            sleep(500);
            turnRad(41*Math.PI / 100 );
            driveDirectionTiles(0, 2.1, 0.8);
            turnRad(Math.PI / 5);
            robot.drive (Math.PI / 2,.5,0);
            sleep(1200);
            robot.drive (3*Math.PI / 2,.6,0);
            sleep(200);
            turnRad(3*Math.PI/100);
            driveDirectionTiles(0,0.5,0.2);
            robot.markerDown();

        }

        }

        //robot.drive (Math.PI / 2,.8,0);
        //sleep(200);

}

