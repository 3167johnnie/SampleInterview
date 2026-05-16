package com.mintstreet.common.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mintstreet.common.entity.MasterCoApplicant;

public class UIBeanList extends UIBeanListStatic implements Serializable {

	private static final long serialVersionUID = -193155338093856427L;
	
	private Map<Integer, String> preferredLocation;
	private Map<Integer, String> statesRACPC;
	private Map<Integer, String>industryTypeData;
	private Map<Integer, String>industryTypeDataCoapplicant1;
	private List<MasterCoApplicant> coApplicants;
	private Map<Integer, String> relationships;
	private Map<Integer, String> years;
	private Map<Integer, String> appliendYears;
	private Map<Integer, String> citiesoptgrp1;
	private Map<Integer, String> citiesoptgrp2;
	private Map<Integer, String> districts;
	private Map<Integer, String> branches;
	private Map<Integer, String> localities;

	private Map<Integer, String> citiesoptgrp1Office;
	private Map<Integer, String> citiesoptgrp2Office;
	private Map<Integer, String> districtsOffice;
	private Map<Integer, String> branchesOffice;

	private Map<Integer, String> businessYears;
	private Map<Integer, String> businessMonths;
	private Map<Integer, String> businessMonthsCoapplicant;
	private Map<Integer, String> citiesoptgrp1Permanent;
	private Map<Integer, String> citiesoptgrp2Permanent;
	private Map<Integer, String> districtsPermanent;
	private Map<Integer, String> branchesPermanent;

	private Map<Integer, String> citiesoptgrp1Pickup;
	private Map<Integer, String> citiesoptgrp2Pickup;
	private Map<Integer, String> citiesCoapplicant1;	
	private Map<Integer, String> citiesoptgrp1Coapplicant1;
	private Map<Integer, String> citiesoptgrp2Coapplicant1;
	private Map<Integer, String> districtsCoapplicant1;
	private Map<Integer, String> branchesCoapplicant1;

	
	private Map<Integer, String> citiesCoapplicant2;
	private Map<Integer, String> citiesoptgrp1Coapplicant2;
	private Map<Integer, String> citiesoptgrp2Coapplicant2;
	private Map<Integer, String> districtsCoapplicant2;
	private Map<Integer, String> branchesCoapplicant2;


	private Map<Integer, String> citiesCoapplicant3;
	private Map<Integer, String> citiesoptgrp1Coapplicant3;
	private Map<Integer, String> citiesoptgrp2Coapplicant3;
	private Map<Integer, String> districtsCoapplicant3;
	private Map<Integer, String> branchesCoapplicant3;

	
	private Map<Integer, String> futureYears;
	private Map<Integer, String> futureMonths;
	private Map<Integer, String> yeartenorExistingHomeLoan;
	private Map<Integer, String> yeartenorExistingAutoLoan;
	private Map<Integer, String> courseTypes;
	private Map<Integer, String> institutListByCourseType;
	private Map<Integer, String> graduationListByCourseType;
	private Map<Integer, String> loanPurposes;
	private Map<Integer, String> propertyCategories;
	private Map<Integer, String> propertyTypes;
	private Map<Integer, String> employementTypes;
	private Map<Integer, String> employementTypesCoapplicants;
	private Map<Integer, String> employementTypesCoapplicants2;
	private Map<Integer, String> relationshipWithBank;
	private Map<Integer, String> occupationTypes;
	private Map<Integer, Integer> occupationCategories;
	
	private Map<Integer, String> residenceTypes;
	private Map<Integer, String> twoWhelerType;
	private Map<Integer, String> carMake;
	private Map<Integer, String> carModel;
	private Map<Integer, String> carVariant;
	private Map<String, String> carType;
	
	private Map<Integer, String> bikeMake;
	private Map<Integer, String> bikeModel;
	private Map<Integer, String> bikeVariant;
	private Map<Integer, String> propertyType;
	private Map<Integer, String> residentTypes;

	private Map<Integer, String> residentTypesCoApplicant;

	private Map<Integer, String> citiesoptgrp1WorkPlace;
	private Map<Integer, String> citiesoptgrp2WorkPlace;
	private Map<Integer, String> districtsWorkPlace;
	private Map<Integer, String> districtsPropertyPlace;
	private Map<Integer, String> branchesWorkPlace;
	private Map<Integer, String> branchesPropertyPlace;
	private Map<Integer, String> citiesoptgrp1PropertyPlace;
	private Map<Integer, String> citiesoptgrp2PropertyPlace;
	
	private Map<Integer, String> citiesoptgrp1Residence;
	private Map<Integer, String> citiesoptgrp2Residence;
	
	private Map<Integer, String> autodealers;
	private Map<Integer, String> professions;
	
	private Map<Integer, String> monthsTillCurrentMonth;
	private Map<Integer, String> monthsFromCurrentMonthTillDecember;
	private Map<Integer, String> salaryPackageData;
	
	private Map<Integer, String> yearsLeaseStart;
	private Map<Integer, String> monthsLeaseStart;
	
	private Map<Integer, String> yearsLeaseEnd;
	private Map<Integer, String> monthsLeaseEnd;
	private Map<Integer, String> dependents;
	
	private Map<Integer, String> tenureYear;
	private Map<Integer, String> tenureMonth;
	private Map<Integer, String> projects;
	
	private Map<Integer, String> modeOfRepayment;
	private Map<Integer, String> courseType;
	private Map<Integer, String> instituteNameList;
	private Map<Integer, String> highestEducationalQualification;

	private Map<Integer, String> completionYear;
	private Map<Integer, String> completionMonth;
	
	private Map<Integer, String> edvantageState;
	private Map<Integer, String> edvantageCity;
	private Map<Integer, String> edvantegeBranch;
	private Map<Integer, String> edvantageDistrict;
	private Map<Integer, String> edvantageCitygroup2;
	
	private Map<Integer, String> state;
	private Map<Integer, String> xpressCreditITcitiesgrp1;
	
	private Map<Integer, String> tractorMake;
	private Map<Integer, String> tractorModel;
	private Map<Integer, String> tractorVariant;
	
