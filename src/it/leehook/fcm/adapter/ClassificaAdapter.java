package it.leehook.fcm.adapter;

import it.leehook.fcm.R;
import it.leehook.fcm.bean.Squadra;

import java.text.DecimalFormat;
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
import android.widget.TextView;

/**
 * @author l.angelini
 * 
 */
public class ClassificaAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private List<Squadra> list;
    private Context context;

    public ClassificaAdapter(Context context, List<Squadra> myList) {
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
	final int displayWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
	SquadraView res;
	convertView = mInflater.inflate(R.layout.classifica_item, parent, false);
	res = new SquadraView();
	final Squadra squadra = (Squadra) getItem(position);
	res.posizione = (TextView) convertView.findViewById(R.id.posizione);
	res.posizione.setText("" + Integer.valueOf(position + 1));
	res.posizione.setWidth(displayWidth * 5 / 100);
	res.posizione.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
	res.logoSquadra = (ImageView) convertView.findViewById(R.id.logo);
	res.logoSquadra.setImageBitmap(squadra.getLogoSquadra());
	res.logoSquadra.setMinimumWidth(displayWidth * 20 / 100);
	res.nomeSquadra = (TextView) convertView.findViewById(R.id.nomeSq);
	res.nomeSquadra.setText(squadra.getNomeSquadra());
	res.nomeSquadra.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
	res.nomeSquadra.setWidth(displayWidth * 61 / 100);
	res.nomeAllenatore = (TextView) convertView.findViewById(R.id.nomePres);
	res.nomeAllenatore.setText(squadra.getNomeAllenatore());
	res.nomeAllenatore.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
	res.nomeAllenatore.setWidth(displayWidth * 61 / 100);
	res.nomeAllenatore.setHorizontallyScrolling(true);
	res.punti = (TextView) convertView.findViewById(R.id.puntiSq);
	res.punti.setText(squadra.getPunti());
	res.punti.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
	res.punti.setWidth(displayWidth * 14 / 100);
	convertView.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Dialog dialog = new Dialog(v.getContext());
		dialog.setContentView(R.layout.classifica_dialog);
		dialog.setTitle(squadra.getNomeSquadra().toUpperCase());
		dialog.getWindow().setTitleColor(Color.YELLOW);
		dialog.setCancelable(true);
		TextView puntiCl = (TextView) dialog.findViewById(R.id.puntiCl);
		puntiCl.setWidth(displayWidth * 20 / 100);
		puntiCl.setText(squadra.getPunti());
		TextView vinteCl = (TextView) dialog.findViewById(R.id.vinteCl);
		vinteCl.setWidth(displayWidth * 10 / 100);
		vinteCl.setText(squadra.getPartiteVinte());
		TextView pareggiateCl = (TextView) dialog.findViewById(R.id.pareggiateCl);
		pareggiateCl.setWidth(displayWidth * 10 / 100);
		pareggiateCl.setText(squadra.getPartitePareggiate());
		TextView perseCl = (TextView) dialog.findViewById(R.id.perseCl);
		perseCl.setWidth(displayWidth * 10 / 100);
		perseCl.setText(squadra.getPartitePerse());
		TextView golFattiCl = (TextView) dialog.findViewById(R.id.golFattiCl);
		golFattiCl.setWidth(displayWidth * 10 / 100);
		golFattiCl.setText(squadra.getGolFatti());
		TextView golSubitiCl = (TextView) dialog.findViewById(R.id.golSubitiCl);
		golSubitiCl.setWidth(displayWidth * 10 / 100);
		golSubitiCl.setText(squadra.getGolSubiti());
		TextView totPuntiCl = (TextView) dialog.findViewById(R.id.totPuntiCl);
		totPuntiCl.setWidth(displayWidth * 20 / 100);
		Double puntiFatti = Double.valueOf(squadra.getTotPuntiFatti());
		totPuntiCl.setText("" + new DecimalFormat("#.##").format(puntiFatti));
		dialog.show();
	    }
	});
	convertView.setTag(res);
	return convertView;
    }

    static class SquadraView {
	ImageView logoSquadra;
	TextView nomeSquadra;
	TextView nomeAllenatore;
	TextView punti;
	TextView posizione;
    }
}
