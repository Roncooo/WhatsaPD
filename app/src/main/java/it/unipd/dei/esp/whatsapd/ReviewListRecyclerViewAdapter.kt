package it.unipd.dei.esp.whatsapd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ReviewListRecyclerViewAdapter(comparator: DiffUtil.ItemCallback<Review> = ALPHABETICAL_COMPARATOR) :
    ListAdapter<Review, ReviewListRecyclerViewAdapter.ReviewViewHolder>(comparator) {

    // ViewHolder per contenere le viste degli elementi
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reviewUsername: TextView = itemView.findViewById(R.id.review_username)
        private val reviewRatingbar: RatingBar = itemView.findViewById(R.id.review_rating_bar)
        private val reviewTextView: TextView = itemView.findViewById(R.id.review_text)
        private val reviewDateTextView: TextView = itemView.findViewById(R.id.review_date)

        fun bind(review: Review) {
            reviewUsername.text = review.username
            reviewRatingbar.rating = review.rating.toFloat()
            reviewTextView.text = review.text
            reviewDateTextView.text = Converters().dateToTimestamp(review.date)
        }

        companion object {
            fun create(parent: ViewGroup): ReviewViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.single_review, parent, false)
                return ReviewViewHolder(view)
            }
        }
    }

    // Crea nuovi ViewHolder (invocato dal layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val ALPHABETICAL_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}


