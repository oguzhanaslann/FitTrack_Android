package com.oguzhanaslann.commonui

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

fun ViewPager2.nextOrStayOnLast() {
    currentItem = minOf(currentItem + 1, adapter?.itemCount ?: 0)
}

val ViewPager2.isLastPage: Boolean
    get() = currentItem == (adapter?.itemCount ?: 0) - 1

fun getUriOfDrawable(context: Context?, @DrawableRes drawableId: Int): Uri? = context?.run {
    Uri.parse("android.resource://${packageName}/$drawableId")
}

//@ColorInt
//fun Context.themeColor(
//    @AttrRes themeAttrId: Int
//): Int {
//    return obtainStyledAttributes(
//        intArrayOf(themeAttrId)
//    ).use {
//        it.getColor(0, Color.MAGENTA)
//    }
//}

@ColorInt
fun Context.themeColor(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return ContextCompat.getColor(this, typedValue.resourceId)
}

fun Fragment.openInBrowser(pageUrl: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl))
    startActivity(browserIntent)
}

fun Activity.openInBrowser(pageUrl: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl))
    startActivity(browserIntent)
}

// if the package name is found, then the app is installed
// else the call throws the exception PackageManager.NameNotFoundException
fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        if (Build.VERSION.SDK_INT >= 33) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

inline fun View.doOnceAfterCompleteDraw(
    crossinline block: (View) -> Unit
) {
    val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            block(this@doOnceAfterCompleteDraw)
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }
    viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
}

inline fun View.doOnceOnHasFocus(
    crossinline block: () -> Unit
) {
    val globalLayoutListener = object : ViewTreeObserver.OnWindowFocusChangeListener {
        override fun onWindowFocusChanged(hasFocus: Boolean) {
            if (hasFocus) {
                block()
                viewTreeObserver.removeOnWindowFocusChangeListener(this)
            }
        }

    }
    viewTreeObserver.addOnWindowFocusChangeListener(globalLayoutListener)
}

inline fun TabLayout.doOnTabSelected(crossinline onTabSelected: (TabLayout.Tab?) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            onTabSelected(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    })
}

fun TabLayout.setTabMargin(marginOffsetPx: Int) {
    val tabStrip = getChildAt(0)
    if (tabStrip is ViewGroup) {
        for (i in 0 until tabStrip.childCount) {
            val tabView = tabStrip.getChildAt(i)
            val marginLayoutParams = tabView.layoutParams as? ViewGroup.MarginLayoutParams
            marginLayoutParams?.let {
                it.leftMargin = marginOffsetPx
                it.rightMargin = marginOffsetPx
            }
        }
        requestLayout()
    }
}

fun View.hide() {
    if (this.visibility != View.INVISIBLE) this.visibility = View.INVISIBLE
}

fun View.gone() {
    if (this.visibility != View.GONE) this.visibility = View.GONE
}

fun View.show() {
    if (this.visibility != View.VISIBLE) this.visibility = View.VISIBLE
}

fun RecyclerView.verticalLinearLayoutManaged(
    reversed: Boolean = false
) {
    this.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, reversed)
}

fun RecyclerView.horizontalLinearLayoutManaged(
    reversed: Boolean = false
) {
    this.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, reversed)
}

fun RecyclerView.gridLinearLayoutManaged(
    spanCount: Int,
    reversed: Boolean = false
) {
    this.layoutManager =
        GridLayoutManager(this.context, spanCount, LinearLayoutManager.VERTICAL, reversed)
}

val View.isNotVisible
    get() = !this.isVisible

var View.isHidden: Boolean
    get() = this.visibility == View.INVISIBLE
    set(value) {
        if (value) this.visibility = View.INVISIBLE else View.VISIBLE
    }

var ViewPager2.isSwipable: Boolean
    get() = isUserInputEnabled
    set(value) {
        this.isUserInputEnabled = value
    }

inline fun ViewPager.doOnPageSelected(crossinline block: (Int) -> Unit) {

    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) = Unit

        override fun onPageSelected(position: Int) = block(position)

        override fun onPageScrollStateChanged(state: Int) = Unit
    })
}

inline fun ViewPager2.doOnPageSelected(crossinline block: (Int) -> Unit) {

    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            block(position)
        }
    })
}

fun SpannableString.setClickableSpan(
    onClick: (View) -> Unit,
    spanRange: Pair<Int, Int>,
    spanFlag: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
) {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            onClick(p0)
        }
    }

    setSpan(clickableSpan, spanRange.first, spanRange.second + 1, spanFlag)
}

fun EditText.clear() {
    this.setText("")
}

fun Context?.isPermissionGranted(permission: String): Boolean {
    return this?.let {
        ContextCompat.checkSelfPermission(
            it,
            permission
        )
    } == PackageManager.PERMISSION_GRANTED
}

fun <T> List<T>.separatedByComma(): String {
    return this.joinToString(", ")
}

fun EditText.clear(applyOnlyWhenNeeded: Boolean = false) {
    val emptyText = text.isNullOrEmpty()
    val clearText = !applyOnlyWhenNeeded || !emptyText
    if (clearText) this.setText("")
}

