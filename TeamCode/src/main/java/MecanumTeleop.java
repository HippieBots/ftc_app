/**
 * Created by pflores on 9/25/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;

import java.util.List;

@TeleOp(name = "TELEOP")
public class MecanumTeleop extends OpMode {

    private Robot robot = null;
    private boolean debug_mode = false;
    private Controller g1, g2;

    @Override
    public void init() {
        robot = new Robot(hardwareMap);

        g1 = new Controller(gamepad1);
        g2 =  new Controller(gamepad2);
    }

    public void init_loop() {
        g1.update();
        if (g1.AOnce()) {
            debug_mode = ! debug_mode;
        }
        telemetry.addData("Debug? (a)", debug_mode ? "on" : "off");
        telemetry.addData("Ready?", "YES.");
        telemetry.update();
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

    }

    private void g2Loop(Controller g) {
//        if (g.X()) {
//            robot.setBackPower(-1.0);
//        } else if (g.Y()) {
//            robot.setBackPower(1.0);
//        } else {
//            robot.setBackPower(0.0);
//        }
//
//        if (g.A()) {
//            robot.setFrontPower(-1.0);
//        } else if (g.B()) {
//            robot.setFrontPower(1.0);
//        } else {
//            robot.setFrontPower(0.0);
//        }

    }

    @Override
    public void loop() {
        g1.update();
        g2.update();
        //robot.loop();
        g1Loop(g1);
        g2Loop(g2);
        if (debug_mode) {
            robot.updateSensorTelemetry();
            telemetry.update();
        }
    }
}
