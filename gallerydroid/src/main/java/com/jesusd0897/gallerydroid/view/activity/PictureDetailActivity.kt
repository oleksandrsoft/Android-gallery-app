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

package com.jesusd0897.gallerydroid.view.activity

import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.jesusd0897.gallerydroid.R
import com.jesusd0897.gallerydroid.databinding.GalleryDroidActivityPictureDetailBinding
import com.jesusd0897.gallerydroid.model.Picture
import com.jesusd0897.gallerydroid.util.EXTRA_USE_LABEL_TAG
import com.jesusd0897.gallerydroid.view.fragment.PicturePagerFragment
import com.jesusd0897.kutil.EXTRA_ITEMS_TAG
import com.jesusd0897.kutil.EXTRA_ITEM_TAG
import com.jesusd0897.kutil.EXTRA_POSITION_TAG
import com.jesusd0897.kutil.extension.gone
import com.jesusd0897.kutil.extension.visible
import com.jesusd0897.kutil.replaceFragment
import java.util.*

class PictureDetailActivity : AppCompatActivity() {

    private lateinit var binding: GalleryDroidActivityPictureDetailBinding

    private var pictures = ArrayList<Picture>()
    private var position = 0
    private var transformerId: Int? = null
    private var useLabels: Boolean? = null
    private var isCurrentImmersive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GalleryDroidActivityPictureDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        pictures = intent.getParcelableArrayListExtra(EXTRA_ITEMS_TAG)!!
        position = intent.getIntExtra(EXTRA_POSITION_TAG, 0)
        transformerId = intent.getIntExtra(EXTRA_ITEM_TAG, -1)
        useLabels = intent.getBooleanExtra(EXTRA_USE_LABEL_TAG, true)
        replaceFragment(
            fm = supportFragmentManager,
            fragment = PicturePagerFragment.newInstance(
                pictures = pictures,
                position = position,
                transformerId = transformerId,
            ),
            containerViewId = R.id.page_container
        )
        updateTitle()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) handleDecorVisibility()
    }

    fun handleDecorVisibility() {
        if (isCurrentImmersive) showSystemUI()
        else hideSystemUI()
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_FULLSCREEN
                or SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        binding.toolbarContainer.gone()
        isCurrentImmersive = true
    }

    private fun showSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        binding.toolbarContainer.visible()
        isCurrentImmersive = false
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is PicturePagerFragment) fragment.onPageChangeListener =
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int, positionOffset: Float, positionOffsetPixels: Int
                ) = Unit

                override fun onPageSelected(position: Int) {
                    this@PictureDetailActivity.position = position
                    updateTitle()
                }

                override fun onPageScrollStateChanged(state: Int) = Unit

            }
    }

    private fun updateTitle() {
        title = if (useLabels == true) (position + 1).toString() + "/" + pictures.size
        else ""
    }

}