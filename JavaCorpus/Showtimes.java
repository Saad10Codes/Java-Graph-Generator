package com.cs.uwindsor.group.project.utils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Application;
import android.content.Context;

import com.cs.uwindsor.group.project.http.AsyncHttpClient;
import com.cs.uwindsor.group.project.http.AsyncHttpResponseHandler;
import com.cs.uwindsor.group.project.http.RequestParams;

public class Showtimes extends Application {

	private static AsyncHttpClient client;
	private static Semaphore s = new Semaphore(1, true);

	private static HashMap<String, ArrayList<Show>> showtimes = new HashMap<String, ArrayList<Show>>(25);

	public static class Show {

		public String theater = "";
		public String[] showtimes = new String[0];

		public Show (String theater, String[] showtimes) {

			this.theater = theater;
			this.showtimes = showtimes;
		}

		public String toString () {

			StringBuilder sb = new StringBuilder();
			sb.append(theater + "\n");

			int c = 0;
			if (s != null)
				for (String s : showtimes) {
					sb.append(((c > 0) ? ", " : "") + s);
					++c;
				}
			sb.append("\n\n");
			return sb.toString();
		}
	}

	public Showtimes () {

		super();
		getShowtimes(this);
	}

	public static void addShow (String name, Show show) {

		if (showtimes.get(name) == null) {

			ArrayList<Show> al = new ArrayList<Show>(10);
			al.add(show);
			showtimes.put(name, al);
		} else {

			showtimes.get(name).add(show);
		}
	}

	private static synchronized void getShowtimes (Context context) {

		getShowtimes(context, 0);
	}

	private static synchronized void getShowtimes (Context context, int date) {

		try {
			s.acquire();
			client = new AsyncHttpClient();
			String url = "http://www.google.com/movies";
			RequestParams p = new RequestParams();
			p.put("date", date + "");
			client.get(context, url, p, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess (String response) {

					Pattern p1 = Pattern
							.compile("<div class=theater>.*?<h2 class=name>.*?<a href.*?>(.*?)</a></h2>.*?<div class=info>.*?</div></div>"
									+ "<div class=showtimes>(.*?)(</div>)(?=(<div class=theater>|</div></div><p class=clear>))");
					Matcher m1 = p1.matcher(response);
					// For each theater
					while (m1.find()) {

						if (!m1.group(1).contains("<")) {
							// Log.i("Theater", m1.group(1));

							Pattern p2 = Pattern
									.compile("<div class=movie>.*?<div class=name>.*?<a href.*?>(.*?)</a>.*?</div>.*?<div class=times>(.*?)</div>.*?</div>");
							Matcher m2 = p2.matcher(m1.group(2));
							// Each movie
							while (m2.find()) {

								// Log.i("Movie", m2.group(1));
								Pattern p3 = Pattern
										.compile("<span style=\"color:\">.*?<!--  -->([0-9:]{1,5}).*?</span>");
								Matcher m3 = p3.matcher(m2.group(2));

								ArrayList<String> times = new ArrayList<String>();
								// Each showtime
								while (m3.find()) {
									// Log.i("Times", m3.group(1).toString());
									times.add(m3.group(1));
								}

								Show s = new Show(URLDecoder.decode(m1.group(1).toString()), times
										.toArray(new String[0]));
								Showtimes.addShow(
										URLDecoder.decode(m2.group(1).trim().toLowerCase().replace("&amp;", "&")), s);
							}
						}
					}
				}

				@Override
				public void onFailure (Throwable t) {

				}

				@Override
				public void onFinish () {

					s.release();
				}
			});
		} catch (Exception e) {
			s.release();
		}
	}

	public static synchronized String getShowtime (String name) {

		try {
			Set<String> keys = showtimes.keySet();
			for (String st : keys) {
				// Log.i("test",st.trim().toLowerCase());
				if (st.trim().toLowerCase().equals(name.trim().toLowerCase())) {
					if (showtimes.get(name.trim().toLowerCase()) != null) {

						StringBuilder sb = new StringBuilder();
						sb.append("Showtimes Today: \n\n");
						ArrayList<Show> shows = showtimes.get(name.trim().toLowerCase());

						for (Show s : shows) {
							sb.append(s.toString());
						}

						return sb.toString();
					}
				}
			}
			return null;
		} catch (Exception e) {

			return null;
		} finally {

		}
	}
}
