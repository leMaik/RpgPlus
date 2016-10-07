package de.craften.plugins.rpgplus.components.cinematic;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Interpolation of camera positions and orientations based on {@link Keyframe}s.
 */
public class InterpolatedFrames {
    private final PolynomialSplineFunction x;
    private final PolynomialSplineFunction y;
    private final PolynomialSplineFunction z;
    private final PolynomialSplineFunction pitch;
    private final PolynomialSplineFunction yaw;
    private final double length;

    /**
     * Interpolates the given keyframes and creates a new InterpolatedFrames instance.
     *
     * @param keyframes keyframes
     */
    InterpolatedFrames(List<Keyframe> keyframes) {
        Collections.sort(keyframes, new Comparator<Keyframe>() {
            @Override
            public int compare(Keyframe a, Keyframe b) {
                return Double.compare(a.getTime(), b.getTime());
            }
        });
        this.length = keyframes.get(keyframes.size() - 1).getTime();

        double[] t = new double[keyframes.size()];
        double[] xValues = new double[keyframes.size()];
        double[] yValues = new double[keyframes.size()];
        double[] zValues = new double[keyframes.size()];
        double[] pitchValues = new double[keyframes.size()];
        double[] yawValues = new double[keyframes.size()];

        int i = 0;
        for (Keyframe frame : keyframes) {
            t[i] = frame.getTime();
            xValues[i] = frame.getX();
            yValues[i] = frame.getY();
            zValues[i] = frame.getZ();
            pitchValues[i] = frame.getPitch();
            yawValues[i] = frame.getYaw();
            i++;
        }

        SplineInterpolator interpolator = new SplineInterpolator();
        this.x = interpolator.interpolate(t, xValues);
        this.y = interpolator.interpolate(t, yValues);
        this.z = interpolator.interpolate(t, zValues);
        this.pitch = interpolator.interpolate(t, pitchValues);
        this.yaw = interpolator.interpolate(t, yawValues);
    }

    public double getX(double t) {
        return x.value(t);
    }

    public double getY(double t) {
        return y.value(t);
    }

    public double getZ(double t) {
        return z.value(t);
    }

    public double getPitch(double t) {
        return pitch.value(t);
    }

    public double getYaw(double t) {
        return yaw.value(t);
    }

    /**
     * Gets the length of the video.
     *
     * @return length of the video
     */
    public double getLength() {
        return length;
    }
}
