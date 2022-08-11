package com.example.socketchat.presentation.chat

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.socketchat.data.dtomodels.wrappers.MessageWrapper
import java.util.concurrent.atomic.AtomicBoolean

class MessageSingleLiveEvent : MutableLiveData<MessageWrapper>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in MessageWrapper>) {
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: MessageWrapper?) {
        mPending.set(true)
        super.setValue(t)
    }
}