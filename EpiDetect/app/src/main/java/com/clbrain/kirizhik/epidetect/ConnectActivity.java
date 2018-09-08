package com.clbrain.kirizhik.epidetect;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class ConnectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    TextView BTenable, SCenable, Visibility;
    TextView incomingMessages;
    StringBuilder messages = new StringBuilder();

    BluetoothConnectionService mBluetoothConnection;
    BluetoothDevice mBluetoothDevice;

    EditText etSend;

    CheckBox cr, lf;



    Button send;
    Button cls;

   private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Toasty.info(getApplicationContext(),"Bluetooth OFF", Toast.LENGTH_SHORT).show();
                        BTenable.setBackgroundColor(Color.GRAY);
                        SCenable.setBackgroundColor(Color.GRAY);
                        Visibility.setBackgroundColor(Color.GRAY);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        //toast = Toast.makeText(getApplicationContext(),"Bluetooth turning OFF", Toast.LENGTH_SHORT);
                        //toast.show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toasty.success(getApplicationContext(),"Bluetooth ON", Toast.LENGTH_SHORT).show();
                        BTenable.setBackgroundColor(Color.GREEN);
                        if(mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) Visibility.setBackgroundColor(Color.BLUE);
                        else Visibility.setBackgroundColor(Color.GRAY);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        //toast = Toast.makeText(getApplicationContext(),"Bluetooth turning ON", Toast.LENGTH_SHORT);
                        //toast.show();
                        break;
                }
            }
        }
    };

        private BroadcastReceiver mReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!mBluetoothDevices.contains(device)) {
                    mBluetoothDevices.add(device);
                    mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBluetoothDevices);
                    lvNewDevices.setAdapter(mDeviceListAdapter);
                }
            }
        }
    };

    public final BroadcastReceiver mReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Toasty.success(getApplicationContext(),"Bonded", Toast.LENGTH_SHORT).show();
                    mBluetoothDevice = mDevice;
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    //toast = Toast.makeText(getApplicationContext(),"Bonding", Toast.LENGTH_SHORT);
                    //toast.show();
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    //toast = Toast.makeText(getApplicationContext(),"None", Toast.LENGTH_SHORT);
                    //toast.show();
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Toasty.success(getApplicationContext(),"Discoverability enabled", Toast.LENGTH_SHORT).show();
                        Visibility.setBackgroundColor(Color.BLUE);
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Visibility.setBackgroundColor(Color.GRAY);
                        //Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Visibility.setBackgroundColor(Color.GRAY);
                        //Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Visibility.setBackgroundColor(Color.GRAY);
                        //Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Visibility.setBackgroundColor(Color.GRAY);
                        //Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    BroadcastReceiver mReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");

            messages.append(text);

            incomingMessages.setText(messages);

            final int scrollAmount = incomingMessages.getLayout().getLineTop(incomingMessages.getLineCount()) - incomingMessages.getHeight();
            if (scrollAmount > 0)
                incomingMessages.scrollTo(0, scrollAmount);
            else
                incomingMessages.scrollTo(0, 0);
        }
    };

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mReceiver);
            unregisterReceiver(mReceiver1);
            unregisterReceiver(mReceiver2);
            mBluetoothAdapter.cancelDiscovery();
            mDeviceListAdapter.clear();
            lvNewDevices.setAdapter(mDeviceListAdapter);
        } catch (Exception e) { }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        send.setVisibility(View.VISIBLE);
        cls.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);




        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        BTenable = (TextView) findViewById(R.id.BTenable);
        SCenable = (TextView) findViewById(R.id.SCenable);
        Visibility = (TextView) findViewById(R.id.visibility);
     //   etSend = (EditText) findViewById(R.id.editText);
      /**  cr = (CheckBox) findViewById(R.id.CR);
        lf = (CheckBox) findViewById(R.id.LF);**/
        incomingMessages = (TextView) findViewById(R.id.incomingMessages);
        incomingMessages.setMovementMethod(new ScrollingMovementMethod());;

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver3, new IntentFilter("incomingMessage"));

        mBluetoothDevices = new ArrayList<>();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvNewDevices.setOnItemClickListener(ConnectActivity.this);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver2, filter);

        if(mBluetoothAdapter.isEnabled()) BTenable.setBackgroundColor(Color.GREEN);
        else BTenable.setBackgroundColor(Color.GRAY);

        if(mBluetoothAdapter.isDiscovering()) SCenable.setBackgroundColor(Color.YELLOW);
        else SCenable.setBackgroundColor(Color.GRAY);
        /**if(mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) Visibility.setBackgroundColor(Color.BLUE);
        else Visibility.setBackgroundColor(Color.GRAY);
**/
        //hideSoftKeyboard();
