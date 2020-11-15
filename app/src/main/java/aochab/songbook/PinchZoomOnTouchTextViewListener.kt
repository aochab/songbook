package aochab.songbook

import android.annotation.SuppressLint
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class PinchZoomOnTouchTextViewListener(private val textView: TextView) : View.OnTouchListener {
    private var ratio = 1.0
    private val STEP = 200
    private var baseRatio = 0.0
    private var baseDist = 0.0
    private val fontSize = 13

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
        if (motionEvent?.pointerCount == 2) {
            val action = motionEvent.action
            val pureAction = action and MotionEvent.ACTION_MASK
            if (pureAction == MotionEvent.ACTION_POINTER_DOWN) {
                baseDist = getDistance(motionEvent)
                baseRatio = ratio
            } else {
                val delta: Double = (getDistance(motionEvent) - baseDist) / STEP
                val multi = 2.0.pow(delta)
                ratio = min(1024.0, max(0.1, baseRatio * multi))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (ratio + fontSize).toFloat())
            }
        }
        return true
    }

    private fun getDistance(motionEvent: MotionEvent): Double {
        val dx = motionEvent.getX(0) - motionEvent.getX(1)
        val dy = motionEvent.getY(0) - motionEvent.getY(1)
        return sqrt(dx * dx + dy * dy).toDouble()
    }
}