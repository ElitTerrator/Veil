package foundry.veil.api.client.necromancer.animation.keyframe;

import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

// todo: support cubic interpolation w/ derivatives
public enum Interpolation {
    STEP(
            (a, b, t) -> t < 1F ? a : b,
            (a, b, t, result) -> result.set(t < 0.5F ? a : b)
    ),
    LINEAR(
            (a, b, t) -> MathHelper.lerp(t, a, b),
            Quaternionfc::slerp
    ),
    EASE_IN(
            (a, b, t) -> MathHelper.lerp(t * t, a, b),
            (a, b, t, result) -> result.set(a).slerp(b, t * t)
    ),
    EASE_OUT(
            (a, b, t) -> MathHelper.lerp(1F - (1F - t) * (1F - t), a, b),
            (a, b, t, result) -> result.set(a).slerp(b, 1F - (1F - t) * (1F - t))
    ),
    EASE_IN_OUT(
            (a, b, t) -> MathHelper.lerp(easeInOut(t), a, b),
            (a, b, t, result) -> result.set(a).slerp(b, easeInOut(t))
    );

    private static float easeInOut(float t) {
        return t < 0.5F ? 2F * t * t : 1F - (float) Math.pow(-2F * t + 2F, 2) / 2F;
    }

    private final FloatInterpolator fInterpolator;
    private final QuaternionInterpolator qInterpolator;

    Interpolation(FloatInterpolator fInterpolator, QuaternionInterpolator qInterpolator) {
        this.fInterpolator = fInterpolator;
        this.qInterpolator = qInterpolator;
    }

    public float interpolate(float a, float b, float t) {
        return this.fInterpolator.interpolate(a, b, t);
    }

    public Quaternionf interpolate(Quaternionfc a, Quaternionfc b, float t, Quaternionf result) {
        this.qInterpolator.interpolate(a, b, t, result);
        return result;
    }

    public interface FloatInterpolator {
        float interpolate(float a, float b, float t);
    }

    public interface QuaternionInterpolator {
        void interpolate(Quaternionfc a, Quaternionfc b, float t, Quaternionf result);
    }
}
