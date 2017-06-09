package com.steven.android28_qiushipulltorefresh;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.steven.android28_qiushipulltorefresh.adapter.QiushiAdapter;
import com.steven.android28_qiushipulltorefresh.helper.OkHttpClientHelper;
import com.steven.android28_qiushipulltorefresh.model.QiushiModel;
import com.steven.android28_qiushipulltorefresh.utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context mContext = this;
    private TextView textView_empty;
    private PullToRefreshListView refreshListView_main;
    private ListView listView_main;
    private ProgressBar progressBar_main;

    private QiushiAdapter adapter = null;
    private List<QiushiModel.ItemsEntity> totalList = new ArrayList<>();

    private int curPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        loadNetworkData();
    }

    private void loadNetworkData() {
        OkHttpClientHelper.getDataAsync(mContext, String.format(Constant.URL_LATEST, curPage),
                new Callback() {

                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, R.string.hint_badnetwork, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            if (body != null) {
                                String jsonString = body.string();

                                //json解析
                                QiushiModel result_model = parseJsonToQiushiModel(jsonString);
                                final List<QiushiModel.ItemsEntity> list = result_model.getItems();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar_main.setVisibility(View.GONE);
                                        if (curPage == 1) {
                                            adapter.reloadListView(list, true);
                                        } else {
                                            adapter.reloadListView(list, false);
                                        }
                                        //让提示条消失
                                        refreshListView_main.onRefreshComplete();
                                    }
                                });
                            }
                        }
                    }
                }, "qiushi_latest");
    }

    private void initView() {
        refreshListView_main = (PullToRefreshListView) findViewById(R.id.refreshListview_main);
        //为了实现置顶功能，需要设置一个ListView对象
        listView_main = refreshListView_main.getRefreshableView();

        textView_empty = (TextView) findViewById(R.id.textView_empty);
        progressBar_main = (ProgressBar) findViewById(R.id.progressBar_main);

        adapter = new QiushiAdapter(mContext, totalList);
        refreshListView_main.setAdapter(adapter);
        refreshListView_main.setEmptyView(textView_empty);
        refreshListView_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick:-->> "+position);
                Log.i(TAG, "data-->>: "+totalList.get((int) id).getContent().toString());
            }
        });

        ILoadingLayout iLoadingLayout = refreshListView_main.getLoadingLayoutProxy();

        iLoadingLayout.setPullLabel(getResources().getString(R.string.pull_label));
        //iLoadingLayout.setLoadingDrawable(R.mipmap.ic_launcher);
        iLoadingLayout.setRefreshingLabel(getResources().getString(R.string.refreshing_label));
        iLoadingLayout.setReleaseLabel(getResources().getString(R.string.release_label));


        //设置刷新模式：上拉加载下一页，下拉刷新第一页
        refreshListView_main.setMode(PullToRefreshBase.Mode.BOTH);

        refreshListView_main.setOnRefreshListener(new PullToRefreshBase
                .OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage = 1;
                loadNetworkData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage++;
                loadNetworkData();
            }
        });
    }

    //gson解析
    private QiushiModel parseJsonToQiushiModel(String jsonString) {
        Gson gson = new Gson();
        QiushiModel model = gson.fromJson(jsonString, new TypeToken<QiushiModel>() {
        }.getType());
        return model;
    }

    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.imageView_backtotop:
                //ListView置顶
                listView_main.setSelection(0);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpClientHelper.cancelCall("qiushi_latest");
    }
}
