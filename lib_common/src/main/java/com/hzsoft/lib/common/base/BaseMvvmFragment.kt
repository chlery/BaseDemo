package com.hzsoft.lib.common.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fly.tour.common.util.log.KLog
import com.hzsoft.lib.common.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.common.utils.ext.view.showToast

/**
 * Describe:
 * 基础 MVVM 页面
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {
    protected val mViewModel: VM by lazy { ViewModelProvider(this).get(onBindViewModel()) }

    override fun initCommonView(view: View) {
        super.initCommonView(view)
        initBaseViewObservable()
        initViewObservable()
    }

    /**
     * 绑定 ViewModel
     */
    abstract fun onBindViewModel(): Class<VM>

    /**
     * 放置 观察者对象
     */
    abstract fun initViewObservable()


    /**
     * 初始化页面观察 变更相应的展示
     */
    protected open fun initBaseViewObservable() {
        mViewModel.mUIChangeLiveData.getShowToastViewEvent()
            .observe(this, Observer { it.showToast(mContext) })
        mViewModel.mUIChangeLiveData.getShowInitLoadViewEvent()
            .observe(this, Observer {
                showInitLoadView(it)
            })
        mViewModel.mUIChangeLiveData.getShowTransLoadingViewEvent()
            .observe(this, Observer {
                KLog.v("MYTAG", "view postShowTransLoadingViewEvent start...")
                showTransLoadingView(it)
            })
        mViewModel.mUIChangeLiveData.getShowNoDataViewEvent()
            .observe(this, Observer {
                showNoDataView(it)
            })
        mViewModel.mUIChangeLiveData.getShowNetWorkErrViewEvent()
            .observe(this, Observer {
                showNetWorkErrView(it)
            })
        mViewModel.mUIChangeLiveData.getStartActivityEvent().observe(
            this,
            Observer {
                val clz =
                    it[BaseViewModel.ParameterField.CLASS] as Class<*>?
                val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                startActivity(clz, bundle)
            })
        mViewModel.mUIChangeLiveData.getFinishActivityEvent()
            .observe(this, Observer { mActivity.finish() })
        mViewModel.mUIChangeLiveData.getOnBackPressedEvent()
            .observe(this, Observer { mActivity.onBackPressed() })
    }

}