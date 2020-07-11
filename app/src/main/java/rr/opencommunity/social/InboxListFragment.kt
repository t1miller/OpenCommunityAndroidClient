package rr.opencommunity.social

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rr.opencommunity.R
import rr.opencommunity.settings.PreferenceUtils
import rr.opencommunity.social.ui.MyInboxRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [InboxListFragment.OnListFragmentInteractionListener] interface.
 */
class InboxListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    private lateinit var viewModel: SocialViewModel

    private lateinit var inboxAdapter: MyInboxRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(SocialViewModel::class.java)
        viewModel._inboxRequests.observe(this, Observer {
            it?.let {
                inboxAdapter.mValues = it
                inboxAdapter.notifyDataSetChanged()
            }
        })

        viewModel.fetchInbox(PreferenceUtils.getUsername(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inbox_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            inboxAdapter =  MyInboxRecyclerViewAdapter(null, listener)
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = inboxAdapter
            }
        }

        viewModel.fetchInbox(PreferenceUtils.getUsername(requireContext()))
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(inboxResponse: MyInboxRecyclerViewAdapter.InboxClicked)
        fun onApproveClicked(inboxResponse: MyInboxRecyclerViewAdapter.InboxClicked)
        fun onDenyClicked(inboxResponse: MyInboxRecyclerViewAdapter.InboxClicked)
    }

    companion object {

        @JvmStatic
        fun newInstance() = InboxListFragment()
    }
}
