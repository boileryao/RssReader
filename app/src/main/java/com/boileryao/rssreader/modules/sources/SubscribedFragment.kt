package com.boileryao.rssreader.modules.sources

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boileryao.rssreader.R
import com.boileryao.rssreader.bean.Article
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.common.NetworkTask
import com.boileryao.rssreader.common.OnResultListener
import com.boileryao.rssreader.common.database.WebsitesDbHelper
import com.boileryao.rssreader.common.recyclerview.ClickListener
import com.boileryao.rssreader.common.recyclerview.RecyclerTouchListener
import com.boileryao.rssreader.common.widgets.PopupMenu
import com.boileryao.rssreader.common.widgets.SourceInfoDialog

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the OnWebsiteListInteraction
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class SubscribedFragment : Fragment() {
    private var listener: OnWebsiteListInteraction? = null
    private lateinit var adapter: WebsiteRecyclerViewAdapter
    private lateinit var websites: List<Website>
    private val map = mutableMapOf<Website, List<Article>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        websites = WebsitesDbHelper.getInstance(context).all()
        adapter = WebsiteRecyclerViewAdapter(mutableMapOf())

        syncWithDatabase()

        // request more info using network
        NetworkTask().execute(websites, object : OnResultListener {
            override fun action(data: Map<Website, List<Article>>?) {
                adapter.load(data)
                data?.keys?.forEach {
                    // sync requested data with db
                    WebsitesDbHelper.getInstance(this@SubscribedFragment.context).update(it)
                }
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_website_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.context
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = adapter
            view.addOnItemTouchListener(RecyclerTouchListener(activity, view,
                    object : ClickListener {
                        override fun onClick(view: View?, position: Int) {
                            listener?.onWebsiteListFragmentInteraction(adapter.getItem(position))
                        }

                        override fun onLongClick(view: View?, position: Int) {
                            val website = adapter.getItem(position).first
                            PopupMenu(this@SubscribedFragment.context).addEntries("编辑" to {
                                SourceInfoDialog.show(this@SubscribedFragment.activity, website)
                            }).show()
                        }
                    }))
        }
        return view
    }

    fun syncWithDatabase() {
        // compose data from db and show instantly
        val emptyList = listOf<Article>()
        websites.forEach { map[it] = emptyList }
        adapter.load(map)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnWebsiteListInteraction) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnWebsiteListInteraction")
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
     */
    interface OnWebsiteListInteraction {
        fun onWebsiteListFragmentInteraction(item: Pair<Website, List<Article>?>)
    }

    companion object {
        var instances = mutableListOf<SubscribedFragment>()

        fun newInstance(): SubscribedFragment {
            val fragment = SubscribedFragment()
            instances.add(fragment)
            return fragment
        }

        fun syncDbData() {
            instances.forEach {

            }
        }
    }
}
