package com.anylife.module_main.blog.model

import com.anylife.module_main.blog.BlogHttpService.BlogApiService
import com.anylife.module_main.blog.BlogHttpService.BlogHttpObserver
import com.anylife.module_main.blog.BlogHttpService.BlogHttpRetrofit
import com.anylife.module_main.blog.viewmodel.ResultData
import com.zlb.httplib.scheduler.SwitchSchedulers
import com.zlb.persistence.entity.Blog

/**
 * Created by WG on 2019-12-09.
 * Email: wg5329@163.com
 * Github: https://github.com/WGwangguan
 * Desc:
 */
object Blog2Repository {

    @JvmStatic
    fun getBlog(action: (ResultData<List<Blog>>) -> Unit) {
        val blogApiService = BlogHttpRetrofit.getRetrofit().create(BlogApiService::class.java)//使用Dagger可以省很多代码
        //1.异步加载网络请求数据
//        BlogHttpRetrofit.getRetrofit().create(BlogApiService.class) //使用Dagger可以省很多代码
        blogApiService.getPopularBlog()
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(object : BlogHttpObserver<List<Blog>?>() {
                    override fun onSuccess(blogList: List<Blog>?) { //更新为网络数据
                        //缓存数据
                        action(ResultData(data = blogList))
                    }

                    override fun onFailure(code: Int, message: String) {
                        super.onFailure(code, message)
                        //把错误信息告知给UI操作层面
                        action(ResultData(code, message))
                    }
                })
    }
}