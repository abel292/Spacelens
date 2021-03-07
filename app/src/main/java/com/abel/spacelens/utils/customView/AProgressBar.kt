package com.abel.spacelens.utils.customView

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.airbnb.lottie.LottieAnimationView

class AProgressBar(context: Context, attrs: AttributeSet) : LottieAnimationView(context, attrs) {

    fun runAfterAnimation(task: () -> Unit) {
        removeAllAnimatorListeners()

        isEnabled = false

        if (isAnimating) {
            cancelAnimation()
            progress = 0.0f
        } else {
            playAnimation()
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    try {
                        task.invoke()
                    } catch (e: Exception) {
                        Log.e("ERROR ${this@AProgressBar.javaClass.name} ", e.toString())
                    }
                    postDelayed({ isEnabled = true }, 160L)
                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
        }
    }

}