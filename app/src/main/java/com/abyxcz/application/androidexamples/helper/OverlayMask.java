package com.abyxcz.application.androidexamples.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.abyxcz.application.androidexamples.R;


/**
 * Created by Tim on 7/24/2017.
 */

public class OverlayMask {

    Context ctx;

    public OverlayMask(Context ctx){
        this.ctx = ctx;
    }

    public void showLoading(){

        try{
            (((Activity)ctx).findViewById(R.id.stub_import)).setVisibility(View.VISIBLE);

        }catch(NullPointerException e){

            (((Activity)ctx).findViewById(R.id.panel_import)).setVisibility(View.VISIBLE);
        }

        ImageView loadingIcon = (ImageView) ((Activity)ctx).findViewById(R.id.customProgress);
        /*loadingIcon.setBackgroundResource(R.drawable.custom_progress);
        loadingViewAnim = (AnimationDrawable) loadingIcon.getBackground();
        loadingViewAnim.start();*/


    }

    public void hideLoading(){
        try{
            (((Activity)ctx).findViewById(R.id.stub_import)).setVisibility(View.GONE);

        }catch(NullPointerException e){

            (((Activity)ctx).findViewById(R.id.panel_import)).setVisibility(View.GONE);
        }
        //loadingViewAnim.stop();
    }
}
