package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Config.TeleOpConfig.kD;
import static org.firstinspires.ftc.teamcode.Config.TeleOpConfig.kF;
import static org.firstinspires.ftc.teamcode.Config.TeleOpConfig.kI;
import static org.firstinspires.ftc.teamcode.Config.TeleOpConfig.kP;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.TeleOpConfig;

@TeleOp(name = "flywheel tuner", group = "Main")
public class ShooterTuner extends OpMode {
    private DcMotorEx shooter;
    private Telemetry dash;
    private double targetVelocity = 0;

    @Override
    public void init() {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");

        shooter.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shooter.setVelocityPIDFCoefficients(kI, kP, kD, kF);
        shooter.setDirection(DcMotorEx.Direction.FORWARD);

        dash = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        shooter();
        report();
    }

    private void shooter() {
        shooter.setVelocityPIDFCoefficients(kI, kP, kD, kF);
        if (gamepad1.right_trigger > 0.3) {
            targetVelocity = TeleOpConfig.FAST;
            shooter.setVelocity(TeleOpConfig.FAST);
        } else if (gamepad1.a) {
            targetVelocity = TeleOpConfig.SLOW;
            shooter.setVelocity(TeleOpConfig.SLOW);
        }
    }

    private void report() {
        double current = shooter.getVelocity();
        dash.addData("target velocity", targetVelocity);
        dash.addData("current velocity", current);
        dash.addData("error", targetVelocity - current);
        dash.addData("motor power", shooter.getPower());
        dash.addData("kP", kP);
        dash.addData("kI", kI);
        dash.addData("kD", kD);
        dash.addData("kF", kF);
        dash.update();
    }
}
