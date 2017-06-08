package com.example.pethoalpar.zxingexample;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
public class ListAdapter extends BaseAdapter {

    private Context context;
    ArrayList<ItemList> datas;
    DataSetObservable dataSetObservable = new DataSetObservable();
    private LayoutInflater inflater;
    public ListAdapter(LayoutInflater layoutInflater, ArrayList<ItemList> datas) {

        this.datas = datas;
        this.inflater = layoutInflater;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        dataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        dataSetObservable.unregisterObserver(observer);
    }

    @Override
    public void notifyDataSetChanged() {
        dataSetObservable.notifyChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
            if (v == null) {
                v = inflater.inflate(R.layout.item_layout, null);
            }

            TextView prID = (TextView) v.findViewById(R.id.productID);
            TextView price = (TextView) v.findViewById(R.id.productPrice);

            String as = Integer.toString(datas.get(position).getPrice());


                prID.setText(datas.get(position).getName());
                price.setText(as);




        return v;
    }

}