package com.lejel.wowbox.apps.common.component.snaphelper;


import android.os.Build;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

class GravityDelegate {

    private OrientationHelper verticalHelper;
    private OrientationHelper horizontalHelper;
    private int gravity;
    private boolean isRtlHorizontal;
    private boolean snapLastItem;
    private GravitySnapHelper.SnapListener listener;
    private boolean snapping;
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                snapping = false;
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE && snapping && listener != null) {
                int position = getSnappedPosition(recyclerView);
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSnap(position);
                }
                snapping = false;
            }
        }
    };

    public GravityDelegate(int gravity) {
        this(gravity, false, null);
    }

    public GravityDelegate(int gravity, GravitySnapHelper.SnapListener listener) {
        this(gravity, false, listener);
    }

    public GravityDelegate(int gravity, boolean enableSnapLast,
                           GravitySnapHelper.SnapListener listener) {
        if (gravity != Gravity.START && gravity != Gravity.END
                && gravity != Gravity.BOTTOM && gravity != Gravity.TOP) {
            throw new IllegalArgumentException("Invalid gravity value. Use START " +
                    "| END | BOTTOM | TOP constants");
        }
        this.snapLastItem = enableSnapLast;
        this.gravity = gravity;
        this.listener = listener;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.setOnFlingListener(null);
            if ((gravity == Gravity.START || gravity == Gravity.END)
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                isRtlHorizontal
                        = recyclerView.getContext().getResources().getConfiguration()
                        .getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
            }
            if (listener != null) {
                recyclerView.addOnScrollListener(mScrollListener);
            }
        }
    }

    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int[] out = new int[2];

        if (layoutManager.canScrollHorizontally()) {
            if (gravity == Gravity.START) {
                out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager), false);
            } else { // END
                out[0] = distanceToEnd(targetView, getHorizontalHelper(layoutManager), false);
            }
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            if (gravity == Gravity.TOP) {
                out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager), false);
            } else { // BOTTOM
                out[1] = distanceToEnd(targetView, getVerticalHelper(layoutManager), false);
            }
        } else {
            out[1] = 0;
        }

        return out;
    }

    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        View snapView = null;
        if (layoutManager instanceof LinearLayoutManager) {
            switch (gravity) {
                case Gravity.START:
                    snapView = findStartView(layoutManager, getHorizontalHelper(layoutManager));
                    break;
                case Gravity.END:
                    snapView = findEndView(layoutManager, getHorizontalHelper(layoutManager));
                    break;
                case Gravity.TOP:
                    snapView = findStartView(layoutManager, getVerticalHelper(layoutManager));
                    break;
                case Gravity.BOTTOM:
                    snapView = findEndView(layoutManager, getVerticalHelper(layoutManager));
                    break;
            }
        }
        snapping = snapView != null;
        return snapView;
    }

    public void enableLastItemSnap(boolean snap) {
        snapLastItem = snap;
    }

    private int distanceToStart(View targetView, OrientationHelper helper, boolean fromEnd) {
        if (isRtlHorizontal && !fromEnd) {
            return distanceToEnd(targetView, helper, true);
        }

        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    private int distanceToEnd(View targetView, OrientationHelper helper, boolean fromStart) {
        if (isRtlHorizontal && !fromStart) {
            return distanceToStart(targetView, helper, true);
        }

        return helper.getDecoratedEnd(targetView) - helper.getEndAfterPadding();
    }

    /**
     * Returns the first view that we should snap to.
     *
     * @param layoutManager the recyclerview's layout manager
     * @param helper        orientation helper to calculate view sizes
     * @return the first view in the LayoutManager to snap to
     */
    private View findStartView(RecyclerView.LayoutManager layoutManager,
                               OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int offset = 1;

            if (layoutManager instanceof GridLayoutManager) {
                offset += ((GridLayoutManager) layoutManager).getSpanCount() - 1;
            }

            if (firstChild == RecyclerView.NO_POSITION) {
                return null;
            }

            View child = layoutManager.findViewByPosition(firstChild);

            float visibleWidth;

            // We should return the child if it's visible width
            // is greater than 0.5 of it's total width.
            // In a RTL configuration, we need to check the start point and in LTR the end point
            if (isRtlHorizontal) {
                visibleWidth = (float) (helper.getTotalSpace() - helper.getDecoratedStart(child))
                        / helper.getDecoratedMeasurement(child);
            } else {
                visibleWidth = (float) helper.getDecoratedEnd(child)
                        / helper.getDecoratedMeasurement(child);
            }

            // If we're at the end of the list, we shouldn't snap
            // to avoid having the last item not completely visible.
            boolean endOfList = ((LinearLayoutManager) layoutManager)
                    .findLastCompletelyVisibleItemPosition()
                    == layoutManager.getItemCount() - 1;

            if (visibleWidth > 0.5f && !endOfList) {
                return child;
            } else if (snapLastItem && endOfList) {
                return child;
            } else if (endOfList) {
                return null;
            } else {
                // If the child wasn't returned, we need to return
                // the next view close to the start.
                return layoutManager.findViewByPosition(firstChild + offset);
            }
        }

        return null;
    }

    private View findEndView(RecyclerView.LayoutManager layoutManager,
                             OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int lastChild = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            int offset = 1;

            if (layoutManager instanceof GridLayoutManager) {
                offset += ((GridLayoutManager) layoutManager).getSpanCount() - 1;
            }

            if (lastChild == RecyclerView.NO_POSITION) {
                return null;
            }

            View child = layoutManager.findViewByPosition(lastChild);

            float visibleWidth;

            if (isRtlHorizontal) {
                visibleWidth = (float) helper.getDecoratedEnd(child)
                        / helper.getDecoratedMeasurement(child);
            } else {
                visibleWidth = (float) (helper.getTotalSpace() - helper.getDecoratedStart(child))
                        / helper.getDecoratedMeasurement(child);
            }

            // If we're at the start of the list, we shouldn't snap
            // to avoid having the first item not completely visible.
            boolean startOfList = ((LinearLayoutManager) layoutManager)
                    .findFirstCompletelyVisibleItemPosition() == 0;

            if (visibleWidth > 0.5f && !startOfList) {
                return child;
            } else if (snapLastItem && startOfList) {
                return child;
            } else if (startOfList) {
                return null;
            } else {
                // If the child wasn't returned, we need to return the previous view
                return layoutManager.findViewByPosition(lastChild - offset);
            }
        }
        return null;
    }

    int getSnappedPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            if (gravity == Gravity.START || gravity == Gravity.TOP) {
                return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (gravity == Gravity.END || gravity == Gravity.BOTTOM) {
                return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
        }

        return RecyclerView.NO_POSITION;
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        if (verticalHelper == null) {
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return verticalHelper;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (horizontalHelper == null) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return horizontalHelper;
    }
}
