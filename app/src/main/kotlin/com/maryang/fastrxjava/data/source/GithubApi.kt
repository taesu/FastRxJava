package com.maryang.fastrxjava.data.source

import com.google.gson.JsonElement
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    fun searchRepos(
        @Query("q") search: String
    ): Single<JsonElement>

    @GET("user/starred/{owner}/{repo}")
    fun checkStar(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Completable

    @GET("users/{userName}/repos")
    fun getRepos(
        @Path("userName") userName: String = "googlesamples"
    ): Single<List<GithubRepo>>

    @GET("users/{userName}")
    fun getUser(
        @Path("userName") userName: String = "octocat"
    ): Maybe<User>

    @PUT("users/{userName}")
    fun changeName(
        @Path("userName") userName: String = "octocat"
    ): Completable
}
