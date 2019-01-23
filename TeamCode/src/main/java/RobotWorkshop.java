import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OrientationSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class RobotWorkshop {
    DcMotor lf, rf, lb, rb;

    RobotWorkshop(final HardwareMap hardwareMap) {
        lf = hardwareMap. dcMotor.get ("lf");
        lb = hardwareMap. dcMotor.get ("lb");
        rf = hardwareMap. dcMotor.get ("rf");
        rb = hardwareMap. dcMotor.get ("rb");
        initializeGyro(hardwareMap);

        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(double left, double right) {
        lf.setPower(left);
        lb.setPower(left);
        rf.setPower(right);
        rb.setPower(right);
    }
    public void setMotorRunmode(DcMotor.RunMode mode) {
        lf.setMode(mode);
        lb.setMode(mode);
        rf.setMode(mode);
        rb.setMode(mode);
    }

    public void setEncoderTargets(final int leftTarget, final int rightTarget) {
       lf.setTargetPosition(leftTarget);
       lb.setTargetPosition(leftTarget);
       rf.setTargetPosition(rightTarget);
       rb.setTargetPosition(rightTarget);
    }

    public boolean motorsAreBusy() {
        return lf.isBusy() || lb.isBusy() || rf.isBusy() || rb.isBusy();
    }
    private BNO055IMU imu;
    private double lastZ, lastX, lastY;
    private void initializeGyro(HardwareMap hardware) {
        imu = hardware.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);

    } private Orientation getOrientation() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
    }
    public void resetGyro() {
        final Orientation orientation = getOrientation();
        lastX = orientation.firstAngle;
        lastY = orientation.secondAngle;
        lastZ = orientation.thirdAngle;
    }

    public double getRotationZ() {
        return getOrientation().thirdAngle - lastZ;
    }

}
