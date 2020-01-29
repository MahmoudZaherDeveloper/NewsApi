package com.zaher.framework.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.airbnb.lottie.LottieAnimationView
import com.zaher.framework.R

/**
 * @author achmad.zaher
 * @date 3-Mar-19
 */
class LoadingView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        injectViews()
    }

    enum class State {
        LOADING, ERROR, EMPTY, UNAUTHORIZE
    }

    private var state: State = State.ERROR
    private var progressBar: LottieAnimationView? = null
    private var progressAnimation: LottieAnimationView? = null
    private var imageView: ImageView? = null
    private var backgroundImageView: ImageView? = null
    private var progressTitle: TextView? = null
    private var progressSubtitle: TextView? = null
    private var progressMessage: TextView? = null
    private var retryButton: Button? = null
    private var listener: OnRetryListener? = null

    private fun injectViews() {
        inflate(context, R.layout.loading_view, this)
        progressBar = findViewById(R.id.progressBar)
        progressAnimation = findViewById(R.id.progressAnimation)
        imageView = findViewById(R.id.errorImage)
        backgroundImageView = findViewById(R.id.backgroundImage)
        progressTitle = findViewById(R.id.progressTitle)
        progressSubtitle = findViewById(R.id.progressSubtitle)
        progressMessage = findViewById(R.id.progressMessage)
        retryButton = findViewById(R.id.retryButton)
        retryButton?.setOnClickListener { onClickRetry() }
        setDefaultRetrybutton()
        showLoading()
    }

    fun showLoading() {
        state = State.LOADING
        progressTitle?.text = null
        progressSubtitle?.text = context.getString(R.string.text_loading)
        progressMessage?.text = null
        showImage(0)
        showButton()
        showProgress()
    }

    fun showLoading(title: String, message: String, jsonName: String) {
        state = State.LOADING
        progressTitle?.text = null
        progressSubtitle?.text = title
        progressMessage?.text = message
        showImage(0)
        showButton()
        progressTitle?.visibility = View.GONE
        progressSubtitle?.visibility = View.VISIBLE
        progressMessage?.visibility = View.VISIBLE
        showProgressAnimation(jsonName)
    }

    private fun showProgressAnimation(jsonName: String) {
        progressAnimation?.setAnimation(jsonName)
        showProgressAnimation()
    }

    private fun showProgressAnimation() {
        progressBar?.visibility = View.GONE
        progressAnimation?.visibility = View.VISIBLE
        progressAnimation?.playAnimation()
    }

    private fun showProgress() {
        progressBar?.visibility = when (state) {
            State.LOADING -> View.VISIBLE
            else -> View.GONE
        }
        progressTitle?.visibility = when (state) {
            State.LOADING -> View.GONE
            else -> View.VISIBLE
        }
        progressMessage?.visibility = when (state) {
            State.LOADING -> View.GONE
            else -> View.VISIBLE
        }
        progressSubtitle?.visibility = when (state) {
            State.LOADING -> View.VISIBLE
            else -> View.GONE
        }
        if (state == State.LOADING)
            backgroundImageView?.visibility = View.GONE
        progressAnimation?.visibility = View.GONE
    }

    private fun showButton() {
        retryButton?.visibility = when (state) {
            State.LOADING -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun showImage(@DrawableRes icon: Int) {
        imageView?.visibility = when (icon) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
        imageView?.setImageResource(icon)
    }

    fun showEmpty(title: String, message: String, buttonText: String?, @DrawableRes icon: Int, showButton: Boolean) {
        state = State.EMPTY
        progressTitle?.text = title
        progressMessage?.text = message
        if (buttonText.isNullOrEmpty())
            setDefaultRetrybutton()
        else
            retryButton?.text = buttonText
        retryButton?.visibility = when (showButton) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        showImage(icon)
        showProgress()
    }

    fun showError(exception: Exception, title: String, message: String) {
        state = State.ERROR
        progressTitle?.text = title
        progressMessage?.text = message
        Log.e("ERROR_EXCEPTION", exception.toString())
        showImage(R.drawable.img_alert)
        showButton()
        showProgress()
    }

    private fun setDefaultRetrybutton() {
        retryButton?.text = context.getString(R.string.button_retry)
    }

    fun setOnRetryListener(listener: OnRetryListener) {
        this.listener = listener
    }

    private fun onClickRetry() {
        listener?.let {
            if (state == State.EMPTY)
                it.onClickEmpty()
            else {
                if (progressAnimation?.animation != null) {
                    showProgressAnimation()
                } else {
                    showLoading()
                }
                it.onRetry()
            }
        }
    }

    interface OnRetryListener {
        fun onRetry()

        fun onClickEmpty() {
            //Do something for empty state
        }
    }
}