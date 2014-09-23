package it.leehook.fcm.adapter;

import it.leehook.fcm.R;
import it.leehook.fcm.bean.Giocatore;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * @author l.angelini
 * 
 */
public class RosaAdapter extends BaseAdapter implements Filterable {

	private LayoutInflater mInflater;
	private List<Giocatore> list;
	private Context context;

	public RosaAdapter(Context context, List<Giocatore> myList) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.list = myList;
	}

	public Filter getFilter() {
		return null;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		RosaView res;
		convertView = mInflater.inflate(R.layout.rosa, parent, false);
		res = new RosaView();
		final Giocatore player = (Giocatore) getItem(position);
		int displayWidth = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth();
		int color = player.getRuolo().trim().equals("P") ? Color.rgb(153, 153,
				0) : (player.getRuolo().trim().equals("D") ? Color.rgb(51, 102,
				0) : (player.getRuolo().trim().equals("C") ? Color.rgb(204, 0,
				0) : (player.getRuolo().trim().equals("A") ? Color.rgb(51, 0,
				255) : Color.WHITE)));
		res.ruolo = (TextView) convertView.findViewById(R.id.ruolo);
		res.ruolo.setText(player.getRuolo());
		res.ruolo.setTextColor(color);
		res.ruolo.setWidth(displayWidth * 10 / 100);
		res.nome = (TextView) convertView.findViewById(R.id.cognome);
		res.nome.setText(player.getCognome() + " ("
				+ player.getSquadraAppartenenza() + ") ");
		res.nome.setTextColor(color);
		res.nome.setWidth(displayWidth * 60 / 100);
		res.prezzo = (TextView) convertView.findViewById(R.id.prezzo);
		res.prezzo.setText(player.getPrezzo() + " ");
		res.prezzo.setTextColor(color);
		res.prezzo.setWidth(displayWidth * 10 / 100);
		convertView.setVerticalScrollBarEnabled(false);
		convertView.setTag(res);
		return convertView;
	}

	static class RosaView {
		TextView ruolo;
		TextView nome;
		TextView prezzo;
	}
}
