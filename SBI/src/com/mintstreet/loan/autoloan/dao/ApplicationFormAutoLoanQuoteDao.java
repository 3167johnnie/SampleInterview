package com.mintstreet.loan.autoloan.dao;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;

public class ApplicationFormAutoLoanQuoteDao extends GenericDao<Integer,ApplicationFormAutoLoanQuote>{
	private static final long serialVersionUID = -5961308936013306452L;
	public Integer getOldVisitId(Integer quoteId){
		List<Object> params = new LinkedList<Object>();
		String query="SELECT QUOTE_SEQUENCE_ID, QUOTE_VISIT_ID, QUOTE_NEW_VISIT_ID FROM RUPEEPOWER_OCAS_T_00099 WHERE QUOTE_SEQUENCE_ID =?";
		params.add(quoteId);
		@SuppressWarnings("unchecked")
		List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);
		if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] quotesList = Objectlist.toArray();
			if(quotesList!=null && quotesList.length>0){
				Object[] visitObj = (Object[])quotesList[0];

				int visitId = Integer.parseInt(visitObj[1].toString());
				return visitId;
			}
		}
		return 0;
	}
}
