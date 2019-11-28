package us.bojie.mvi.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_main.*
import us.bojie.mvi.R
import us.bojie.mvi.model.BlogPost
import us.bojie.mvi.model.User
import us.bojie.mvi.ui.DataStateListener
import us.bojie.mvi.ui.main.state.MainStateEvent.GetBlogPostsEvent
import us.bojie.mvi.ui.main.state.MainStateEvent.GetUserEvent
import us.bojie.mvi.util.TopSpacingItemDecoration

class MainFragment : Fragment(),
    BlogListAdapter.Interaction {

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("debug: clicked $position")
        println("debug: clicked $item")
    }

    lateinit var viewModel: MainViewModel

    lateinit var dataStateListener: DataStateListener

    lateinit var blogListAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        subscribeObservers()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: dataState : $dataState")

            // Hand loading and message
            dataStateListener.onDataStateChange(dataState)

            // Handle Data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.blogPosts?.let { blogPosts ->
                        // setBlogPost data
                        viewModel.setBlogListData(blogPosts)
                    }

                    mainViewState.user?.let { user ->
                        // set User data
                        viewModel.setUser(user)
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let {
                println("DEBUG: setting blog posts to RecyclerView")
                blogListAdapter.submitList(it)
            }

            viewState.user?.let {
                println("Debug : Setting user data : $it")
                setUserProperties(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserProperties(user: User) {
        email.text = user.email
        username.text = user.username
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostsEvent)
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateListener = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG: $context must implement DataStateListener")
        }
    }
}