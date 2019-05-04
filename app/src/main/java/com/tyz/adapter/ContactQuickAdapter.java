package com.tyz.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tyz.R;
import com.tyz.cn.CNPinyin;
import com.tyz.search.Contact;
import com.tyz.stickyheader.StickyHeaderAdapter;

import java.util.List;

public class ContactQuickAdapter  extends BaseQuickAdapter<CNPinyin<Contact> , BaseViewHolder>  implements StickyHeaderAdapter<HeaderHolder> {

    private final List<CNPinyin<Contact>> cnPinyinList;

    public ContactQuickAdapter(int layoutResId, @Nullable List<CNPinyin<Contact> > data) {
        super(layoutResId, data);
        this.cnPinyinList = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, CNPinyin<Contact> item) {

        helper.setImageResource(R.id.iv_header,item.data.imgUrl);
        helper.setText(R.id.tv_name,item.data.name);
    }


    @Override
    public long getHeaderId(int childAdapterPosition) {
        return cnPinyinList.get(childAdapterPosition).getFirstChar();
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder holder, int childAdapterPosition) {
        holder.tv_header.setText(String.valueOf(cnPinyinList.get(childAdapterPosition).getFirstChar()));
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false));
    }
}
