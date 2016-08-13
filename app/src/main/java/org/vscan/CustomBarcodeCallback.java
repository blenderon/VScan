package org.vscan;

import android.app.Activity;

import com.journeyapps.barcodescanner.BarcodeCallback;

/**
 * Created by Magellana on 8/13/2016.
 */
public abstract class CustomBarcodeCallback implements BarcodeCallback {
    Activity m_activity;
    public CustomBarcodeCallback(Activity activity) {
        m_activity = activity;
    }
}
