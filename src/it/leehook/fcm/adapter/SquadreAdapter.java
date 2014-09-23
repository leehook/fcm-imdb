package it.leehook.fcm.adapter;

import it.leehook.fcm.R;
import it.leehook.fcm.bean.Squadra;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author l.angelini
 * 
 */
public class SquadreAdapter extends BaseAdapter implements Filterable {

	private LayoutInflater mInflater;
	private Context context;
	private List<Squadra> list;
	private Typeface match;

	public SquadreAdapter(Context context, List<Squadra> myList) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		match = Typeface.createFromAsset(context.getAssets(),
				"fonts/candles.ttf");
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
		int displayWidth = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth();
		SquadraView res;
		convertView = mInflater.inflate(R.layout.squadra, parent, false);
		res = new SquadraView();
		final Squadra squadra = (Squadra) getItem(position);
		res.logoSquadra = (ImageView) convertView.findViewById(R.id.logoSq);
		res.logoSquadra.setMinimumWidth(displayWidth * 10 / 100);
		res.logoSquadra.setImageBitmap(squadra.getLogoSquadra());
		res.nomeSquadra = (TextView) convertView.findViewById(R.id.nomeSq);
		res.nomeSquadra.setText(squadra.getNomeSquadra().toUpperCase());
		res.nomeSquadra.setTextColor(Color.RED);
		res.nomeSquadra.setMinimumWidth(displayWidth * 70 / 100);
		res.nomeSquadra.setTypeface(match);
		res.fotoAllenatore = (ImageView) convertView.findViewById(R.id.fotoSq);
		res.fotoAllenatore.setMinimumWidth(displayWidth * 10 / 100);
		res.fotoAllenatore.setImageBitmap(squadra.getFotoAllenatore());
		res.nomeAllenatore = (TextView) convertView
				.findViewById(R.id.nomeAllenatore);
		res.nomeAllenatore.setText(squadra.getNomeAllenatore());
		res.nomeAllenatore.setTextColor(Color.LTGRAY);
		res.mailAllenatore = (TextView) convertView
				.findViewById(R.id.mailAllenatore);
		res.mailAllenatore.setText(squadra.getMailAllenatore());
		res.mailAllenatore.setTextColor(Color.LTGRAY);
		res.creditiAllenatore = (TextView) convertView
				.findViewById(R.id.creditiAllenatore);
		res.creditiAllenatore.setText(" " + squadra.getCreditiResidui());
		res.creditiAllenatore.setTextColor(Color.LTGRAY);
		convertView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Dialog dialog = new Dialog(v.getContext());
				dialog.setContentView(R.layout.rosa_dialog);
				dialog.setTitle("Rosa " + squadra.getNomeSquadra());
				dialog.getWindow().setTitleColor(Color.YELLOW);
				dialog.setCancelable(true);
				ListView rosa = (ListView) dialog.findViewById(R.id.players);
				rosa.setAdapter(new RosaAdapter(dialog.getContext(), squadra
						.getRosa()));
				dialog.show();
			}
		});
		convertView.setTag(res);
		return convertView;
	}

	static class SquadraView {
		ImageView logoSquadra;
		TextView nomeSquadra;
		ImageView fotoAllenatore;
		TextView nomeAllenatore;
		TextView mailAllenatore;
		TextView creditiAllenatore;
	}
}
