package rr.opencommunity.social

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_following_list.*
import rr.opencommunity.R
import rr.opencommunity.settings.PreferenceUtils
import rr.opencommunity.social.ui.MyFollowingRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FollowingListFragment.OnListFragmentInteractionListener] interface.
 */
class FollowingListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    private lateinit var viewModel: SocialViewModel

    private lateinit var followingAdapter: MyFollowingRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(SocialViewModel::class.java)
        viewModel._following.observe(this, Observer {
            it?.let {
                followingAdapter.mValues = it
                followingAdapter.notifyDataSetChanged()
            }
        })

        viewModel.fetchFollowing(PreferenceUtils.getUsername(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_following_list, container, false)

        followingAdapter = MyFollowingRecyclerViewAdapter(null, listener)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        with(recyclerView){
            adapter = followingAdapter
        }

        val requestToFollowButton = view.findViewById<Button>(R.id.requestToFollow)
        requestToFollowButton.setOnClickListener {
            val username = PreferenceUtils.getPreference(requireContext(),"inboxApiUsername","")
            viewModel.addInboxRequest(nameSearching.text.toString(), username)
        }
        viewModel.fetchFollowing(PreferenceUtils.getUsername(requireContext()))

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
        fun onListFragmentInteraction(followingResponse: MyFollowingRecyclerViewAdapter.FollowingClicked)
        fun removeFollowingClicked(followingResponse: MyFollowingRecyclerViewAdapter.FollowingClicked)
    }

    companion object {

        @JvmStatic
        fun newInstance() = FollowingListFragment()
    }
}
