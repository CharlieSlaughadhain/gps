package com.example.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class City {
    private Location cityLoc;
    private String name;
    private int distance;
    private ImageView pointyBit;
    private TextView cityText;
    private TextView distanceText;
    private Activity activity;
    private ConstraintSet constraintSet = new ConstraintSet();

    private int id; // Unused but keep it

    //private TextView printText;

    public City(Location l, String cityName, Activity _activity, int[] newId, ConstraintLayout constraintLayout, Context context){
        activity = _activity;
        cityLoc = l;
        name = cityName;
        id = newId[0]++;

        buildTheBastard(constraintLayout, context);
    }

    public void update(Location l, int azimuth){
        getDistance(l);
        double bearing = getInitBearing(l);
        double relAngle = (((((360 - bearing + azimuth) % 360) + 180) % 360) -180);
        int xTrans = (int) (-8.0 * relAngle) - 25;

        pointyBit.setTranslationX(xTrans);
        pointyBit.setRotation((float) -relAngle);
        cityText.setTranslationX(xTrans);
        distanceText.setTranslationX(xTrans);
    }

    public double toRad(double angle){
        return angle / 180 * Math.PI;
    }
    public double toDeg(double angle){
        return angle / Math.PI * 180;
    }

    // Need to find a way to format this to decide between km or m.
    public void getDistance(Location myLocation){
        distance = ((int)myLocation.distanceTo(cityLoc));
        String text = (distance / 1000.0) + " km";
        distanceText.setText(text);
    }

    public double getInitBearing(Location myLocation){
        double bearing;
        double dL = toRad(cityLoc.getLongitude() - myLocation.getLongitude());
        double Ta = toRad(myLocation.getLatitude());
        double Tb = toRad(cityLoc.getLatitude());

        double X = Math.cos(Tb) * Math.sin(dL);
        double Y = Math.cos(Ta) * Math.sin(Tb) - Math.sin(Ta) * Math.cos(Tb) * Math.cos(dL);

        bearing = Math.atan2(X, Y); // May need to be swapped

        return toDeg(bearing);
    }

    private void buildTheBastard(ConstraintLayout constraintLayout, Context context){

        int screenWidth = activity.findViewById(R.id.activity_main).getWidth();
        constraintSet.clone(constraintLayout);
        int startMargin;

        // Arrow
        pointyBit = new ImageView(context);
        pointyBit.setImageResource(R.drawable.pointer);
        pointyBit.setLayoutParams(new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        pointyBit.setId(View.generateViewId());
        int height = 500;
        int width = 400;
        pointyBit.setAdjustViewBounds(true);
        pointyBit.setScaleType(ImageView.ScaleType.FIT_CENTER);
        pointyBit.setMaxHeight(height);
        pointyBit.setMaxWidth(width);
        startMargin = ((screenWidth - pointyBit.getWidth()) / 2) + 340;
        constraintSet.constrainHeight(pointyBit.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(pointyBit.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(pointyBit.getId(), ConstraintSet.BOTTOM, R.id.activity_main, ConstraintSet.BOTTOM, 350);
        constraintSet.connect(pointyBit.getId(), ConstraintSet.START, R.id.activity_main, ConstraintSet.START, startMargin);

        // Name Text
        cityText = new TextView(context);
        cityText.setText(name);
        cityText.setLayoutParams(new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        cityText.setId(View.generateViewId());
        cityText.setTextColor(0xFA005EFF);
        cityText.setTextSize(22);
        cityText.setWidth(1100);
        cityText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cityText.setVisibility(View.VISIBLE);
        startMargin = (screenWidth - cityText.getWidth()) / 2;
        constraintSet.constrainHeight(cityText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(cityText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(cityText.getId(), ConstraintSet.BOTTOM, R.id.activity_main, ConstraintSet.BOTTOM, 250);
        constraintSet.connect(cityText.getId(), ConstraintSet.START, R.id.activity_main, ConstraintSet.START, startMargin);

        // Distance Text
        distanceText = new TextView(context);
        distanceText.setText("");
        distanceText.setLayoutParams(new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        distanceText.setId(View.generateViewId());
        distanceText.setTextColor(0xFA228DFF);
        distanceText.setTextSize(16);
        distanceText.setWidth(1100);
        distanceText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        distanceText.setVisibility(View.VISIBLE);
        startMargin = (screenWidth - distanceText.getWidth()) / 2;
        constraintSet.constrainHeight(distanceText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(distanceText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(distanceText.getId(), ConstraintSet.BOTTOM, R.id.activity_main, ConstraintSet.BOTTOM, 185);
        constraintSet.connect(distanceText.getId(), ConstraintSet.START, R.id.activity_main, ConstraintSet.START, startMargin);

        // Apply constraints and show
        constraintLayout.addView(distanceText);
        constraintLayout.addView(cityText);
        constraintLayout.addView(pointyBit);
        constraintSet.applyTo(constraintLayout);
    }
}


