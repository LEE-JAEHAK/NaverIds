package com.example.naverid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    private ArrayList<BlogList> arrayList;
    private LayoutInflater inflater;
    private Context mContext;

    private Adapter(ArrayList<BlogList> arrayList, Context context) {
        this.mContext = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public BlogList getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.listview_main, parent, false);
        System.out.println(position);
        final BlogList blogList = arrayList.get(position);

        Button mTitle = convertView.findViewById(R.id.textView10);
        //TextView mLink = convertView.findViewById(R.id.textView12);
        TextView mDescription = convertView.findViewById(R.id.textView14);
        //TextView mPubdate = convertView.findViewById(R.id.textView16);


        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, webview.class);
                intent.putExtra("url", blogList.link);
                mContext.startActivity(intent);
                //mWebView.loadUrl(blogList.link); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
            }
        });

        mTitle.setText(blogList.title);
        //mLink.setText(blogList.link);
        mDescription.setText(blogList.description);
        //mPubdate.setText(blogList.pubDate);

        return convertView;
    }
}
//git에 잘 올라가나? 한번더 수정 깃에서 수정
