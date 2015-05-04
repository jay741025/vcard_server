package com.fet.carpool.serv.dto;

public class CardInfoDto {

	public static final String STATUS_INIT = "00";
	public static final String STATUS_ACTIVED = "50";
	public static final String STATUS_ACTIVE_RESERVED = "52";
	public static final String STATUS_USED = "90";

	private String accCode;
	private String seqNo;
	private int amount;
	private String status;
	private String notes;
	private String cardNoIn;
	private String cardNoOriginal;

	public String getAccCode() {
		return accCode;
	}

	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isStatusActive() {
		return STATUS_ACTIVED.equals(status) || STATUS_ACTIVE_RESERVED.equals(status);
	}

	public void setStatusToActived() {
		this.status = STATUS_ACTIVED;
	}
	
	public void setStatusToActiveReserved() {
		this.status = STATUS_ACTIVE_RESERVED;
	}
	
	public void setStatusToUsed() {
		this.status = STATUS_USED;
	}

	public String getCardNoIn() {
		return cardNoIn;
	}

	public void setCardNoIn(String cardNoIn) {
		this.cardNoIn = cardNoIn;
	}
	
	public String getCardNoOut() {
		return getNotes();
	}

	public String getCardNoOriginal() {
		return cardNoOriginal;
	}

	public void setCardNoOriginal(String cardNoOriginal) {
		this.cardNoOriginal = cardNoOriginal;
	}
	
	
}