inline fun EditText.doOnDoneAction(crossinline action: () -> Unit) {
    this.setOnEditorActionListener { textView, i, keyEvent ->
        (i == EditorInfo.IME_ACTION_DONE).also {
            action()
        }
    }
}

fun Fragment.launchOnViewLifecycleOwnerScope(
    block: suspend (lifecycleOwner: LifecycleOwner, scope: CoroutineScope) -> Unit
) {

    viewLifecycleOwner.lifecycleScope.launch {
        block(viewLifecycleOwner, this)
    }
}

val Int.dp: Int
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return (density * this).toInt()
    }

val Float.dp: Float
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return (density * this)
    }

val Int.px: Int
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return (this / density).toInt()
    }

val Float.px: Float
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return (this / density)
    }

fun View.removeClickListener() {
    this.setOnClickListener(null)
}

//fun Fragment.createDialog(
//    @LayoutRes layoutRes: Int,
//    root: ViewGroup? = null,
//    initBlock: (View, Dialog) -> Unit,
//    onShow: (Dialog) -> Unit = {}
//): AlertDialog? {
//    return context?.createDialog(
//        inflater = layoutInflater,
//        layoutRes = layoutRes,
//        root = root,
//        initBlock = initBlock,
//        onShow = onShow
//    )
//}
//
//fun Activity.createDialog(
//    @LayoutRes layoutRes: Int,
//    root: ViewGroup? = null,
//    initBlock: (View, Dialog) -> Unit,
//    onShow: (Dialog) -> Unit = {}
//): AlertDialog {
//    return createDialog(
//        inflater = layoutInflater,
//        layoutRes = layoutRes,
//        root = root,
//        initBlock = initBlock,
//        onShow = onShow
//    )
//}

fun Button.setActivationBy(condition: Boolean) {
    isEnabled = condition
    isClickable = condition
}

val Context?.isNightMode: Boolean
    get() {
        if (this == null) return false
        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return uiMode == Configuration.UI_MODE_NIGHT_YES

    }

/**
 * Search this view and any children for a [ColorDrawable] `background` and return it's `color`,
 * else return `colorSurface`.
 */
@ColorInt
fun View.descendantBackgroundColor(): Int {
    val bg = backgroundColor()
    if (bg != null) {
        return bg
    } else if (this is ViewGroup) {
        forEach {
            val childBg = descendantBackgroundColorOrNull()
            if (childBg != null) {
                return childBg
            }
        }
    }
    return context.themeColor(android.R.attr.colorBackground)
}

@ColorInt
private fun View.descendantBackgroundColorOrNull(): Int? {
    val bg = backgroundColor()
    if (bg != null) {
        return bg
    } else if (this is ViewGroup) {
        forEach {
            val childBg = backgroundColor()
            if (childBg != null) {
                return childBg
            }
        }
    }
    return null
}

/**
 * Check if this [View]'s `background` is a [ColorDrawable] and if so, return it's `color`,
 * otherwise `null`.
 */
@ColorInt
fun View.backgroundColor(): Int? {
    val bg = background
    if (bg is ColorDrawable) {
        return bg.color
    }
    return null
}

/**
 * Walk up from a [View] looking for an ancestor with a given `id`.
 */
fun View.findAncestorById(@IdRes ancestorId: Int): View {
    return when {
        id == ancestorId -> this
        parent is View -> (parent as View).findAncestorById(ancestorId)
        else -> throw IllegalArgumentException("$ancestorId not a valid ancestor")
    }
}

/**
 * Potentially animate showing a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot of the view, and animate this in, only changing the visibility (and
 * thus layout) when the animation completes.
 */
fun BottomNavigationView.show() {
    if (visibility == VISIBLE) return

    val parent = parent as ViewGroup
    // View needs to be laid out to create a snapshot & know position to animate. If view isn't
    // laid out yet, need to do this manually.
    if (!isLaidOut) {
        measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST)
        )
        layout(parent.left, parent.height - measuredHeight, parent.right, parent.height)
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    drawable.setBounds(left, parent.height, right, parent.height + height)
    parent.overlay.add(drawable)
    ValueAnimator.ofInt(parent.height, top).apply {
        startDelay = 100L
        duration = 300L
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.linear_out_slow_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
            visibility = VISIBLE
        }
        start()
    }
}

/**
 * Potentially animate hiding a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot, instantly hide the view (so content lays out to fill), then animate
 * out the snapshot.
 */
fun BottomNavigationView.hide() {
    if (visibility == GONE) return

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    val parent = parent as ViewGroup
    drawable.setBounds(left, top, right, bottom)
    parent.overlay.add(drawable)
    visibility = GONE
    ValueAnimator.ofInt(top, parent.height).apply {
        startDelay = 100L
        duration = 200L
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.fast_out_linear_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
        }
        start()
    }
}

