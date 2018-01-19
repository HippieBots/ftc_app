import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Locale;

/**
 * Created by pflores on 9/19/17.
 */

/*
THE ORDER OF MOTORS IS: lf, lb, rf, rb (REMEMBER LEFT IS ALWAYS FIRST)
 */

public class Robot  {
    private DcMotor lf, lb, rf, rb, grabber;
    private Servo lg, rg,top, ja;
    private double lastG;
    private Telemetry telemetry;
    private BNO055IMU imu;
    private ColorSensor CS;
    //private ColorSensor redVsBlue;
    //private Servo colorReader;
    public Robot(HardwareMap h, Telemetry telemetry) {
        this.telemetry = telemetry;
        imu = h.get(BNO055IMU.class, "imu");
        initilizeGyro();
        //redVsBlue = h.colorSensor.get("redVsBlue");

        //colorReader = h.servo.get("color sensor");


        lf = h.dcMotor.get("lf");
        lb = h.dcMotor.get("lb");
        rf = h.dcMotor.get("rf");
        rb = h.dcMotor.get("rb");

        lg = h.servo.get("lg");
        rg = h.servo.get("rg");
        top = h.servo.get("top");
        grabber = h.dcMotor.get("grabber");
        ja = h.servo.get("ja");
        CS = h.colorSensor.get("CS");




        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }




    public void grabBlock() {
        lg.setPosition(.70);
        rg.setPosition(.20);
        top.setPosition(.65);
    }

    public void dropBlock() {
        lg.setPosition(.85);
        rg.setPosition(.02);
        top.setPosition(.2);
    }

    public void lifterUp() {
        grabber.setPower(1.0);
    }

    public void lifterDown() {
        grabber.setPower(-1.0);
    }

    public void lifterStop() {
        grabber.setPower(0.0);
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
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        return angles.firstAngle;
    }

    public double getGyro() {
        return (getGyroRaw() - lastG) % (2.0 * Math.PI);
    }

    public double getGyroDeg() {
        return Math.toDegrees(getGyro());
    }

    public boolean isGyroCalibrated() {
        return imu.isSystemCalibrated();
    }

    public double getHeadingRadians() {
        return getGyro();
    }

    public double getHeadingDeg() {
        return (double) getGyroDeg();
    }

    public void resetHeading() {
        lastG = getGyroRaw();
    }

    public void setMotorSpeeds(double lfs, double lbs, double rfs, double rbs){
        lf.setPower(lfs);
        lb.setPower(lbs);
        rf.setPower(rfs);
        rb.setPower(rbs);
    }

    private void setPower(double p, DcMotor... ms) {
        for (DcMotor m : ms) {
            m.setPower(p);
        }
    }
    public void setPowerNew(double p){
        setPower(p, lf, lb, rf, rb);
    }

    public void setMotorMode(DcMotor.RunMode mode, DcMotor... ms) {
        for (DcMotor m :ms) {
            m.setMode(mode);
        }
    }