	private Map<Integer, String> combineHarvestorMake;
	private Map<Integer, String> combineHarvestorModel;
	private Map<Integer, String> combineHarvestorVariant;
	
	private Map<Integer, String> powerTillerMake;
	private Map<Integer, String> powerTillerModel;
	private Map<Integer, String> powerTillerVariant;
	private Map<Integer, String> typeOfSecurity;
	private Map<Integer, String> noOfAnimals;
	
	private Map<Integer, String> residenceState;
	private Map<Integer, String> residenceDistrict;

	private Map<Integer, String> districtsCoapplicant4;
	private Map<Integer, String> masterQualifications;
	private Map<Integer, String> masterAnnualIncome;
	private Map<Integer, String> companyName;
	private Map<Integer, String> natureOfBusiness;
	private Map<Integer, String> currentEmpYears;
	private Map<Integer, String> currentEmpMonth;
	private Map<Integer, String> cities;
	private Map<Integer, String> cardTypes;
	private Map<Integer, String> districtPickup;
	private Map<Integer, String> yeartenorExistingPersonalLoan;	
	private Map<Integer, String> pensionLoanTypes;
	private Map<Integer,String> pensionPayingBranches;
	private Map<Integer,String> pensionDistricts;
	private Map<Integer,String> preferredPayingBranches;
	private Map<Integer,String> preferredDistricts;
	private Map<Integer, String> educationTakeOverGroupBank;
	private Map<Integer, String> yearTenorExistingEducationTakeOverLoan;
	private Map<Integer, String> industryType;
	private Map<Integer, String> organisationName;
	private Map<Integer, String> pensionPayingCity;
	private Map<Integer, String> pensionPayingState;
	private Map<Integer, String> residenceSubDistrict;
	private Map<Integer, String> residenceVillage;
	private Map<Integer, String> contractStartYear;
	private Map<Integer, String> contractStartMonth;
	private Map<Integer, String> contractEndYear;
	private Map<Integer, String> contractEndMonth;
	private Map<Integer, String> corpSalaryPackageRank;
	
	private Map<String, String> cveProductCategories;
	
	private String consentHomeLoan;
	private String consentAutoLoanEtb;
	private String consentAutoLoanNtb;
	private String consentEducationLoanEtb;
	private String consentEducationLoanNtb;
	private String consentPersonalLoanEtb;
	private String consentPersonalLoanNtb;
	private String consentGoldLoanEtb;
	private String consentGoldLoanNtb;
	private String consentAgriLoanEtb;
	private String consentAgriLoanNtb;
	private String consentCveLoan;
	private String consentCveRevoke;
	private String consentCreditCardEtb;
	private String consentCreditCardNtb;
	private String consentExistingCreditCard;
	
	public Map<Integer, String> getState() {
		return state;
	}
	public void setState(Map<Integer, String> state) {
		this.state = state;
	}
	public Map<Integer, String> getEdvantageCitygroup2() {
		return edvantageCitygroup2;
	}
	public void setEdvantageCitygroup2(Map<Integer, String> edvantageCitygroup2) {
		this.edvantageCitygroup2 = edvantageCitygroup2;
	}
	public Map<Integer, String> getEdvantageDistrict() {
		return edvantageDistrict;
	}
	public void setEdvantageDistrict(Map<Integer, String> edvantageDistrict) {
		this.edvantageDistrict = edvantageDistrict;
	}
	public Map<Integer, String> getEdvantageState() {
		return edvantageState;
	}
	public void setEdvantageState(Map<Integer, String> edvantageState) {
		this.edvantageState = edvantageState;
	}
	public Map<Integer, String> getEdvantageCity() {
		return edvantageCity;
	}
	public void setEdvantageCity(Map<Integer, String> edvantageCity) {
		this.edvantageCity = edvantageCity;
	}
	public Map<Integer, String> getEdvantegeBranch() {
		return edvantegeBranch;
	}
	public void setEdvantegeBranch(Map<Integer, String> edvantegeBranch) {
		this.edvantegeBranch = edvantegeBranch;
	}
	public Map<Integer, String> getAutodealers() {
		return autodealers;
	}
	public void setAutodealers(Map<Integer, String> autodealers) {
		this.autodealers = autodealers;
	}
	public Map<Integer, String> getResidentTypes() {
		return residentTypes;
	}
	public void setResidentTypes(Map<Integer, String> residentTypes) {
		this.residentTypes = residentTypes;
	}
	public Map<Integer, String> getRelationships() {
		return relationships;
	}
	public void setRelationships(Map<Integer, String> relationships) {
		this.relationships = relationships;
	}
	public Map<Integer, String> getCitiesoptgrp1() {
		return citiesoptgrp1;
	}
	public void setCitiesoptgrp1(Map<Integer, String> citiesoptgrp1) {
		this.citiesoptgrp1 = citiesoptgrp1;
	}
	public Map<Integer, String> getCitiesoptgrp2() {
		return citiesoptgrp2;
	}
	public void setCitiesoptgrp2(Map<Integer, String> citiesoptgrp2) {
		this.citiesoptgrp2 = citiesoptgrp2;
	}
	public Map<Integer, String> getDistricts() {
		return districts;
	}
	public void setDistricts(Map<Integer, String> districts) {
		this.districts = districts;
	}
	public Map<Integer, String> getBranches() {
		return branches;
	}
	public void setBranches(Map<Integer, String> branches) {
		this.branches = branches;
	}
	public Map<Integer, String> getLocalities() {
		return localities;
	}
	public void setLocalities(Map<Integer, String> localities) {
		this.localities = localities;
	}
	
	public Map<Integer, String> getCitiesoptgrp1Office() {
		return citiesoptgrp1Office;
	}
	public void setCitiesoptgrp1Office(Map<Integer, String> citiesoptgrp1Office) {
		this.citiesoptgrp1Office = citiesoptgrp1Office;
	}
	public Map<Integer, String> getCitiesoptgrp2Office() {
		return citiesoptgrp2Office;
	}
	public void setCitiesoptgrp2Office(Map<Integer, String> citiesoptgrp2Office) {
		this.citiesoptgrp2Office = citiesoptgrp2Office;
	}
	public Map<Integer, String> getDistrictsOffice() {
		return districtsOffice;
	}
	public void setDistrictsOffice(Map<Integer, String> districtsOffice) {
		this.districtsOffice = districtsOffice;
	}
	public Map<Integer, String> getBranchesOffice() {
		return branchesOffice;
	}
	public void setBranchesOffice(Map<Integer, String> branchesOffice) {
		this.branchesOffice = branchesOffice;
	}
	
