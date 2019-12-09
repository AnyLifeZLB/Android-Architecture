package com.anylife.module_main.blog.ui;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anylife.module_main.R;
import com.anylife.module_main.blog.viewmodel.Blog2ViewModel;
import com.anylife.module_main.blog.viewmodel.BlogViewModel;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseStatusFragment;
import com.zlb.commontips.TimeoutCallback;
import com.zlb.dagger.scope.ActivityScope;
import com.zlb.dagger.viewmodel.MyViewModelFactory;
import com.zlb.persistence.entity.Blog;
import com.zlb.statelivedata.StateData;

import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link BlogListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ActivityScope
public class BlogListFragment extends BaseStatusFragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private BlogViewModel blogViewModel;

    @Inject
    MyViewModelFactory viewModelFactory;

    @Inject
    SPDao spDao;

    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_video;
    }

    @Override
    public void initView(View view) {
        swipeRefresh = view.findViewById(R.id.swiperefresh);
        mRecyclerView = view.findViewById(R.id.blogRecyclerView);

        //

        //当Activity重建的时候，虽然 onCreate() 方法会重新走一遍，但是这个mainViewModel实例，
        //仍然是第一次创建的那个实例，在ViewModelProviders.of(this).get(***.class)中的get方法中进行了缓存
        //可是和Dagger 结合起来就麻烦了，哈哈哈
//        blogViewModel = ViewModelProviders.of(this).get(BlogViewModel.class);

        //use Dagger
//        blogViewModel = ViewModelProviders.of(this, viewModelFactory).get(BlogViewModel.class);


        //ViewModel 最终消亡是在 Activity 被销毁的时候，会执行它的onCleared()进行数据的清理。
        //去获取博客数据
//        getPopularBlog();

        Blog2ViewModel viewModel = ViewModelProviders.of(this).get(Blog2ViewModel.class);
        viewModel.getShowLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toasty.info(getActivity(), "开始加载").show();
                } else {
                    Toasty.info(getActivity(), "加载完毕").show();
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

        viewModel.getShowErrMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toasty.error(getActivity(), s).show();
            }
        });

        viewModel.getBlogLiveData().observe(this, new Observer<List<Blog>>() {
            @Override
            public void onChanged(List<Blog> blogs) {
                prepareRecyclerView(blogs);
            }
        });


        // 数据刷新获取数据
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
//                blogViewModel.getAllBlog();
                viewModel.loadData();
            }
        });


        viewModel.loadData();

    }


    /**
     * 重写请求重试
     *
     * @param v
     */
    @Override
    protected void onHttpReload(View v) {
        blogViewModel.getAllBlog();
    }


    /**
     * 去获取数据
     */
    public void getPopularBlog() {
        swipeRefresh.setRefreshing(true);


        blogViewModel.getAllBlog().observe(this, new Observer<StateData<List<Blog>>>() {
            @Override
            public void onChanged(StateData<List<Blog>> stateData) {
                switch (stateData.getStatus()) {
                    case SUCCESS:
                        swipeRefresh.setRefreshing(false);
                        prepareRecyclerView(stateData.getData());
                        break;

                    case ERROR:
                        swipeRefresh.setRefreshing(false);

                        //这里可以根据ErrorCode进行封装统一处理
                        mBaseLoadService.showCallback(TimeoutCallback.class);//其他回调

                        break;
                    case LOADING:

                        Log.e("TTT", "显示Loading UI ");

                        break;
                }
            }
        });
    }


    /**
     * 准备设置数据的改变
     *
     * @param blogList
     */
    private void prepareRecyclerView(List<Blog> blogList) {
        BlogAdapter mBlogAdapter = new BlogAdapter(blogList);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBlogAdapter);
        mBlogAdapter.notifyDataSetChanged();
    }


    public BlogListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    public static BlogListFragment newInstance() {
        BlogListFragment fragment = new BlogListFragment();
        return fragment;
    }


}
