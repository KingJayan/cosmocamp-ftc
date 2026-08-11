package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Config.TeleOpConfig;
import org.firstinspires.ftc.teamcode.helpers.Curve;

@TeleOp(name="main tele", group ="Main")
public class TeleOpMain extends OpMode {
    private DcMotorEx leftBack, rightBack, leftFront, rightFront;

    @Override
    public void init() {
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        leftBack.setDirection(DcMotorEx.Direction.REVERSE);
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
    }

    @Override
    public void loop() {
        Drive();
    }

    private void Drive() {
        double y = deadband(gamepad1.left_stick_y);
        double x = deadband(-gamepad1.left_stick_x) * 1.1;
        double rx = deadband(-gamepad1.right_stick_x);

        /*
        y = TeleOpConfig.DRIVE_CURVE.apply(y, TeleOpConfig.BEZIER_P1, TeleOpConfig.BEZIER_P2, TeleOpConfig.EXP_A);
        x = TeleOpConfig.DRIVE_CURVE.apply(x, TeleOpConfig.BEZIER_P1, TeleOpConfig.BEZIER_P2, TeleOpConfig.EXP_A);
        rx = TeleOpConfig.DRIVE_CURVE.apply(rx, TeleOpConfig.BEZIER_P1, TeleOpConfig.BEZIER_P2, TeleOpConfig.EXP_A);
        */

        if (gamepad1.right_stick_button) rx *= TeleOpConfig.AIM_TURN_SCALE;
        if (gamepad1.left_stick_button) {
            y *= TeleOpConfig.AIM_TURN_SCALE + 0.1;
            x *= TeleOpConfig.AIM_TURN_SCALE + 0.1;
        }

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
        leftBack.setPower((y + x + rx) / denominator);
        leftFront.setPower((y - x + rx) / denominator);
        rightFront.setPower((y - x - rx) / denominator);
        rightBack.setPower((y + x - rx) / denominator);
    }

    private double deadband(double input) {
        if (Math.abs(input) < TeleOpConfig.STICK_DB) return 0;
        return (input - Math.signum(input) * TeleOpConfig.STICK_DB) / (1 - TeleOpConfig.STICK_DB);
    }
}