	public Map<Integer, String> getCitiesoptgrp1Permanent() {
		return citiesoptgrp1Permanent;
	}
	public void setCitiesoptgrp1Permanent(Map<Integer, String> citiesoptgrp1Permanent) {
		this.citiesoptgrp1Permanent = citiesoptgrp1Permanent;
	}
	public Map<Integer, String> getCitiesoptgrp2Permanent() {
		return citiesoptgrp2Permanent;
	}
	public void setCitiesoptgrp2Permanent(Map<Integer, String> citiesoptgrp2Permanent) {
		this.citiesoptgrp2Permanent = citiesoptgrp2Permanent;
	}
	public Map<Integer, String> getDistrictsPermanent() {
		return districtsPermanent;
	}
	public void setDistrictsPermanent(Map<Integer, String> districtsPermanent) {
		this.districtsPermanent = districtsPermanent;
	}
	public Map<Integer, String> getBranchesPermanent() {
		return branchesPermanent;
	}
	public void setBranchesPermanent(Map<Integer, String> branchesPermanent) {
		this.branchesPermanent = branchesPermanent;
	}
	
	public Map<Integer, String> getCitiesCoapplicant1() {
		return citiesCoapplicant1;
	}
	public void setCitiesCoapplicant1(Map<Integer, String> citiesCoapplicant1) {
		this.citiesCoapplicant1 = citiesCoapplicant1;
	}
	public Map<Integer, String> getCitiesoptgrp1Coapplicant1() {
		return citiesoptgrp1Coapplicant1;
	}
	public void setCitiesoptgrp1Coapplicant1(
			Map<Integer, String> citiesoptgrp1Coapplicant1) {
		this.citiesoptgrp1Coapplicant1 = citiesoptgrp1Coapplicant1;
	}
	public Map<Integer, String> getCitiesoptgrp2Coapplicant1() {
		return citiesoptgrp2Coapplicant1;
	}
	public void setCitiesoptgrp2Coapplicant1(
			Map<Integer, String> citiesoptgrp2Coapplicant1) {
		this.citiesoptgrp2Coapplicant1 = citiesoptgrp2Coapplicant1;
	}
	public Map<Integer, String> getDistrictsCoapplicant1() {
		return districtsCoapplicant1;
	}
	public void setDistrictsCoapplicant1(Map<Integer, String> districtsCoapplicant1) {
		this.districtsCoapplicant1 = districtsCoapplicant1;
	}
	public Map<Integer, String> getBranchesCoapplicant1() {
		return branchesCoapplicant1;
	}
	public void setBranchesCoapplicant1(Map<Integer, String> branchesCoapplicant1) {
		this.branchesCoapplicant1 = branchesCoapplicant1;
	}
	
	public Map<Integer, String> getCitiesCoapplicant2() {
		return citiesCoapplicant2;
	}
	public void setCitiesCoapplicant2(Map<Integer, String> citiesCoapplicant2) {
		this.citiesCoapplicant2 = citiesCoapplicant2;
	}
	public Map<Integer, String> getCitiesoptgrp1Coapplicant2() {
		return citiesoptgrp1Coapplicant2;
	}
	public void setCitiesoptgrp1Coapplicant2(
			Map<Integer, String> citiesoptgrp1Coapplicant2) {
		this.citiesoptgrp1Coapplicant2 = citiesoptgrp1Coapplicant2;
	}
	public Map<Integer, String> getCitiesoptgrp2Coapplicant2() {
		return citiesoptgrp2Coapplicant2;
	}
	public void setCitiesoptgrp2Coapplicant2(
			Map<Integer, String> citiesoptgrp2Coapplicant2) {
		this.citiesoptgrp2Coapplicant2 = citiesoptgrp2Coapplicant2;
	}
	public Map<Integer, String> getDistrictsCoapplicant2() {
		return districtsCoapplicant2;
	}
	public void setDistrictsCoapplicant2(Map<Integer, String> districtsCoapplicant2) {
		this.districtsCoapplicant2 = districtsCoapplicant2;
	}
	public Map<Integer, String> getBranchesCoapplicant2() {
		return branchesCoapplicant2;
	}
	public void setBranchesCoapplicant2(Map<Integer, String> branchesCoapplicant2) {
		this.branchesCoapplicant2 = branchesCoapplicant2;
	}
	
