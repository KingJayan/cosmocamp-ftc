package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Config.TeleOpConfig;

@TeleOp(name = "main tele", group = "Main")
public class TeleOpMain extends OpMode {
    private DcMotorEx leftBack, rightBack, leftFront, rightFront;
    private DcMotorEx intake;

    private int intakeState = 0;
    private boolean prevIn = false;
    private boolean prevOut = false;

    @Override
    public void init() {
        leftBack = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightBack = hardwareMap.get(DcMotorEx.class, "backRight");
        leftFront = hardwareMap.get(DcMotorEx.class, "frontLeft");
        rightFront = hardwareMap.get(DcMotorEx.class, "frontRight");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        DcMotorEx[] motors = {leftBack, rightBack, leftFront, rightFront, intake};

        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        }

        leftBack.setDirection(DcMotorEx.Direction.REVERSE);
        leftFront.setDirection(DcMotorEx.Direction.FORWARD);
        rightBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        intake.setDirection(DcMotorEx.Direction.FORWARD);
    }

    @Override
    public void loop() {
        drive();
        intake();
    }

    private void intake() {
        boolean in = gamepad1.right_bumper;
        boolean out = gamepad1.right_trigger > 0.3;

        if (in && !prevIn)
            intakeState = intakeState == 1 ? 0 : 1;

        if (out && !prevOut)
            intakeState = intakeState == -1 ? 0 : -1;

        prevIn = in;
        prevOut = out;

        intake.setPower(intakeState);
    }

    private void drive() {
        double y = -deadband(gamepad1.left_stick_y);
        double x = deadband(-gamepad1.left_stick_x) * 1.1;
        double rx = deadband(-gamepad1.right_stick_x);

        if (gamepad1.left_stick_button) {
            y = TeleOpConfig.AIM_TURN_SCALE * y;
            x = TeleOpConfig.AIM_TURN_SCALE * x;
        } else {
            y = curve(y);
            x = curve(x);
        }
        if (gamepad1.right_stick_button) {
            rx = TeleOpConfig.AIM_TURN_SCALE * rx;
        } else {
            rx = curve(rx);
        }

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);

        leftFront.setPower((y + x + rx) / denominator);
        leftBack.setPower((y - x + rx) / denominator);
        rightFront.setPower((y - x - rx) / denominator);
        rightBack.setPower((y + x - rx) / denominator);
    }

    private double curve(double input) {
        return TeleOpConfig.DRIVE_CURVE.apply(input, TeleOpConfig.BEZIER_P1, TeleOpConfig.BEZIER_P2, TeleOpConfig.EXP_A);
    }

    private double deadband(double input) {
        if (Math.abs(input) < TeleOpConfig.STICK_DB) return 0;
        return (input - Math.signum(input) * TeleOpConfig.STICK_DB) / (1 - TeleOpConfig.STICK_DB);
    }
}