/**
 * 
 */
package it.leehook.fcm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author l.angelini
 * 
 */
public class Partita implements Serializable {

	private static final long serialVersionUID = -3124953776298409126L;

	private String squadraCasa;

	private String squadraTrasferta;

	private String golCasa;

	private String golTrasferta;

	private String modificatoreCasa;

	private String modificatoreTrasferta;

	private List<Giocatore> formazioneCasa;

	private List<Giocatore> formazioneTrasferta;

	private List<Giocatore> panchinaCasa;

	private List<Giocatore> panchinaTrasferta;

	/**
	 * @return the squadraCasa
	 */
	public String getSquadraCasa() {
		return squadraCasa;
	}

	/**
	 * @param squadraCasa
	 *            the squadraCasa to set
	 */
	public void setSquadraCasa(String squadraCasa) {
		this.squadraCasa = squadraCasa;
	}

	/**
	 * @return the squadraTrasferta
	 */
	public String getSquadraTrasferta() {
		return squadraTrasferta;
	}

	/**
	 * @param squadraTrasferta
	 *            the squadraTrasferta to set
	 */
	public void setSquadraTrasferta(String squadraTrasferta) {
		this.squadraTrasferta = squadraTrasferta;
	}

	/**
	 * @return the golCasa
	 */
	public String getGolCasa() {
		return golCasa;
	}

	/**
	 * @param golCasa
	 *            the golCasa to set
	 */
	public void setGolCasa(String golCasa) {
		this.golCasa = golCasa;
	}

	/**
	 * @return the golTrasferta
	 */
	public String getGolTrasferta() {
		return golTrasferta;
	}

	/**
	 * @param golTrasferta
	 *            the golTrasferta to set
	 */
	public void setGolTrasferta(String golTrasferta) {
		this.golTrasferta = golTrasferta;
	}

	/**
	 * @return the formazioneCasa
	 */
	public List<Giocatore> getFormazioneCasa() {
		return formazioneCasa;
	}

	/**
	 * @param formazioneCasa
	 *            the formazioneCasa to set
	 */
	public void setFormazioneCasa(List<Giocatore> formazioneCasa) {
		this.formazioneCasa = formazioneCasa;
	}

	/**
	 * @return the formazioneTrasferta
	 */
	public List<Giocatore> getFormazioneTrasferta() {
		return formazioneTrasferta;
	}

	/**
	 * @param formazioneTrasferta
	 *            the formazioneTrasferta to set
	 */
	public void setFormazioneTrasferta(List<Giocatore> formazioneTrasferta) {
		this.formazioneTrasferta = formazioneTrasferta;
	}

	/**
	 * @return the modificatoreCasa
	 */
	public String getModificatoreCasa() {
		return modificatoreCasa;
	}

	/**
	 * @param modificatoreCasa
	 *            the modificatoreCasa to set
	 */
	public void setModificatoreCasa(String modificatoreCasa) {
		this.modificatoreCasa = modificatoreCasa;
	}

	/**
	 * @return the modificatoreTrasferta
	 */
	public String getModificatoreTrasferta() {
		return modificatoreTrasferta;
	}

	/**
	 * @param modificatoreTrasferta
	 *            the modificatoreTrasferta to set
	 */
	public void setModificatoreTrasferta(String modificatoreTrasferta) {
		this.modificatoreTrasferta = modificatoreTrasferta;
	}

	/**
	 * @return the panchinaCasa
	 */
	public List<Giocatore> getPanchinaCasa() {
		return panchinaCasa;
	}

	/**
	 * @param panchinaCasa
	 *            the panchinaCasa to set
	 */
	public void setPanchinaCasa(List<Giocatore> panchinaCasa) {
		this.panchinaCasa = panchinaCasa;
	}

	/**
	 * @return the panchinaTrasferta
	 */
	public List<Giocatore> getPanchinaTrasferta() {
		return panchinaTrasferta;
	}

	/**
	 * @param panchinaTrasferta
	 *            the panchinaTrasferta to set
	 */
	public void setPanchinaTrasferta(List<Giocatore> panchinaTrasferta) {
		this.panchinaTrasferta = panchinaTrasferta;
	}
}