	public Map<Integer, String> getFutureYears() {
		return futureYears;
	}
	public void setFutureYears(Map<Integer, String> futureYears) {
		this.futureYears = futureYears;
	}
	public Map<Integer, String> getYeartenorExistingHomeLoan() {
		return yeartenorExistingHomeLoan;
	}
	public void setYeartenorExistingHomeLoan(
			Map<Integer, String> yeartenorExistingHomeLoan) {
		this.yeartenorExistingHomeLoan = yeartenorExistingHomeLoan;
	}
	public Map<Integer, String> getYeartenorExistingAutoLoan() {
		return yeartenorExistingAutoLoan;
	}
	public void setYeartenorExistingAutoLoan(
			Map<Integer, String> yeartenorExistingAutoLoan) {
		this.yeartenorExistingAutoLoan = yeartenorExistingAutoLoan;
	}
	public Map<Integer, String> getInstitutListByCourseType() {
		return institutListByCourseType;
	}
	public void setInstitutListByCourseType(
			Map<Integer, String> institutListByCourseType) {
		this.institutListByCourseType = institutListByCourseType;
	}
	public Map<Integer, String> getGraduationListByCourseType() {
		return graduationListByCourseType;
	}
	public void setGraduationListByCourseType(
			Map<Integer, String> graduationListByCourseType) {
		this.graduationListByCourseType = graduationListByCourseType;
	}
	public Map<Integer, String> getLoanPurposes() {
		return loanPurposes;
	}
	public void setLoanPurposes(Map<Integer, String> loanPurposes) {
		this.loanPurposes = loanPurposes;
	}
	public Map<Integer, String> getPropertyCategories() {
		return propertyCategories;
	}
	public void setPropertyCategories(Map<Integer, String> propertyCategories) {
		this.propertyCategories = propertyCategories;
	}
	public Map<Integer, String> getPropertyTypes() {
		return propertyTypes;
	}
	public void setPropertyTypes(Map<Integer, String> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}
	public Map<Integer, String> getEmployementTypes() {
		return employementTypes;
	}
	public void setEmployementTypes(Map<Integer, String> employementTypes) {
		this.employementTypes = employementTypes;
	}
	public Map<Integer, String> getTwoWhelerType() {
		return twoWhelerType;
	}
	public void setTwoWhelerType(Map<Integer, String> twoWhelerType) {
		this.twoWhelerType = twoWhelerType;
	}
	public Map<Integer, String> getCarMake() {
		return carMake;
	}
	public void setCarMake(Map<Integer, String> carMake) {
		this.carMake = carMake;
	}
	public Map<Integer, String> getCarModel() {
		return carModel;
	}
	public void setCarModel(Map<Integer, String> carModel) {
		this.carModel = carModel;
	}
	public Map<Integer, String> getCarVariant() {
		return carVariant;
	}
	public void setCarVariant(Map<Integer, String> carVariant) {
		this.carVariant = carVariant;
	}
	public Map<Integer, String> getBikeMake() {
		return bikeMake;
	}
	public void setBikeMake(Map<Integer, String> bikeMake) {
		this.bikeMake = bikeMake;
	}
	public Map<Integer, String> getBikeModel() {
		return bikeModel;
	}
	public void setBikeModel(Map<Integer, String> bikeModel) {
		this.bikeModel = bikeModel;
	}
	public Map<Integer, String> getBikeVariant() {
		return bikeVariant;
	}
	public void setBikeVariant(Map<Integer, String> bikeVariant) {
		this.bikeVariant = bikeVariant;
	}
	public Map<Integer, String> getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(Map<Integer, String> propertyType) {
		this.propertyType = propertyType;
	}
	public Map<Integer, String> getYears() {
		return years;
	}
	public void setYears(Map<Integer, String> years) {
		this.years = years;
	}
	
