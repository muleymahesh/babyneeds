package com.maks.babyneeds.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Activity.R;
import com.maks.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 03/01/2016.
 */
public class CardArrayAdapter extends ArrayAdapter<Card> {
    private static final String TAG = "CardArrayAdapter";
    private List<Card> cardList = new ArrayList<Card>();

    static class CardViewHolder {
       TextView title;
       // TextView line2;
        ImageView imageView ;
    }
    public CardArrayAdapter(Context context, int resource) {
        super(context, resource);
    }
    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.title = (TextView) row.findViewById(R.id.title);
           // viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            viewHolder.imageView = (ImageView) row.findViewById(R.id.img);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        Card card = getItem(position);
        viewHolder.title.setText(card.getTitle());
       // viewHolder.line2.setText(card.getLine2());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
