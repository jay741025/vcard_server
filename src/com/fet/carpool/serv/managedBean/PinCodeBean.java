package com.fet.carpool.serv.managedBean;

import java.util.List;	

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.test.CardInfoList;

@ManagedBean
public class PinCodeBean extends BaseManagedBean {

	private String accCode;
	private String seqNo;
	private String cardNo;
	private String status;
	private int amount;

	private List<CardInfoDto> cardList;

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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CardInfoDto> getCardList() {
		return cardList;
	}

	public void setCardList(List<CardInfoDto> cardList) {
		this.cardList = cardList;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void queryPinCodeList() {

		CardInfoDto criteria = new CardInfoDto();
		criteria.setAccCode(accCode);
		criteria.setSeqNo(seqNo);
		criteria.setNotes(cardNo);
		criteria.setStatus(status);
		criteria.setAmount(amount);
//		CardQueryService cardQueryService = (CardQueryService) getBeanById("cardQueryService");
//		int pageNo = 1;
//		int pageSize = BaseDao.MAX_PAGE_SIZE;
		cardList = null;

//		cardList = cardQueryService.getCardInfoList(criteria, pageNo, pageSize);
		
		CardInfoList myCardList = new CardInfoList( BaseDao.DEFAULT_PAGE_SIZE );
		myCardList.setQueryParam(criteria);
		cardList = myCardList;
		
		if (cardList == null || cardList.size() == 0)
			addMessage(FacesMessage.SEVERITY_WARN, "查詢範圍無資料");
//		else if (cardList.size() == pageSize)
//			addMessage(FacesMessage.SEVERITY_WARN, "已達查詢筆數上限");
		else
			addMessage(FacesMessage.SEVERITY_INFO, "查詢共 " + cardList.size() + " 筆資料");
	}

}
