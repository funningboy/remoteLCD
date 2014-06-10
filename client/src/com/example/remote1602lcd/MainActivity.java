package com.example.remote1602lcd;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

	static EditText et_addr, et_port;
	static EditText et_str1, et_str2;
	static Button bt_send;
	static String addr;
	static int    port; 
	static Context table;
	static String str;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        final Context table = new Context();
        Context.STR1 str1 = table.new STR1();
        Context.STR2 str2 = table.new STR2();
        table.setStr1(str1); 
        table.setStr2(str2);
        
        // trigger thread event when the send button clicked
        // addr text
        et_addr = (EditText) findViewById(R.id.EditTextAddr); 
        // port text
        et_port = (EditText) findViewById(R.id.EditTextPort);
        // str1 msg text
        et_str1 = (EditText) findViewById(R.id.EditTextStr1);
        // str2 msg text
        et_str2 = (EditText) findViewById(R.id.EditTextStr2);
        // send button
        bt_send = (Button) findViewById(R.id.ButtonSend);
        bt_send.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		try {
        			// construct add, port and status update
      	    	  	addr = et_addr.getText().toString();
      	    	  	port = Integer.parseInt(et_port.getText().toString());
      	    	  	table.getStr1().setStr(et_str1.getText().toString());
      	    	  	table.getStr2().setStr(et_str2.getText().toString());
      	    	  	str  = JsonUtil.toJSon(table);
      	            // spawn as thread	  	
            		Thread t = new Thread(new ClientThread());
            		t.start();
        			t.sleep(500);
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}});    
    }
    
    // thread run
    public class ClientThread implements Runnable {
    	@Override
	     public void run() {
	      try{
	    	  Socket socket =new Socket(addr, port);
	    	  PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())),
						true);	
	    	  out.println(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
    

}
