package ren.solid.ganhuoio.module.read;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import me.solidev.statusviewlayout.StatusViewLayout;
import ren.solid.ganhuoio.R;
import ren.solid.ganhuoio.api.XianDuService;
import ren.solid.ganhuoio.bean.XianDuCategory;
import ren.solid.library.fragment.base.BaseFragment;
import ren.solid.library.rx.retrofit.RxUtils;
import ren.solid.library.utils.ViewUtils;

/**
 * Created by _SOLID
 * Date:2016/11/29
 * Time:17:01
 * Desc:闲读
 */

public class ReadingTabFragment extends BaseFragment {

    private TabLayout tab_layout;
    private ViewPager view_pager;
    private StatusViewLayout status_view_layout;
    private XianDuTabAdapter mAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_reading_tab;
    }

    @Override
    protected void setUpView() {
        status_view_layout = $(R.id.status_view_layout);
        tab_layout = $(R.id.tab_layout);
        view_pager = $(R.id.view_pager);
        ViewCompat.setElevation(tab_layout, ViewUtils.dp2px(getContext(), 4));
        status_view_layout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpData();
            }
        });
    }

    @Override
    protected void setUpData() {
        status_view_layout.showLoading();
        XianDuService.getCategorys()
                .compose(RxUtils.<List<XianDuCategory>>defaultSchedulers())
                .subscribe(new Subscriber<List<XianDuCategory>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        status_view_layout.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<XianDuCategory> list) {
                        status_view_layout.showContent();
                        mAdapter = new XianDuTabAdapter(getChildFragmentManager(), list);
                        view_pager.setAdapter(mAdapter);
                        tab_layout.setupWithViewPager(view_pager);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //TODO: 点击返回时现将列表滚动到顶部
    public boolean scrollToTop() {

        return true;
    }
}
