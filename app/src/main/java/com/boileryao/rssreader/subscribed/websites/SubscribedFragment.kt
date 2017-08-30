package com.boileryao.rssreader.subscribed.websites

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
import com.boileryao.rssreader.subscribed.NetworkTask
import com.boileryao.rssreader.subscribed.OnResultListener
import com.boileryao.rssreader.util.recyclerview.ClickListener
import com.boileryao.rssreader.util.recyclerview.RecyclerTouchListener

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

    //fixme
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = WebsiteRecyclerViewAdapter(mutableMapOf(), listener)

        if (arguments != null) {
            val tmp = arguments.get(ARG_WEBSITE_LIST)
            if (tmp is List<*>) {
                NetworkTask().execute(tmp, object : OnResultListener {
                    override fun action(data: Map<Website, List<Article>>?) {
                        adapter.load(data)
                    }
                })
            }
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
            view.addOnItemTouchListener(RecyclerTouchListener(activity, view,
                    ClickListener { _, position ->
                        listener?.onWebsiteListFragmentInteraction(adapter.getItem(position))
                    }))
        }
        return view
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
        internal val ARG_WEBSITE_LIST = "website-list"

        fun newInstance(): SubscribedFragment {
            val fragment = SubscribedFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
