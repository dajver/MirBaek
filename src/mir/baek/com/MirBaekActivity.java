package mir.baek.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.IOException;

public class MirBaekActivity extends Activity {

	Document doc = null;
	FileInputStream fis;
	Elements links;
	Menu myMenu = null;
	ProgressDialog progress;
	Elements tit;
	TextView tw;
	TextView twf;
	String url = "http://mirbaek.com";

	/** @return */
	public boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			request();
			return true;
		}
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Ошибка!");
		alertDialog.setMessage("Включите интернет");
		alertDialog.show();
		tw.setText("Нету истории из за отсутствия интернета :(");
		twf.setText("Нету описания :(");
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tw = (TextView) findViewById(R.id.textView1);
		twf = (TextView) findViewById(R.id.textView2);
		isOnline();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		myMenu = menu;
		menu.add(0, 1, 0, "Обновить").setIcon(R.drawable.refresh);
		menu.add(0, 2, 0, "Выйти").setIcon(R.drawable.exit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case 1: {
				progress = ProgressDialog.show(this, "Загрузка", "Подождите загружаем историю...", true);
				new Thread(new Runnable() {

					@Override
					public void run() {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								request();
								progress.dismiss();
							}
						});
					}
				}).start();
			}
				break;
			case 2:
				finish();
				break;
		}
		return true;
	}

	/** @return */
	public void request() {

		try {
			doc = Jsoup.connect(url).get();
			links = doc.select("#story_text");
			tit = doc.select("#story_title");
			for (Element link : links) {
				tw.setText(link.text());
				twf.setText(tit.text());
			}
		} catch (IOException e) {
			Log.d("", "" + e.toString());
		}
	}
}