package com.example.imartsekha.plantanimation.recycler.helper;

import android.animation.AnimatorSet;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.example.imartsekha.plantanimation.recycler.CircleShape;
import com.example.imartsekha.plantanimation.recycler.Shape;
import com.example.imartsekha.plantanimation.recycler.SimpleAdapter;

import java.util.ArrayList;

/**
 * Created by imartsekha on 11/27/17.
 */

public class BubblePlantHelper {

    ViewGroup overlayView;
    RecyclerView recyclerView;
    ArrayList<BubbleCircle> bubbleCircles = new ArrayList<>();

    public BubblePlantHelper(ViewGroup overlayView) {
        this.overlayView = overlayView;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
            throws IllegalStateException {
        if (this.recyclerView == recyclerView) {
            return; // nothing to do
        }
        if (this.recyclerView != null) {
            destroyCallbacks();
        }
        this.recyclerView = recyclerView;
        if (this.recyclerView != null) {
            setupCallbacks();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
            }

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
            }

            @Override
            public void onAttachedToWindow(RecyclerView view) {
                super.onAttachedToWindow(view);
            }

            @Override
            public void attachView(View child, int index, RecyclerView.LayoutParams lp) {
                super.attachView(child, index, lp);
            }

            
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

            }
        });

        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });

    }

    public void addAnimateBubble(BubbleCircle bubbleCircle) {
        bubbleCircles.add(bubbleCircle);
    }


    private int currentPosition() {
        return ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    private void applyAnimateBubble(SimpleAdapter.SimpleAdapterViewHolder simpleAdapterViewHolder) {
        overlayView.removeAllViews();
        for(BubbleCircle bubbleCircle : bubbleCircles) {
            Shape shape = simpleAdapterViewHolder.provideCircle(bubbleCircle.bubleId);
            if(shape instanceof CircleShape) {
                overlayView.addView(drawCircleView(bubbleCircle, (CircleShape)shape));
            }
        }

        overlayView.requestLayout();
    }

    private View drawCircleView(BubbleCircle bubbleCircle, CircleShape circleShape) {
        View view = new View(recyclerView.getContext());
        int newRadius = (circleShape.getRadius()/100)*bubbleCircle.score;

        view.setX(circleShape.getX() - newRadius);
        view.setY(circleShape.getY() - newRadius);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(2*newRadius, 2*newRadius);
        view.setLayoutParams(layoutParams);

        view.setBackgroundColor(bubbleCircle.color);

        return view;
    }

    private void setupCallbacks() {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        SimpleAdapter.SimpleAdapterViewHolder viewGroup = (SimpleAdapter.SimpleAdapterViewHolder)recyclerView.findViewHolderForLayoutPosition(currentPosition());
                        applyAnimateBubble(viewGroup);



                        //                        linearLayoutManager.scrollToPositionWithOffset(1, 0);
//                        applyBubbles(1);
//                        applyBubbles(0);
//
//                        applyBubulesAtStart();
                    }
                });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            float currentPosition = 0;
//
//            AnimatorSet animatorSet = new AnimatorSet();
//
//            float moveX;
//            float moveY;
//
//            float moveHeight;
//            float moveWidth;
//
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                Log.d("Scroll", "state= "+newState);
//                if(newState == 1) {
////                    position = 0;
//                } else if (newState == 2) {
//
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if(end_layout == null) {
//                    applyBubbles(0);
//                    if(end_layout == null) {
//                        return;
//                    } else {
////                        final ObjectAnimator animY = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_Y, moveView.getY(), end_layout.getY());
////                        final ObjectAnimator animX = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_X, moveView.getX(), end_layout.getX());
//////
////                        animatorSet.setInterpolator(new LinearInterpolator());
////                        animatorSet.playTogether(animX, animY);
////                        animatorSet.setDuration(1000);
//
//                        moveX = moveView.getX();
//                        moveY = moveView.getY();
//
//                        moveHeight = moveView.getHeight();
//                        moveWidth = moveView.getWidth();
//                    }
//                }
//
////                int recyclerHeight = recyclerView.getHeight();
////
//                currentPosition = currentPosition+dy;
////                int percentPosition = 0;
//
////                if(currentPosition == 0) {
////                    percentPosition = 0;
////                } else {
////                    percentPosition = (int) Math.abs((currentPosition/(float) recyclerHeight) * 100.0f);//(recyclerHeight/100);
////                }
//
//                int offset = recyclerView.computeVerticalScrollOffset();
//                int extent = recyclerView.computeVerticalScrollExtent();
//                int range = recyclerView.computeVerticalScrollRange();
//
//                float percentage = 100 - (100.0f * offset / (float)(range - extent));
//
//                Log.d("Scroll", "Position = "+percentage + " and perc = "+percentage);
//
//
////                animatorSet.setCurrentPlayTime(percentPosition*10);
//
//                float endY = end_layout.getY();
//                float endX = end_layout.getX();
//
//
//                float endHeight = end_layout.getHeight();
//                float endWidth = end_layout.getWidth();
//
//
//
////                float newX=moveView.getX();
////                float newY=moveView.getY();
//
//                if(percentage != 0) {
//                    float newX = ((endX-moveX)/100.0f)*percentage;
//                    float newY = ((endY-moveY)/100.0f)*percentage;
//
//                    float newHeight = ((endHeight-moveHeight)/100.0f)*percentage;
//                    float newWidth = ((endWidth-moveWidth)/100.0f)*percentage;
//
//                    if(percentage == 100) {
//                        newX = endX-moveX;
//                        newY = endY-moveY;
//
//                        newHeight = endHeight;
//                        newWidth = endWidth;
//                    }
//                    moveView.setX(moveX+newX);
//                    moveView.setY(moveY+newY);
//                } else {
//                    moveView.setX(moveX);
//                    moveView.setY(moveY);
////                    moveView.setX(moveX);
////                    moveView.setY(moveY);
//                }
//
//
//
//
//
//
//
//
////                int newMoveViewY = (int)moveView.getY()+dy;
////                if(newMoveViewY < 0) {newMoveViewY = 0;}
////                if(newMoveViewY > recyclerView.getHeight() - moveView.getHeight()) {newMoveViewY = recyclerView.getHeight() - moveView.getHeight();}
////                moveView.setY(newMoveViewY);
//
//
//                int position = 0;
//                /*Log.d("Scroll", "position= "+ percentPosition
//                        +"; dx= "+dx
//                        +"; dy= "+dy
//                        +"; moveViewX="+moveView.getX()
//                        +"; moveViewY="+moveView.getY()
//                        +"; recyclerHeight="+recyclerView.getHeight()
//                        +"; currentPosition="+currentPosition);*/
//
//            }
//        });
    }

    private void destroyCallbacks() {

    }
}
