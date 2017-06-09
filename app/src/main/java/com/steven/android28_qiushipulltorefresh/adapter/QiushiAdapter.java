package com.steven.android28_qiushipulltorefresh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.steven.android28_qiushipulltorefresh.R;
import com.steven.android28_qiushipulltorefresh.helper.BaseAdapterHelper;
import com.steven.android28_qiushipulltorefresh.model.QiushiModel;

import java.util.List;


public class QiushiAdapter extends BaseAdapterHelper<QiushiModel.ItemsEntity> {
    private final static int TYPE1 = 0, TYPE2 = 1;

    public QiushiAdapter(Context context, List<QiushiModel.ItemsEntity> list) {
        super(context , list);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        String imageUrl = getImageUrl(list.get(position).getImage() + "");
        return (imageUrl==null) ? TYPE2 : TYPE1;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, List<QiushiModel
            .ItemsEntity> list, LayoutInflater inflater) {
        ViewHolder1 mHolder1 = null;
        ViewHolder2 mHolder2 = null;
        int type = getItemViewType(position);

        if (convertView == null) {
            switch (type) {
                case TYPE1:
                    convertView = inflater.inflate(R.layout.item_listview_main1, parent, false);
                    mHolder1 = new ViewHolder1(convertView);
                    convertView.setTag(mHolder1);
                    break;
                case TYPE2:
                    convertView = inflater.inflate(R.layout.item_listview_main2, parent, false);
                    mHolder2 = new ViewHolder2(convertView);
                    convertView.setTag(mHolder2);
                    break;
            }
        } else {
            switch (type) {
                case TYPE1:
                    mHolder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE2:
                    mHolder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }
        // 给控件赋值
        switch (type) {
            case TYPE1:
                mHolder1.textView_item_content.setText(list.get(position).getContent());
                if (list.get(position).getUser() != null) {
                    mHolder1.textView_item_login.setText(list.get(position).getUser().getLogin());
                }
                mHolder1.textView_item_commentscount.setText(list.get(position).getComments_count
                        () + "");

                final String imageUrl = getImageUrl(list.get(position).getImage() + "");

                // 使用Picasso框架加载图片
                Picasso.with(context).load(imageUrl)
                        .noFade()
                        /*.resize()
                        .resizeDimen()*/
                        .placeholder(android.R.drawable.ic_search_category_default)
                        .error(android.R.drawable.stat_notify_error)
                        .into(mHolder1.imageView_item_show);

                break;
            case TYPE2:
                mHolder2.textView_item_content.setText(list.get(position).getContent());
                if (list.get(position).getUser() != null) {
                    mHolder2.textView_item_login.setText(list.get(position).getUser().getLogin());
                }
                mHolder2.textView_item_commentscount.setText(list.get(position).getComments_count
                        () + "");
                break;
        }
        return convertView;
    }

    // 根据图片的名称拼凑图片的网络访问地址
    private String getImageUrl(String imageName) {
        String urlFirst = "", urlSecond = "";
        if (imageName.indexOf('.') > 0) {
            StringBuilder sb = new StringBuilder();
            if (imageName.indexOf("app") == 0) {
                urlSecond = imageName.substring(3, imageName.indexOf('.'));
                switch (urlSecond.length()) {
                    case 8:
                        urlFirst = imageName.substring(3, 7);
                        break;
                    case 9:
                        urlFirst = imageName.substring(3, 8);
                        break;
                    case 10:
                        urlFirst = imageName.substring(3, 9);
                        break;
                }
            } else {
                urlSecond = imageName.substring(0, imageName.indexOf('.'));
                urlFirst = imageName.substring(0, 6);
            }

            sb.append("http://pic.qiushibaike.com/system/pictures/");
            sb.append(urlFirst);
            sb.append("/");
            sb.append(urlSecond);
            sb.append("/");
            sb.append("small/");
            sb.append(imageName);
            return sb.toString();
        } else {
            return null;
        }
    }

    class ViewHolder1 {
        private ImageView imageView_item_show;
        private TextView textView_item_content;
        private TextView textView_item_login;
        private TextView textView_item_commentscount;

        public ViewHolder1(View convertView) {
            imageView_item_show = ((ImageView) convertView.findViewById(R.id.imageView_item_show));
            textView_item_content = ((TextView) convertView.findViewById(R.id
                    .textView_item_content));
            textView_item_login = ((TextView) convertView.findViewById(R.id.textView_item_login));
            textView_item_commentscount = ((TextView) convertView.findViewById(R.id
                    .textView_item_commentscount));
        }
    }


    class ViewHolder2 {
        private TextView textView_item_content;
        private TextView textView_item_login;
        private TextView textView_item_commentscount;

        public ViewHolder2(View convertView) {
            textView_item_content = ((TextView) convertView.findViewById(R.id
                    .textView_item_content));
            textView_item_login = ((TextView) convertView.findViewById(R.id.textView_item_login));
            textView_item_commentscount = ((TextView) convertView.findViewById(R.id
                    .textView_item_commentscount));
        }
    }


}
