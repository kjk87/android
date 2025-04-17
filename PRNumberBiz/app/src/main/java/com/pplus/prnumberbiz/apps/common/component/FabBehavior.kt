package com.pplus.prnumberbiz.apps.common.component

import android.content.Context
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import com.pplus.prnumberbiz.R

/**
 * Copyright (C) 2017 Tetsuya Masuda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class FabBehavior<V : View>(context: Context?, attrs: AttributeSet? = null
) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<V>(context, attrs) {

  private val isScrollOut: Boolean
  private var parentHeight = 0

  init {
    if (attrs == null) {
      isScrollOut = false
    } else {
      val fabBehaviorParams = context?.obtainStyledAttributes(attrs,
          R.styleable.FabBehaviorParam)!!
      isScrollOut = fabBehaviorParams.getBoolean(R.styleable.FabBehaviorParam_isScrollOut, false)
    }
  }

  override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V,
                             layoutDirection: Int): Boolean {
    parentHeight = parent.height
    return super.onLayoutChild(parent, child, layoutDirection)
  }

  override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V,
                               dependency: View): Boolean =
      super.layoutDependsOn(parent, child, dependency) || hasBottomNavigationBehavior(dependency)

  override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V,
                                      dependency: View): Boolean {
    if (dependency is AppBarLayout) {
      super.onDependentViewChanged(parent, child, dependency)
    } else if (hasBottomNavigationBehavior(dependency)) {
      updateFabPosition(dependency, child)
    } else {
      super.onDependentViewChanged(parent, child, dependency)
    }
    return false
  }

  private fun hasBottomNavigationBehavior(dependency: View?): Boolean =
      BottomNavigationBehavior.from(dependency) != null

  private fun updateFabPosition(dependency: View, child: V) {
    val top = dependency.y
    val layoutParams = child.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
    val rate = if (isScrollOut) (parentHeight - top) / dependency.height else 1f
    child.y = top - (child.height + layoutParams.bottomMargin) * rate
  }
}
