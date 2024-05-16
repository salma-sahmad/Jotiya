import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ItemLikedBinding

class WishlistAdapter(private val wishlistItems: List<ItemModel>) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val binding = ItemLikedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(wishlistItems[position])
    }

    override fun getItemCount(): Int = wishlistItems.size

    inner class WishlistViewHolder(private val binding: ItemLikedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemModel) {
            binding.titleTxt.text = item.title
            binding.feeEachItem.text = item.price.toString()
            binding.totalEachItem.text = item.price.toString()
            Glide.with(binding.pic.context).load(item.picUrl).into(binding.pic)

            // Set initial state of the like button based on the isLiked field
            if (item.isLiked) {
                binding.liked.setImageResource(R.drawable.liked) // Liked icon
            } else {
                binding.liked.setImageResource(R.drawable.fav_icon) // Unliked icon
            }

            // Set click listener for the like button
            binding.liked.setOnClickListener {
                // Toggle the isLiked status of the item
                item.isLiked = !item.isLiked

                // Change the icon based on the updated isLiked status
                if (item.isLiked) {
                    binding.liked.setImageResource(R.drawable.liked) // Change to liked icon
                } else {
                    binding.liked.setImageResource(R.drawable.fav_icon) // Change to unliked icon
                }

                // Optionally, you can handle other actions here, like updating the database
            }
        }
    }
}
