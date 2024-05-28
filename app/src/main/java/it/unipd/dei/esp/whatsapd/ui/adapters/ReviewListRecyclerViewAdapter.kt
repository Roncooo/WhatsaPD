package it.unipd.dei.esp.whatsapd.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.databinding.SingleReviewBinding
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
	class ReviewViewHolder(private val singleReviewBinding: SingleReviewBinding) :
		RecyclerView.ViewHolder(singleReviewBinding.root) {
		
		fun bind(review: Review) {
			val reviewUsername: TextView = singleReviewBinding.reviewUsername
			reviewUsername.text = review.username
			
			val reviewRatingbar: RatingBar = singleReviewBinding.reviewRatingBar
			reviewRatingbar.rating = review.rating.toFloat()
			
			val reviewTextView: TextView = singleReviewBinding.reviewText
			reviewTextView.text = review.text
			
			val reviewDateTextView: TextView = singleReviewBinding.reviewDate
			reviewDateTextView.text = Converters.dateToString(review.date)
		}
		
		companion object {
			fun create(parent: ViewGroup): ReviewViewHolder {
				val singleReviewBinding: SingleReviewBinding = SingleReviewBinding.inflate(
					LayoutInflater.from(parent.context), parent, false
				)
				return ReviewViewHolder(singleReviewBinding)
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
