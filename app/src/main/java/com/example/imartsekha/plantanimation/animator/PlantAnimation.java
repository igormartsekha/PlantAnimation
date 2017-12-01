package com.example.imartsekha.plantanimation.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by imartsekha on 12/1/17.
 */

public class PlantAnimation {

    private AnimationEvaluator evaluator;
    private int duration;

    public PlantAnimation(View target, int duration, float startX, float startY, float endX, float endY) {
        this.evaluator = new AnimationEvaluator(target, startX, startY, endX, endY);
        this.duration = duration;
    }

    public Animator playAnimation(float fraction) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, fraction);
        animator.addListener(evaluator);
        animator.setDuration((long) (duration * fraction));
        return animator;
    }

    public Animator playReverseAnimation(float fraction) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 1f - fraction);
        animator.addListener(evaluator);
        animator.setDuration((long) (duration * fraction));

//        ValueAnimator.
        return animator;
    }

    public class AnimationEvaluator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

        private View animationTarget;
        private float startX, endX;
        private float startY, endY;
//        private float startTranslationX, endTranslationX;

        public AnimationEvaluator(View animationTarget, float startX, float startY, float endX, float endY ) {
            this.animationTarget = animationTarget;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }


        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float animationProgress = valueAnimator.getAnimatedFraction();
//            float scale = startScale + (endScale - startScale) * animationProgress;
//            float translationX = startTranslationX + (endTranslationX - startTranslationX) * animationProgress;
//            animationTarget.setScaleX(scale);
//            animationTarget.setScaleY(scale);
//            animationTarget.setTranslationX(translationX);

            float x = startX + (endX - startX) * animationProgress;
            float y = startY + (endY - startY) * animationProgress;
            animationTarget.setX(x);
            animationTarget.setY(y);
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
