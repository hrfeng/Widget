package hrf.widget.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hrf.widget.R;

/**
 * User: HRF
 * Date: 2017/10/9
 * Time: 14:14
 * Description: 列表显示对话框
 */
public class ListViewDialog extends Dialog {

    protected Context mContext;
    protected ListView lv_content;
    protected LinearLayout ll_content;

    protected List<ListViewDialogBean> mList;


    protected int mWidth;
    protected int mHeight;

    private float percentWidth = 0.6f;
    private float percentHgight = 0.5f;

    private String textColor;
    private float textSize;
    private int titleHeight;

    private DialogAdapter mAdapter;

    private DisplayMetrics mDisplayMetrics;
    private float mDensity;

    public ListViewDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ListViewDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public ListViewDialog(@NonNull Context context, @StyleRes int themeResId, @NonNull List<ListViewDialogBean> list) {
        super(context, themeResId);
        init(context);
        initList(list);
    }

    protected ListViewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context c) {
        mContext = c;
        mList = new ArrayList<>();
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mDensity = mDisplayMetrics.density;
    }


    private void initList(List<ListViewDialogBean> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        initAdapter();
    }


    private void initAdapter() {
        mAdapterListener = new AdapterListener() {
            @Override
            public void setAdapter(ListView lv) {
                mAdapter = new DialogAdapter(mContext, mList);
                mAdapter.setTextSize(textSize);
                mAdapter.setTextColor(textColor);
                mAdapter.setTitleHeight(titleHeight);
                lv_content.setAdapter(mAdapter);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onItemClick(parent, view, position, mList.get(position));
                        dismiss();
                    }
                });
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_view);

        mWidth = mDisplayMetrics.widthPixels;
        mHeight = mDisplayMetrics.heightPixels;
        ll_content = findViewById(R.id.ll_content);
        lv_content = findViewById(R.id.lv_content);
        setW();
        setH();
        if (mAdapterListener != null)
            mAdapterListener.setAdapter(lv_content);
    }


    private final void setW() {
        if (percentWidth == 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = ll_content.getLayoutParams();
        layoutParams.width = (int) (mWidth * percentWidth);
        ll_content.setLayoutParams(layoutParams);
    }

    /**
     * @param percentWidth 宽度占屏幕宽度%
     */
    public final void setDialogWidth(float percentWidth) {
        this.percentWidth = percentWidth;
    }

    private final void setH() {
        if (percentHgight == 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = ll_content.getLayoutParams();
        layoutParams.height = (int) (mHeight * percentHgight);
        ll_content.setLayoutParams(layoutParams);
    }

    /**
     * @param percentHgight 高度占屏幕高度%
     */
    public final void setDialogHeight(float percentHgight) {
        this.percentHgight = percentHgight;
    }


    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = (int) (titleHeight*mDensity);
    }

    public static class DialogAdapter extends BaseAdapter<ListViewDialogBean> {

        private String textColor;
        private float textSize = 0;
        private int titleHeight = 0;


        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }

        public void setTitleHeight(int titleHeight) {
            this.titleHeight = titleHeight;
        }

        public DialogAdapter(Context context, List<ListViewDialogBean> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_dialog_list_view, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mTv.setText(getItem(position).getTitle());
            if (!TextUtils.isEmpty(textColor)) {
                vh.mTv.setTextColor(Color.parseColor(textColor));
            }
            if (textSize != 0) {
                vh.mTv.setTextSize(textSize);
            }
            if (titleHeight != 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vh.mTv.getLayoutParams();
                layoutParams.height = titleHeight;
                vh.mTv.setLayoutParams(layoutParams);
            }
            return convertView;
        }

        public static class ViewHolder {
            TextView mTv;

            public ViewHolder(View v) {
                mTv = v.findViewById(R.id.tv_content);
            }
        }
    }

    private AdapterListener mAdapterListener;

    public void setAdapterListener(AdapterListener listener) {
        this.mAdapterListener = listener;
    }

    public interface AdapterListener {
        void setAdapter(ListView lv);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, ListViewDialogBean lvdb);
    }
}
