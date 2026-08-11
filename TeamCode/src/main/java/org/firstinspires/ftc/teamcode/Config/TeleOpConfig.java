package org.firstinspires.ftc.teamcode.Config;

import com.bylazar.configurables.annotations.Configurable;
import org.firstinspires.ftc.teamcode.helpers.Curve;

@Configurable
public class TeleOpConfig {
    public static double AIM_TURN_SCALE = 0.2;
    public static double STICK_DB = 0.05;
    public static Curve DRIVE_CURVE = Curve.LINEAR; //linear, cubic_bezier, smoothstep, exponential, quintic, lerp, log (ALL CAPS)
    //preset specific
    public static double BEZIER_P1 = 0.5;
    public static double BEZIER_P2 = 0.5;
    public static double EXP_A = 2.0;
}
