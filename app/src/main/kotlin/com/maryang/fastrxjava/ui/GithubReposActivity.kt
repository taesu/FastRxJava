package com.maryang.fastrxjava.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.base.BaseApplication
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_github_repos.*
import java.util.concurrent.TimeUnit


class GithubReposActivity : AppCompatActivity() {

    private val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    var subject = PublishSubject.create<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

//        refreshLayout.setOnRefreshListener { load() }
//
//        load(true)

        Log.d(BaseApplication.TAG, "current Thread: ${Thread.currentThread().name}")

        subject
            .debounce(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchLoad(it, true)
            }

        searchText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(text: Editable?) {
                subject.onNext(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun searchLoad(search: String, showLoading: Boolean) {
        if (showLoading)
            showLoading()

        viewModel.searchGithubRepos(search)
            .subscribe(object : DisposableSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(t: List<GithubRepo>) {
                    hideLoading()
                    adapter.items = t
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                }
            })
    }

    private fun load(showLoading: Boolean = false) {
        if (showLoading)
            showLoading()
        viewModel.getGithubRepos()
            .subscribe({
                hideLoading()
                adapter.items = it
            }, {
                hideLoading()
            })
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }
}
