/**
 * Created by pflores on 9/25/17.
 */

/*
INCOMPLETE!!!!!
 */

public class DriverHelper {
    // assumes that the controller is updated
    static void drive(Controller g, Robot robot) {
        double theta = 0.0, v_theta = 0.0, v_rotation = 0.0;
        final double dpad_speed = 0.5;

        if (g.dpadUp()) {
            theta = 0.0;
            v_theta = dpad_speed;
        } else if (g.dpadDown()) {
            theta = Math.PI;
            v_theta = dpad_speed;
        } else if (g.dpadLeft()) {
            theta = 3.0 * Math.PI / 2.0;
            v_theta = dpad_speed;
        } else if (g.dpadRight()) {
            theta = Math.PI / 2.0;
            v_theta = dpad_speed;
        } else {
            final double lx = g.left_stick_x;
            final double ly = - g.left_stick_y;

            theta = Math.atan2(lx, ly);
            v_theta = Math.sqrt(lx * lx + ly * ly);
            v_rotation = g.right_stick_x;

        }
        robot.drive(theta, v_theta, v_rotation);
    }
}