/**     send = (Button) findViewById(R.id.btnSend);
        cls = (Button) findViewById(R.id.clr);**/

    }

    public void onOff(View view) {
        if(mBluetoothAdapter.isDiscovering() && mBluetoothAdapter.enable()) {
            lvNewDevices.setAdapter(null);

            mBluetoothAdapter.disable();

            IntentFilter bluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, bluetoothIntent);
        }
        else if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetoothIntent);

            IntentFilter bluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, bluetoothIntent);
        }
        else if(mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();

            IntentFilter bluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, bluetoothIntent);
        }
    }

    public void discover(View view) {
        if(!mBluetoothAdapter.isEnabled()) {
            SCenable.setBackgroundColor(Color.GRAY);
            Alerter.hide();
            Alerter.create(this)
                    .setTitle("Error")
                    .setText("Turn on bluetooth to start discovering")
                    .show();
        }
        else if(!mBluetoothAdapter.isDiscovering()) {
            SCenable.setBackgroundColor(Color.YELLOW);
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();

            Toasty.info(getApplicationContext(),"Looking for unpaired devices", Toast.LENGTH_SHORT).show();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver1, discoverDevicesIntent);
        }
        else {
            SCenable.setBackgroundColor(Color.GRAY);
            checkBTPermissions();

            mBluetoothAdapter.cancelDiscovery();

            Toasty.info(getApplicationContext(),"Canceling discovery", Toast.LENGTH_SHORT).show();

            lvNewDevices.setAdapter(null);

            //mBluetoothAdapter.startDiscovery();
            //IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            //registerReceiver(mReceiver1, discoverDevicesIntent);
        }
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        }
        else {
            //Toast toast = Toast.makeText(getApplicationContext(),"No need to check permissions. SDK version < LOLLIPOP", Toast.LENGTH_SHORT);
            //toast.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothAdapter.cancelDiscovery();

        String deviceName = mBluetoothDevices.get(i).getName();
        String deviceAddress = mBluetoothDevices.get(i).getAddress();

        Toasty.success(getApplicationContext(), deviceName + " " + deviceAddress, Toast.LENGTH_SHORT).show();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothDevices.get(i).createBond();

            mBluetoothDevice = mBluetoothDevices.get(i);
            mBluetoothConnection = new BluetoothConnectionService(ConnectActivity.this);
        }
    }

    public void startBluetoothConnection(BluetoothDevice device, UUID uuid) {
        mBluetoothConnection.startClient(device, uuid);
    }

    public void start(View view) {
        if(!mBluetoothAdapter.isEnabled()) {
            Alerter.hide();
            Alerter.create(this)
                    .setTitle("Error")
                    .setText("Turn on bluetooth firstly")
                    .show();
        }
        else {
            try {
                startConnection();
            }catch(Exception e) {
                Alerter.hide();
                Alerter.create(this)
                        .setTitle("Error")
                        .setText("Pair device to start connection")
                        .show();
            }
            if(mBluetoothAdapter.isDiscovering()) SCenable.setBackgroundColor(Color.YELLOW);
            else SCenable.setBackgroundColor(Color.GRAY);
        }
    }

    public void send(View view) {
        if(!mBluetoothAdapter.isEnabled()) {
            Alerter.hide();
            Alerter.create(this)
                    .setTitle("Error")
                    .setText("Turn on bluetooth firstly")
                    .show();
        }
        else {
            String mes = null;
            try {
                mes = etSend.getText().toString();
                if(cr.isChecked() && lf.isChecked()){
                    byte[] bytes = (mes + "\n" + "\r").getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                else if(!cr.isChecked() && lf.isChecked()){
                    byte[] bytes = (mes + "\n").getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                else if(cr.isChecked() && !lf.isChecked()){
                    byte[] bytes = (mes + "\r").getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                else {
                    byte[] bytes = mes.getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                messages.append("    >  " + mes + "\n");
                incomingMessages.setText(messages);
            }catch(Exception e) {
                Alerter.hide();
                Alerter.create(this)
                        .setTitle("Error")
                        .setText("Connect device")
                        .show();
            }
        }
    }

    public void sendText(String text){
        if(!mBluetoothAdapter.isEnabled()) {
            Alerter.hide();
            Alerter.create(this)
                    .setTitle("Error")
                    .setText("Turn on bluetooth firstly")
                    .show();
        }
        else {
            try {
                if(cr.isChecked() && lf.isChecked()){
                    byte[] bytes = (text + "\n" + "\r").getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                else if(!cr.isChecked() && lf.isChecked()){
                    byte[] bytes = (text + "\n").getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                else if(cr.isChecked() && !lf.isChecked()){
                    byte[] bytes = (text + "\r").getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                else {
                    byte[] bytes = text.getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);

                    etSend.setText("");
                }
                messages.append("    >  " + text + "\n");
                incomingMessages.setText(messages);
            }catch(Exception e) {
                Alerter.hide();
                Alerter.create(this)
                        .setTitle("Error")
                        .setText("Connect device")
                        .show();
            }
        }
    }

    public void startConnection() {
        startBluetoothConnection(mBluetoothDevice, MY_UUID_INSECURE);
    }

   /* public void visibility(View view) {
        if(!mBluetoothAdapter.isEnabled()) {
            Alerter.hide();
            Alerter.create(this)
                    .setTitle("Error")
                    .setText("Turn on bluetooth firstly")
                    .show();
        }
        else if(mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            if(mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) Visibility.setBackgroundColor(Color.BLUE);
            else Visibility.setBackgroundColor(Color.GRAY);
        }
        else {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivity(discoverableIntent);
            IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            registerReceiver(mBroadcastReceiver4, intentFilter);
        }
    }

    public void clr(View view) {
        messages.delete(0, messages.length());
        incomingMessages.setText(messages);
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }**/


}