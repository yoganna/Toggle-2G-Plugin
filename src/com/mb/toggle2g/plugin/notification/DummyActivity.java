/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
