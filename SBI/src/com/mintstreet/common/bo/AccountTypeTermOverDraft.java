package com.mintstreet.common.bo;

import java.io.Serializable;

public class AccountTypeTermOverDraft implements Serializable {

	private static final long serialVersionUID = 1L;
	private CommonQuote accountTypeTerm;
	private CommonQuote accountTypeOverDraft;
	private CommonQuote interestDuringMoratorium;
	private CommonQuote noInterestDuringMoratorium;
	private CommonQuote checkOff;
	private CommonQuote partialCheckOff;
	private CommonQuote noCheckOff;
	public CommonQuote getAccountTypeTerm() {
		return accountTypeTerm;
	}
	public void setAccountTypeTerm(CommonQuote accountTypeTerm) {
		this.accountTypeTerm = accountTypeTerm;
	}
	public CommonQuote getAccountTypeOverDraft() {
		return accountTypeOverDraft;
	}
	public void setAccountTypeOverDraft(CommonQuote accountTypeOverDraft) {
		this.accountTypeOverDraft = accountTypeOverDraft;
	}
	public CommonQuote getInterestDuringMoratorium() {
		return interestDuringMoratorium;
	}
	public void setInterestDuringMoratorium(CommonQuote interestDuringMoratorium) {
		this.interestDuringMoratorium = interestDuringMoratorium;
	}
	public CommonQuote getNoInterestDuringMoratorium() {
		return noInterestDuringMoratorium;
	}
	public void setNoInterestDuringMoratorium(CommonQuote noInterestDuringMoratorium) {
		this.noInterestDuringMoratorium = noInterestDuringMoratorium;
	}
	public CommonQuote getCheckOff() {
		return checkOff;
	}
	public void setCheckOff(CommonQuote checkOff) {
		this.checkOff = checkOff;
	}
	public CommonQuote getPartialCheckOff() {
		return partialCheckOff;
	}
	public void setPartialCheckOff(CommonQuote partialCheckOff) {
		this.partialCheckOff = partialCheckOff;
	}
	public CommonQuote getNoCheckOff() {
		return noCheckOff;
	}
	public void setNoCheckOff(CommonQuote noCheckOff) {
		this.noCheckOff = noCheckOff;
	}
	
}
