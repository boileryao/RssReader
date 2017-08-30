package com.boileryao.rssreader.subscribed.articles

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boileryao.rssreader.R
import com.boileryao.rssreader.bean.Article
import com.boileryao.rssreader.util.recyclerview.ClickListener
import com.boileryao.rssreader.util.recyclerview.RecyclerTouchListener

/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class ArticlesFragment : Fragment() {
    private var listener: OnListFragmentInteractionListener? = null
    private var articleList = mutableListOf<Article>()
    private lateinit var adapter: ArticleRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val tmp = arguments.get(ARG_ARTICLE_LIST)
            (tmp as List<*>).forEach {
                articleList.add(it as Article)
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_article_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.context
            view.layoutManager = LinearLayoutManager(context)
            adapter = ArticleRecyclerViewAdapter(articleList, listener)
            view.adapter = adapter
            view.addOnItemTouchListener(RecyclerTouchListener(activity, view,
                    ClickListener { _, position ->
                        listener?.onListFragmentInteraction(adapter.getItem(position))
                    }))
        }

        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = object : OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: Article) {
                val url = item.url
                val builder = CustomTabsIntent.Builder()
                val customtabsIntent = builder.build()
                customtabsIntent.launchUrl(context, Uri.parse(url))
            }
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
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Article)
    }

    companion object {
        internal val ARG_ARTICLE_LIST = "article-list"

        fun newInstance(): ArticlesFragment {
            val fragment = ArticlesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}