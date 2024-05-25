package it.unipd.dei.esp.whatsapd.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.repository.database.Converters
import it.unipd.dei.esp.whatsapd.repository.database.Review

/**
 * Adapter for displaying a list of [Review]s in a [RecyclerView].
 */
class ReviewListRecyclerViewAdapter(comparator: DiffUtil.ItemCallback<Review> = REVIEW_COMPARATOR) :
	ListAdapter<Review, ReviewListRecyclerViewAdapter.ReviewViewHolder>(comparator) {
	
	/**
	 * [RecyclerView.ViewHolder] for holding the view of individual [Review] items.
	 */
	class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		
		fun bind(review: Review) {
			val reviewUsername: TextView = itemView.findViewById(R.id.review_username)
			reviewUsername.text = review.username
			
			val reviewRatingbar: RatingBar = itemView.findViewById(R.id.review_rating_bar)
			reviewRatingbar.rating = review.rating.toFloat()
			
			val reviewTextView: TextView = itemView.findViewById(R.id.review_text)
			reviewTextView.text = review.text
			
			val reviewDateTextView: TextView = itemView.findViewById(R.id.review_date)
			reviewDateTextView.text = Converters.dateToString(review.date)
		}
		
		companion object {
			fun create(parent: ViewGroup): ReviewViewHolder {
				val view: View = LayoutInflater.from(parent.context)
					.inflate(R.layout.single_review, parent, false)
				return ReviewViewHolder(view)
			}
		}
	}
	
	/**
	 * Create new [RecyclerView.ViewHolder] instances (called by the layout manager)
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
		return ReviewViewHolder.create(parent)
	}
	
	override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
		val current = getItem(position)
		holder.bind(current)
	}
	
	companion object {
		/**
		 * Comparator that spots the difference between two [Review] objects.
		 */
		private val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
			/**
			 * Checks if two [Review] items are the same.
			 */
			override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
				return oldItem.id == newItem.id
			}
			
			/**
			 * Checks if the contents of two [Review] items are the same.
			 */
			override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
				return oldItem.username == newItem.username && oldItem.poi == newItem.poi && oldItem.rating == newItem.rating && oldItem.text == newItem.text && oldItem.date == newItem.date
			}
		}
	}
}
