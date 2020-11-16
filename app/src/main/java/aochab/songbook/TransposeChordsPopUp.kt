package aochab.songbook

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*

class TransposeChordsPopUp {

    @SuppressLint("ClickableViewAccessibility")
    fun showPopUp(view: View) {
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.window_chords_transposition, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        val focusable = true

        val transposeChordsPopUp = PopupWindow(popupView, width, height, focusable)
        transposeChordsPopUp.animationStyle = R.style.Animation
        val scrollView = popupView.findViewById<ScrollView>(R.id.transpose_chords_scroll_view)

        transposeChordsPopUp.showAtLocation(view, Gravity.CENTER, 0, 0)
        scrollView.post {
            val scrollViewCenterY = (scrollView.top + scrollView.bottom) / 2
            scrollView.scrollTo(0, scrollViewCenterY)
        }

        popupView.setOnTouchListener { v, event -> //Close the window when clicked
            transposeChordsPopUp.dismiss()
            true
        }
    }
}