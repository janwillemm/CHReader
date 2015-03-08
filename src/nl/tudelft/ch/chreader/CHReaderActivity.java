/**
 * 
 */
package nl.tudelft.ch.chreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.view.Menu;

/**
 * @author jw
 *
 */
public abstract class CHReaderActivity extends Activity {
	

	public NfcAdapter mAdapter;
    public PendingIntent mPendingIntent;
    
    public AlertDialog mDialog;

	public void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("NFC is gedisabled!");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }
	
	public void showMessage(String string, String string2) {
		if(mDialog == null){
			mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();
		}
        mDialog.setTitle(string);
        mDialog.setMessage(string2);
        mDialog.show();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

	@Override
    public void onNewIntent(Intent intent) {
    	setIntent(intent);
    	resolveIntent(intent);
    }
	
	public abstract void resolveIntent(Intent intent);

}