    public void resetDriveMotorModes() {
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rf, rb);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, lf, lb, rf, rb);
    }

    private void setMode(DcMotor.RunMode mode, DcMotor... ms) {
        for (DcMotor m : ms) {
            m.setMode(mode);
        }
    }

    private void setTargetPosition(int pos, DcMotor... ms) {
        for (DcMotor m : ms) {
            m.setTargetPosition(pos);
        }
    }

    public static final int ENCODERS_CLOSE_ENOUGH = 10;
    private boolean busy(DcMotor... ms) {
        int total = 0;
        for (DcMotor m : ms) {
            if (m.isBusy()) {
                final int c = Math.abs(m.getCurrentPosition());
                final int t = Math.abs(m.getTargetPosition());
                total += c < t ? (t - c) : 0;
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

    private static final double ENCODER_DRIVE_POWER = .25;

    public static final double TICKS_PER_REV = 1120;
    public static final double WHEEL_DIAMETER = 4.0;
    public static final double TICKS_PER_INCH = TICKS_PER_REV*(16. / 24.) / (int)(WHEEL_DIAMETER * Math.PI);
    private static final double TICKS_PER_CM = TICKS_PER_INCH/2.54;

    void setEncoderDrivePower(double p) {
        encoder_drive_power = p;
    }

    private double encoder_drive_power = ENCODER_DRIVE_POWER;


    void clearEncoderDrivePower() {
        encoder_drive_power = ENCODER_DRIVE_POWER;
    }

    private int averageRemainingTicks(DcMotor... ms) {
        int total = 0;
        int count = 0;
        for (DcMotor m : ms) {
            if (m.getMode() == DcMotor.RunMode.RUN_TO_POSITION && 100 < Math.abs(m.getTargetPosition())) {
                total += Math.abs(m.getTargetPosition() - m.getCurrentPosition());
                count += 1;
            }
        }
        return 0 == count ? 0 : total / count;
    }

    private static int SLOW_DOWN_HERE = 1120;
    private static double ARBITRARY_SLOW_SPEED = .4;
    private boolean slowedDown = false;

    private void encoderDriveSlowdown() {
        if (!slowedDown) {
            if (lf.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                int remaining = averageRemainingTicks(lf, lb, rf, rb);
                if (remaining < SLOW_DOWN_HERE) {
                    slowedDown = true;
                    setPower(ARBITRARY_SLOW_SPEED, lf, lb, rf, rb);
                }
            }
        }
    }



    private static class Wheels {
        public double lf, lb, rf, rb;

        public Wheels(double lf, double rf, double lb, double rb) {
            this.lf = lf;
            this.lb = lb;
            this.rf = rf;
            this.rb = rb;
        }
    }

    private Wheels getWheels(double direction, double velocity, double rotationVelocity) {
        final double vd = velocity;
        final double td = direction;
        final double vt = rotationVelocity;

        double s =  Math.sin(td + Math.PI / 4.0);
        double c = Math.cos(td + Math.PI / 4.0);
        double m = Math.max(Math.abs(s), Math.abs(c));
        s /= m;
        c /= m;

        final double v1 = vd * s + vt;
        final double v2 = vd * c - vt;
        final double v3 = vd * c + vt;
        final double v4 = vd * s - vt;

        // Ensure that none of the values go over 1.0. If none of the provided values are
        // over 1.0, just scale by 1.0 and keep all values.
        double scale = ma(1.0, v1, v2, v3, v4);

        return new Wheels(v1 / scale, v2 / scale, v3 / scale, v4 / scale);
    }

    private static double ma(double... xs) {
        double ret = 0.0;
        for (double x : xs) {
            ret = Math.max(ret, Math.abs(x));
        }
        return ret;
    }

    public void drive(double direction, double velocity, double rotationVelocity) {
        Wheels w = getWheels(direction, velocity, rotationVelocity);
        lf.setPower(w.lf);
        lb.setPower(w.lb);
        rf.setPower(w.rf);
        rb.setPower(w.rb);

        telemetry.addData("Powers", String.format(Locale.US, "%.2f %.2f %.2f %.2f", w.lf, w.lb, w.rf, w.rb));
    }

    public void encoderDriveTiles(double direction, double tiles) {
        encoderDriveInches(direction, 24.0 * tiles);
    }


    //Make pattern lf lb rf rb

    public void encoderDriveInches(double direction, double inches) {
        final Wheels w = getWheels(direction, 1.0, 0.0);
        final int ticks = (int) (inches * TICKS_PER_INCH);
        encoderDrive(ticks * w.lf, ticks * w.rf, ticks * w.lb, ticks * w.rb);
    }
    private void encoderDrive(double lft, double rft, double lrt, double rrt) {
        encoderDrive((int) lft, (int) rft, (int) lrt, (int) rrt);
    }

    private void encoderDrive(int lft, int rft, int lrt, int rrt) {
        setPower(0.0, lf, lb, rf, rb);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rf, rb);
        setTargetPosition(lft, lf);
        setTargetPosition(rft, rf);
        setTargetPosition(lrt, lb);
        setTargetPosition(rrt, rb);
        setMode(DcMotor.RunMode.RUN_TO_POSITION, lf, rf, lb, rb);
        setPower(encoder_drive_power, lf, lb, rf, rb);
    }

    public void announceEncoders() {
        telemetry.addData("LF", lf.getCurrentPosition() + ", " + lf.getTargetPosition());
        telemetry.addData("LB", lb.getCurrentPosition() + ", " + lb.getTargetPosition());
        telemetry.addData("RF", rf.getCurrentPosition() + ", " + rf.getTargetPosition());
        telemetry.addData("RB", rb.getCurrentPosition() + ", " + rb.getTargetPosition());
    }

    public void resetEncoders() {
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rf, rb);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, lf, lb, rf, rb);
    }

    private Orientation orientation;
    private Velocity velocity;
    public void loop() {
        orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }

    public boolean isRed(){
         if(CS.red()>CS.blue()){
             return true;
         }
         else {

             return false;
         }

    }
    public void PutArmDown(){
        ja.setPosition(1.0);
    }
    public void PutArmUp() {
        ja.setPosition(.35);
    }
}