	public  Map<Integer, String> getIndustryTypeData() {
		return industryTypeData;
	}
	public  void setIndustryTypeData(Map<Integer, String> industryTypeData) {
		this.industryTypeData = industryTypeData;
	}
	public List<MasterCoApplicant> getCoApplicants() {
		return coApplicants;
	}
	public void setCoApplicants(List<MasterCoApplicant> coApplicants) {
		this.coApplicants = coApplicants;
	}
	public Map<Integer, String> getRelationshipWithBank() {
		return relationshipWithBank;
	}
	public void setRelationshipWithBank(Map<Integer, String> relationshipWithBank) {
		this.relationshipWithBank = relationshipWithBank;
	}
	public Map<Integer, String> getResidenceTypes() {
		return residenceTypes;
	}
	public void setResidenceTypes(Map<Integer, String> residenceTypes) {
		this.residenceTypes = residenceTypes;
	}
	public Map<Integer, String> getAppliendYears() {
		return appliendYears;
	}
	public void setAppliendYears(Map<Integer, String> appliendYears) {
		this.appliendYears = appliendYears;
	}
	public Map<Integer, String> getCitiesoptgrp1WorkPlace() {
		return citiesoptgrp1WorkPlace;
	}
	public void setCitiesoptgrp1WorkPlace(Map<Integer, String> citiesoptgrp1WorkPlace) {
		this.citiesoptgrp1WorkPlace = citiesoptgrp1WorkPlace;
	}
	public Map<Integer, String> getCitiesoptgrp2WorkPlace() {
		return citiesoptgrp2WorkPlace;
	}
	public void setCitiesoptgrp2WorkPlace(Map<Integer, String> citiesoptgrp2WorkPlace) {
		this.citiesoptgrp2WorkPlace = citiesoptgrp2WorkPlace;
	}
	public Map<Integer, String> getCitiesoptgrp1PropertyPlace() {
		return citiesoptgrp1PropertyPlace;
	}
	public void setCitiesoptgrp1PropertyPlace(
			Map<Integer, String> citiesoptgrp1PropertyPlace) {
		this.citiesoptgrp1PropertyPlace = citiesoptgrp1PropertyPlace;
	}
	public Map<Integer, String> getCitiesoptgrp2PropertyPlace() {
		return citiesoptgrp2PropertyPlace;
	}
	public void setCitiesoptgrp2PropertyPlace(
			Map<Integer, String> citiesoptgrp2PropertyPlace) {
		this.citiesoptgrp2PropertyPlace = citiesoptgrp2PropertyPlace;
	}
	public Map<Integer, String> getCitiesoptgrp1Residence() {
		return citiesoptgrp1Residence;
	}
	public void setCitiesoptgrp1Residence(Map<Integer, String> citiesoptgrp1Residence) {
		this.citiesoptgrp1Residence = citiesoptgrp1Residence;
	}
	public Map<Integer, String> getCitiesoptgrp2Residence() {
		return citiesoptgrp2Residence;
	}
	public void setCitiesoptgrp2Residence(Map<Integer, String> citiesoptgrp2Residence) {
		this.citiesoptgrp2Residence = citiesoptgrp2Residence;
	}
	public Map<Integer, String> getResidentTypesCoApplicant() {
		return residentTypesCoApplicant;
	}
	public void setResidentTypesCoApplicant(
			Map<Integer, String> residentTypesCoApplicant) {
		this.residentTypesCoApplicant = residentTypesCoApplicant;
	}
	public Map<Integer, String> getBusinessYears() {
		return businessYears;
	}
	public void setBusinessYears(Map<Integer, String> businessYears) {
		this.businessYears = businessYears;
	}
	public Map<Integer, String> getProfessions() {
		return professions;
	}
	public void setProfessions(Map<Integer, String> professions) {
		this.professions = professions;
	}
	public Map<Integer, String> getCourseTypes() {
		return courseTypes;
	}
	public void setCourseTypes(Map<Integer, String> courseTypes) {
		this.courseTypes = courseTypes;
	}
	public Map<Integer, String> getMonthsTillCurrentMonth() {
		return monthsTillCurrentMonth;
	}
	public void setMonthsTillCurrentMonth(
			Map<Integer, String> monthsTillCurrentMonth) {
		this.monthsTillCurrentMonth = monthsTillCurrentMonth;
	}
	public Map<Integer, String> getMonthsFromCurrentMonthTillDecember() {
		return monthsFromCurrentMonthTillDecember;
	}
	public void setMonthsFromCurrentMonthTillDecember(
			Map<Integer, String> monthsFromCurrentMonthTillDecember) {
		this.monthsFromCurrentMonthTillDecember = monthsFromCurrentMonthTillDecember;
	}
	public Map<Integer, String> getSalaryPackageData() {
		return salaryPackageData;
	}
	public void setSalaryPackageData(Map<Integer, String> salaryPackageData) {
		this.salaryPackageData = salaryPackageData;
	}
	public Map<Integer, String> getDistrictsWorkPlace() {
		return districtsWorkPlace;
	}
	public void setDistrictsWorkPlace(Map<Integer, String> districtsWorkPlace) {
		this.districtsWorkPlace = districtsWorkPlace;
	}
	public Map<Integer, String> getDistrictsPropertyPlace() {
		return districtsPropertyPlace;
	}
	public void setDistrictsPropertyPlace(
			Map<Integer, String> districtsPropertyPlace) {
		this.districtsPropertyPlace = districtsPropertyPlace;
	}
	public Map<Integer, String> getBranchesWorkPlace() {
		return branchesWorkPlace;
	}
	public void setBranchesWorkPlace(Map<Integer, String> branchesWorkPlace) {
		this.branchesWorkPlace = branchesWorkPlace;
	}
	public Map<Integer, String> getBranchesPropertyPlace() {
		return branchesPropertyPlace;
	}
	public void setBranchesPropertyPlace(Map<Integer, String> branchesPropertyPlace) {
		this.branchesPropertyPlace = branchesPropertyPlace;
	}
	public Map<Integer, String> getYearsLeaseStart() {
		return yearsLeaseStart;
	}
	public void setYearsLeaseStart(Map<Integer, String> yearsLeaseStart) {
		this.yearsLeaseStart = yearsLeaseStart;
	}
	public Map<Integer, String> getMonthsLeaseStart() {
		return monthsLeaseStart;
	}
	public void setMonthsLeaseStart(Map<Integer, String> monthsLeaseStart) {
		this.monthsLeaseStart = monthsLeaseStart;
	}
	public Map<Integer, String> getFutureMonths() {
		return futureMonths;
	}
	public void setFutureMonths(Map<Integer, String> futureMonths) {
		this.futureMonths = futureMonths;
	}
	public Map<Integer, String> getBusinessMonths() {
		return businessMonths;
	}
	public void setBusinessMonths(Map<Integer, String> businessMonths) {
		this.businessMonths = businessMonths;
	}
	public Map<Integer, String> getYearsLeaseEnd() {
		return yearsLeaseEnd;
	}
	public void setYearsLeaseEnd(Map<Integer, String> yearsLeaseEnd) {
		this.yearsLeaseEnd = yearsLeaseEnd;
	}
	public Map<Integer, String> getMonthsLeaseEnd() {
		return monthsLeaseEnd;
	}
	public void setMonthsLeaseEnd(Map<Integer, String> monthsLeaseEnd) {
		this.monthsLeaseEnd = monthsLeaseEnd;
	}
	public Map<Integer, String> getDependents() {
		return dependents;
	}
	public void setDependents(Map<Integer, String> dependents) {
		this.dependents = dependents;
	}
	public Map<Integer, String> getCitiesCoapplicant3() {
		return citiesCoapplicant3;
	}
	public void setCitiesCoapplicant3(Map<Integer, String> citiesCoapplicant3) {
		this.citiesCoapplicant3 = citiesCoapplicant3;
	}
	public Map<Integer, String> getCitiesoptgrp1Coapplicant3() {
		return citiesoptgrp1Coapplicant3;
	}
	public void setCitiesoptgrp1Coapplicant3(
			Map<Integer, String> citiesoptgrp1Coapplicant3) {
		this.citiesoptgrp1Coapplicant3 = citiesoptgrp1Coapplicant3;
	}
	public Map<Integer, String> getCitiesoptgrp2Coapplicant3() {
		return citiesoptgrp2Coapplicant3;
	}
	public void setCitiesoptgrp2Coapplicant3(
			Map<Integer, String> citiesoptgrp2Coapplicant3) {
		this.citiesoptgrp2Coapplicant3 = citiesoptgrp2Coapplicant3;
	}
	public Map<Integer, String> getDistrictsCoapplicant3() {
		return districtsCoapplicant3;
	}
	public void setDistrictsCoapplicant3(Map<Integer, String> districtsCoapplicant3) {
		this.districtsCoapplicant3 = districtsCoapplicant3;
	}
	public Map<Integer, String> getBranchesCoapplicant3() {
		return branchesCoapplicant3;
	}
	public void setBranchesCoapplicant3(Map<Integer, String> branchesCoapplicant3) {
		this.branchesCoapplicant3 = branchesCoapplicant3;
	}
	
