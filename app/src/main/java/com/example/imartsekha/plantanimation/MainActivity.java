package com.example.imartsekha.plantanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.transition.ChangeTransform;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.chart_view) ViewGroup week_view;
    @BindView(R.id.plant_view) ViewGroup plant_view;
    @BindView(R.id.root_container) ViewGroup rootParent;


    @BindView(R.id.button)
    View button1;

    @BindView(R.id.button2)
    View button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        runSecondAnimation();
//        runAnimationFirstButton();
//        runTestAnimation();
//        runAnimationSecondButton();
    }

    private void runChangeFirstButton() {
        final ViewGroup container = (ViewGroup) button1.getParent().getParent();
        container.getOverlay().add(button1);

        ObjectAnimator anim = ObjectAnimator.ofFloat(button1, "translationY", 10);
//        ObjectAnimator rotate = ObjectAnimator.ofFloat(button1, "rotation", 0, 360);
//        rotate.setRepeatCount(Animation.INFINITE);
//        rotate.setDuration(350);

        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                container.getOverlay().remove(button1);
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                container.getOverlay().remove(button1);
            }
        });

        anim.setDuration(2000);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim);
        set.start();

    }


    private void runSecondAnimation() {
        Transition move = new ChangeTransform()
                .addTarget(button1)
                .setDuration(2000);
        TransitionManager.beginDelayedTransition(rootParent, move);
        plant_view.removeView(button1);
        button1.setY(250.0f);
//        button1.setPadding(2,2,2,2);
//        button1.setElevation(4);
        week_view.addView(button1, 0);
    }

    private void runTestAnimation() {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(button1, "alpha", 1f, 0f);
        fadeOut.setDuration(500);

				/*
				 * Here we add our button to center layout's ViewGroupOverlay
				 * when first fade-out animation ends.
				 */
//        final ViewGroup container = (ViewGroup) button2.getParent();
//        final ObjectAnimator anim = ObjectAnimator.ofFloat(button1, View.TRANSLATION_Y, week_view.getHeight(), 20);
//        anim.setDuration(2000);

//        anim.addListener(new Animator.AnimatorListener() {
//
//            @Override
//            public void onAnimationStart(Animator animation) { }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) { }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
////                week_view.getOverlay().remove(button1);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
////                week_view.getOverlay().remove(button1);
//            }
//        });

        fadeOut.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                plant_view.removeView(button1);
                week_view.addView(button1);
                button1.setAlpha(1f);
                final ObjectAnimator anim = ObjectAnimator.ofFloat(button1, View.TRANSLATION_Y, week_view.getHeight(), 20);
                anim.setDuration(2000);
                anim.start();
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                plant_view.removeView(button1);
                week_view.addView(button1);
                button1.setAlpha(1f);
                final ObjectAnimator anim = ObjectAnimator.ofFloat(button1, View.TRANSLATION_Y, week_view.getHeight(), 20);
                anim.setDuration(2000);
                anim.start();
            }
        });

        fadeOut.start();
    }

    private void runAnimationSecondButton() {
        ObjectAnimator buttonFirstAnimator = ObjectAnimator.ofFloat(button2, View.TRANSLATION_Y, 0f, 500f);
        buttonFirstAnimator.setDuration(2000);
        buttonFirstAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        /*buttonFirstAnimator.start();*/


        ObjectAnimator buttonSecondAnimator = ObjectAnimator.ofFloat(button2, View.TRANSLATION_X, 0f, -500f);
        buttonSecondAnimator.setDuration(2000);
        buttonSecondAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
       /* butt*//**//*onFirstAnimator.start();*/


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(buttonFirstAnimator, buttonSecondAnimator);
        animatorSet.start();
    }

    private void runAnimationFirstButton() {
        ObjectAnimator buttonFirstAnimator = ObjectAnimator.ofFloat(button1, View.TRANSLATION_Y, 0f, 600f);
        buttonFirstAnimator.setDuration(2000);
        buttonFirstAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        /*buttonFirstAnimator.start();*/


        ObjectAnimator buttonSecondAnimator = ObjectAnimator.ofFloat(button1, View.TRANSLATION_X, 0f, 600f);
        buttonSecondAnimator.setDuration(2000);
        buttonSecondAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
       /* butt*//**//*onFirstAnimator.start();*/


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(buttonFirstAnimator, buttonSecondAnimator);
        animatorSet.start();
    }
}
