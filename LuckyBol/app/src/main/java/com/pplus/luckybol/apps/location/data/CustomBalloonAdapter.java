package com.pplus.luckybol.apps.location.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pplus.luckybol.R;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

/**
 * Created by ksh on 2017-01-18.
 */

public class CustomBalloonAdapter implements CalloutBalloonAdapter{

    private final View mCalloutBalloon;

    public CustomBalloonAdapter(Context context){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mCalloutBalloon = inflater.inflate(R.layout.item_custom_balloon, null);
    }

    @Override
    public View getCalloutBalloon(MapPOIItem poiItem){

        TextView textBalloon = (TextView) mCalloutBalloon.findViewById(R.id.text_custom_balloon);
        textBalloon.setText(poiItem.getItemName());
        return mCalloutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem poiItem){

        return null;
    }
}