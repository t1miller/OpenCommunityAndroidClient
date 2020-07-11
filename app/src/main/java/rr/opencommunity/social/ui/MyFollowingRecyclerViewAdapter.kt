package rr.opencommunity.social.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_inbox.view.*
import rr.opencommunity.R
import rr.opencommunity.http.FollowingResponse
import rr.opencommunity.social.FollowingListFragment

/**
 * [RecyclerView.Adapter] that can display a [String] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyFollowingRecyclerViewAdapter(
    var mValues: FollowingResponse?,
    private val mListener: FollowingListFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyFollowingRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as FollowingClicked
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_inbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mValues?.let {
            val item = it.following[position]
            val following = FollowingClicked(id = it.id, personClicked = item)

            holder.mContentView.text = item

            with(holder.mView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
            // this has no action in following list
            holder.mAcceptButton.visibility = View.GONE

            holder.mDenyButton.setOnClickListener {
                mListener?.removeFollowingClicked(following)
            }
        }
    }

    override fun getItemCount(): Int = mValues?.following?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content
        val mAcceptButton: Button = mView.addFollowerButton
        val mDenyButton: Button = mView.denyFollowerButton

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

    data class FollowingClicked(
        var personClicked: String,
        var id: Int
    )
}
