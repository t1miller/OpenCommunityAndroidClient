package rr.opencommunity.social.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_inbox.view.*
import rr.opencommunity.R
import rr.opencommunity.http.InboxResponse
import rr.opencommunity.social.InboxListFragment.OnListFragmentInteractionListener

/**
 * [RecyclerView.Adapter] that can display a [String] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyInboxRecyclerViewAdapter(
    var mValues: InboxResponse?,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyInboxRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as InboxClicked
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
            val item = it.inbox[position]
            val inboxClicked = InboxClicked(inboxId = it.id, personClicked = item)

            holder.mContentView.text = item

            with(holder.mView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
            holder.mAcceptButton.setOnClickListener {
                mListener?.onApproveClicked(inboxClicked)
            }
            holder.mDenyButton.setOnClickListener {
                mListener?.onDenyClicked(inboxClicked)
            }
        }
    }

    override fun getItemCount(): Int = mValues?.inbox?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content
        val mAcceptButton: Button = mView.addFollowerButton
        val mDenyButton: Button = mView.denyFollowerButton

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

    data class InboxClicked(
        var personClicked: String,
        var inboxId: Int
    )
}