	public Map<Integer, String> getEmployementTypesCoapplicants() {
		return employementTypesCoapplicants;
	}
	public void setEmployementTypesCoapplicants(
			Map<Integer, String> employementTypesCoapplicants) {
		this.employementTypesCoapplicants = employementTypesCoapplicants;
	}
	public Map<Integer, String> getBusinessMonthsCoapplicant() {
		return businessMonthsCoapplicant;
	}
	public void setBusinessMonthsCoapplicant(
			Map<Integer, String> businessMonthsCoapplicant) {
		this.businessMonthsCoapplicant = businessMonthsCoapplicant;
	}
	public Map<Integer, String> getEmployementTypesCoapplicants2() {
		return employementTypesCoapplicants2;
	}
	public void setEmployementTypesCoapplicants2(
			Map<Integer, String> employementTypesCoapplicants2) {
		this.employementTypesCoapplicants2 = employementTypesCoapplicants2;
	}
	public Map<Integer, String> getTenureYear() {
		return tenureYear;
	}
	public void setTenureYear(Map<Integer, String> tenureYear) {
		this.tenureYear = tenureYear;
	}
	public Map<Integer, String> getTenureMonth() {
		return tenureMonth;
	}
	public void setTenureMonth(Map<Integer, String> tenureMonth) {
		this.tenureMonth = tenureMonth;
	}
	public Map<Integer, String> getStatesRACPC() {
		return statesRACPC;
	}
	public void setStatesRACPC(Map<Integer, String> statesRACPC) {
		this.statesRACPC = statesRACPC;
	}
	public Map<Integer, String> getProjects() {
		return projects;
	}
	public void setProjects(Map<Integer, String> projects) {
		this.projects = projects;
	}
	public Map<Integer, String> getCitiesoptgrp1Pickup() {
		return citiesoptgrp1Pickup;
	}
	public void setCitiesoptgrp1Pickup(Map<Integer, String> citiesoptgrp1Pickup) {
		this.citiesoptgrp1Pickup = citiesoptgrp1Pickup;
	}
	public Map<Integer, String> getCitiesoptgrp2Pickup() {
		return citiesoptgrp2Pickup;
	}
	public void setCitiesoptgrp2Pickup(Map<Integer, String> citiesoptgrp2Pickup) {
		this.citiesoptgrp2Pickup = citiesoptgrp2Pickup;
	}
	public Map<Integer, String> getModeOfRepayment() {
		return modeOfRepayment;
	}
	public void setModeOfRepayment(Map<Integer, String> modeOfRepayment) {
		this.modeOfRepayment = modeOfRepayment;
	}
	public Map<Integer, String> getHighestEducationalQualification() {
		return highestEducationalQualification;
	}
	public void setHighestEducationalQualification(
			Map<Integer, String> highestEducationalQualification) {
		this.highestEducationalQualification = highestEducationalQualification;
	}
	public Map<Integer, String> getCourseType() {
		return courseType;
	}
	public void setCourseType(Map<Integer, String> courseType) {
		this.courseType = courseType;
	}
	public Map<Integer, String> getCompletionYear() {
		return completionYear;
	}
	public void setCompletionYear(Map<Integer, String> completionYear) {
		this.completionYear = completionYear;
	}
	public Map<Integer, String> getCompletionMonth() {
		return completionMonth;
	}
	public void setCompletionMonth(Map<Integer, String> completionMonth) {
		this.completionMonth = completionMonth;
	}
	public Map<Integer, String> getIndustryTypeDataCoapplicant1() {
		return industryTypeDataCoapplicant1;
	}
	public void setIndustryTypeDataCoapplicant1(
			Map<Integer, String> industryTypeDataCoapplicant1) {
		this.industryTypeDataCoapplicant1 = industryTypeDataCoapplicant1;
	}
	public Map<Integer, String> getInstituteNameList() {
		return instituteNameList;
	}
	public void setInstituteNameList(Map<Integer, String> instituteNameList) {
		this.instituteNameList = instituteNameList;
	}
	public Map<Integer, String> getTractorMake() {
		return tractorMake;
	}
	public void setTractorMake(Map<Integer, String> tractorMake) {
		this.tractorMake = tractorMake;
	}
	public Map<Integer, String> getTractorModel() {
		return tractorModel;
	}
	public void setTractorModel(Map<Integer, String> tractorModel) {
		this.tractorModel = tractorModel;
	}
	public Map<Integer, String> getTractorVariant() {
		return tractorVariant;
	}
	public void setTractorVariant(Map<Integer, String> tractorVariant) {
		this.tractorVariant = tractorVariant;
	}
	public Map<Integer, String> getCombineHarvestorMake() {
		return combineHarvestorMake;
	}
	public void setCombineHarvestorMake(Map<Integer, String> combineHarvestorMake) {
		this.combineHarvestorMake = combineHarvestorMake;
	}
	public Map<Integer, String> getCombineHarvestorModel() {
		return combineHarvestorModel;
	}
	public void setCombineHarvestorModel(Map<Integer, String> combineHarvestorModel) {
		this.combineHarvestorModel = combineHarvestorModel;
	}
	public Map<Integer, String> getCombineHarvestorVariant() {
		return combineHarvestorVariant;
	}
	public void setCombineHarvestorVariant(
			Map<Integer, String> combineHarvestorVariant) {
		this.combineHarvestorVariant = combineHarvestorVariant;
	}
	public Map<Integer, String> getPowerTillerMake() {
		return powerTillerMake;
	}
	public void setPowerTillerMake(Map<Integer, String> powerTillerMake) {
		this.powerTillerMake = powerTillerMake;
	}
	public Map<Integer, String> getPowerTillerModel() {
		return powerTillerModel;
	}
	public void setPowerTillerModel(Map<Integer, String> powerTillerModel) {
		this.powerTillerModel = powerTillerModel;
	}
	public Map<Integer, String> getPowerTillerVariant() {
		return powerTillerVariant;
	}
	public void setPowerTillerVariant(Map<Integer, String> powerTillerVariant) {
		this.powerTillerVariant = powerTillerVariant;
	}
	public Map<Integer, String> getResidenceState() {
		return residenceState;
	}
	public void setResidenceState(Map<Integer, String> residenceState) {
		this.residenceState = residenceState;
	}
	public Map<Integer, String> getResidenceDistrict() {
		return residenceDistrict;
	}
	public void setResidenceDistrict(Map<Integer, String> residenceDistrict) {
		this.residenceDistrict = residenceDistrict;
	}
	public Map<Integer, String> getDistrictsCoapplicant4() {
		return districtsCoapplicant4;
	}
	public void setDistrictsCoapplicant4(Map<Integer, String> districtsCoapplicant4) {
		this.districtsCoapplicant4 = districtsCoapplicant4;
	}
	public Map<Integer, String> getTypeOfSecurity() {
		return typeOfSecurity;
	}
	public void setTypeOfSecurity(Map<Integer, String> typeOfSecurity) {
		this.typeOfSecurity = typeOfSecurity;
	}
	public Map<Integer, String> getNoOfAnimals() {
		return noOfAnimals;
	}
	public void setNoOfAnimals(Map<Integer, String> noOfAnimals) {
		this.noOfAnimals = noOfAnimals;
	}
	public Map<Integer, String> getMasterQualifications() {
		return masterQualifications;
	}
	public void setMasterQualifications(Map<Integer, String> masterQualifications) {
		this.masterQualifications = masterQualifications;
	}
	public Map<Integer, String> getMasterAnnualIncome() {
		return masterAnnualIncome;
	}
	public void setMasterAnnualIncome(Map<Integer, String> masterAnnualIncome) {
		this.masterAnnualIncome = masterAnnualIncome;
	}
	public Map<Integer, String> getCompanyName() {
		return companyName;
	}
	public void setCompanyName(Map<Integer, String> companyName) {
		this.companyName = companyName;
	}
	public Map<Integer, String> getNatureOfBusiness() {
		return natureOfBusiness;
	}
	public void setNatureOfBusiness(Map<Integer, String> natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}
	
