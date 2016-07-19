package com.wu.allen.myone.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.wu.allen.myone.R;
import com.wu.allen.myone.adapter.SaveAdapter;
import com.wu.allen.myone.injector.components.AppComponent;
import com.wu.allen.myone.model.ArticleSave;
import com.wu.allen.myone.utils.ToastUtil;
import com.wu.allen.myone.view.ISaveView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.List;

import static com.wu.allen.myone.R.id.recyclerView;

/**
 * Created by allen on 2016/7/15.
 */

public class SaveArtActivity extends BaseActivity implements ISaveView {

    private static final String TAG = "SaveArtActivity";
    private EasyRecyclerView mRecyclerView;
    private SaveAdapter mSaveAdapter;
    private Handler handler = new Handler();
    private int page = 0;
    private Realm mRealm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articlesave_layout);
        SwipeBackHelper.onCreate(this);
        initView();
        QueryArticle();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (EasyRecyclerView) findViewById(recyclerView);
        mSaveAdapter = new SaveAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapterWithProgress(mSaveAdapter);
        mSaveAdapter.setNoMore(R.layout.view_nomore);
        mSaveAdapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemClick(int position) {
                // TODO: 2016/7/16 can delete the item of save artice and in realm also delete
                ToastUtil.showLong(SaveArtActivity.this,"long click can delete this item");
                return false;
            }
        });
    }

    private void QueryArticle() {
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<ArticleSave> query = realm.where(ArticleSave.class);
                RealmResults<ArticleSave> result1 = query.findAll();
                fillData(result1);
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void fillData(List<ArticleSave> list) {
        mSaveAdapter.addAll(list);
        mSaveAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
        if(mRealm != null)
            mRealm.close();
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {

    }
}