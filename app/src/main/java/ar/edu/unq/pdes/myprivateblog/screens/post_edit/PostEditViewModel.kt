package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.logger.AnalyticsLogger
import ar.edu.unq.pdes.myprivateblog.logger.TypeEventAnalytics
import ar.edu.unq.pdes.myprivateblog.services.PostService
import javax.inject.Inject

class PostEditViewModel @Inject constructor(private val postService: PostService, val analytics: AnalyticsLogger) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    val post = MutableLiveData<BlogEntry?>()
    val bodyText = MutableLiveData<String?>()

    fun fetchBlogEntry(id: EntityID) {
        val disposable = postService.getById(id).subscribe {
            post.value = it.first
            bodyText.value = it.second
        }
    }

    fun updatePost() {
        val disposable = postService.update(post.value!!, bodyText.value!!)
            .subscribe { state.value = State.SUCCESS }
        analytics.logEvent(TypeEventAnalytics.EDIT_POST)
    }

    fun updateTitle(title: String) {
        post.value = post.value?.copy(title = title)
    }

    fun updateColor(color: Int) {
        post.value = post.value?.copy(cardColor = color)
    }
}