	public Map<Integer, String> getXpressCreditITcitiesgrp1() {
		return xpressCreditITcitiesgrp1;
	}
	public void setXpressCreditITcitiesgrp1(
			Map<Integer, String> xpressCreditITcitiesgrp1) {
		this.xpressCreditITcitiesgrp1 = xpressCreditITcitiesgrp1;
	}
	
	public Map<Integer, String> getCurrentEmpYears() {
		return currentEmpYears;
	}
	public void setCurrentEmpYears(Map<Integer, String> currentEmpYears) {
		this.currentEmpYears = currentEmpYears;
	}
	public Map<Integer, String> getCurrentEmpMonth() {
		return currentEmpMonth;
	}
	public void setCurrentEmpMonth(Map<Integer, String> currentEmpMonth) {
		this.currentEmpMonth = currentEmpMonth;
	}
	public Map<Integer, String> getCities() {
		return cities;
	}
	public void setCities(Map<Integer, String> cities) {
		this.cities = cities;
	}
	public Map<Integer, String> getCardTypes() {
		return cardTypes;
	}
	public void setCardTypes(Map<Integer, String> cardTypes) {
		this.cardTypes = cardTypes;
	}

	public Map<Integer, String> getDistrictPickup() {
		return districtPickup;
	}
	public void setDistrictPickup(Map<Integer, String> districtPickup) {
		this.districtPickup = districtPickup;
	}	
	public Map<Integer, String> getYeartenorExistingPersonalLoan() {
		return yeartenorExistingPersonalLoan;
	}
	public void setYeartenorExistingPersonalLoan(
			Map<Integer, String> yeartenorExistingPersonalLoan) {
		this.yeartenorExistingPersonalLoan = yeartenorExistingPersonalLoan;
	}
	public Map<Integer, String> getPensionLoanTypes() {
		return pensionLoanTypes;
	}
	public void setPensionLoanTypes(Map<Integer, String> pensionLoanTypes) {
		this.pensionLoanTypes = pensionLoanTypes;
	}

	public Map<Integer, String> getPensionPayingBranches() {
		return pensionPayingBranches;
	}
	public void setPensionPayingBranches(Map<Integer, String> pensionPayingBranches) {
		this.pensionPayingBranches = pensionPayingBranches;
	}
	public Map<Integer, String> getPreferredPayingBranches() {
		return preferredPayingBranches;
	}
	public void setPreferredPayingBranches(
			Map<Integer, String> preferredPayingBranches) {
		this.preferredPayingBranches = preferredPayingBranches;
	}
	public Map<Integer, String> getPensionDistricts() {
		return pensionDistricts;
	}
	public void setPensionDistricts(Map<Integer, String> pensionDistricts) {
		this.pensionDistricts = pensionDistricts;
	}
	public Map<Integer, String> getPreferredDistricts() {
		return preferredDistricts;
	}
	public void setPreferredDistricts(Map<Integer, String> preferredDistricts) {
		this.preferredDistricts = preferredDistricts;
	}	
	
		public Map<Integer, String> getEducationTakeOverGroupBank() {
		return educationTakeOverGroupBank;
	}
	public void setEducationTakeOverGroupBank(
			Map<Integer, String> educationTakeOverGroupBank) {
		this.educationTakeOverGroupBank = educationTakeOverGroupBank;
	}
	public Map<Integer, String> getYearTenorExistingEducationTakeOverLoan() {
		return yearTenorExistingEducationTakeOverLoan;
	}
	public void setYearTenorExistingEducationTakeOverLoan(
			Map<Integer, String> yearTenorExistingEducationTakeOverLoan) {
		this.yearTenorExistingEducationTakeOverLoan = yearTenorExistingEducationTakeOverLoan;
	}
	public Map<Integer, String> getIndustryType() {
		return industryType;
	}
	public void setIndustryType(Map<Integer, String> industryType) {
		this.industryType = industryType;
	}
	public Map<Integer, String> getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(Map<Integer, String> organisationName) {
		this.organisationName = organisationName;
	}
	public Map<Integer, String> getPreferredLocation() {
		return preferredLocation;
	}
	public void setPreferredLocation(Map<Integer, String> preferredLocation) {
		this.preferredLocation = preferredLocation;
	}
	public Map<Integer, String> getPensionPayingCity() {
		return pensionPayingCity;
	}
	public void setPensionPayingCity(Map<Integer, String> pensionPayingCity) {
		this.pensionPayingCity = pensionPayingCity;
	}
	public Map<Integer, String> getPensionPayingState() {
		return pensionPayingState;
	}
	public void setPensionPayingState(Map<Integer, String> pensionPayingState) {
		this.pensionPayingState = pensionPayingState;
	}
	public Map<Integer, String> getResidenceSubDistrict() {
		return residenceSubDistrict;
	}
	public void setResidenceSubDistrict(Map<Integer, String> residenceSubDistrict) {
		this.residenceSubDistrict = residenceSubDistrict;
	}
	public Map<Integer, String> getResidenceVillage() {
		return residenceVillage;
	}
	public void setResidenceVillage(Map<Integer, String> residenceVillage) {
		this.residenceVillage = residenceVillage;
	}
	public Map<Integer, String> getContractStartYear() {
		return contractStartYear;
	}
	public void setContractStartYear(Map<Integer, String> contractStartYear) {
		this.contractStartYear = contractStartYear;
	}
	public Map<Integer, String> getContractStartMonth() {
		return contractStartMonth;
	}
	public void setContractStartMonth(Map<Integer, String> contractStartMonth) {
		this.contractStartMonth = contractStartMonth;
	}
	public Map<Integer, String> getContractEndYear() {
		return contractEndYear;
	}
	public void setContractEndYear(Map<Integer, String> contractEndYear) {
		this.contractEndYear = contractEndYear;
	}
	public Map<Integer, String> getContractEndMonth() {
		return contractEndMonth;
	}
	public void setContractEndMonth(Map<Integer, String> contractEndMonth) {
		this.contractEndMonth = contractEndMonth;
	}
	