fun showCustomSnackbar(
    context: Context,
    container: View,
    durationMillis: Int = Snackbar.LENGTH_SHORT,
    @LayoutRes layout: Int,
    paddingTop: Int = 0,
    paddingBottom: Int = 0,
    paddingStart: Int = 0,
    paddingEnd: Int = 0,
    initViews: (layout: View, Snackbar) -> Unit
): Snackbar {
    val snackbar = Snackbar.make(context, container, "", durationMillis)
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    snackbarLayout.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
    val customView = LayoutInflater.from(context).inflate(layout, null)
    initViews(customView, snackbar)
    snackbarLayout.addView(customView, 0)
    snackbar.show()
    return snackbar
}

fun showInfoPopUpDialog(
    context: Context,
    message: String?,
    durationMillis: Int = Snackbar.LENGTH_SHORT,
    container: View,
    onDialogDismissed: () -> Unit = {},
) {

    showCustomSnackbar(
        context = context,
        container = container,
        durationMillis = durationMillis,
        layout = R.layout.custom_snackbar_layout,
        paddingTop = 0,
        paddingBottom = 16.dp,
        paddingStart = 0,
        paddingEnd = 0,
        initViews = { view: View, snackbar: Snackbar ->
            view.apply {
                findViewById<TextView>(R.id.inform_message).text = message
                findViewById<View>(R.id.cross).setOnClickListener {
                    snackbar.dismiss()
                }
            }
        }
    ).addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDialogDismissed()
        }

        override fun onShown(sb: Snackbar?) {
            super.onShown(sb)
        }
    })
}

fun showSuccessSnackbar(
    context: Context,
    message: String?,
    onDialogDismissed: () -> Unit = {},
    durationMillis: Int = Snackbar.LENGTH_SHORT,
    container: View
) {
    showCustomSnackbar(
        context = context,
        container = container,
        durationMillis = durationMillis,
        layout = R.layout.custom_snackbar_layout,
        paddingTop = 0,
        paddingBottom = 16.dp,
        paddingStart = 0,
        paddingEnd = 0,
        initViews = { view: View, snackbar: Snackbar ->
            view.apply {
                findViewById<TextView>(R.id.inform_message).text = message
                findViewById<View>(R.id.cross).setOnClickListener {
                    snackbar.dismiss()
                }
            }
        }
    ).addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDialogDismissed()
        }

        override fun onShown(sb: Snackbar?) {
            super.onShown(sb)
        }
    })
}

fun showSuccessSnackbar(
    container: View,
    message: String?,
    onDialogDismissed: () -> Unit = {},
    durationMillis: Int = Snackbar.LENGTH_SHORT
) {
    showSuccessSnackbar(
        context = container.context,
        message = message,
        onDialogDismissed = onDialogDismissed,
        durationMillis = durationMillis,
        container = container
    )
}

fun showErrorSnackbar(
    context: Context,
    message: String?,
    container: View,
    onDialogDismissed: () -> Unit = {},
    durationMillis: Int = Snackbar.LENGTH_SHORT
) {
    showCustomSnackbar(
        context = context,
        container = container,
        durationMillis = durationMillis,
        layout = R.layout.custom_error_snackbar_layout,
        paddingTop = 0,
        paddingBottom = 16.dp,
        paddingStart = 0,
        paddingEnd = 0,
        initViews = { view: View, snackbar: Snackbar ->
            view.apply {
                findViewById<TextView>(R.id.inform_message).text = message
                findViewById<View>(R.id.cross).setOnClickListener {
                    snackbar.dismiss()
                }
            }
        }
    ).addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDialogDismissed()
        }

        override fun onShown(sb: Snackbar?) {
            super.onShown(sb)
        }
    })
}

fun showErrorSnackbar(
    message: String?,
    container: View,
    onDialogDismissed: () -> Unit = {},
    durationMillis: Int = Snackbar.LENGTH_SHORT
) {
    showErrorSnackbar(
        message = message,
        container = container,
        context = container.context,
        onDialogDismissed = onDialogDismissed,
        durationMillis = durationMillis
    )
}

fun Context.showSnackbar(
    container: View,
    message: String,
    durationMillis: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar.make(
        container,
        message,
        durationMillis
    ).show()
}

val Fragment.navController
    get() = findNavController()

fun TextView.setSpan(
    hint: String,
    spannedText: String,
    onSpanClicked: (View) -> Unit
) {

    fun String.indexRangeOf(sub: String): Pair<Int, Int>? {
        val start = indexOf(sub)
        return when (start != -1) {
            true -> Pair(start, start + sub.length - 1)
            false -> null
        }
    }

    val spannableString = SpannableString(hint)
    val spanRange = hint.indexRangeOf(spannedText)

    if (spanRange != null) {
        spannableString.setClickableSpan(
            spanRange = spanRange,
            onClick = onSpanClicked,
            spanFlag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    movementMethod = LinkMovementMethod.getInstance()
    text = spannableString
}


fun Uri.copyTo(file : File,context: Context) {
    val inputStream = context.contentResolver?.openInputStream(this)
    val outputStream = file.outputStream()
    inputStream?.copyTo(outputStream)
    inputStream?.close()
}

fun File.createIfNotExist() {
    if (!exists()) {
        createNewFile()
    }
}

fun File.toUrlString(): String {
    return toURI().toURL().toString()
}
