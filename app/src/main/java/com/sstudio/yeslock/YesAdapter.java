package com.sstudio.yeslock;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YesAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Drawable[] imgid;

    public YesAdapter(Activity context, String[] itemname, Drawable[] imgid) {
        super(context, R.layout.grid, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.grid, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.gtv);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.giv);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
try {
    txtTitle.setText(itemname[position]);
    imageView.setImageDrawable(imgid[position]);
    //extratxt.setText("Description "+itemname[position]);

}catch (Exception e){
    e.printStackTrace();
}

        return rowView;
    }
}