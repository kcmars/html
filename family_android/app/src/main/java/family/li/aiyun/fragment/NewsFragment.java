package family.li.aiyun.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by keyC on 2019/6/15.
 * 消息
 */

public class NewsFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    private TabLayout mTabLayout;

    //首先需要先实例好n个全局Fragment
    private BaseFragment currentFragment = new BaseFragment();
    private ClanNoticeFragment mClanNoticeFragment;          //宗族公告
    private SystemNoticeFragment mSystemNoticeFragment;      //系统消息
    private InterNoticeFragment mInterNoticeFragment;        //互动消息

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mClanNoticeFragment = (ClanNoticeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("mClanNoticeFragment");
            mSystemNoticeFragment = (SystemNoticeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("mSystemNoticeFragment");
            mInterNoticeFragment = (InterNoticeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("mInterNoticeFragment");
        }
        if (mClanNoticeFragment == null) {
            mClanNoticeFragment = ClanNoticeFragment.newInstance();
        }
        if (mSystemNoticeFragment == null) {
            mSystemNoticeFragment = SystemNoticeFragment.newInstance();
        }
        if (mInterNoticeFragment == null) {
            mInterNoticeFragment = InterNoticeFragment.newInstance();
        }
        View view = inflater.inflate(family.li.aiyun.R.layout.fragment_news, container, false);
        mTabLayout = (TabLayout) view.findViewById(family.li.aiyun.R.id.tab_news_layout);
        TabLayout.Tab tab1 = mTabLayout.newTab().setCustomView(family.li.aiyun.R.layout.teb_news_layout_item1);
        TabLayout.Tab tab2 = mTabLayout.newTab().setCustomView(family.li.aiyun.R.layout.teb_news_layout_item2);
//        TabLayout.Tab tab3 = mTabLayout.newTab().setCustomView(family.li.aiyun.R.layout.teb_news_layout_item3);

        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);
//        mTabLayout.addTab(tab3);
        tab1.select();
        switchFragment(mClanNoticeFragment, "mClanNoticeFragment").commit();
        mTabLayout.addOnTabSelectedListener(this);
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                switchFragment(mClanNoticeFragment, "mClanNoticeFragment").commit();
                break;
            case 1:
                switchFragment(mSystemNoticeFragment, "mSystemNoticeFragment").commit();
                break;
            case 2:
                switchFragment(mInterNoticeFragment, "mInterNoticeFragment").commit();
                break;
            default:break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private FragmentTransaction switchFragment(BaseFragment targetFragment, String tag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(family.li.aiyun.R.id.fl_news_content, targetFragment, tag);
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        return transaction;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
