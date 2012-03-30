package apt.tutorial;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class FeedService extends IntentService {

public static final String EXTRA_URL = "apt.tutorial.EXTRA_URL"; 	
public static final String EXTRA_MESSENGER="apt.tutorial.EXTRA_MESSENGER";

	public FeedService() {
		super("FeedService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
	    RSSReader reader=new RSSReader();
	    Messenger messenger=(Messenger)arg0.getExtras().get(EXTRA_MESSENGER);
	    Message msg=Message.obtain();
	    
	    try {
	      RSSFeed result=reader.load(arg0.getStringExtra(EXTRA_URL));
	      
	      msg.arg1=Activity.RESULT_OK;
	      msg.obj=result;
	    }
	    catch (Exception e) {
	      Log.e("LunchList", "Exception parsing feed", e);
	      msg.arg1=Activity.RESULT_CANCELED;
	      msg.obj=e;
	    }
	    
	    try {
	      messenger.send(msg);
	    }
	    catch (Exception e) {
	      Log.w("LunchList", "Exception sending results to activity", e);
	    }
	  }


	

}
