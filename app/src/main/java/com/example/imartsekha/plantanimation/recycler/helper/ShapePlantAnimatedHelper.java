package com.example.imartsekha.plantanimation.recycler.helper;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
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
            setupVerticalCallbacks(this.verticalRecyclerView);
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
        view.setBackgroundColor(bubbleCircle.getColor());
        view.setX(circleShape.getX());
        view.setY(circleShape.getY());
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
            ScrollingHelper scrollingHelper = new ScrollingHelper(OrientationHelper.HORIZONTAL);

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItenIndex = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                scrollingHelper.onScrolled(recyclerView, dx, dy);
                float percentage = scrollingHelper.getCurrentProgress();


                IItemTypeProvider startVH = null;
                IItemTypeProvider finishVH = null;

                if(scrollingHelper.getCurrentDirection() == ScrollingHelper.DIRECTION.RIGHT/*direction == DIRECTION_RIGHT*/) {
                    startVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(lastVisibleItenIndex);
                    finishVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(firstVisibleItenIndex);
                } else if(scrollingHelper.getCurrentDirection() == ScrollingHelper.DIRECTION.LEFT /* direction == DIRECTION_LEFT*/) {
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

                    PlantCustomAnimation plantCustomAnimation = new PlantCustomAnimation(startView, finishView, scrollingHelper.isReverse());
                    plantCustomAnimation.calculatePositionAnimation(percentage);

                    animateView.setX(plantCustomAnimation.getCurrentX());
                    animateView.setY(plantCustomAnimation.getCurrentY());

                    animateView.setLayoutParams(new RelativeLayout.LayoutParams((int)plantCustomAnimation.getCurrentWidth(), (int)plantCustomAnimation.getCurrentHeight()));

//                    break;
                }
            }
        });
    }

    private void setupVerticalCallbacks(final RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            ScrollingHelper scrollingHelper = new ScrollingHelper(OrientationHelper.VERTICAL);

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

                scrollingHelper.onScrolled(recyclerView, dx, dy);
                float percentage = scrollingHelper.getCurrentProgress();

                if(isAllowTransfer) {

                    IItemTypeProvider startVH = null;
                    IItemTypeProvider finishVH = null;

                    if(scrollingHelper.getCurrentDirection() == ScrollingHelper.DIRECTION.UP /*== DIRECTION_UP*/) {
                        startVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(lastVisibleItenIndex);
                        finishVH = (IItemTypeProvider)recyclerView.findViewHolderForLayoutPosition(firstVisibleItenIndex);
                    } else if(scrollingHelper.getCurrentDirection() == ScrollingHelper.DIRECTION.DOWN/*direction == DIRECTION_DOWN*/) {
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

                        PlantCustomAnimation plantCustomAnimation = new PlantCustomAnimation(startView, finishView, scrollingHelper.isReverse());
                        plantCustomAnimation.calculatePositionAnimation(percentage);

                        animateView.setX(plantCustomAnimation.getCurrentX());
                        animateView.setY(plantCustomAnimation.getCurrentY());

                        animateView.setLayoutParams(new RelativeLayout.LayoutParams((int)plantCustomAnimation.getCurrentWidth(), (int)plantCustomAnimation.getCurrentHeight()));

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
            }
        });

    }

    private void destroyCallbacks() {

    }
}
