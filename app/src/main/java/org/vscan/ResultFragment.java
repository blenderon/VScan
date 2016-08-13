package org.vscan;

import android.app.Fragment;
import android.os.Bundle;
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

        Product product = DBHelper.getProduct(CustomCaptureActivity.db,te);

        ImageView im = (ImageView) getActivity().findViewById(R.id.im);
        ImageView im1 = (ImageView) getActivity().findViewById(R.id.im2);
        ImageView im2 = (ImageView) getActivity().findViewById(R.id.im3);
        TextView te0 = (TextView) getActivity().findViewById(R.id.te0);
        TextView te1 = (TextView) getActivity().findViewById(R.id.te1);
        TextView te2 = (TextView) getActivity().findViewById(R.id.te2);
        TextView te3 = (TextView) getActivity().findViewById(R.id.te3);
        TextView te4 = (TextView) getActivity().findViewById(R.id.te4);
        TextView te5 = (TextView) getActivity().findViewById(R.id.te5);
        TextView aaa = (TextView) getActivity().findViewById(R.id.aaa);
        im2.setVisibility(View.GONE);
        aaa.setVisibility(View.GONE);
        te5.setVisibility(View.GONE);

        if(product == null) {
            im.setImageResource(R.drawable.sad_cow);
            te0.setText("עדיין לא רשום");
        } else { // 7290000300139 =>  7290015673198
            if(product.is_vegan.equals("לא")) {
                im.setImageResource(R.drawable.sad_cow);
                te0.setText("לא טבעוני");
                te1.setText("מוצר: "+product.name);
                te2.setText("יצרן: "+product.company);
                if(product.barcode.equals("7290000300139")) {
                    im1.setImageResource(R.drawable.p1);
                    im2.setVisibility(View.VISIBLE);
                    im2.setImageResource(R.drawable.p2);
                    aaa.setVisibility(View.VISIBLE);

                } else {
                    if(product.barcode.equals("7290015673198")) {
                        im1.setImageResource(R.drawable.p2);
                    } else {
                        im1.setImageResource(R.drawable.p3);
                    }
                }
//                te3.setText(product.food_type);
//                te4.setText(product.product_type);
                te5.setText("מכיל: "+product.why_not);
                te5.setVisibility(View.VISIBLE);
            } else {
                im.setImageResource(R.drawable.happy_cow);
                te0.setText("טבעוני");
                te1.setText("מוצר: "+product.name);
                te2.setText("יצרן: "+product.company);
//                te3.setText(product.food_type);
//                te4.setText(product.product_type);
            }
        }

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