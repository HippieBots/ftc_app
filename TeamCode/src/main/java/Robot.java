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
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Locale;

/**
 * Created by pflores on 9/19/17.
 */

public class Robot  {
    private DcMotor lf, rf, lb, rb, grabber;
    private Servo lg, rg;
    private double lastG;
    private Telemetry telemetry;
    private BNO055IMU imu;
    //private ColorSensor redVsBlue;
    //private Servo colorReader;
    public Robot(HardwareMap h, Telemetry _telemetry) {
        _telemetry = _telemetry;
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
        grabber = h.dcMotor.get("grabber");
        

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }




    public void grabBlock() {
        lg.setPosition(.82);
        rg.setPosition(.10);
    }

    public void dropBlock() {
        lg.setPosition(.64);
        rg.setPosition(.25);
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

//    private void readColor() {
//        if(getLight() >= 200) { //sensor reads white
//            red = true;
//        }
//        if(getLight() >= 200) {
//            blue = true;
//        }
//    }
//
//
//    public boolean red = false;
//    public boolean blue = false;
//
//    public int getLight() {
//        return redVsBlue.alpha();
//    }



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
        setPower(p, lf, lb, rb, rf);
    }

    public void setMotorMode(DcMotor.RunMode mode, DcMotor... ms) {
        for (DcMotor m :ms) {
            m.setMode(mode);
        }
    }


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

    private static final double ENCODER_DRIVE_POWER = .25;

    public static final double TICKS_PER_REV = 1120;
    public static final double WHEEL_DIAMETER = 4.0;
    public static final double TICKS_PER_INCH = TICKS_PER_REV*(16. / 24.) / (int)(WHEEL_DIAMETER * Math.PI);
    private static final double TICKS_PER_CM = TICKS_PER_INCH/2.54;

    private double encoder_drive_power = ENCODER_DRIVE_POWER;

    void setEncoderDrivePower(double p) {
        encoder_drive_power = p;
    }

//    public void setEncoderDrivePower(double p){
//        setPower(p,lf, lb, rb, rf);
//    }

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
    private static double ARBITRARY_SLOW_SPEED = .3;
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
            this.rf = rf;
            this.lb = lb;
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
        rf.setPower(w.rf);
        lb.setPower(w.lb);
        rb.setPower(w.rb);

        telemetry.addData("Powers", String.format(Locale.US, "%.2f %.2f %.2f %.2f", w.lf, w.rf, w.lb, w.rb));
    }

    public void encoderDriveTiles(double direction, double tiles) {
        encoderDriveInches(direction, 24.0 * tiles);
    }

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
        setPower(ENCODER_DRIVE_POWER, lf, lb, rf, rb);
        slowedDown = false;
    }

    public void announceEncoders() {
        telemetry.addData("LF", lf.getCurrentPosition());
        telemetry.addData("RF", rf.getCurrentPosition());
        telemetry.addData("LB", lb.getCurrentPosition());
        telemetry.addData("RB", rb.getCurrentPosition());
    }

    public void resetEncoders() {
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, lf, lb, rf, rb);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, lf, lb, rf, rb);
    }



}
