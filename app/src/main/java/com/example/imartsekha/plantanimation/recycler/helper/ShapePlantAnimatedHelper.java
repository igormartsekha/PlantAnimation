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
import com.example.imartsekha.plantanimation.recycler.adapter.BubbleCircle;
import com.example.imartsekha.plantanimation.recycler.adapter.DayAdapter;
import com.example.imartsekha.plantanimation.recycler.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imartsekha on 11/27/17.
 */

public class ShapePlantAnimatedHelper {

    ViewGroup overlayView;
    RecyclerView verticalRecyclerView;
    RecyclerView horizontalRecyclerView;
    List<BubbleCircle> bubbleCircles = new ArrayList<>();

    public ShapePlantAnimatedHelper(ViewGroup overlayView) {
        this.overlayView = overlayView;
    }

    public void attachToVerticalRecyclerView(@Nullable final RecyclerView recyclerView) throws IllegalStateException {
        if (this.verticalRecyclerView == recyclerView) {
            return;
        }
        if (this.verticalRecyclerView != null) {
            destroyCallbacks();
        }
        this.verticalRecyclerView = recyclerView;
        if (this.verticalRecyclerView != null) {
            setupVericalCallbacks(this.verticalRecyclerView);
        }
    }

    public void attachToHorizontalRecyclerView(@Nullable final RecyclerView recyclerView) throws IllegalStateException {
        if (this.horizontalRecyclerView == recyclerView) {
            return;
        }
        if (this.horizontalRecyclerView != null) {
            destroyCallbacks();
        }
        this.horizontalRecyclerView = recyclerView;
        if (this.horizontalRecyclerView != null) {
            setupHorizontalCallbacks(this.horizontalRecyclerView);
        }
    }

    private void applyAnimateBubble(SimpleAdapter.SimpleAdapterViewHolder simpleAdapterViewHolder, List<BubbleCircle> bubbleCirclesNew) {
        overlayView.removeAllViews();

        this.bubbleCircles = bubbleCirclesNew;
        for(BubbleCircle bubbleCircle : bubbleCircles) {
            if(simpleAdapterViewHolder != null) {
                Shape shape = simpleAdapterViewHolder.provideShape(bubbleCircle.getBubleId());
                if (shape != null && shape.getType() == Shape.ShapeType.CIRCLE) {
                    View view = drawCircleView(bubbleCircle, shape);
                    bubbleCircle.setOverlayView(view);
                    overlayView.addView(view);
                }
            }
        }

        overlayView.requestLayout();
    }



    private View drawCircleView(BubbleCircle bubbleCircle, Shape circleShape) {
        View view = new BubbleView(verticalRecyclerView.getContext());
//        int newRadius = calculateRadiusForScoreBubble(circleShape.getWidth()/2, bubbleCircle.score);
        view.setBackgroundColor(bubbleCircle.getColor());

//        view.setX(circleShape.getX() + circleShape.getWidth()/2 - newRadius);
//        view.setY(circleShape.getY() + circleShape.getWidth()/2 - newRadius);
//
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(2*newRadius, 2*newRadius);

        view.setX(circleShape.getX());
        view.setY(circleShape.getY());
//
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(circleShape.getWidth(), circleShape.getHeight());
        view.setLayoutParams(layoutParams);
        return view;
    }

