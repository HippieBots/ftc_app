/**
 * Created by pflores on 9/25/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "TELEOP")
public class MecanumTeleop extends OpMode {
    private Robot robot = null;
    private Controller g1, g2;
    private boolean debug_mode =false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);

        g1 = new Controller(gamepad1);
        g2 =  new Controller(gamepad2);
    }

    public void init_loop() {
        g1.update();
        g2.update();
//        if (g1.AOnce()) {
//            debug_mode = ! debug_mode;
//        }
//        telemetry.addData("Debug? (a)", debug_mode ? "on" : "off");
//        telemetry.addData("Ready?", "YES.");
//        telemetry.update();
    }

    @Override
    public void start() {
        robot.onStart();
        robot.markerUp();
    }

    @Override
    public void stop() {
        robot.onStop();
    }
    private void g1Loop(Controller g) {
        DriverHelper.drive(g, robot);

        if (g.X()) {
            robot.stopDown();
        } else if (g.Y()) {
            robot.stopUp();
        }

        if (g.A()) {
            robot.markerUp();
        } else if (g.B()) {
            robot.markerDown();
        }

        if (g.left_bumper > .2) {
            robot.upSlow();
        } else if (g.right_bumper > .2) {
            robot.downSlow();
        } else {
            robot.laStop();
        }

        if (g.left_trigger > .2) {
            robot.laUp();
        } else if (g.right_trigger > .2) {
            robot.laDown();
        } else {
            robot.laStop();
        }


        /*if (g.left_trigger > .2) {
            robot.grabBlock();
        }

        if (g.right_trigger > .2) {
            robot.dropBlock();*/



        /*if (g.A()) {
            robot.lifterUp();
        } else if (g.B()) {
            robot.lifterDown();
        } else {
            robot.lifterStop();
        }*/


    }


    private void g2Loop(Controller g) {
        //robot.extendIn(g.left_stick_y);

        if (g.left_trigger > .2) {
            robot.tiltUp();
        } else if (g.right_trigger > .2) {
            robot.tiltDown();
        } else {
            robot.stopTilt();
        }
        /*if (g.A()) {
            robot.harvestIn();
        } else if (g.Y()) {
            robot.harvestOut();
        } else if (g.X()) {
            robot.harvestStop();

        }*/

        if (g.left_bumper > .2) {
            robot.harvestIn();
            //robot.extendOut();
        } else if (g.right_bumper > .2) {
            robot.harvestOut();
            //robot.retract();
        } else {
            robot.harvestStop();
            //robot.extendStop();\
        }


        /*if (g.Y()) {
            robot.jointUp();
        } else if (g.X()) {
            robot.jointDown();

        }*/



    }


    @Override
    public void loop() {
        g1.update();
        g2.update();
        g1Loop(g1);
        g2Loop(g2);


//        if (debug_mode) {
//            robot.updateSensorTelemetry();
//            telemetry.update();
//        }
    }
}
