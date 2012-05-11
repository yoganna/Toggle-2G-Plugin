package com.mb.toggle2g.plugin.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class DummyActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            Intent intent = new Intent();
            intent.setClassName("com.mb.toggle2g", "com.mb.toggle2g.Toggle2G");
            startActivity(intent);
            this.finish();
        }
        catch ( Exception e )
        {
            setContentView(R.layout.main);
            
            String msg = "This plugin requires the <a href=\"http://forum.xda-developers.com/showthread.php?t=739530\"> Toggle 2G </a> application to be installed before it will work!  It will not work without it so please don\'t give a bad rating!";
            TextView tv = (TextView) findViewById(R.id.msg);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(Html.fromHtml(msg));
        }
    }
}