    private void setupHorizontalCallbacks(final RecyclerView recyclerView) {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int currentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                        final DayAdapter.DayViewHolder viewGroup = (DayAdapter.DayViewHolder)recyclerView.findViewHolderForLayoutPosition(currentPosition);
                        applyAnimateBubble(viewGroup.provideSimpleAdapterVH(), viewGroup.getBubbleCircles());
                    }
                });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final int DIRECTION_LEFT = 1;
            final int DIRECTION_RIGHT = 0;

            float currentPosition = 0;
            float lastPercentage = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();


                // calculate current direction
                int direction = -1;
                if(currentPosition > currentPosition+dx) {
                    direction = DIRECTION_LEFT;
                } else {
                    direction = DIRECTION_RIGHT;
                }

                // calculate scroll item percent
                currentPosition = currentPosition+dx;
                float percentage = 0;

                if(currentPosition == 0) {
                    percentage = 0;
                    lastPercentage = 0;
                } else {
                    float widthPerPercent =((float)recyclerView.getWidth())/100.0f;
                    percentage = Math.abs((currentPosition/widthPerPercent)%100);

                    if(direction == DIRECTION_RIGHT) {
                        percentage = 100-percentage;
                    }
                }
                if((int)percentage == 100) {
                    lastPercentage = 0;
                }

                if (Math.abs(lastPercentage - percentage) > 50.0f) {
                    lastPercentage = percentage;
                }


                IItemTypeProvider startVH = null;
                IItemTypeProvider finishVH = null;

                if(direction == DIRECTION_RIGHT) {
                    startVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(lastVisibleItenIndex);
                    finishVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(firstVisibleItenIndex);
                } else if(direction == DIRECTION_LEFT) {
                    startVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(firstVisibleItenIndex);
                    finishVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(lastVisibleItenIndex);
                }


                for (BubbleCircle circle : bubbleCircles) {
                    Shape startView = startVH.provideShape(circle.getBubleId());
                    Shape finishView = finishVH.provideShape(circle.getBubleId());
                    View animateView = circle.getOverlayView();
                    if (animateView == null || finishView == null || startView == null) {
                        break;
                    }

                    float finishX = finishView.getX();
                    float finishY = finishView.getY();
                    float finishWidth = finishView.getWidth();
                    float finishHeight = finishView.getHeight();


                    float startX = startView.getX();
                    float startY = startView.getY();
                    float startWidth = startView.getWidth();
                    float startHeight = startView.getHeight();

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

//                        if((lastPercentage == 0 && direction != DIRECTION_LEFT) || lastPercentage >= percentage) {
//                        if((lastPercentage == 0 && direction != DIRECTION_LEFT) || (lastPercentage > percentage) || (lastPercentage >= percentage && direction == DIRECTION_RIGHT) ) {
                        if(lastPercentage > percentage) {
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


                            Log.d("Scroll",
                                    "startX= "+startX+
                                        " finishX= "+finishX+
                                        " newX= "+stepX+
//                                        " newY= "+stepY+
                                            " currentX= "+currentX+
//                                            "currentY= "+currentY+
                                        " percentage= "+percentage +
                                        " lastPercentage= "+lastPercentage +
                                        " direction= "+((direction == DIRECTION_LEFT) ? "LEFT":"RIGHT")+
                                        " currentPosition= "+currentPosition+
                                        " dx= "+dx+
                                        " firstIndex= "+firstVisibleItenIndex +
                                        " lastIndex= "+lastVisibleItenIndex
                            );
                    }

                    animateView.setX(currentX);
                    animateView.setY(currentY);

                    animateView.setLayoutParams(new RelativeLayout.LayoutParams((int)currentWidth, (int)currentHeight));

//                    break;
                }

                lastPercentage = percentage;
            }
        });
    }

    private void setupVericalCallbacks(final RecyclerView recyclerView) {
//        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @SuppressWarnings("deprecation")
//                    @Override
//                    public void onGlobalLayout() {
//                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                        int currentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                        final SimpleAdapter.SimpleAdapterViewHolder viewGroup = (SimpleAdapter.SimpleAdapterViewHolder)recyclerView.findViewHolderForLayoutPosition(currentPosition);
////                        applyAnimateBubble(viewGroup, viewGroup.provideBubbleCircles());
//                    }
//                });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final int DIRECTION_UP = 1;
            final int DIRECTION_DOWN = 0;

            float currentPosition = 0;
            float lastPercentage = 0;

//            AnimatorSet animatorSet = new AnimatorSet();

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // calculate first and last item
                int firstVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                ArrayList<BubbleCircle> allowAnimateBubbles = new ArrayList<>();
                for(BubbleCircle bubbleCircle : bubbleCircles) {
                    for (BubbleCircle.BubbleDirection direction: bubbleCircle.getBubbleDirections()) {
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
//                    if(direction == DIRECTION_UP) {
//                        percentage = 100-percentage;
//                    }
                }
                if((int)percentage == 100) {
                    lastPercentage = 0;
                }

                if (Math.abs(lastPercentage - percentage) > 50.0f) {
                    lastPercentage = percentage;
                }


                if(isAllowTransfer) {
//                    long currentPlayTime = (long)(percentage * (animatorSet.getTotalDuration()/100));

//                    animatorSet.setCurrentPlayTime(currentPlayTime);

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
                        Shape startView = startVH.provideShape(circle.getBubleId());
                        Shape finishView = finishVH.provideShape(circle.getBubleId());
                        View animateView = circle.getOverlayView();
                        if(animateView == null || finishView == null || startView == null) {
                            break;
                        }

                        float finishX = finishView.getX();
                        float finishY = finishView.getY();

                        float finishWidth = finishView.getWidth();
                        float finishHeight = finishView.getHeight();

                        float startX = startView.getX();
                        float startY = startView.getY();
                        float startWidth = startView.getWidth();
                        float startHeight = startView.getHeight();


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

                            if(lastPercentage > percentage) {
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

//                            Log.d("Scroll",
//                                        "startWidth= "+startWidth+
//                                            " finishWidth= "+finishWidth+
//                                            " newWidth= "+currentWidth+
//                                            " percentage= "+percentage +
//                                            " direction= "+((direction == DIRECTION_UP) ? "UP":"DOWN")
//                                                +
//                                            " currentPosition= "+currentPosition+
//                                            " dy= "+dy+
//                                            " firstIndex= "+firstVisibleItenIndex +
//                                            " lastIndex= "+lastVisibleItenIndex
//                            );

                        }

                        animateView.setX(currentX);
                        animateView.setY(currentY);

                        animateView.setLayoutParams(new RelativeLayout.LayoutParams((int)currentWidth, (int)currentHeight));


//                        break;
                    }
                } else {
                    for (BubbleCircle circle : bubbleCircles) {
                        View animateView = circle.getOverlayView();
                        if(animateView != null) {
                            animateView.setY(animateView.getY() - dy);
                        }
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

    }

    private void destroyCallbacks() {

    }
}