	public Map<String, String> getCarType() {
		return carType;
	}
	public void setCarType(Map<String, String> carType) {
		this.carType = carType;
	}

	public Map<Integer, String> getOccupationTypes() {
		return occupationTypes;
	}
	public void setOccupationTypes(Map<Integer, String> occupationTypes) {
		this.occupationTypes = occupationTypes;
	}
	public Map<Integer, Integer> getOccupationCategories() {
		return occupationCategories;
	}
	public void setOccupationCategories(Map<Integer, Integer> occupationCategories) {
		this.occupationCategories = occupationCategories;
	}
	public Map<Integer, String> getCorpSalaryPackageRank() {
		return corpSalaryPackageRank;
	}
	public void setCorpSalaryPackageRank(Map<Integer, String> corpSalaryPackageRank) {
		this.corpSalaryPackageRank = corpSalaryPackageRank;
	}
	public String getConsentHomeLoan() {
		return consentHomeLoan;
	}
	public void setConsentHomeLoan(String consentHomeLoan) {
		this.consentHomeLoan = consentHomeLoan;
	}
	public String getConsentAutoLoanEtb() {
		return consentAutoLoanEtb;
	}
	public void setConsentAutoLoanEtb(String consentAutoLoanEtb) {
		this.consentAutoLoanEtb = consentAutoLoanEtb;
	}
	public String getConsentAutoLoanNtb() {
		return consentAutoLoanNtb;
	}
	public void setConsentAutoLoanNtb(String consentAutoLoanNtb) {
		this.consentAutoLoanNtb = consentAutoLoanNtb;
	}
	public String getConsentEducationLoanEtb() {
		return consentEducationLoanEtb;
	}
	public void setConsentEducationLoanEtb(String consentEducationLoanEtb) {
		this.consentEducationLoanEtb = consentEducationLoanEtb;
	}
	public String getConsentEducationLoanNtb() {
		return consentEducationLoanNtb;
	}
	public void setConsentEducationLoanNtb(String consentEducationLoanNtb) {
		this.consentEducationLoanNtb = consentEducationLoanNtb;
	}
	public String getConsentPersonalLoanEtb() {
		return consentPersonalLoanEtb;
	}
	public void setConsentPersonalLoanEtb(String consentPersonalLoanEtb) {
		this.consentPersonalLoanEtb = consentPersonalLoanEtb;
	}
	public String getConsentPersonalLoanNtb() {
		return consentPersonalLoanNtb;
	}
	public void setConsentPersonalLoanNtb(String consentPersonalLoanNtb) {
		this.consentPersonalLoanNtb = consentPersonalLoanNtb;
	}
	public String getConsentGoldLoanEtb() {
		return consentGoldLoanEtb;
	}
	public void setConsentGoldLoanEtb(String consentGoldLoanEtb) {
		this.consentGoldLoanEtb = consentGoldLoanEtb;
	}
	public String getConsentGoldLoanNtb() {
		return consentGoldLoanNtb;
	}
	public void setConsentGoldLoanNtb(String consentGoldLoanNtb) {
		this.consentGoldLoanNtb = consentGoldLoanNtb;
	}
	public String getConsentAgriLoanEtb() {
		return consentAgriLoanEtb;
	}
	public void setConsentAgriLoanEtb(String consentAgriLoanEtb) {
		this.consentAgriLoanEtb = consentAgriLoanEtb;
	}
	public String getConsentAgriLoanNtb() {
		return consentAgriLoanNtb;
	}
	public void setConsentAgriLoanNtb(String consentAgriLoanNtb) {
		this.consentAgriLoanNtb = consentAgriLoanNtb;
	}
	public String getConsentCveLoan() {
		return consentCveLoan;
	}
	public void setConsentCveLoan(String consentCveLoan) {
		this.consentCveLoan = consentCveLoan;
	}
	public String getConsentCreditCardEtb() {
		return consentCreditCardEtb;
	}
	public void setConsentCreditCardEtb(String consentCreditCardEtb) {
		this.consentCreditCardEtb = consentCreditCardEtb;
	}
	public String getConsentCreditCardNtb() {
		return consentCreditCardNtb;
	}
	public void setConsentCreditCardNtb(String consentCreditCardNtb) {
		this.consentCreditCardNtb = consentCreditCardNtb;
	}
	public String getConsentCveRevoke() {
		return consentCveRevoke;
	}
	public void setConsentCveRevoke(String consentCveRevoke) {
		this.consentCveRevoke = consentCveRevoke;
	}
	public String getConsentExistingCreditCard() {
		return consentExistingCreditCard;
	}
	public void setConsentExistingCreditCard(String consentExistingCreditCard) {
		this.consentExistingCreditCard = consentExistingCreditCard;
	}
	public Map<String, String> getCveProductCategories() {
		return cveProductCategories;
	}
	public void setCveProductCategories(Map<String, String> cveProductCategories) {
		this.cveProductCategories = cveProductCategories;
	}

}

