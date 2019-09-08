package com.maryang.fastrxjava.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

class Operators  {
    fun rxjavaOperators() {
        Single.just(true)
            .map { 1 }

        Single.just(true)
            .flatMap {
                Single.just(1)
            }

        Observable.just(true) // 1초 이내에 3번정도 연속으로 수행되었다.
            .debounce(1, TimeUnit.SECONDS)
            .doOnNext {
                // 1번만 내려온다
            }

        Single.concat(
            Single.just(true),
            Single.just(true)
        )

        Single.merge(
            Single.just(true),
            Single.just(true)
        )

        Single.zip<Boolean, Int, String>(
            Single.just(true),
            Single.just(1),
            BiFunction { first, second ->
                ""
            }
        )

//        Observable.combineLatest()

//        Single.just(true)
//            .compose()
    }

    fun zipExample() {
        Single.zip<Boolean, Boolean, String>(
            Single.just(true),
            Single.just(false)
                .retry(3),
            BiFunction { first, second ->
                ""
            }
        )
    }

    fun executorService() {
//        ExecutorService
        Single.just(true)
            .subscribeOn(Schedulers.io()) //
            .subscribeOn(Schedulers.computation()) //계산작업이 들어갈때
            .subscribeOn(Schedulers.trampoline()) //기존 쓰레드를 이어서 해라
            .subscribeOn(Schedulers.newThread()) //새로운 쓰레드를 만들어라
            .subscribeOn(Schedulers.single()) //
            .observeOn(AndroidSchedulers.mainThread())
    }
}