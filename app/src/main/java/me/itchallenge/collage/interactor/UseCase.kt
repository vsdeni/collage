package com.urancompany.indoorapp.interactor

import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject

abstract class UseCase<out Type, in Params> where Type : Any {

    internal val disposables = CompositeDisposable()

    fun dispose() = disposables.dispose()

    abstract fun build(params: Params?): Type

    abstract class RxSingle<T, in P> : UseCase<Single<T>, P>() {
        fun execute(observer: UseCaseObserver.RxSingle<T>, params: P? = null) =
                disposables.add(build(params).subscribeWith(observer))

        fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit, params: P? = null) =
                disposables.add(build(params).subscribe(onSuccess, onError))
    }

    abstract class RxMaybe<T, in P> : UseCase<Maybe<T>, P>() {
        fun execute(observer: UseCaseObserver.RxMaybe<T>, params: P? = null) =
                disposables.add(build(params).subscribeWith(observer))

        fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit, params: P? = null) =
                disposables.add(build(params).subscribe(onSuccess, onError))
    }

    abstract class RxObservable<T, in P> : UseCase<Observable<T>, P>() {
        fun execute(observer: UseCaseObserver.RxObservable<T>, params: P? = null) =
                disposables.add(build(params).subscribeWith(observer))

        fun execute(onNext: (T) -> Unit, onError: (Throwable) -> Unit, params: P? = null) {
            disposables.add(build(params).subscribe(onNext, onError))
        }
    }

    abstract class RxFlowable<T, in P> : UseCase<Flowable<T>, P>() {
        fun execute(subscriber: UseCaseObserver.RxFlowable<T>, params: P? = null) =
                disposables.add(build(params).subscribeWith(subscriber))

        fun execute(onNext: (T) -> Unit, onError: (Throwable) -> Unit, params: P? = null) {
            disposables.add(build(params).subscribe(onNext, onError))
        }
    }

    abstract class RxSubject<T, in P> : UseCase<Subject<T>, P>() {
        fun execute(observer: UseCaseObserver.RxObservable<T>, params: P? = null) =
                disposables.add(build(params).subscribeWith(observer))

        fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit, params: P? = null) =
                disposables.add(build(params).subscribe(onSuccess, onError))
    }

    abstract class RxCompletable<in P> : UseCase<Completable, P>() {
        fun execute(params: P? = null) = execute({}, {}, params)

        fun execute(onComplete: () -> Unit = {}, onError: (Throwable) -> Unit, params: P? = null) =
                disposables.add(build(params).subscribe(onComplete, onError))
    }

    class None
}
