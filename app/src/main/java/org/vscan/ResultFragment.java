package org.vscan;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultFragment extends Fragment {

    public static String te = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        if(CustomCaptureActivity.barcodeView.getVisibility() == View.VISIBLE) {
            System.out.println("here2");
            CustomCaptureActivity.barcodeView.pause();
            CustomCaptureActivity.barcodeView.setVisibility(View.GONE);
        }

        TextView v2 = (TextView) getActivity().findViewById(R.id.te);
        v2.setText(te);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("here1");
        if(CustomCaptureActivity.barcodeView.getVisibility() == View.GONE) {
            CustomCaptureActivity.barcodeView.resume();
            CustomCaptureActivity.barcodeView.setVisibility(View.VISIBLE);
//            CustomCaptureActivity.barcodeView.decodeSingle(CustomCaptureActivity.callback);
        }
    }
}