package nl.tudelft.ch.chreader;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends CHReaderActivity {

    Button mButtonAlLid;
    Button mButtonGeenLid;
    
    String cardId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		resolveIntent(getIntent());
		
		mButtonAlLid = (Button) findViewById(R.id.button_al_lid);
		mButtonGeenLid = (Button) findViewById(R.id.button_geen_lid);
		
		addListenerOnButtons();
		
		// Hier moet een andere oplossing voor gevonden worden
		// Alert kan niet getoond worden in het createn, starten of resumen van een activity als deze aangemaakt wordt.
//		if (mAdapter == null) {
//            showMessage("Error", "NFC is niet geenabled");
//            finish();
//            return;
//        }
		
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);

		mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
	}
	
	public void addListenerOnButtons() {

		mButtonAlLid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(MainActivity.this, RegisterCardActivity.class);
				myIntent.putExtra("id", cardId); //Optional parameters
				MainActivity.this.startActivity(myIntent);
				
			}
 
		});
		
		mButtonGeenLid.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0){
				Intent myIntent = new Intent(MainActivity.this, RegisterCHActivity.class);
				myIntent.putExtra("id", cardId); //Optional parameters
				MainActivity.this.startActivity(myIntent);
			}
		});
 
	}

	@Override
	public void resolveIntent(Intent intent){
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			
			
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] id = tag.getId();
            String hexId = getHex(id);
            Log.d("NewIntent!", hexId);
            TextView tv1 = (TextView)findViewById(R.id.welcome_text);
            tv1.setText("Je persoonlijke kaart-code is: " + hexId);
            mButtonAlLid.setVisibility(View.VISIBLE);
            mButtonGeenLid.setVisibility(View.VISIBLE);
		}
	}
	
	private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
//            if (i > 0) {
//                sb.append(" ");
//            }
        }
        return sb.toString();
	}
}
