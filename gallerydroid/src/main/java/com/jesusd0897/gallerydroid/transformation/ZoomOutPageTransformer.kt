/*
 * Copyright (c) 2021.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jesusd0897.gallerydroid.transformation

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max

private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

class ZoomOutPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        when {
            position < -1 -> view.alpha = 0f
            position <= 1 -> {
                val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                val horizontalMargin = pageWidth * (1 - scaleFactor) / 2
                if (position < 0) view.translationX = horizontalMargin - verticalMargin / 2
                else view.translationX = -horizontalMargin + verticalMargin / 2
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.alpha =
                    MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            }
            else -> view.alpha = 0f
        }
    }
}