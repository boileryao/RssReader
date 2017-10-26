package com.boileryao.rssreader.modules.sources

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boileryao.rssreader.R
import com.boileryao.rssreader.bean.Article
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.common.Logger
import com.boileryao.rssreader.common.NetworkTask
import com.boileryao.rssreader.common.OnResultListener
import com.boileryao.rssreader.common.database.WebsitesDbHelper
import com.boileryao.rssreader.common.recyclerview.ClickListener
import com.boileryao.rssreader.common.recyclerview.RecyclerTouchListener
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
    private var websitesDbList: MutableList<Website> = mutableListOf()
    private val map = mutableMapOf<Website, List<Article>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = WebsiteRecyclerViewAdapter(mutableMapOf())
        syncWithDatabase()

        // request more info using network
        websitesDbList.forEach {
            NetworkTask().execute(it, object : OnResultListener {
                override fun action(data: Pair<Website, List<Article>>) {
                    map[data.first] = data.second
                    adapter.load(data)
                    // sync requested data with db
                    WebsitesDbHelper.getInstance(this@SubscribedFragment.context).update(data.first)
                }
            })
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_website_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.context
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = adapter
            val listener = object : ClickListener {
                override fun onClick(view: View?, position: Int) {
                    listener?.onWebsiteListFragmentInteraction(adapter.getItem(position))
                }

                override fun onLongClick(view: View?, position: Int) {
                    val website = adapter.getItem(position).first
                    val optionMenu = android.support.v7.widget.PopupMenu(context, view!!)
                    optionMenu.inflate(R.menu.fragment_source_item)
                    optionMenu.gravity = Gravity.CENTER
                    optionMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.edit_src -> {
                                SourceInfoDialog.show(this@SubscribedFragment.activity, website)
                                syncWithDatabase()
                                true
                            }
                            R.id.delete_src -> {
                                WebsitesDbHelper.getInstance(context).delete(website)
                                syncWithDatabase()
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    optionMenu.show()
                }
            }
            view.addOnItemTouchListener(RecyclerTouchListener(activity, view, listener))
        }
        return view
    }

    fun syncWithDatabase() {
        //check if data really changed
        val old = websitesDbList
        val new = WebsitesDbHelper.getInstance(context).all()
        if (old.toString().hashCode() == new.toString().hashCode()) {
            Logger.debug("SyncDb", "Same content of data set")
            return
        }

        //data really changed, clear and reload
        old.clear()
        websitesDbList.addAll(new)
        websitesDbList.forEach {
            if (!map.containsKey(it)) {
                map[it] = listOf()
            }
        }

        //remove redundant entries in map<website, articles>, stupid
        val iter = map.iterator()
        while (iter.hasNext()) {
            val pair = iter.next()
            if (!websitesDbList.contains(pair.key)) {
                iter.remove()
                Logger.debug("SyncDb", "removing ${pair.key}")
                continue
            }
            Logger.debug("SyncDb", " ${pair.key} : ${pair.value.size}")
        }

        //refresh the view
        adapter.load(map)
        Logger.debug("SyncDb", websitesDbList.toString())
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
        private var instance: SubscribedFragment? = null

        fun newInstance(): SubscribedFragment {
            instance = SubscribedFragment()
            return instance!!
        }

        fun syncDbData() {
            instance?.syncWithDatabase()
        }
    }
}
