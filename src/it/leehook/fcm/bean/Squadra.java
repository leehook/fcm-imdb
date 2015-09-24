package it.leehook.fcm.bean;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;

/**
 * Bean che rappresente la singola squadra
 * 
 * @author l.angelini
 * 
 */
public class Squadra implements Serializable {

    private static final long serialVersionUID = -6801029713737013449L;

    private Integer idSquadra;

    private String nomeSquadra;

    private String nomeAllenatore;

    private String creditiResidui;

    private String mailAllenatore;

    private Bitmap logoSquadra;

    private Bitmap fotoAllenatore;

    private String punti;

    private String partiteVinte;

    private String partitePareggiate;

    private String partitePerse;

    private String golFatti;

    private String golSubiti;

    private String totPuntiFatti;

    private List<Giocatore> rosa;

    /**
     * @return the nomeSquadra
     */
    public String getNomeSquadra() {
	return nomeSquadra;
    }

    /**
     * @return the idSquadra
     */
    public Integer getIdSquadra() {
	return idSquadra;
    }

    /**
     * @param idSquadra
     *            the idSquadra to set
     */
    public void setIdSquadra(Integer idSquadra) {
	this.idSquadra = idSquadra;
    }

    /**
     * @param nomeSquadra
     *            the nomeSquadra to set
     */
    public void setNomeSquadra(String nomeSquadra) {
	this.nomeSquadra = nomeSquadra;
    }

    /**
     * @return the nomeAllenatore
     */
    public String getNomeAllenatore() {
	return nomeAllenatore;
    }

    /**
     * @param nomeAllenatore
     *            the nomeAllenatore to set
     */
    public void setNomeAllenatore(String nomeAllenatore) {
	this.nomeAllenatore = nomeAllenatore;
    }

    /**
     * @return the creditiResidui
     */
    public String getCreditiResidui() {
	return creditiResidui;
    }

    /**
     * @param creditiResidui
     *            the creditiResidui to set
     */
    public void setCreditiResidui(String creditiResidui) {
	this.creditiResidui = creditiResidui;
    }

    /**
     * @return the mailAllenatore
     */
    public String getMailAllenatore() {
	return mailAllenatore;
    }

    /**
     * @param mailAllenatore
     *            the mailAllenatore to set
     */
    public void setMailAllenatore(String mailAllenatore) {
	this.mailAllenatore = mailAllenatore;
    }

    /**
     * @return the logoSquadra
     */
    public Bitmap getLogoSquadra() {
	return logoSquadra;
    }

    /**
     * @param logoSquadra
     *            the logoSquadra to set
     */
    public void setLogoSquadra(Bitmap logoSquadra) {
	this.logoSquadra = logoSquadra;
    }

    /**
     * @return the fotoAllenatore
     */
    public Bitmap getFotoAllenatore() {
	return fotoAllenatore;
    }

    /**
     * @param fotoAllenatore
     *            the fotoAllenatore to set
     */
    public void setFotoAllenatore(Bitmap fotoAllenatore) {
	this.fotoAllenatore = fotoAllenatore;
    }

    /**
     * @return the punti
     */
    public String getPunti() {
	return punti;
    }

    /**
     * @param punti
     *            the punti to set
     */
    public void setPunti(String punti) {
	this.punti = punti;
    }

    /**
     * @return the partiteVinte
     */
    public String getPartiteVinte() {
	return partiteVinte;
    }

    /**
     * @param partiteVinte
     *            the partiteVinte to set
     */
    public void setPartiteVinte(String partiteVinte) {
	this.partiteVinte = partiteVinte;
    }

    /**
     * @return the partitePareggiate
     */
    public String getPartitePareggiate() {
	return partitePareggiate;
    }

    /**
     * @param partitePareggiate
     *            the partitePareggiate to set
     */
    public void setPartitePareggiate(String partitePareggiate) {
	this.partitePareggiate = partitePareggiate;
    }

    /**
     * @return the partitePerse
     */
    public String getPartitePerse() {
	return partitePerse;
    }

    /**
     * @param partitePerse
     *            the partitePerse to set
     */
    public void setPartitePerse(String partitePerse) {
	this.partitePerse = partitePerse;
    }

    /**
     * @return the golFatti
     */
    public String getGolFatti() {
	return golFatti;
    }

    /**
     * @param golFatti
     *            the golFatti to set
     */
    public void setGolFatti(String golFatti) {
	this.golFatti = golFatti;
    }

    /**
     * @return the golSubiti
     */
    public String getGolSubiti() {
	return golSubiti;
    }

    /**
     * @param golSubiti
     *            the golSubiti to set
     */
    public void setGolSubiti(String golSubiti) {
	this.golSubiti = golSubiti;
    }

    /**
     * @return the totPuntiFatti
     */
    public String getTotPuntiFatti() {
	return totPuntiFatti;
    }

    /**
     * @param totPuntiFatti
     *            the totPuntiFatti to set
     */
    public void setTotPuntiFatti(String totPuntiFatti) {
	this.totPuntiFatti = totPuntiFatti;
    }

    /**
     * @return the rosa
     */
    public List<Giocatore> getRosa() {
	return rosa;
    }

    /**
     * @param rosa
     *            the rosa to set
     */
    public void setRosa(List<Giocatore> rosa) {
	this.rosa = rosa;
    }
}
