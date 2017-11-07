//import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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

public class Robot {
    private DcMotor lf, rf, lb, rb, grabber;
    Servo lg, rg;
    private double lastG;
    //private Telemetry telemetry;
    //private BNO055IMU imu;
    //private ColorSensor redVsBlue;
    //private Servo colorReader;
    public Robot(HardwareMap h) {
       //imu = h.get(BNO055IMU.class, "gyro");
        // initilizeGyro();
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
    }




    public void grabBlock() {
        lg.setPosition(.82);
        rg.setPosition(.14);
    }

    public void dropBlock() {
        lg.setPosition(.6);
        rg.setPosition(.3);
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


//    private void initilizeGyro() {
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
//        parameters.loggingEnabled      = true;
//        parameters.loggingTag          = "IMU";
//        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
//        imu.initialize(parameters);
//    }

//    public void resetGyro() {
//        lastG = getGyroRaw();
//    }
//    public double getGyroRaw() {
//        Orientation angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
//        return angles.firstAngle;
//    }
//    public double getGyro(){
//        return (getGyroRaw()-lastG)* (2.0 * Math.PI);
//    }

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
//    public void updateSensorTelemetry() {
//
//        telemetry.addData("EncodersC", String.format(Locale.US, "%d\t%d\t%d\t%d\t%d",
//                lf.getCurrentPosition(),
//                lb.getCurrentPosition(),
//                rf.getCurrentPosition(),
//                rb.getCurrentPosition()));
//        telemetry.addData("EncodersT", String.format(Locale.US, "%d\t%d\t%d\t%d\t%d",
//                lf.getTargetPosition(),
//                lb.getTargetPosition(),
//                rf.getTargetPosition(),
//                rb.getTargetPosition()));
//    }

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
        //telemetry.addData("Powers", String.format(Locale.US, "%.2f %.2f %.2f %.2f", w.lf, w.rf, w.lb, w.rb));
    }

}
