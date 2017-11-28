package com.example.imartsekha.plantanimation.recycler.helper;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.example.imartsekha.plantanimation.customviews.BubbleView;
import com.example.imartsekha.plantanimation.recycler.IItemTypeProvider;
import com.example.imartsekha.plantanimation.recycler.Shape;
import com.example.imartsekha.plantanimation.recycler.SimpleAdapter;

import java.util.ArrayList;

/**
 * Created by imartsekha on 11/27/17.
 */

public class ShapePlantAnimatedHelper {

    ViewGroup overlayView;
    RecyclerView recyclerView;
    ArrayList<BubbleCircle> bubbleCircles = new ArrayList<>();

    public ShapePlantAnimatedHelper(ViewGroup overlayView) {
        this.overlayView = overlayView;
    }

    public void attachToRecyclerView(@Nullable final RecyclerView recyclerView)
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
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
            Shape shape = simpleAdapterViewHolder.provideShape(bubbleCircle.bubleId);
            if(shape != null && shape.getType() == Shape.ShapeType.CIRCLE) {
                View view = drawCircleView(bubbleCircle, shape);
                bubbleCircle.setOverlayView(view);
                overlayView.addView(view);
            }
        }

        overlayView.requestLayout();
    }

    private int calculateRadiusForScoreBubble(int finalRadius, int score) {
        return ((finalRadius)/100)*score;
    }

    private View drawCircleView(BubbleCircle bubbleCircle, Shape circleShape) {
        View view = new BubbleView(recyclerView.getContext());
        int newRadius = calculateRadiusForScoreBubble(circleShape.getWidth()/2, bubbleCircle.score);
        view.setBackgroundColor(bubbleCircle.color);

        view.setX(circleShape.getX() + circleShape.getWidth()/2 - newRadius);
        view.setY(circleShape.getY() + circleShape.getWidth()/2 - newRadius);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(2*newRadius, 2*newRadius);
        view.setLayoutParams(layoutParams);

//        view.setBackgroundColor(bubbleCircle.color);

        return view;
    }

    private void setupCallbacks() {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        final SimpleAdapter.SimpleAdapterViewHolder viewGroup = (SimpleAdapter.SimpleAdapterViewHolder)recyclerView.findViewHolderForLayoutPosition(currentPosition());
                        applyAnimateBubble(viewGroup);
                    }
                });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final int DIRECTION_UP = 1;
            final int DIRECTION_DOWN = 0;

            float currentPosition = 0;
            float lastPercentage = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // calculate first and last item
                int firstVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                ArrayList<BubbleCircle> allowAnimateBubbles = new ArrayList<>();
                for(BubbleCircle bubbleCircle : bubbleCircles) {
                    for (BubbleCircle.BubbleDirection direction: bubbleCircle.bubbleDirections) {
                        boolean isAllowStartAnimation = recyclerView.getAdapter().getItemViewType(firstVisibleItenIndex) == direction.getStartViewItemType() || recyclerView.getAdapter().getItemViewType(firstVisibleItenIndex) == direction.getFinishViewItemType();
                        boolean isAllowFinishAnimation = recyclerView.getAdapter().getItemViewType(lastVisibleItenIndex) == direction.getFinishViewItemType() || recyclerView.getAdapter().getItemViewType(lastVisibleItenIndex) == direction.getStartViewItemType();

                        if(isAllowStartAnimation && isAllowFinishAnimation) {
                            allowAnimateBubbles.add(bubbleCircle);
                            break;
                        }
                    }
                }
                boolean isAllowTransfer = allowAnimateBubbles.size() > 0;

                // calculate current direction
                int direction = -1;
                if(currentPosition > currentPosition+dy) {
                    direction = DIRECTION_UP;
                } else {
                    direction = DIRECTION_DOWN;
                }

                // calculate scroll item percent
                currentPosition = currentPosition+dy;
                float percentage = 0;

                if(currentPosition == 0) {
                    percentage = 0;
                    lastPercentage = 0;
                } else {
                    percentage = Math.abs((currentPosition/((float)recyclerView.getHeight()/100.0f)));
                }
                if((int)percentage == 100) {
                    lastPercentage = 0;
                }


                if(isAllowTransfer) {
                    IItemTypeProvider startVH = null;
                    IItemTypeProvider finishVH = null;

                    if(direction == DIRECTION_UP) {
                        startVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(lastVisibleItenIndex);
                        finishVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(firstVisibleItenIndex);
                    } else if(direction == DIRECTION_DOWN) {
                        startVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(firstVisibleItenIndex);
                        finishVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(lastVisibleItenIndex);
                    }

                    for (BubbleCircle circle : bubbleCircles) {
                        Shape startView = startVH.provideShape(circle.bubleId);
                        Shape finishView = finishVH.provideShape(circle.bubleId);
                        View animateView = circle.overlayView;
                        if(animateView == null || finishView == null || startView == null) {
                            break;
                        }

                        float finishX = finishView.getX();
                        float finishY = finishView.getY();

                        float finishWidth = finishView.getWidth();
                        float finishHeight = finishView.getHeight();
                        if(finishView.getType() == Shape.ShapeType.CIRCLE) {
                            int newRadius = calculateRadiusForScoreBubble(finishView.getWidth()/2, circle.score);
                            finishX = finishView.getX()+finishView.getWidth()/2 - newRadius;
                            finishY = finishView.getY()+finishView.getHeight()/2 - newRadius;

                            finishWidth = 2*newRadius;
                            finishHeight = 2*newRadius;
                        }

                        float startX = startView.getX();
                        float startY = startView.getY();
                        float startWidth = startView.getWidth();
                        float startHeight = startView.getHeight();
                        if(startView.getType() == Shape.ShapeType.CIRCLE) {
                            int newRadius = calculateRadiusForScoreBubble(startView.getWidth()/2, circle.score);
                            startX = startView.getX()+startView.getWidth()/2 - newRadius;
                            startY = startView.getY()+startView.getHeight()/2 - newRadius;

                            startWidth = 2*newRadius;
                            startHeight = 2*newRadius;
                        }


                        float currentX = startX;
                        float currentY = startY;

                        float currentWidth = startWidth;
                        float currentHeight = startHeight;

                        if(percentage != 0) {
                            float stepX = ((finishX - startX) / 100.0f) * (int)percentage;
                            float stepY = ((finishY - startY) / 100.0f) * (int)percentage;

                            float stepWidth = ((finishWidth - startWidth) / 100.0f) * (int)percentage;
                            float stepHeight = ((finishHeight - startHeight) / 100.0f) * (int)percentage;


                            if((int)percentage >= 100) {
                                stepX = finishX-startX;
                                stepY = finishY-startY;

                                stepWidth = finishWidth - finishWidth;
                                stepHeight = finishHeight - finishHeight;
                            }

                            if(lastPercentage >= percentage) {
                                currentX = finishX - stepX;
                                currentY = finishY - stepY;

                                currentWidth = finishWidth - stepWidth;
                                currentHeight = finishHeight - stepHeight;
                            } else {
                                currentX = startX + stepX;
                                currentY = startY + stepY;

                                currentWidth = startWidth + stepWidth;
                                currentHeight = startHeight + stepHeight;
                            }


//                            Log.d("Scroll",
//                                    "startX= "+startX+
//                                        " finishX= "+finishX+
//                                        " newX= "+stepX+
//                                        " percentage= "+percentage +
//                                        " direction= "+((direction == DIRECTION_UP) ? "UP":"DOWN")+
//                                        " currentPosition= "+currentPosition+
//                                        " dy= "+dy+
//                                        " firstIndex= "+firstVisibleItenIndex +
//                                        " lastIndex= "+lastVisibleItenIndex
//                            );

                            Log.d("Scroll",
                                        "startWidth= "+startWidth+
                                            " finishWidth= "+finishWidth+
                                            " newWidth= "+currentWidth+
                                            " percentage= "+percentage +
                                            " direction= "+((direction == DIRECTION_UP) ? "UP":"DOWN")
//                                                +
//                                            " currentPosition= "+currentPosition+
//                                            " dy= "+dy+
//                                            " firstIndex= "+firstVisibleItenIndex +
//                                            " lastIndex= "+lastVisibleItenIndex
                            );

                        }

                        animateView.setX(currentX);
                        animateView.setY(currentY);

                        animateView.setLayoutParams(new RelativeLayout.LayoutParams((int)currentWidth, (int)currentHeight));


//                        break;
                    }
                } else {
                    for (BubbleCircle circle : bubbleCircles) {
                        View animateView = circle.overlayView;
                        animateView.setY(animateView.getY()-dy);
                    }
                }

                lastPercentage = percentage;



//                Log.d("Scroll", "firstIndex= "+firstVisibleItenIndex
//                        +"lastIndex= "+lastVisibleItenIndex
//                        +"dy="+dy
//                        +"progress="+percentage
//                        +"isAllowTransform="+(allowAnimateBubbles.size() > 0 ? "YES":"NO"));

                /*Log.d("Scroll", "position= "+ percentPosition
//                        +"; dx= "+dx
//                        +"; dy= "+dy
//                        +"; moveViewX="+moveView.getX()
//                        +"; moveViewY="+moveView.getY()
//                        +"; recyclerHeight="+recyclerView.getHeight()
//                        +"; currentPosition="+currentPosition);*/
            }
        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            float currentPosition = 0;
//
////            AnimatorSet animatorSet = new AnimatorSet();
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
////                Log.d("Scroll", "state= "+newState);
////                if(newState == 1) {
//////                    position = 0;
////                } else if (newState == 2) {
////
////                }
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
