package rr.opencommunity.social

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import rr.opencommunity.R
import rr.opencommunity.settings.PreferenceUtils
import rr.opencommunity.social.ui.MyFollowingRecyclerViewAdapter
import rr.opencommunity.social.ui.MyInboxRecyclerViewAdapter
import rr.opencommunity.social.ui.SectionsPagerAdapter
import timber.log.Timber

class UserActivity : AppCompatActivity(),
    FollowingListFragment.OnListFragmentInteractionListener,
    InboxListFragment.OnListFragmentInteractionListener {

    lateinit var socialViewModel: SocialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        socialViewModel = ViewModelProviders.of(this).get(SocialViewModel::class.java)
    }

    override fun onListFragmentInteraction(followingResponse: MyFollowingRecyclerViewAdapter.FollowingClicked) {
    }

    override fun removeFollowingClicked(followingResponse: MyFollowingRecyclerViewAdapter.FollowingClicked) {
        // remove user from your following list
        Timber.d("remove user %s from following", followingResponse.personClicked)
        socialViewModel.removeFollowing(followingResponse.id, PreferenceUtils.getUsername(this), followingResponse.personClicked)
    }

    override fun onListFragmentInteraction(inboxResponse: MyInboxRecyclerViewAdapter.InboxClicked) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onApproveClicked(inboxResponse: MyInboxRecyclerViewAdapter.InboxClicked) {
        Timber.d("add follower %s clicked", inboxResponse.personClicked)
        // todo add yourself to others following
        socialViewModel.updateFollowing(inboxResponse.personClicked, PreferenceUtils.getUsername(this))
    }

    override fun onDenyClicked(inboxResponse: MyInboxRecyclerViewAdapter.InboxClicked) {
        Timber.d("deny follower %s clicked", inboxResponse.personClicked)
        socialViewModel.removeInboxRequest(inboxResponse.inboxId, PreferenceUtils.getUsername(this), inboxResponse.personClicked)
    }
}