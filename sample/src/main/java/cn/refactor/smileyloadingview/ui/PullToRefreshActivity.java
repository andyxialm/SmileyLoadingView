package cn.refactor.smileyloadingview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.smileyloadingview.R;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/8/24 09:41
 * Description : Sample: SmileyLoadingView in pull to refresh
 */
public class PullToRefreshActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SamplePtrFrameLayout mPtrFrameLayout;
    private SampleListAdapter mListAdapter;

    private List<String> mDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupViews();
        autoRefresh();
    }

    private void setupViews() {
        mPtrFrameLayout = (SamplePtrFrameLayout) findViewById(R.id.ptr_frame_layout);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(mPtrFrameLayout, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });

        mListAdapter = new SampleListAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL,
                                                            1, getResources().getColor(android.R.color.darker_gray));
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void autoRefresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 500);
    }

    private void refresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDataList == null) {
                    mDataList = new ArrayList<>();
                } else {
                    mDataList.clear();
                    mListAdapter.notifyDataSetChanged();
                }

                for (int i = 0; i < 15; i ++) {
                    mDataList.add(String.valueOf(i));
                }
                mListAdapter.notifyItemRangeChanged(0, mDataList.size() - 1);
                mPtrFrameLayout.refreshComplete();
            }
        }, 3000);
    }

    private class SampleListAdapter extends RecyclerView.Adapter<SampleListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTextTv;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextTv = (TextView) itemView.findViewById(R.id.tv_text);
            }

            public void bind(String text) {
                mTextTv.setText(text);
            }
        }
    }
}
