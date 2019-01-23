import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "PracticeTeleop")
public class SimpleTeleop extends OpMode {
    RobotWorkshop robot;

    @Override
    public void init() {
        robot = new RobotWorkshop(hardwareMap);
    }

    @Override
    public void loop() {
        double left = gamepad1.left_stick_y;
        double right = gamepad1.right_stick_y;

        robot.drive(left, right);


    }
}
