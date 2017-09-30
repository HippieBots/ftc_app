import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Locale;

/**
 * Created by pflores on 9/19/17.
 */

public class Robot {
    private DcMotor lf, rf, lb, rb;
    private double lastG;
    private Telemetry telemetry;
    private BNO055IMU imu;
    private ColorSensor redVsBlue;
    private Servo colorReader;
    public Robot(HardwareMap h) {
        imu = h.get(BNO055IMU.class, "gyro");
        initilizeGyro();
        redVsBlue = h.colorSensor.get("redVsBlue");

        colorReader = h.servo.get("color sensor");

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    private void initilizeGyro() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
    }

    public void resetGyro() {
        lastG = getGyroRaw();
    }
    public double getGyroRaw() {
        Orientation angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        return angles.firstAngle;
    }
    public double getGyro(){
        return (getGyroRaw()-lastG)* (2.0 * Math.PI);
    }

    private void readColor() {
        if(getLight() >= 200) { //sensor reads white
            red = true;
        }
        if(getLight() >= 200) {
            blue = true;
        }
    }


    public boolean red = false;
    public boolean blue = false;

    public int getLight() {
        return redVsBlue.alpha();
    }

    public void setMotorSpeeds(double lfs, double lbs, double rfs, double rbs){
        lf.setPower(lfs);
        lb.setPower(lbs);
        rf.setPower(rfs);
        rb.setPower(rbs);
    }
    //wait...why do we need this?
    //OOHHHHHHH!!! Found out
    public void setMotorMode(DcMotor.RunMode mode, DcMotor... ms) {
        for (DcMotor m :ms) {
            m.setMode(mode);
        }
    }

    private static final double ENCODER_DRIVE_POWER = .3; // .35;
    public void setEncoderTargets(int lfs, int lbs, int rfs, int rbs) {
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rb, rf);
        lf.setTargetPosition(lfs);
        lb.setTargetPosition(lbs);
        rf.setTargetPosition(rfs);
        rb.setTargetPosition(rbs);
        setMotorMode(DcMotor.RunMode.RUN_TO_POSITION, lf, lb, rb, rf);
        setPower(ENCODER_DRIVE_POWER, lf, lb, rf, rb);
        while(busy(lf, lb, rf, rb)){
            //wait(); // wait, it this right? I keep getting errors
        }
        setPower(0.0, lf, lb, rf, rb);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, lf, lb, rb, rf);
    }
    public void resetDriveMotorModes() {
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rf, rb);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, lf, lb, rf, rb);
    }
    //    private void setTargetPosition(int pos, DcMotor... ms) { //we probably don't need this
//        for (DcMotor m : ms) {
//            m.setTargetPosition(pos);
//        }
//    }
    private void setPower(double p, DcMotor... ms) {
        for (DcMotor m : ms) {
            m.setPower(p);
        }
    }

    public static final int ENCODERS_CLOSE_ENOUGH = 10;
    private boolean busy(DcMotor... ms) {
        int total = 0;
        for (DcMotor m : ms) {
            if (m.isBusy()) {
                total += Math.abs(m.getCurrentPosition() - m.getTargetPosition());
            }
        }
        return total > ENCODERS_CLOSE_ENOUGH;
    }
    public boolean driveMotorsBusy() {
        return busy(lf, lb, rf, rb);

    }
    public void onStart() {
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rb, rf);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, lf, lb, rb, rf);
    }

    public void onStop() {
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rb, rf);
        stopDriveMotors();
    }
    public void stopDriveMotors() {
        lf.setPower(0.0);
        lb.setPower(0.0);

        rf.setPower(0.0);
        rb.setPower(0.0);
    }
    //    public void setFrontPower(double p) { pf.setPower(p); }
//    public void setBackPower(double p) { pr.setPower(p); }
    public void updateSensorTelemetry() {
        telemetry.addData("Imu", getGyro());
        //telemetry.addData("Color", String.format(Locale.US, "R: %d\tB: %d", color.red(), color.blue()));
        telemetry.addData("Light", getLight());
        telemetry.addData("EncodersC", String.format(Locale.US, "%d\t%d\t%d\t%d\t%d",
                lf.getCurrentPosition(),
                lb.getCurrentPosition(),
                rf.getCurrentPosition(),
                rb.getCurrentPosition()));
        telemetry.addData("EncodersT", String.format(Locale.US, "%d\t%d\t%d\t%d\t%d",
                lf.getTargetPosition(),
                lb.getTargetPosition(),
                rf.getTargetPosition(),
                rb.getTargetPosition()));
    }

//    public void drive(double direction, double velocity, double rotationVelocity) {
//        Wheels w = getWheels(direction, velocity, rotationVelocity);
//        lf.setPower(w.lf);
//        rf.setPower(w.rf);
//        lr.setPower(w.lr);
//        rr.setPower(w.rr);
//        telemetry.addData("Powers", String.format(Locale.US, "%.2f %.2f %.2f %.2f", w.lf, w.rf, w.lr, w.rr));
//    }

}

