import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.capstone.trashcan.R

class ViewPagerAdapter(private val context: Context) : PagerAdapter() {

    private val sliderAllImages = intArrayOf(
        R.drawable.onboarding_page_1,
        R.drawable.onboarding_page_2,
        R.drawable.onboarding_page_3
    )

    private val sliderAllTitle = intArrayOf(
        R.string.screen1,
        R.string.screen2,
        R.string.screen3
    )

    private val sliderAllDesc = intArrayOf(
        R.string.screen1desc,
        R.string.screen2desc,
        R.string.screen3desc
    )

    override fun getCount(): Int {
        return sliderAllTitle.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.slider_screen, container, false)

        val sliderImage = view.findViewById<ImageView>(R.id.sliderImage)
        val sliderTitle = view.findViewById<TextView>(R.id.sliderTitle)
        val sliderDesc = view.findViewById<TextView>(R.id.sliderDesc)

        Glide.with(context)
            .load(sliderAllImages[position])
            .into(sliderImage)

        sliderTitle.setText(sliderAllTitle[position])
        sliderDesc.setText(sliderAllDesc[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
