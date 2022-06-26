package com.aperfectpolygon.extentions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


/**
 * Extension function to creating normalFont in snackbar
 */
val Snackbar.normalFont
	get() = (view.findViewById(R.id.snackbar_text) as TextView).apply { typeface = createNormal }

/** Extension function to creating boldFont in snackbar*/
val Snackbar.boldFont
	get() = (view.findViewById(R.id.snackbar_text) as TextView).apply { typeface = createBold }

val Snackbar.actionTypeface
	get() = (view.findViewById(R.id.snackbar_action) as TextView).apply {
		textSize = 12f
		typeface = createNormal
	}

/** Extension function to changing textSize in snackbar*/
fun Snackbar.textSize(txtSize: Float) {
	(view.findViewById<TextView>(R.id.snackbar_text)).apply { textSize = txtSize }
}

/** Extension function to changing gravity in snackbar*/
fun Snackbar.gravity(gravity: Int) {
	(view.findViewById<TextView>(R.id.snackbar_text)).apply { this.gravity = gravity }
}

val View.makeGone: Boolean
	get() {
		isGone = true
		return isGone
	}
val View.makeInvisible: Boolean
	get() {
		isInvisible = true
		return isInvisible
	}
val View.makeVisible: Boolean
	get() {
		isVisible = true
		return isVisible
	}

val View.makeVisibleWithFadeAnimation
	get() = apply {
		alpha = 0f
		translationX = width.toFloat()
		makeVisible
		animate().apply {
			duration = 500
			translationX = 0f
			setListener(
				object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator?) {
						super.onAnimationEnd(animation)
						clearAnimation()
					}
				}
			)
			alpha(1f)
			start()
		}
	}

val View.makeGoneWithAnimation
	get() = apply {
		alpha = 1f
		translationX = 0f
		animate().apply {
			duration = 500
			translationX = width.toFloat()
			setListener(
				object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator?) {
						super.onAnimationEnd(animation)
						makeGone
						clearAnimation()
					}
				}
			)
			alpha(0f)
			start()
		}
	}

@Suppress("DEPRECATION", "EXTENSION_SHADOWED_BY_MEMBER")
fun Context.getColor(@ColorRes resId: Int): Int = when {
	Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> resources.getColor(resId, null)
	else -> resources.getColor(resId)
}

inline fun View.showIf(crossinline condition: (View) -> Boolean): Int {
	if (condition(this)) makeVisible else makeGone
	return visibility
}

inline fun View.hideIf(crossinline condition: (View) -> Boolean): Int {
	if (condition(this)) makeGone else makeVisible
	return visibility
}

fun ViewPager2.reduceDragSensitivity() {
	val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
	recyclerViewField.isAccessible = true
	val recyclerView = recyclerViewField.get(this) as RecyclerView
	
	val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
	touchSlopField.isAccessible = true
	val touchSlop = touchSlopField.get(recyclerView) as Int
	touchSlopField.set(recyclerView, touchSlop * 8)       // "8" was obtained experimentally
}

fun List<String>.attachTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2) =
	TabLayoutMediator(tabLayout, viewPager) { tab, position ->
		when {
			position < size -> this[position]
			else -> ""
		}.also { tab.text = it }
	}.attach()