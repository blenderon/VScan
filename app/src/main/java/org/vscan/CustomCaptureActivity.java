package org.vscan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class CustomCaptureActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = CustomCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;

//    EditText editRollno;
//    Button btnSearch,btnViewAll;
    SQLiteDatabase db;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText editSearch;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barcodeView.setStatusText(result.getText());
            }
            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcode_scanner);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db=openOrCreateDatabase("VeganDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS products(barcode VARCHAR,name VARCHAR,status VARCHAR);");
        DBHelper.populateDB(this.getApplicationContext(),db);
        Cursor c=db.rawQuery("SELECT * FROM products", null);
        if(c.getCount()==0)
        {
            db.execSQL("INSERT INTO products VALUES('7290013085610','פסטה פנה אורגני','VEGAN');");
            db.execSQL("INSERT INTO products VALUES('7290013085611','פסטה פנה','NOT VEGAN');");
        }

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if(id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorites) {
            Intent intent = new Intent(this, MyFavorites.class);
            startActivity(intent);
        } else if (id == R.id.nav_score) {
            Intent intent = new Intent(this, MyScore.class);
            startActivity(intent);
        } else if (id == R.id.nav_products_data_base) {
            Intent intent = new Intent(this, MyDB.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onClick(View v) {
//        if(v.getId()==R.id.scan_button){
//            //scan
//            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
//            scanIntegrator.setPrompt("Scan a barcode");
//            scanIntegrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
//            scanIntegrator.setCaptureLayout(R.layout.scan_layout);
//            scanIntegrator.initiateScan();
//        }
//        if(v==btnViewAll)
//        {
//            Cursor c=db.rawQuery("SELECT * FROM products", null);
//            if(c.getCount()==0)
//            {
//                showMessage("שגיאה", "אין מוצרים");
//                return;
//            }
//            StringBuffer buffer=new StringBuffer();
//            while(c.moveToNext())
//            {
//                buffer.append("ברקוד: "+c.getString(0)+"\n");
//                buffer.append("שם: "+c.getString(1)+"\n");
//                buffer.append("טבעונות "+c.getString(2)+"\n\n");
//            }
//            showMessage("פרטים", buffer.toString());
//        }
//        if(v==btnSearch)
//        {
//            Cursor c=db.rawQuery("SELECT * FROM products where barcode='"+editRollno.getText()+"'", null);
//            if(c.getCount()==0)
//            {
//                showMessage("שגיאה", "לא נמצא");
//                return;
//            }
//            StringBuffer buffer=new StringBuffer();
//            while(c.moveToNext())
//            {
//                buffer.append("ברקוד: "+c.getString(0)+"\n");
//                buffer.append("שם: "+c.getString(1)+"\n");
//                buffer.append("טבעונות "+c.getString(2)+"\n\n");
//            }
//            showMessage("פרטים", buffer.toString());
//        }
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            //retrieve scan result
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                //we have a result
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
//                formatTxt.setText("FORMAT: " + scanFormat);
//                contentTxt.setText("CONTENT: " + scanContent);
            } else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
//        editRollno.setText("");
//        editRollno.requestFocus();
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_menu_share));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            editSearch = (EditText)action.getCustomView().findViewById(R.id.editSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch(editSearch.getText().toString());
                        return true;
                    }
                    return false;
                }
            });


            editSearch.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_menu_share));

            isSearchOpened = true;
        }
    }
    private void doSearch(String query) {
        Cursor c = db.rawQuery("SELECT * FROM products where barcode='" + query + "'", null);
        if (c.getCount() == 0) {
            showMessage("שגיאה", "לא נמצא");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("ברקוד: " + c.getString(0) + "\n");
            buffer.append("שם: " + c.getString(1) + "\n");
            buffer.append("טבעונות " + c.getString(2) + "\n\n");
        }
        showMessage("פרטים", buffer.toString());
    }
}
