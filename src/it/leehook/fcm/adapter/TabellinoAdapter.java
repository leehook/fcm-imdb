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
public class TabellinoAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private List<Giocatore> list;
    private Context context;

    public TabellinoAdapter(Context context, List<Giocatore> myList) {
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
	SquadraView res;
	convertView = mInflater.inflate(R.layout.tabellino, parent, false);
	res = new SquadraView();
	final Giocatore player = (Giocatore) getItem(position);
	int displayWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
	int color = player.getRuolo().trim().equals("P") ? Color.rgb(153, 153, 0) : (player.getRuolo().trim().equals("D") ? Color.rgb(51,
		102, 0) : (player.getRuolo().trim().equals("C") ? Color.rgb(204, 0, 0) : (player.getRuolo().trim().equals("A") ? Color.rgb(
		51, 0, 255) : Color.BLACK)));
	res.ruolo = (TextView) convertView.findViewById(R.id.ruoloTab);
	res.ruolo.setText(player.getRuolo());
	res.ruolo.setTextColor(color);
	res.ruolo.setWidth(displayWidth * 5 / 100);
	res.nome = (TextView) convertView.findViewById(R.id.cognomeTab);
	res.nome.setText(player.getCognome());
	res.nome.setTextColor(color);
	res.nome.setWidth(displayWidth * 29 / 100);
	res.voto = (TextView) convertView.findViewById(R.id.votoTab);
	res.voto.setText(player.getVoto());
	res.voto.setTextColor(color);
	res.voto.setWidth(displayWidth * 13 / 100);
	convertView.setTag(res);
	return convertView;
    }

    static class SquadraView {
	TextView ruolo;
	TextView nome;
	TextView voto;
    }
}
