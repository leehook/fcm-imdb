package it.leehook.fcm.bean;

import java.io.Serializable;

/**
 * 
 * Bean che rappresente il singolo giocatore
 * 
 * @author l.angelini
 * 
 */
public class Giocatore implements Serializable {

	private static final long serialVersionUID = -6143173104756882634L;

	private Integer ruolo;

	private String cognome;

	private String squadraAppartenenza;

	private String fantaSquadra;

	private String prezzo;

	private String voto;

	/**
	 * @return the ruolo
	 */
	public String getRuolo() {
		String siglaRuolo = "";
		switch (ruolo) {
		case 1:
			siglaRuolo = " P ";
			break;
		case 2:
			siglaRuolo = " D ";
			break;
		case 3:
			siglaRuolo = " C ";
			break;
		case 4:
			siglaRuolo = " A ";
			break;
		default:
			siglaRuolo = " - ";
			break;
		}
		return siglaRuolo;
	}

	/**
	 * @param ruolo
	 *            the ruolo to set
	 */
	public void setRuolo(Integer ruolo) {
		this.ruolo = ruolo;
	}

	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @param cognome
	 *            the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @return the squadraAppartenenza
	 */
	public String getSquadraAppartenenza() {
		return squadraAppartenenza;
	}

	/**
	 * @param squadraAppartenenza
	 *            the squadraAppartenenza to set
	 */
	public void setSquadraAppartenenza(String squadraAppartenenza) {
		this.squadraAppartenenza = squadraAppartenenza;
	}

	/**
	 * @return the fantaSquadra
	 */
	public String getFantaSquadra() {
		return fantaSquadra;
	}

	/**
	 * @param fantaSquadra
	 *            the fantaSquadra to set
	 */
	public void setFantaSquadra(String fantaSquadra) {
		this.fantaSquadra = fantaSquadra;
	}

	/**
	 * @return the prezzo
	 */
	public String getPrezzo() {
		return prezzo;
	}

	/**
	 * @param prezzo
	 *            the prezzo to set
	 */
	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	/**
	 * @return the voto
	 */
	public String getVoto() {
		return voto;
	}

	/**
	 * @param voto
	 *            the voto to set
	 */
	public void setVoto(String voto) {
		this.voto = voto;
	}
}
