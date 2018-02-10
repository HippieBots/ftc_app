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
    }

    @Override
    public void stop() {
        robot.onStop();
    }

    private void g1Loop(Controller g) {
        DriverHelper.drive(g, robot);
        if (g.left_trigger > .2) {
            robot.grabBlock();
        }

        if (g.right_trigger > .2) {
            robot.dropBlock();

        }

        if (g.A()) {
            robot.lifterUp();
        } else if (g.B()) {
            robot.lifterDown();
        } else {
            robot.lifterStop();
        }

        if (g.X()) {
            robot.PutArmDown();
        } else if (g.Y()) {
            robot.PutArmUp();
        }
    }

    private void g2Loop(Controller g) {
        if (g.A()) {
            robot.grabRelic();
        } else if (g.B()) {
            robot.dropRelic();

        }

        if (g.Y()) {
            robot.jointUp();
        } else if (g.X()) {
            robot.jointDown();

        }

        if (g.left_trigger > .2) {
            robot.tiltUp();
        } else if (g.right_trigger > .2) {
            robot.tiltDown();
        } else {
            robot.stopTilt();
        }

        robot.extendRelicArm(g.left_stick_y);
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
