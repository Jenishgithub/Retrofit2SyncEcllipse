package com.example.retrofit2ecllipse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	TextView textView;
	List<Github> curatorlistsynchronous = new ArrayList<>();
	Retrofit retrofit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textView);
		BackgroundTask task = new BackgroundTask();
		task.execute();
	}

	private class BackgroundTask extends AsyncTask<Void, Void, List<Github>> {

		@Override
		protected void onPreExecute() {
			retrofit = new Retrofit.Builder()
					.baseUrl("https://api.github.com/")
					.addConverterFactory(GsonConverterFactory.create()).build();
		}

		@Override
		protected List<Github> doInBackground(Void... params) {
			IApiMethods service = retrofit.create(IApiMethods.class);

			for (String login : Data.githubList) {
				Call<Github> curators = service.getCurators(login);
				try {
					Response<Github> responsse = curators.execute();
					curatorlistsynchronous.add(responsse.body());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return curatorlistsynchronous;
		}

		@Override
		protected void onPostExecute(List<Github> curators) {

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < curatorlistsynchronous.size(); i++) {
				builder.append(curatorlistsynchronous.get(i).getBlog());
				builder.append("\n");
			}
			String result = builder.toString();
			textView.setText(result);
		}
	}

}
