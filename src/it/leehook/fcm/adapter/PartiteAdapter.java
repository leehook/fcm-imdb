package it.leehook.fcm.adapter;

import it.leehook.fcm.R;
import it.leehook.fcm.bean.Partita;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author l.angelini
 * 
 */
public class PartiteAdapter extends BaseAdapter implements Filterable {

	private LayoutInflater mInflater;
	private Context context;
	private List<Partita> list;
	private int[] colors = new int[] { Color.WHITE, Color.rgb(255, 100, 100) };
	private Typeface match;

	public PartiteAdapter(Context context, List<Partita> myList) {
		mInflater = LayoutInflater.from(context);
		match = Typeface.createFromAsset(context.getAssets(),
				"fonts/newrepublic.ttf");
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
		RisultatiView res;
		final int displayWidth = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth();
		if (convertView == null) {
			int colorPos = position % colors.length;
			convertView = mInflater.inflate(R.layout.partita, parent, false);
			res = new RisultatiView();
			final Partita partita = (Partita) getItem(position);
			res.vs = (TextView) convertView.findViewById(R.id.vs);
			if (partita.getGolCasa() == null) {
				// Caso prossima giornata o giornata non ancora disputata
				if (StringUtils.isEmpty(partita.getSquadraCasa())) {
					res.vs.setText("Partita non programmata");
				} else {
					res.vs.setText(partita.getSquadraCasa() + " vs "
							+ partita.getSquadraTrasferta());
				}
				res.vs.setTextColor(colors[colorPos]);
				res.vs.setWidth(displayWidth);
				res.vs.setTypeface(match);
				res.vs.setGravity(Gravity.CENTER);
				res.vs.setTextSize(Float.valueOf(16));
				if (partita.getFormazioneCasa() != null) {
					convertView.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Dialog dialog = new Dialog(v.getContext());
							dialog.getWindow().requestFeature(
									Window.FEATURE_CUSTOM_TITLE);
							dialog.setContentView(R.layout.next_form_dialog);
							dialog.getWindow().setFeatureInt(
									Window.FEATURE_CUSTOM_TITLE,
									R.layout.next_form_title);
							TextView titleSqCasa = (TextView) dialog
									.findViewById(R.id.titleSqCasa);
							titleSqCasa.setText(partita.getSquadraCasa()
									.toUpperCase());
							titleSqCasa
									.setMovementMethod(new ScrollingMovementMethod());
							titleSqCasa.setWidth(displayWidth * 32 / 100);
							TextView titleSqTr = (TextView) dialog
									.findViewById(R.id.titleSqTr);
							titleSqTr.setText(partita.getSquadraTrasferta()
									.toUpperCase());
							titleSqTr
									.setMovementMethod(new ScrollingMovementMethod());
							titleSqTr.setWidth(displayWidth * 32 / 100);
							ListView titolariCasa = (ListView) dialog
									.findViewById(R.id.formCasaNext);
							titolariCasa.setAdapter(new TabellinoAdapter(dialog
									.getContext(), partita.getFormazioneCasa()));
							ListView panchinaCasa = (ListView) dialog
									.findViewById(R.id.pancaCasaNext);
							panchinaCasa.setAdapter(new TabellinoAdapter(dialog
									.getContext(), partita.getPanchinaCasa()));
							ListView titolariTrasf = (ListView) dialog
									.findViewById(R.id.formTrasfertaNext);
							titolariTrasf.setAdapter(new TabellinoAdapter(
									dialog.getContext(), partita
											.getFormazioneTrasferta()));
							ListView panchinaTrasf = (ListView) dialog
									.findViewById(R.id.pancaTrasfertaNext);
							panchinaTrasf.setAdapter(new TabellinoAdapter(
									dialog.getContext(), partita
											.getPanchinaTrasferta()));
							dialog.setCancelable(true);
							dialog.show();
						}
					});
				}
			} else {
				// Caso risultati giornata
				res.sqCasa = (TextView) convertView.findViewById(R.id.sqCasa);
				res.sqCasa.setText(partita.getSquadraCasa());
				res.sqCasa.setTextColor(colors[colorPos]);
				res.sqCasa.setTypeface(match);
				res.sqCasa.setWidth(displayWidth * 80 / 100);
				res.golCasa = (TextView) convertView.findViewById(R.id.golCasa);
				res.golCasa.setText(partita.getGolCasa().substring(0, 1));
				res.golCasa.setTypeface(match);
				res.golCasa.setTextColor(colors[colorPos]);
				res.vs = (TextView) convertView.findViewById(R.id.vs);
				res.vs.setText("vs");
				res.vs.setTypeface(match);
				res.vs.setTextColor(colors[colorPos]);
				res.sqTrasferta = (TextView) convertView
						.findViewById(R.id.sqTr);
				res.sqTrasferta.setTextColor(colors[colorPos]);
				res.sqTrasferta.setTypeface(match);
				res.sqTrasferta.setText(partita.getSquadraTrasferta());
				res.golTrasferta = (TextView) convertView
						.findViewById(R.id.golTr);
				res.golTrasferta.setTextColor(colors[colorPos]);
				res.golTrasferta.setTypeface(match);
				res.golTrasferta.setText(partita.getGolTrasferta().substring(0,
						1));
				convertView.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Dialog dialog = new Dialog(v.getContext());
						dialog.getWindow().requestFeature(
								Window.FEATURE_CUSTOM_TITLE);
						dialog.setContentView(R.layout.tabellini_dialog);
						dialog.getWindow().setFeatureInt(
								Window.FEATURE_CUSTOM_TITLE,
								R.layout.dialog_title);
						TextView titleSqCasa = (TextView) dialog
								.findViewById(R.id.titleSqCasa);
						titleSqCasa.setText(partita.getSquadraCasa()
								.toUpperCase());
						titleSqCasa
								.setMovementMethod(new ScrollingMovementMethod());
						titleSqCasa.setWidth(displayWidth * 32 / 100);
						TextView titleGolCasa = (TextView) dialog
								.findViewById(R.id.titleGolCasa);
						titleGolCasa.setText(partita.getGolCasa().substring(0,
								1));
						titleGolCasa.setWidth(displayWidth * 10 / 100);
						TextView titleSqTr = (TextView) dialog
								.findViewById(R.id.titleSqTr);
						titleSqTr.setText(partita.getSquadraTrasferta()
								.toUpperCase());
						titleSqTr
								.setMovementMethod(new ScrollingMovementMethod());
						titleSqTr.setWidth(displayWidth * 32 / 100);
						TextView titleGolTr = (TextView) dialog
								.findViewById(R.id.titleGolTr);
						titleGolTr.setText(partita.getGolTrasferta().substring(
								0, 1));
						titleGolTr.setWidth(displayWidth * 10 / 100);
						ListView tabellinoCasa = (ListView) dialog
								.findViewById(R.id.formCasa);
						tabellinoCasa.setAdapter(new TabellinoAdapter(dialog
								.getContext(), partita.getFormazioneCasa()));
						ListView tabellinoTrasf = (ListView) dialog
								.findViewById(R.id.formTrasferta);
						tabellinoTrasf.setAdapter(new TabellinoAdapter(dialog
								.getContext(), partita.getFormazioneTrasferta()));
						((TextView) dialog.findViewById(R.id.nullCasa))
								.setWidth(displayWidth * 5 / 100);
						((TextView) dialog.findViewById(R.id.nullTr))
								.setWidth(displayWidth * 5 / 100);
						((TextView) dialog.findViewById(R.id.nullTotCasa))
								.setWidth(displayWidth * 5 / 100);
						((TextView) dialog.findViewById(R.id.nullTotTr))
								.setWidth(displayWidth * 5 / 100);
						((TextView) dialog.findViewById(R.id.labelModCasa))
								.setWidth(displayWidth * 30 / 100);
						((TextView) dialog.findViewById(R.id.labelModTr))
								.setWidth(displayWidth * 30 / 100);
						((TextView) dialog.findViewById(R.id.labelTotCasa))
								.setWidth(displayWidth * 30 / 100);
						((TextView) dialog.findViewById(R.id.labelTotTr))
								.setWidth(displayWidth * 30 / 100);
						TextView modCasa = (TextView) dialog
								.findViewById(R.id.modCasa);
						modCasa.setText(partita.getModificatoreCasa());
						modCasa.setWidth(displayWidth * 10 / 100);
						TextView modTrasf = (TextView) dialog
								.findViewById(R.id.modTrasferta);
						modTrasf.setText(partita.getModificatoreTrasferta());
						modCasa.setWidth(displayWidth * 10 / 100);
						TextView totCasa = (TextView) dialog
								.findViewById(R.id.totCasa);
						totCasa.setText(partita.getGolCasa().substring(3,
								partita.getGolCasa().length() - 2));
						totCasa.setWidth(displayWidth * 10 / 100);
						TextView totTr = (TextView) dialog
								.findViewById(R.id.totTrasf);
						totTr.setText(partita.getGolTrasferta().substring(3,
								partita.getGolTrasferta().length() - 2));
						totTr.setWidth(displayWidth * 10 / 100);
						dialog.setCancelable(true);
						dialog.show();
					}
				});
			}
			convertView.setTag(res);
		} else {
			res = (RisultatiView) convertView.getTag();
		}
		return convertView;
	}

	static class RisultatiView {
		TextView sqCasa;
		TextView golCasa;
		TextView vs;
		TextView sqTrasferta;
		TextView golTrasferta;
		TextView trattino;
	}
}
