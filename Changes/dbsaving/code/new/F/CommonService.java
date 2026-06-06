package com.mintstreet.common.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.campaign.dao.TrackCampaignDao;
import com.mintstreet.campaign.dao.TrackCampaignPlacementSourceDao;
import com.mintstreet.campaign.dao.TrackMainCampaignDao;
import com.mintstreet.campaign.dao.TrackVisitDao;
import com.mintstreet.campaign.entity.TrackCampaign;
import com.mintstreet.campaign.entity.TrackCampaignPlacementSource;
import com.mintstreet.campaign.entity.TrackMainCampaign;
import com.mintstreet.campaign.entity.TrackVisit;
import com.mintstreet.common.bo.CallLogsDetails;
import com.mintstreet.common.bo.RsmData;
import com.mintstreet.common.dao.ApplicationFormLeadCallsDao;
import com.mintstreet.common.dao.ApplicationFormLeadDao;
import com.mintstreet.common.dao.BankLMSUserDao;
import com.mintstreet.common.dao.BankLmsUserRoleDao;
import com.mintstreet.common.dao.BiometricRequestDao;
import com.mintstreet.common.dao.BiometricResponseDao;
import com.mintstreet.common.dao.BureauLinkRequestResponseDao;
import com.mintstreet.common.dao.CRMFetchLeadStatusDao;
import com.mintstreet.common.dao.CRMNextLogDao;
import com.mintstreet.common.dao.CbsProductMappingDao;
import com.mintstreet.common.dao.ConsentDao;
import com.mintstreet.common.dao.ContactCenterLeadResponseDao;
import com.mintstreet.common.dao.CountryDao;
import com.mintstreet.common.dao.CustomerFeedbackDao;
import com.mintstreet.common.dao.DocumentTypeDao;
import com.mintstreet.common.dao.EmployeeOccupationTypeDao;
import com.mintstreet.common.dao.EmploymentTypeDao;
import com.mintstreet.common.dao.IntermediaryRelDao;
import com.mintstreet.common.dao.LastAppRefDao;
import com.mintstreet.common.dao.LoanCategoryDao;
import com.mintstreet.common.dao.LoanPurposeDao;
import com.mintstreet.common.dao.LoanStatusDao;
import com.mintstreet.common.dao.LoanTypeDao;
import com.mintstreet.common.dao.MasterBankDao;
import com.mintstreet.common.dao.MasterBranchDao;
import com.mintstreet.common.dao.MasterBuilderDao;
import com.mintstreet.common.dao.MasterCBSCallDao;
import com.mintstreet.common.dao.MasterCBSResponseDao;
import com.mintstreet.common.dao.MasterCarDealerDao;
import com.mintstreet.common.dao.MasterCityDao;
import com.mintstreet.common.dao.MasterCoApplicantDao;
import com.mintstreet.common.dao.MasterContentDao;
import com.mintstreet.common.dao.MasterCorpSalaryPackageDao;
import com.mintstreet.common.dao.MasterDealerDao;
import com.mintstreet.common.dao.MasterDistrictDao;
import com.mintstreet.common.dao.MasterEmployerDao;
import com.mintstreet.common.dao.MasterGenderDao;
import com.mintstreet.common.dao.MasterIndustryTypeDao;
import com.mintstreet.common.dao.MasterInstituteDao;
import com.mintstreet.common.dao.MasterLabelTooltipDao;
import com.mintstreet.common.dao.MasterLmsIntermediaryDao;
import com.mintstreet.common.dao.MasterLocalityDao;
import com.mintstreet.common.dao.MasterProfessionDao;
import com.mintstreet.common.dao.MasterQualificationDao;
import com.mintstreet.common.dao.MasterSalesTeamDao;
import com.mintstreet.common.dao.MasterStateDao;
import com.mintstreet.common.dao.RelationshipWithBankDao;
import com.mintstreet.common.dao.RequestACallBackDao;
import com.mintstreet.common.dao.ResidenceTypeDao;
import com.mintstreet.common.dao.SbiCampaignDao;
import com.mintstreet.common.dao.SecurityChecksDao;
import com.mintstreet.common.dao.TemplateDao;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.ApplicationFormLeadCall;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.BankLmsUserRole;
import com.mintstreet.common.entity.BiometricRequest;
import com.mintstreet.common.entity.BiometricResponse;
import com.mintstreet.common.entity.BureauLinkRequestConfig;
import com.mintstreet.common.entity.BureauLinkRequestResponse;
import com.mintstreet.common.entity.CRMFetchLeadStatus;
import com.mintstreet.common.entity.CRMNextLog;
import com.mintstreet.common.entity.CallBackLeadPushLog;
import com.mintstreet.common.entity.CbsProductMapping;
import com.mintstreet.common.entity.Consent;
import com.mintstreet.common.entity.CustomerFeedback;
import com.mintstreet.common.entity.Domain;
import com.mintstreet.common.entity.IntermediaryRel;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterBank;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterBuilder;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterCarDealer;
import com.mintstreet.common.entity.MasterCity;
import com.mintstreet.common.entity.MasterCoApplicant;
import com.mintstreet.common.entity.MasterContent;
import com.mintstreet.common.entity.MasterCorpSalaryPackage;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterDealer;
import com.mintstreet.common.entity.MasterDistrict;
import com.mintstreet.common.entity.MasterDocumentType;
import com.mintstreet.common.entity.MasterEmployeeOccupationType;
import com.mintstreet.common.entity.MasterEmployer;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterGender;
import com.mintstreet.common.entity.MasterIndustryType;
import com.mintstreet.common.entity.MasterInstitute;
import com.mintstreet.common.entity.MasterLabelTooltip;
import com.mintstreet.common.entity.MasterLmsIntermediary;
import com.mintstreet.common.entity.MasterLoanCategory;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterLoanType;
import com.mintstreet.common.entity.MasterLocality;
import com.mintstreet.common.entity.MasterProfession;
import com.mintstreet.common.entity.MasterRelationshipWithBank;
import com.mintstreet.common.entity.MasterResidenceType;
import com.mintstreet.common.entity.MasterSalesTeam;
import com.mintstreet.common.entity.MasterState;
import com.mintstreet.common.entity.RequestACallBack;
import com.mintstreet.common.entity.SbiCampaignMaster;
import com.mintstreet.common.entity.SecurityChecks;
import com.mintstreet.common.entity.Template;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.dao.CcmsConfigDao;
import com.mintstreet.consent.dao.MasterLanguageDao;
import com.mintstreet.consent.dao.PrivacyNoticeDao;
import com.mintstreet.consent.dao.PrivacyRequestResponseDao;
import com.mintstreet.consent.entity.CCMSConfig;
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
import com.mintstreet.loan.agriloan.dao.MasterAgriProductDao;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoan;
import com.mintstreet.loan.agriloan.entity.MasterAgriProduct;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.cveloan.dao.ApplicationFormCaseCveDao;
import com.mintstreet.loan.cveloan.dao.ApplicationFormCveLoanDao;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCaseCve;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.product.dao.AlProductDao;
import com.mintstreet.loan.product.dao.ElProductDao;
import com.mintstreet.loan.product.dao.HlProductDao;
import com.mintstreet.loan.product.dao.PlProductDao;
import com.mintstreet.loan.product.entity.AlProduct;
import com.mintstreet.loan.product.entity.HlProduct;
import com.mintstreet.loan.product.entity.MasterElProduct;
import com.mintstreet.loan.product.entity.MasterPlProduct;
import com.mintstreet.webservice.dao.WebServiceDao;

public class CommonService {
  private static final Logger logger = LogManager.getLogger(CommonService.class.getName());
  
  @Autowired
  private SbiUtil SbiUtil;
  
  private ApplicationFormLeadDao applicationFormLeadDao;
  
  private ApplicationFormLeadCallsDao applicationFormLeadCallsDao;
  
  private MasterDistrictDao masterDistrictDao;
  
  private MasterBranchDao masterBranchDao;
  
  private MasterStateDao stateDao;
  
  private MasterCityDao cityDao;
  
  private MasterLocalityDao localityDao;
  
  private LoanPurposeDao loanPurposeDao;
  
  private LoanCategoryDao loanCategoryDao;
  
  private LoanTypeDao loanTypeDao;
  
  private EmploymentTypeDao employmentTypeDao;
  
  private MasterCoApplicantDao masterCoApplicantDao;
  
  private CountryDao countryDao;
  
  private MasterBankDao bankDao;
  
  private TemplateDao templateDao;
  
  private MasterBuilderDao masterBuilderDao;
  
  private MasterEmployerDao masterEmployerDao;
  
  private DocumentTypeDao documentTypeDao;
  
  private ResidenceTypeDao residenceTypeDao;
  
  private RelationshipWithBankDao relationshipWithBankDao;
  
  private MasterCarDealerDao masterCarDealerDao;
  
  private MasterIndustryTypeDao masterIndustryTypeDao;
  
  private MasterGenderDao masterGenderDao;
  
  private HlProductDao hlProductDao;
  
  private AlProductDao alProductDao;
  
  private ElProductDao elProductDao;
  
  private PlProductDao plProductDao;
  
  private MasterAgriProductDao aglProductDao;
  
  private MasterSalesTeamDao masterSalesTeamDao;
  
  private MasterProfessionDao masterProfessionDao;
  
  private MasterDealerDao masterDealerDao;
  
  private BankLMSUserDao bankLMSUserDao;
  
  private MasterCorpSalaryPackageDao masterCorpSalaryPackageDao;
  
  private MasterLabelTooltipDao masterLabelTooltipDao;
  
  private MasterInstituteDao masterInstituteDao;
  
  private IntermediaryRelDao intermediaryRelDao;
  
  private MasterLmsIntermediaryDao masterLmsIntermediaryDao;
  
  private MasterContentDao masterContentDao;
  
  private BankLmsUserRoleDao bankLmsUserRoleDao;
  
  private LoanStatusDao loanStatusDao;
  
  private MasterQualificationDao masterQualificationDao;
  
  private SecurityChecksDao securityChecksDao;
  
  //private VlpRequestResponseDao vlpRequestResponseDao;
  
  private SbiCampaignDao sbiCampaignDao;
  
  private PrivacyNoticeDao privacyNoticeDao;
  
  private BiometricRequestDao biometricRequestDao;
  
  private BiometricResponseDao biometricResponseDao;
  
  private MasterCBSResponseDao masterCBSResponseDao;
  
  private MasterCBSCallDao masterCBSCallDao;
  
  private CbsProductMappingDao cbsProductMappingDao;
  
  private BureauLinkRequestResponseDao bureauLinkRequestResponseDao;
  
  /*private VLPExportDao vlpExportDao;
  
  private VLPLogInfoDao vlpLogInfoDao;*/
  
  private WebServiceDao webServiceDao;
  
  //private FlipkartMicroLeadDao flipkartMicroDao;
  
  //private MingleDao mingleDao;
  
  private CRMNextLogDao crmNextLogDao;
  
  private CRMFetchLeadStatusDao crmFetchLeadStatusDao;
  
  private ContactCenterLeadResponseDao contactCenterLeadResponseDao;
  
  private EmployeeOccupationTypeDao employeeOccupationTypeDao;
  
  private RequestACallBackDao requestACallBackDao;
  
  private TrackVisitDao trackVisitDao;
  
  private TrackCampaignDao trackCampaignDao;
  
  private TrackMainCampaignDao trackMainCampaignDao;
  
  private TrackCampaignPlacementSourceDao trackCampaignPlacementSourceDao;
  
  private LastAppRefDao lastAppRefDao;
  
  private CustomerFeedbackDao customerFeedbackDao;
  
  private ApplicationFormCveLoanDao applicationFormCveLoanDao;
  private ApplicationFormCaseCveDao applicationFormCaseCveDao;
  
  private ConsentDao consentDao;

  private CcmsConfigDao ccmsConfigDao;
  
  private PrivacyRequestResponseDao privacyRequestResponseDao;
  
  private MasterLanguageDao masterLanguageDao;
  




  public RequestACallBackDao getRequestACallBackDao() {
    return this.requestACallBackDao;
  }
  
  public void setRequestACallBackDao(RequestACallBackDao requestACallBackDao) {
    this.requestACallBackDao = requestACallBackDao;
  }
  
  public EmployeeOccupationTypeDao getEmployeeOccupationTypeDao() {
    return this.employeeOccupationTypeDao;
  }
  
  public void setEmployeeOccupationTypeDao(EmployeeOccupationTypeDao employeeOccupationTypeDao) {
    this.employeeOccupationTypeDao = employeeOccupationTypeDao;
  }
  
  public CustomerFeedbackDao getCustomerFeedbackDao() {
    return this.customerFeedbackDao;
  }
  
  public void setCustomerFeedbackDao(CustomerFeedbackDao customerFeedbackDao) {
    this.customerFeedbackDao = customerFeedbackDao;
  }
  
  public JSONArray getLanguageQuery() {
    return this.webServiceDao.getLanguageQuery();
  }
  
  public Integer getHLSTMPSTByLoanTypeAndBranchId(Integer loanTypeId, Integer branchId, Integer loanPurposeId) throws SQLException {
    return this.masterBranchDao.getHLSTMPSTByLoanTypeAndBranchId(loanTypeId, branchId, loanPurposeId);
  }
  
  public Integer[] getCircleIdNetworkModuleRegionByBranchId(Integer branchId) throws SQLException {
    return this.masterBranchDao.getCircleIdNetworkModuleRegionByBranchId(branchId);
  }
  
  public MasterState getStateById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    if (id == null)
      return null; 
    return (MasterState)this.stateDao.findById(id, MasterState.class);
  }
  
  public JSONArray getAllStatesForScholarLoan(Integer loanType, Integer isRACPC, Integer isScholar, Integer isNRI, Integer isEdvantage) throws SQLException, JSONException {
    JSONArray jsonArray = new JSONArray();
    Map<Integer, String> stateMap = getStateCityDistrictBranch(Integer.valueOf(1), loanType, null, null, null, null, Integer.valueOf(1), null, null, null);
    if (stateMap != null)
      for (Map.Entry<Integer, String> entry : stateMap.entrySet()) {
        JSONObject json = new JSONObject();
        json.put("key", entry.getKey());
        json.put("value", entry.getValue());
        json.put("displayOrder", 2);
        jsonArray.put(json);
      }  
    return jsonArray;
  }
  
  public List<MasterContent> getContentForProductId(Integer contentLoanTypeId, Integer contentRequestIndex) throws NoResultException {
    List<MasterContent> list = this.masterContentDao.getContentForProductId(contentLoanTypeId, contentRequestIndex);
    if (list != null && !list.isEmpty())
      return list; 
    return list;
  }
  
  public Integer getCountByAppIdAndLoanType(String blApplicationId, String blLoanType, String blFetchResponseStatus) throws NoResultException, SQLException {
    return this.bureauLinkRequestResponseDao.getCountByAppIdAndLoanType(blApplicationId, blLoanType, blFetchResponseStatus);
  }
  
  /*public List<VlpFileListDetails> getFindVlpFileListDetailsByFileId(String vlpFileId) throws Exception {
    return this.vlpRequestResponseDao.getFindVlpFileListDetailsByFileId(vlpFileId);
  }
  
  public List<VlpFileListDetails> getVlAllFilesByStatus(String vlpFileStatus) throws Exception {
    return this.vlpRequestResponseDao.getVlAllFilesByStatus(vlpFileStatus);
  }
  
  public String setUpdateStatusByFileListDetailsId(String vlpFileStatus) {
    return this.vlpRequestResponseDao.setUpdateStatusByFileListDetailsId(vlpFileStatus);
  }*/
  
  public String setSbiCampignMasterByAcntNumber(String sbiHlCampName) {
    return this.sbiCampaignDao.setSearchSbiCampignMasterByAcntNumber(sbiHlCampName);
  }
  
  public List<MasterLoanType> getAllLoanType() throws NoResultException {
    return this.loanTypeDao.getAllLoanType();
  }
  
  public MasterLoanType getLoanTypeById(Integer loanTypeid) throws NoResultException {
    return this.loanTypeDao.getLoanTypeById(loanTypeid);
  }
  
  public List<MasterLoanPurpose> getAllLoanPurpose() throws NoResultException {
    return this.loanPurposeDao.getAllLoanPurpose();
  }
  
  public List<MasterLoanPurpose> getAllLoanPurposeByLoanType(Integer loanTypeId) throws NoResultException {
    return this.loanPurposeDao.getAllLoanPurposeByLoanType(loanTypeId);
  }
  
  public List<MasterLoanPurpose> getLoanPurposeByIds(String ids) throws NoResultException {
    return this.loanPurposeDao.getLoanPurposeByIds(ids);
  }
  
  public MasterLoanPurpose getLoanPurposeById(Integer Id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterLoanPurpose)this.loanPurposeDao.findById(Id, MasterLoanPurpose.class);
  }
  
  public HlProduct getHomeLoanProductById(Integer homeLoanProductId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (HlProduct)this.hlProductDao.findById(homeLoanProductId, HlProduct.class);
  }
  
  public AlProduct getAutoLoanProductById(Integer autoLoanProductId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (AlProduct)this.alProductDao.findById(autoLoanProductId, AlProduct.class);
  }
  
  public MasterElProduct getEducationLoanProductById(Integer elProductId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterElProduct)this.elProductDao.findById(elProductId, MasterElProduct.class);
  }
  
  public MasterPlProduct getPersonalLoanProductById(Integer plProductId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterPlProduct)this.plProductDao.findById(plProductId, MasterPlProduct.class);
  }
  
  public MasterAgriProduct getAgriLoanProductById(Integer aglProductId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
	  return (MasterAgriProduct)this.aglProductDao.findById(aglProductId, MasterAgriProduct.class);
  }
	  
  public List<MasterPlProduct> getAllPlProduct() throws NoResultException {
    return this.plProductDao.getAllPlProduct();
  }
  
  public List<BankLmsUserRole> getBankLmsUserRoleByid(Integer userId) {
      List<BankLmsUserRole> bankLmsUserRole = this.bankLmsUserRoleDao.getBankLmsUserRolesByUserId(userId);
      return bankLmsUserRole;
  }
  
  public List<MasterLoanCategory> getAllLoanCategory() throws NoResultException {
    return this.loanCategoryDao.getAllLoanCategory();
  }
  
  public List<MasterLoanCategory> getAllLoanCategoryByLoanTypeAndPurposeId(Integer loanTypeId, Integer loanPurposeId) throws NoResultException {
    return this.loanCategoryDao.getAllLoanCategoryByLoanTypeAndPurposeId(loanTypeId, loanPurposeId);
  }
  
  public List<MasterLoanCategory> getLoanCategoryByIds(String ids) throws NoResultException {
    return this.loanCategoryDao.getLoanCategoryByIds(ids);
  }
  
  public MasterLoanCategory getLoanCategoryById(Integer Id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterLoanCategory)this.loanCategoryDao.findById(Id, MasterLoanCategory.class);
  }
  
  public List<MasterResidenceType> getAllResidenceType(Integer loanTypeId) throws NoResultException {
    return this.residenceTypeDao.getAllResidenceType(loanTypeId);
  }
  
  public List<MasterRelationshipWithBank> getAllRelationshipWithBank(Integer loanTypeId) throws NoResultException {
    return this.relationshipWithBankDao.getAllRelationshipWithBank(loanTypeId);
  }
  
  public List<MasterEmploymentType> getAllEmploymentTypeByLoanType(Integer loanTypeId) throws NoResultException {
    return this.employmentTypeDao.getAllEmploymentTypeByLoanType(loanTypeId);
  }
  
  public List<MasterEmploymentType> getAllEmploymentTypeByLoanTypeActive(Integer loanTypeId) throws NoResultException {
    return this.employmentTypeDao.getAllEmploymentTypeByLoanTypeActive(loanTypeId);
  }
  
  public MasterEmploymentType getEmploymentTypeById(Integer employmentTypeId) throws NoResultException {
    return this.employmentTypeDao.getEmploymentTypeById(employmentTypeId);
  }
  
  public List<MasterCountry> getAllCountries() throws NoResultException {
    return this.countryDao.getAllCountries();
  }
  
  public List<MasterCountry> getAllCountriesIncludingIndia() throws NoResultException {
    return this.countryDao.getAllCountriesIncludingIndia();
  }
  
  public MasterCountry getCountryById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    if (id == null)
      return null; 
    return (MasterCountry)this.countryDao.findById(id, MasterCountry.class);
  }
  
  public MasterCountry getCountrieById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterCountry)this.countryDao.findById(id, MasterCountry.class);
  }
  
  public MasterDistrict getDistrictById(Integer Id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterDistrict)this.masterDistrictDao.findById(Id, MasterDistrict.class);
  }
  
  public List<MasterLocality> getlocalityByCityId(int localityStateId, int localityCityId) throws NoResultException {
    return this.localityDao.getLocalityByCityId(localityStateId, localityCityId);
  }
  
  public List<MasterLocality> getLocalityBySalesTeamIds(List<Integer> salesTeamIds) throws NoResultException {
    return this.localityDao.getLocalityBySalesTeamIds(salesTeamIds);
  }
  
  public MasterLocality getLocalityById(Integer localityId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterLocality)this.localityDao.findById(localityId, MasterLocality.class);
  }
  
  public List<MasterBranch> getBranchByLoanTypeStateDistrictRACPCScholar(Integer loanType, Integer branchStateId, Integer branchCityId, Integer branchDistrictId, Integer branchIsRACPC, Integer branchIsScholar, Integer isNRI, Integer isEdvantage) throws SQLException {
    return this.masterBranchDao.getBranchByLoanTypeStateDistrictRACPCScholar(loanType, branchStateId, branchCityId, branchDistrictId, branchIsRACPC, branchIsScholar, isNRI, isEdvantage);
  }
  
  public Map<Integer, String> getStateCityDistrictBranch(Integer callingFor, Integer loanType, Integer branchStateId, Integer branchCityId, Integer branchDistrictId, Integer branchIsRACPC, Integer branchIsScholar, Integer isNRI, Integer isEdvantage, String isXpressCreditIT) throws SQLException {
    return this.masterBranchDao.getStateCityDistrictBranch(callingFor, loanType, branchStateId, branchCityId, branchDistrictId, branchIsRACPC, branchIsScholar, isNRI, isEdvantage, isXpressCreditIT, true);
  }
  
  public Map<Integer, String> getStateCityDistrictBranchWithoutLoanType(Integer callingFor, Integer loanType, Integer branchStateId, Integer branchCityId, Integer branchDistrictId, Integer branchIsRACPC, Integer branchIsScholar, Integer isNRI, Integer isEdvantage, String isXpressCreditIT) throws SQLException {
    return this.masterBranchDao.getStateCityDistrictBranch(callingFor, loanType, branchStateId, branchCityId, branchDistrictId, branchIsRACPC, branchIsScholar, isNRI, isEdvantage, isXpressCreditIT, false);
  }
  
  public Map<Integer, String> getStateCityDistrictBranchForBidya(Integer callingFor, Integer loanType, Integer branchStateId, Integer branchCityId, Integer branchDistrictId, Integer branchIsRACPC, Integer branchIsScholar, Integer isNRI, Integer isEdvantage, String isXpressCreditIT, Integer isBidyaLakhmi) throws SQLException {
    return this.masterBranchDao.getStateCityDistrictBranchBid(callingFor, loanType, branchStateId, branchCityId, branchDistrictId, branchIsRACPC, branchIsScholar, isNRI, isEdvantage, isXpressCreditIT, isBidyaLakhmi, true);
  }
  
  public Integer getTeamLeadBySalesTeamId(Integer salesTeamId, Integer branchId) {
    return this.masterBranchDao.getTeamLeadBySalesTeamId(salesTeamId, branchId);
  }
  
  public MasterInstitute getInstituteByInstituteName(String instituteName) throws NoResultException {
    return this.masterInstituteDao.getInstituteByInstituteName(instituteName);
  }
  
  public MasterBranch getBranchById(Integer branchId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterBranch)this.masterBranchDao.findById(branchId, MasterBranch.class);
  }
  
  public List<MasterCarDealer> getAllCarDealer() throws NoResultException {
    return this.masterCarDealerDao.getAllCarDealer();
  }
  
  public MasterCarDealer getCarDealerByName(String dealerName) throws NoResultException {
    return this.masterCarDealerDao.getPreownedDealerByName(dealerName);
  }
  
  public List<MasterCoApplicant> getAllCoApplicantByLoanId(Integer LoanId, Integer coapplicantIsNRI) throws NoResultException {
    return this.masterCoApplicantDao.getAllCoaaplicantByLoanId(LoanId, coapplicantIsNRI);
  }
  
  public MasterCoApplicant getCoApplicantById(Integer coapplicantId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterCoApplicant)this.masterCoApplicantDao.findById(coapplicantId, MasterCoApplicant.class);
  }
  
  public MasterCity getCityById(Integer cityId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
      if (ValidatorUtil.isValid(cityId))
        return (MasterCity)this.cityDao.findById(cityId, MasterCity.class); 
    return null;
  }
  
  public ApplicationFormLead save(ApplicationFormLead applicationFormLead) throws SQLException {
    return (ApplicationFormLead)this.applicationFormLeadDao.save(applicationFormLead.getLeadId(), applicationFormLead);
  }
  
  public PrivacyRequestResponse save(PrivacyRequestResponse privacyRequestResponse) throws SQLException {
	    return (PrivacyRequestResponse)this.privacyNoticeDao.save(privacyRequestResponse.getPrivacyId(), privacyRequestResponse);
  }
  
  public RequestACallBack save(RequestACallBack requestacallBack) throws SQLException {
    return (RequestACallBack)this.requestACallBackDao.save(requestacallBack.getRqstCallId(), requestacallBack);
  }
  
  public ApplicationFormLeadCall save(ApplicationFormLeadCall applicationFormLeadCall) {
    return (ApplicationFormLeadCall)this.applicationFormLeadCallsDao.save(applicationFormLeadCall.getCallId(), applicationFormLeadCall);
  }
  
  public BiometricRequest save(BiometricRequest biometricRequest) throws SQLException {
    return (BiometricRequest)this.biometricRequestDao.save(biometricRequest.getBioRequestId(), biometricRequest);
  }
  
  public BiometricResponse save(BiometricResponse biometricResponse) throws SQLException {
    return (BiometricResponse)this.biometricResponseDao.save(biometricResponse.getBioAppseqId(), biometricResponse);
  }
  
  public SbiCampaignMaster save(SbiCampaignMaster sbiCampignMaster) {
    return (SbiCampaignMaster)this.sbiCampaignDao.save(sbiCampignMaster.getWebCampaignId(), sbiCampignMaster);
  }
  
  public void insertCallLog(Integer callApplicationId, Integer callUserId, Integer callStatusId, String message) {
    try {
      if (callStatusId != null && callStatusId.intValue() > 0) {
        ApplicationFormLeadCall callsLog = new ApplicationFormLeadCall();
        if (callApplicationId != null)
          callsLog.setCallAppId(callApplicationId); 
        callsLog.setCallActive(Integer.valueOf(1));
        callsLog.setCallDeleted(Integer.valueOf(0));
        callsLog.setCallIsGenericAlert("N");
        callsLog.setCallEndTime(new Date());
        if (callUserId == null) {
          callsLog.setCallLmsUserId(Constants.OTHER_ID_INTEGER);
        } else {
          callsLog.setCallLmsUserId(callUserId);
        } 
        callsLog.setCallStatusId(callStatusId);
        callsLog.setCallAppBankId(Constants.LEAD_BANK_ID);
        if (message == null) {
          LoanStatus loanStatus = getLoanStatusByLoanStatusId(callStatusId);
          callsLog.setCallDescription((loanStatus != null) ? loanStatus.getLoanStatusTitle() : null);
        } else {
          callsLog.setCallDescription(message);
        } 
        callsLog = save(callsLog);
      } 
    } catch (IllegalArgumentException e) {
        logger.info("CommonService.java LNo : 476 : Exception Caught", e);
      }  
  }
  
  public ApplicationFormLead getLeadById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    if (id == null)
      return null; 
    return (ApplicationFormLead)this.applicationFormLeadDao.findById(id, ApplicationFormLead.class);
  }
  
  public RequestACallBack getRqstIdById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    if (id == null)
      return null; 
    return (RequestACallBack)this.requestACallBackDao.findById(id, RequestACallBack.class);
  }
  
  public List<MasterBank> getAllBank() throws NoResultException {
    return this.bankDao.getAllBank();
  }
  
  public List<MasterBank> getAllBankForHL() throws NoResultException {
    return this.bankDao.getAllBankForHL();
  }
  
  public List<MasterBank> getAllBankForAL() throws NoResultException {
    return this.bankDao.getAllBankForAL();
  }
  
  public List<MasterBank> getAllBankForPL() throws NoResultException {
    return this.bankDao.getAllBankForPL();
  }
  
  public List<MasterBank> getAllBankForELTakeOver() throws NoResultException {
    return this.bankDao.getAllBankForELTakeOver();
  }
  
  public MasterBank getBankByBankId(Integer bankId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterBank)this.bankDao.findById(bankId, MasterBank.class);
  }
  
  public TrackVisit getVisitDetails(Integer visitId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return this.trackVisitDao.getTrackVisitDetails(visitId);
  }
  
  public TrackCampaign getCampaignDetails(Integer campignId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return this.trackCampaignDao.getCampaignDetails(campignId);
  }
  
  public TrackMainCampaign getMainCampaignDetails(Integer mainCampignId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return this.trackMainCampaignDao.getMainCampaignDetails(mainCampignId);
  }
  
  public TrackCampaignPlacementSource getCampaignPlacementSourceDetails(Integer mainCampaignPlacementSrcId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return this.trackCampaignPlacementSourceDao.getCampaignPlacementSourceDetails(mainCampaignPlacementSrcId);
  }
  
  public List<MasterEmployer> getAllEmployerByQuerySting(String queryString, boolean forPL, String isNasscom) throws NoResultException {
    return this.masterEmployerDao.getAllEmployer(queryString, forPL, isNasscom);
  }
  
  public List<MasterBranch> getAllBranchNameByQueryString(String queryString) throws NoResultException, SQLException {
	    return this.masterBranchDao.getAllBranchByName(queryString);
  }
  
  public String getBranchNameByQueryString(String queryString) throws NoResultException, SQLException {
	    return masterBranchDao.getBranchName(queryString);
  }
  
  public Integer getBranchIdByBranchName(String queryString) throws NoResultException, SQLException {
	    return masterBranchDao.getIdByBranchName(queryString);
  }
    
  public Integer getAllEmployerIdByName(String employerName) throws NoResultException {
    List<MasterEmployer> emplist = this.masterEmployerDao.getAllEmployerIdByName(employerName);
    if (emplist == null || emplist.size() == 0)
      return Integer.valueOf(0); 
    MasterEmployer obj = emplist.get(0);
    return obj.getEmployerCompanyId();
  }
  
  public MasterEmployer getEmployerIdByName(String employerName) throws NoResultException {
    List<MasterEmployer> emplist = this.masterEmployerDao.getAllEmployerIdByName(employerName);
    if (emplist == null || emplist.size() == 0)
      return null; 
    MasterEmployer obj = emplist.get(0);
    return obj;
  }
  
  public List<MasterCarDealer> getPreownedCarDealer(String queryString) throws NoResultException {
    return this.masterCarDealerDao.getPreownedDealer(queryString);
  }
  
  public MasterDealer findCarDealerById(Integer carDealerId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterDealer)this.masterDealerDao.findById(carDealerId, MasterDealer.class);
  }
  
  public MasterCarDealer findCarDealerByCarDealerId(Integer carDealerId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterCarDealer)this.masterCarDealerDao.findById(carDealerId, MasterCarDealer.class);
  }
  
  public List<MasterDealer> getDealerByCompanyId(String queryString, Integer companyId) throws NoResultException {
    return this.masterDealerDao.getAllDealersByQueryString(queryString, companyId);
  }
  
  public MasterBuilder getBuilderById(Integer builderId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterBuilder)this.masterBuilderDao.findById(builderId, MasterBuilder.class);
  }
  
  public List<MasterBuilder> getAllBuilderByQuerySting(String queryString) throws NoResultException {
    return masterBuilderDao.getAllBuilders(queryString);
  }
  
  public Integer getAllBuilderById(String builderName) throws NoResultException {
    List<MasterBuilder> builderList = this.masterBuilderDao.getAllBuilderById(builderName);
    if (builderList == null || builderList.size() == 0)
      return null; 
    if (builderList != null && builderList.size() > 0) {
      MasterBuilder obj = builderList.get(0);
      return obj.getBuilderId();
    } 
    return null;
  }
  
  public List<MasterDocumentType> getDocumentTypeList(int loanTypeId, int loanProductId) throws NoResultException {
    return this.documentTypeDao.getDocumentTypeList(loanTypeId, loanProductId);
  }
  
  public MasterDocumentType getDocumentType(int documentTypeId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterDocumentType)this.documentTypeDao.findById(Integer.valueOf(documentTypeId), MasterDocumentType.class);
  }
  
  public List<MasterIndustryType> getIndustryTypeByLoanId(Integer loanTypeId) throws NoResultException {
    return this.masterIndustryTypeDao.getIndustryTypeByLoanId(loanTypeId);
  }
  
  public MasterIndustryType getIndustryTypeById(Integer industryTypeId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterIndustryType)this.masterIndustryTypeDao.findById(industryTypeId, MasterIndustryType.class);
  }
  
  public String getTemplateByTemplateType(int templateid, int typeId) throws NoResultException {
    List<Template> list = this.templateDao.getTemplateByTemplateType(Integer.valueOf(templateid), Integer.valueOf(typeId));
    if (list != null) {
    	Integer templateId = ((Template)list.get(0)).getTemplateId();
    	String templateContent = getClobdata("RUPEEPOWER_OCAS_T_13688", "template_content", "template_id", templateId);
    	return templateContent;
    }
    return "Error wile fetching mails";
  }
  
  public List<MasterGender> getAllGender() throws NoResultException {
    return this.masterGenderDao.getAllGender();
  }
  
  public String getTemplateByLoanTypeRequestIndexTemplateTypeId(Integer loanType, Integer requestIndex, Integer templateType, Integer havePan) throws NoResultException {
    List<Template> list = this.templateDao.getTemplateByLoanTypeRequestIndexTemplateTypeId(loanType, requestIndex, templateType, havePan);
    if (list != null && list.size() > 0) {
    	Integer templateId = ((Template)list.get(0)).getTemplateId();
    	String templateContent = getClobdata("RUPEEPOWER_OCAS_T_13688", "template_content", "template_id", templateId);
    	return templateContent;
    }
    return "";
  }
  
  public List<MasterSalesTeam> getAllHLSalesTeam(Integer salesTeamCityId) throws NoResultException {
    return this.masterSalesTeamDao.getAllHLSalesTeam(salesTeamCityId);
  }
  
  public MasterSalesTeam getHLSalesTeamById(Integer salesTeamCityId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterSalesTeam)this.masterSalesTeamDao.findById(salesTeamCityId, MasterSalesTeam.class);
  }
  
  public List<MasterProfession> getAllProfession(Integer productId) throws NoResultException {
    return this.masterProfessionDao.getAllProfession(productId);
  }
  
  public BankLmsUser getUserByPHPSessionId(String productId) throws SQLException, NullPointerException {
    return this.bankLMSUserDao.getUserByPHPSessionId(productId);
  }
  
  public BankLmsUser getBankLmsUserById(Integer lmsUserId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (BankLmsUser)this.bankLMSUserDao.findById(lmsUserId, BankLmsUser.class);
  }
  
  public List<MasterCorpSalaryPackage> getAllSalaryPackageByLoanType(Integer corpSalPackLoanTypeId) throws NoResultException {
    return this.masterCorpSalaryPackageDao.getAllSalaryPackageByLoanType(corpSalPackLoanTypeId);
  }
  
  public List<MasterCorpSalaryPackage> getAllSalaryPackageByLoanTypeAndIndustryTypeId(Integer corpSalPackLoanTypeId, Integer corpSalPackIndustryTypeId) throws NoResultException {
    return this.masterCorpSalaryPackageDao.getAllSalaryPackageByLoanTypeAndIndustryTypeId(corpSalPackLoanTypeId, corpSalPackIndustryTypeId);
  }
  
  public List<MasterLabelTooltip> getToolTipByLoanTypeAndReqestIndex(Integer loanType, Integer requestIndex) throws NoResultException {
    return this.masterLabelTooltipDao.getToolTipByLoanTypeAndReqestIndex(loanType, requestIndex);
  }
  
  public List<ApplicationFormLeadCall> getCallLogByLeadAppId(Integer callAppId) throws NoResultException {
    return this.applicationFormLeadCallsDao.getCallLogByLeadAppId(callAppId);
  }
  
  public ApplicationFormLead getLeadByAppSeqId(Integer leadAppSeqId, Integer leadProductTypeId) throws SQLException,NoResultException {
    return this.applicationFormLeadDao.getLeadByAppSeqId(leadAppSeqId, leadProductTypeId);
  }
  
  public boolean getLeadByProductTypeAndMobileNo(Integer leadProductTypeId, Integer leadLoanPurposeId, String leadIsdCode, String leadMobileNo, Integer loanTypeId) throws SQLException {
    return this.applicationFormLeadDao.getLeadByProductTypeAndMobileNo(leadProductTypeId, leadLoanPurposeId, leadIsdCode, leadMobileNo, loanTypeId);
  }
  
  public IntermediaryRel findRelationByIntermediatryIdAndLoanType(Integer intermediaryRelId, Integer loanType) throws NoResultException {
    return this.intermediaryRelDao.findRelationByIntermediatryIdAndLoanType(intermediaryRelId, loanType);
  }
  
  public MasterLmsIntermediary getLmsIntermediaryRelByIntermediatryId(Integer intermediaryRelId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterLmsIntermediary)this.masterLmsIntermediaryDao.findById(intermediaryRelId, MasterLmsIntermediary.class);
  }
  
  public boolean checkLmsUserLogin(String sessionId) throws SQLException, NullPointerException {
    return this.bankLMSUserDao.checkLmsUserLogin(sessionId);
  }
  
  public boolean checkLmsUserLoginCookie(String sessionId) throws SQLException, NullPointerException {
    return this.bankLMSUserDao.checkLmsUserLoginCookie(sessionId);
  }
  
  public SbiCampaignMaster getOtpByMobileNumber(String Mobile) throws NoResultException {
    return (SbiCampaignMaster)this.sbiCampaignDao.getOtpByMobileNumber(Mobile);
  }
  
  public Integer getAutoAssignLMSUserId(Integer branchId, Integer salesTeamId) {
    Integer banklmsUserId = null;
    try {
      banklmsUserId = this.bankLMSUserDao.getAutoAssignLMSUserId(branchId, salesTeamId);
    } catch (SQLException e) {
        logger.info("CommonService.java LNo : 766 : Exception Caught", e);
    }
    return banklmsUserId;
  }
  
  public Integer getLmsUserLocationId(Integer lmsUserId) {
    Integer lmsUserLocationId = null;
    try {
      lmsUserLocationId = this.bankLMSUserDao.getLmsUserLocationId(lmsUserId);
    } catch (SQLException e) {
        logger.info("CommonService.java LNo : 742 : Exception Caught", e);
    } 
    return lmsUserLocationId;
  }
  
  public LoanStatus getLoanStatusByLoanStatusId(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    if (id == null || id.intValue() == 0)
      return null; 
    return (LoanStatus)this.loanStatusDao.findById(id, LoanStatus.class); 
  }
  
  public List<CallLogsDetails> getCallLogByLeadAppId(int loanTypeId, Integer appSeqId, Integer appLeadId) throws SQLException {
    return this.applicationFormLeadCallsDao.getCallLogByLeadAppId(loanTypeId, appSeqId, appLeadId);
  }
  
  public Integer getCircleIdByBranchId(Integer branchId) {
    try {
      return this.masterBranchDao.getCircleIdByBranchId(branchId);
    } catch (SQLException e) {
        logger.info("CommonService.java LNo : 754 : Exception Caught", e);
        return null;
    } 
  }
  
  public String getBMOrSalesTeamMobile(Integer salesTeamId, Integer branchId, int isSalesTeam) {
    try {
      return this.masterBranchDao.getBMOrSalesTeamMobile(salesTeamId, branchId, isSalesTeam);
    } catch (SQLException e) {
        logger.info("CommonService.java LNo : 754 : Exception Caught", e);
        return null;
      }  
  }
  
  public MasterCBSCall getMasterCBSCallObjById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    if (id == null)
      return null; 
    return (MasterCBSCall)this.masterCBSCallDao.findById(id, MasterCBSCall.class);
  }
  
  public MasterCBSResponse getMasterCBSResponseById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    if (id == null)
      return null; 
    return (MasterCBSResponse)this.masterCBSResponseDao.findById(id, MasterCBSResponse.class);
  }
  
  public List<MasterSalesTeam> getHLSTMPSTByBranchId(Integer branchId, Integer loanPurposeId) throws SQLException {
    return this.masterBranchDao.getHLSTMPSTByBranchId(branchId, loanPurposeId);
  }
  
  public boolean isBankUser(Integer bankLmsUserId) {
    return this.bankLmsUserRoleDao.isBankUser(bankLmsUserId);
  }
  
  public RsmData getRSMData(RsmData rsmData, Integer productVariantId) {
    return this.masterBranchDao.getRSMData(rsmData, productVariantId);
  }
  
  public int getRSMDecision(int rsmScore, int rsmDecPId, int loanTypeId) {
    return this.masterBranchDao.getRSMDecision(rsmScore, rsmDecPId, Integer.valueOf(loanTypeId));
  }
  
  public SecurityChecks getSecurityChecksById(Integer securityChecksId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (SecurityChecks)this.securityChecksDao.findById(securityChecksId, SecurityChecks.class);
  }
  
  public List<Integer> getApplicationForMissingPDF(int loanTypeId, String startDate, String endDate) throws SQLException {
    return this.masterBranchDao.getApplicationForMissingPDF(loanTypeId, startDate, endDate);
  }
  
  public List<Integer> getLeadForMissingClickToCall(String startDate, String endDate) throws SQLException {
    return this.masterBranchDao.getLeadForMissingClickToCall(startDate, endDate);
  }
  
  public List<Integer> getLeadForMissingClickToCallInfronics(String startDate, String endDate) throws SQLException {
    return this.masterBranchDao.getLeadForMissingClickToCallInfronics(startDate, endDate);
  }
  
  public String getBranchCodeByLMSUserId(Integer lmsUserId) throws SQLException {
    return this.masterBranchDao.getBranchCodeByLMSUserId(lmsUserId);
  }
  
  /*public VlpFileListDetails save(VlpFileListDetails vlpsaveRequest) {
    return (VlpFileListDetails)this.vlpRequestResponseDao.save(vlpsaveRequest.getVlpFileId(), vlpsaveRequest);
  }*/
  
  public MasterCBSResponse save(MasterCBSResponse masterCBSResponse) throws SQLException {
    return (MasterCBSResponse)this.masterCBSResponseDao.save(masterCBSResponse.getCbsResponseId(), masterCBSResponse);
  }
  
  public MasterCBSCall save(MasterCBSCall masterCBSCall) throws SQLException {
    return (MasterCBSCall)this.masterCBSCallDao.save(masterCBSCall.getCbsResponseId(), masterCBSCall);
  }
  
  public CbsProductMapping getCbsMappingsByProductId(Integer loanTypeId, String cbsProId) throws NoResultException, NullPointerException {
    return this.cbsProductMappingDao.getCbsMappingsByProductId(loanTypeId, cbsProId);
  }
  
  public boolean isCbsMappingsExistByProductId(Integer loanTypeId, String cbsProId) throws SQLException {
    return this.cbsProductMappingDao.isCbsMappingsExistByProductId(loanTypeId, cbsProId);
  }
  
  public boolean getBankLmsUserRole(Integer bankLmsUserId) throws SQLException {
    return this.masterBranchDao.getBankLmsUserRole(bankLmsUserId);
  }
  
  public Integer getBranchIdByBranchCode(Integer BranchCode) throws SQLException {
    return this.masterBranchDao.getBranchIdByBranchCode(BranchCode);
  }
  
  public Integer getBranchCodeByBranchId(Integer BranchId) throws SQLException {
    return this.masterBranchDao.getBranchCodeByBranchId(BranchId);
  }
  
  public BureauLinkRequestResponse save(BureauLinkRequestResponse bureauLinkRequestResponse) {
    return (BureauLinkRequestResponse)this.bureauLinkRequestResponseDao.save(bureauLinkRequestResponse.getBlOcasRequestId(), bureauLinkRequestResponse);
  }
  
  public boolean getBankLmsUserRoleExceptContactCenter(Integer bankLmsUserId) throws NullPointerException {
    return this.masterBranchDao.getBankLmsUserRoleExceptContactCenter(bankLmsUserId);
  }
  
  public boolean isMobileFoundForDedupeInLead(String mobileNumber, int productTypeId) throws SQLException, NoResultException {
    return this.applicationFormLeadDao.isMobileFoundForDedupeInLead(mobileNumber, productTypeId);
  }
  
  public boolean isUniqueReferenceNo(String referenceNumber, int productTypeId) {
    return this.applicationFormLeadDao.isUniqueReferenceNo(referenceNumber, productTypeId);
  }
  
  public Map<Integer, String> getStateQuery(Integer loanType) throws SQLException {
    return this.masterBranchDao.getStateQuery(loanType);
  }
  
  public Map<Integer, String> getCityQuery(Integer stateId) throws SQLException {
    return this.masterBranchDao.getAgriCityQuery(stateId);
  }
  
  public Map<Integer, String> getDistrictQuery(Integer loanType, Integer stateId) throws SQLException {
    return this.masterBranchDao.getDistrictQuery(loanType, stateId);
  }
  
  public Map<Integer, String> getSubdistrictQuery(Integer loanType, Integer districtId) throws SQLException {
    return this.masterBranchDao.getSubdistrictQuery(loanType, districtId);
  }
  
  public Map<Integer, String> getVillageQuery(Integer loanType, Integer subdistrictId) throws SQLException {
    return this.masterBranchDao.getVillageQuery(loanType, subdistrictId);
  }
  
  /*public MicroLeadEntity getFlipkartMicroLeadByMobileNo(String mobileNo) throws Exception {
    return this.flipkartMicroDao.getFlipkartMicroLeadByMobileNo(mobileNo);
  }
  
  public MicroLeadEntity getFlipkartMicroLeadByMobileAndAccountNo(String mobileNo, String accountNo) throws Exception {
    return this.flipkartMicroDao.getFlipkartMicroLeadByMobileAndAccountNo(mobileNo, accountNo);
  }
  
  public Map<Integer, String> getcitiesSCCMetro(Integer loanType) throws SQLException {
    return this.masterBranchDao.getcitiesSCCMetro(loanType);
  }
  
  public Map<Integer, String> getCititesSCC(Integer loanType) throws NoResultException {
    return this.masterBranchDao.getCititesSCC(loanType);
  }
  
  public MicroLeadEntity getFlipkartLeadById(Integer leadId) throws Exception {
    return this.flipkartMicroDao.getFlipkartLeadById(leadId);
  }*/
  
  public int getCallBackLeadCount(Integer leadLoanPurposeId, String leadIsdCode, String leadMobileNo) throws SQLException {
    return this.applicationFormLeadDao.getCallBackLeadCount(leadLoanPurposeId, leadIsdCode, leadMobileNo);
  }
  
  public CustomerFeedback getCustFeedbackLeadById(Integer leadId) throws SQLException {
    return this.customerFeedbackDao.getCustFeedbackLeadById(leadId);
  }
  
  public CustomerFeedback getCustFeedbackLeadByMobileAndLoantype(String mobileNo, String loantype) throws SQLException {
    return this.customerFeedbackDao.getCustFeedbackLeadByMobileAndLoantype(mobileNo, loantype);
  }
  
  public CustomerFeedback getCustFeedbackLeadByMobileNo(String mobileNo) throws SQLException {
    logger.info("in common service getCustFeedbackLeadByMobileNo() Lno.1026");
    return this.customerFeedbackDao.getCustFeedbackLeadByMobileNo(mobileNo);
  }
  
  public MasterCityDao getCityDao() {
    return this.cityDao;
  }
  
  public void setCityDao(MasterCityDao cityDao) {
    this.cityDao = cityDao;
  }
  
  public LoanPurposeDao getLoanPurposeDao() {
    return this.loanPurposeDao;
  }
  
  public void setLoanPurposeDao(LoanPurposeDao loanPurposeDao) {
    this.loanPurposeDao = loanPurposeDao;
  }
  
  public LoanTypeDao getLoanTypeDao() {
    return this.loanTypeDao;
  }
  
  public void setLoanTypeDao(LoanTypeDao loanTypeDao) {
    this.loanTypeDao = loanTypeDao;
  }
  
  public EmploymentTypeDao getEmploymentTypeDao() {
    return this.employmentTypeDao;
  }
  
  public void setEmploymentTypeDao(EmploymentTypeDao employmentTypeDao) {
    this.employmentTypeDao = employmentTypeDao;
  }
  
  public CountryDao getCountryDao() {
    return this.countryDao;
  }
  
  public void setCountryDao(CountryDao countryDao) {
    this.countryDao = countryDao;
  }
  
  public MasterBankDao getBankDao() {
    return this.bankDao;
  }
  
  public void setBankDao(MasterBankDao bankDao) {
    this.bankDao = bankDao;
  }
  
  public DocumentTypeDao getDocumentTypeDao() {
    return this.documentTypeDao;
  }
  
  public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
    this.documentTypeDao = documentTypeDao;
  }
  
  public MasterBuilderDao getMasterBuilderDao() {
    return this.masterBuilderDao;
  }
  
  public void setMasterBuilderDao(MasterBuilderDao masterBuilderDao) {
    this.masterBuilderDao = masterBuilderDao;
  }
  
  public MasterEmployerDao getMasterEmployerDao() {
    return this.masterEmployerDao;
  }
  
  public void setMasterEmployerDao(MasterEmployerDao masterEmployerDao) {
    this.masterEmployerDao = masterEmployerDao;
  }
  
  public MasterDistrictDao getMasterDistrictDao() {
    return this.masterDistrictDao;
  }
  
  public void setMasterDistrictDao(MasterDistrictDao masterDistrictDao) {
    this.masterDistrictDao = masterDistrictDao;
  }
  
  public MasterBranchDao getMasterBranchDao() {
    return this.masterBranchDao;
  }
  
  public void setMasterBranchDao(MasterBranchDao masterBranchDao) {
    this.masterBranchDao = masterBranchDao;
  }
  
  public MasterLocalityDao getLocalityDao() {
    return this.localityDao;
  }
  
  public void setLocalityDao(MasterLocalityDao localityDao) {
    this.localityDao = localityDao;
  }
  
  public TemplateDao getTemplateDao() {
    return this.templateDao;
  }
  
  public void setTemplateDao(TemplateDao templateDao) {
    this.templateDao = templateDao;
  }
  
  public MasterStateDao getStateDao() {
    return this.stateDao;
  }
  
  public void setStateDao(MasterStateDao stateDao) {
    this.stateDao = stateDao;
  }
  
  public MasterCarDealerDao getMasterCarDealerDao() {
    return this.masterCarDealerDao;
  }
  
  public void setMasterCarDealerDao(MasterCarDealerDao masterCarDealerDao) {
    this.masterCarDealerDao = masterCarDealerDao;
  }
  
  public ApplicationFormLeadDao getApplicationFormLeadDao() {
    return this.applicationFormLeadDao;
  }
  
  public void setApplicationFormLeadDao(ApplicationFormLeadDao applicationFormLeadDao) {
    this.applicationFormLeadDao = applicationFormLeadDao;
  }
  
  public ResidenceTypeDao getResidenceTypeDao() {
    return this.residenceTypeDao;
  }
  
  public void setResidenceTypeDao(ResidenceTypeDao residenceTypeDao) {
    this.residenceTypeDao = residenceTypeDao;
  }
  
  public RelationshipWithBankDao getRelationshipWithBankDao() {
    return this.relationshipWithBankDao;
  }
  
  public void setRelationshipWithBankDao(RelationshipWithBankDao relationshipWithBankDao) {
    this.relationshipWithBankDao = relationshipWithBankDao;
  }
  
  public MasterCoApplicantDao getMasterCoApplicantDao() {
    return this.masterCoApplicantDao;
  }
  
  public void setMasterCoApplicantDao(MasterCoApplicantDao masterCoApplicantDao) {
    this.masterCoApplicantDao = masterCoApplicantDao;
  }
  
  public MasterIndustryTypeDao getMasterIndustryTypeDao() {
    return this.masterIndustryTypeDao;
  }
  
  public void setMasterIndustryTypeDao(MasterIndustryTypeDao masterIndustryTypeDao) {
    this.masterIndustryTypeDao = masterIndustryTypeDao;
  }
  
  public LoanCategoryDao getLoanCategoryDao() {
    return this.loanCategoryDao;
  }
  
  public void setLoanCategoryDao(LoanCategoryDao loanCategoryDao) {
    this.loanCategoryDao = loanCategoryDao;
  }
  
  public HlProductDao getHlProductDao() {
    return this.hlProductDao;
  }
  
  public void setHlProductDao(HlProductDao hlProductDao) {
    this.hlProductDao = hlProductDao;
  }
  
  public AlProductDao getAlProductDao() {
    return this.alProductDao;
  }
  
  public void setAlProductDao(AlProductDao alProductDao) {
    this.alProductDao = alProductDao;
  }
  
  public ElProductDao getElProductDao() {
    return this.elProductDao;
  }
  
  public void setElProductDao(ElProductDao elProductDao) {
    this.elProductDao = elProductDao;
  }
  
  public PlProductDao getPlProductDao() {
    return this.plProductDao;
  }
  
  public void setPlProductDao(PlProductDao plProductDao) {
    this.plProductDao = plProductDao;
  }
  
  public MasterGenderDao getMasterGenderDao() {
    return this.masterGenderDao;
  }
  
  public void setMasterGenderDao(MasterGenderDao masterGenderDao) {
    this.masterGenderDao = masterGenderDao;
  }
  
  public MasterSalesTeamDao getMasterSalesTeamDao() {
    return this.masterSalesTeamDao;
  }
  
  public void setMasterSalesTeamDao(MasterSalesTeamDao masterSalesTeamDao) {
    this.masterSalesTeamDao = masterSalesTeamDao;
  }
  
  public MasterProfessionDao getMasterProfessionDao() {
    return this.masterProfessionDao;
  }
  
  public void setMasterProfessionDao(MasterProfessionDao masterProfessionDao) {
    this.masterProfessionDao = masterProfessionDao;
  }
  
  public List<MasterDealer> getAllDealer() throws NoResultException {
    return this.masterDealerDao.getAllDealers();
  }
  
  public MasterDealerDao getMasterDealerDao() {
    return this.masterDealerDao;
  }
  
  public void setMasterDealerDao(MasterDealerDao masterDealerDao) {
    this.masterDealerDao = masterDealerDao;
  }
  
  public BankLMSUserDao getBankLMSUserDao() {
    return this.bankLMSUserDao;
  }
  
  public void setBankLMSUserDao(BankLMSUserDao bankLMSUserDao) {
    this.bankLMSUserDao = bankLMSUserDao;
  }
  
  public MasterCorpSalaryPackageDao getMasterCorpSalaryPackageDao() {
    return this.masterCorpSalaryPackageDao;
  }
  
  public void setMasterCorpSalaryPackageDao(MasterCorpSalaryPackageDao masterCorpSalaryPackageDao) {
    this.masterCorpSalaryPackageDao = masterCorpSalaryPackageDao;
  }
  
  public MasterLabelTooltipDao getMasterLabelTooltipDao() {
    return this.masterLabelTooltipDao;
  }
  
  public void setMasterLabelTooltipDao(MasterLabelTooltipDao masterLabelTooltipDao) {
    this.masterLabelTooltipDao = masterLabelTooltipDao;
  }
  
  public ApplicationFormLeadCallsDao getApplicationFormLeadCallsDao() {
    return this.applicationFormLeadCallsDao;
  }
  
  public void setApplicationFormLeadCallsDao(ApplicationFormLeadCallsDao applicationFormLeadCallsDao) {
    this.applicationFormLeadCallsDao = applicationFormLeadCallsDao;
  }
  
  public MasterInstituteDao getMasterInstituteDao() {
    return this.masterInstituteDao;
  }
  
  public void setMasterInstituteDao(MasterInstituteDao masterInstituteDao) {
    this.masterInstituteDao = masterInstituteDao;
  }
  
  public IntermediaryRelDao getIntermediaryRelDao() {
    return this.intermediaryRelDao;
  }
  
  public void setIntermediaryRelDao(IntermediaryRelDao intermediaryRelDao) {
    this.intermediaryRelDao = intermediaryRelDao;
  }
  
  public MasterLmsIntermediaryDao getMasterLmsIntermediaryDao() {
    return this.masterLmsIntermediaryDao;
  }
  
  public void setMasterLmsIntermediaryDao(MasterLmsIntermediaryDao masterLmsIntermediaryDao) {
    this.masterLmsIntermediaryDao = masterLmsIntermediaryDao;
  }
  
  public MasterContentDao getMasterContentDao() {
    return this.masterContentDao;
  }
  
  public void setMasterContentDao(MasterContentDao masterContentDao) {
    this.masterContentDao = masterContentDao;
  }
  
  public BankLmsUserRoleDao getBankLmsUserRoleDao() {
    return this.bankLmsUserRoleDao;
  }
  
  public void setBankLmsUserRoleDao(BankLmsUserRoleDao bankLmsUserRoleDao) {
    this.bankLmsUserRoleDao = bankLmsUserRoleDao;
  }
  
  public LoanStatusDao getLoanStatusDao() {
    return this.loanStatusDao;
  }
  
  public void setLoanStatusDao(LoanStatusDao loanStatusDao) {
    this.loanStatusDao = loanStatusDao;
  }
  
  public MasterQualificationDao getMasterQualificationDao() {
    return this.masterQualificationDao;
  }
  
  public void setMasterQualificationDao(MasterQualificationDao masterQualificationDao) {
    this.masterQualificationDao = masterQualificationDao;
  }
  
  public SecurityChecksDao getSecurityChecksDao() {
    return this.securityChecksDao;
  }
  
  public void setSecurityChecksDao(SecurityChecksDao securityChecksDao) {
    this.securityChecksDao = securityChecksDao;
  }
  
  public BureauLinkRequestResponseDao getBureauLinkRequestResponseDao() {
    return this.bureauLinkRequestResponseDao;
  }
  
  public void setBureauLinkRequestResponseDao(BureauLinkRequestResponseDao bureauLinkRequestResponseDao) {
    this.bureauLinkRequestResponseDao = bureauLinkRequestResponseDao;
  }
  
  public BiometricRequestDao getBiometricRequestDao() {
    return this.biometricRequestDao;
  }
  
  public void setBiometricRequestDao(BiometricRequestDao biometricRequestDao) {
    this.biometricRequestDao = biometricRequestDao;
  }
  
  public BiometricResponseDao getBiometricResponseDao() {
    return this.biometricResponseDao;
  }
  
  public void setBiometricResponseDao(BiometricResponseDao biometricResponseDao) {
    this.biometricResponseDao = biometricResponseDao;
  }
  
  public MasterCBSResponseDao getMasterCBSResponseDao() {
    return this.masterCBSResponseDao;
  }
  
  public void setMasterCBSResponseDao(MasterCBSResponseDao masterCBSResponseDao) {
    this.masterCBSResponseDao = masterCBSResponseDao;
  }
  
  public MasterCBSCallDao getMasterCBSCallDao() {
    return this.masterCBSCallDao;
  }
  
  public void setMasterCBSCallDao(MasterCBSCallDao masterCBSCallDao) {
    this.masterCBSCallDao = masterCBSCallDao;
  }
  
  public CbsProductMappingDao getCbsProductMappingDao() {
    return this.cbsProductMappingDao;
  }
  
  public void setCbsProductMappingDao(CbsProductMappingDao cbsProductMappingDao) {
    this.cbsProductMappingDao = cbsProductMappingDao;
  }
  
  /*public VLPFileExportInfo save(VLPFileExportInfo vlpFileExportInfo) {
    return (VLPFileExportInfo)this.vlpExportDao.save(vlpFileExportInfo.getFileId(), vlpFileExportInfo);
  }
  
  public VlpRequestResponseDao getVlpRequestResponseDao() {
    return this.vlpRequestResponseDao;
  }
  
  public void setVlpRequestResponseDao(VlpRequestResponseDao vlpRequestResponseDao) {
    this.vlpRequestResponseDao = vlpRequestResponseDao;
  }
  
  public VLPExportDao getVlpExportDao() {
    return this.vlpExportDao;
  }
  
  public void setVlpExportDao(VLPExportDao vlpExportDao) {
    this.vlpExportDao = vlpExportDao;
  }
  
  public List<VLPFileExportInfo> getAllExportedFilesByStatus(String fileStatus) throws Exception {
    return this.vlpExportDao.getAllExportedFilesByStatus(fileStatus);
  }
  
  public VLPLogInfo save(VLPLogInfo vlpLogInfo) {
    return (VLPLogInfo)this.vlpLogInfoDao.save(vlpLogInfo.getVlpExportId(), vlpLogInfo);
  }
  
  public VLPLogInfoDao getVlpLogInfoDao() {
    return this.vlpLogInfoDao;
  }
  
  public void setVlpLogInfoDao(VLPLogInfoDao vlpLogInfoDao) {
    this.vlpLogInfoDao = vlpLogInfoDao;
  }*/
  
  public LastAppRefDao getLastAppRefDao() {
    return this.lastAppRefDao;
  }
  
  public void setLastAppRefDao(LastAppRefDao lastAppRefDao) {
    this.lastAppRefDao = lastAppRefDao;
  }
  
  public SbiCampaignDao getSbiCampaignDao() {
    return this.sbiCampaignDao;
  }
  
  public void setSbiCampaignDao(SbiCampaignDao sbiCampaignDao) {
    this.sbiCampaignDao = sbiCampaignDao;
  }
  
  public WebServiceDao getWebServiceDao() {
    return this.webServiceDao;
  }
  
  public void setWebServiceDao(WebServiceDao webServiceDao) {
    this.webServiceDao = webServiceDao;
  }
  
  public Integer getBranchIdByLMSUserId(Integer lmsUserId, Integer loanTypeId) throws SQLException {
    return this.masterBranchDao.getBranchIdByLMSUserId(lmsUserId, loanTypeId);
  }
  
  public String getCampaignByVisitId(Integer visitId) {
    return this.masterBranchDao.getCampaignByVisitId(visitId);
  }
  
  /*public FlipkartMicroLeadDao getFlipkartMicroDao() {
    return this.flipkartMicroDao;
  }
  
  public void setFlipkartMicroDao(FlipkartMicroLeadDao flipkartMicroDao) {
    this.flipkartMicroDao = flipkartMicroDao;
  }
  
  public MicroLeadEntity save(MicroLeadEntity flipkartMicroEntity) {
    return (MicroLeadEntity)this.flipkartMicroDao.save(flipkartMicroEntity.getMicroLeadId(), flipkartMicroEntity);
  }
  
  public MingleDao getMingleDao() {
    return this.mingleDao;
  }
  
  public void setMingleDao(MingleDao mingleDao) {
    this.mingleDao = mingleDao;
  }*/
  
  public CRMNextLog save(CRMNextLog crmNextLog) throws SQLException {
    return (CRMNextLog)this.crmNextLogDao.save(crmNextLog.getCrmLogId(), crmNextLog);
  }
  
  public CRMFetchLeadStatus save(CRMFetchLeadStatus insertInDb) {
	  return (CRMFetchLeadStatus)this.crmFetchLeadStatusDao.save(insertInDb.getFetchStatusLogId(), insertInDb);
	}
  
  public CRMFetchLeadStatus getLeadByReferenceID(String appReferenceId) {
	  CRMFetchLeadStatus application = this.crmFetchLeadStatusDao.getCRMFetchLeadStatusByAppReferenceId(appReferenceId);
		if(application==null){
			logger.info("CommonService.java LNo : 1528 result is null");
			return null;
		}
		return application;
  }
  
  public CRMNextLog getCRMLeadIdByReferenceID(String appReferenceId) {
	  CRMNextLog application = this.crmFetchLeadStatusDao.getCRMLeadIDByReferenceId(appReferenceId);
		if(application==null){
			logger.info("CommonService.java LNo : 1528 result is null");
			return null;
		}
		return application;
  }
  
  public CRMFetchLeadStatusDao getCrmFetchLeadStatusDao() {
	return crmFetchLeadStatusDao;
}

public void setCrmFetchLeadStatusDao(CRMFetchLeadStatusDao crmFetchLeadStatusDao) {
	this.crmFetchLeadStatusDao = crmFetchLeadStatusDao;
}

public CRMNextLogDao getCrmNextLogDao() {
    return this.crmNextLogDao;
  }
  
  public void setCrmNextLogDao(CRMNextLogDao crmNextLogDao) {
    this.crmNextLogDao = crmNextLogDao;
  }
  
  public TrackVisitDao getTrackVisitDao() {
    return this.trackVisitDao;
  }
  
  public void setTrackVisitDao(TrackVisitDao trackVisitDao) {
    this.trackVisitDao = trackVisitDao;
  }
  
  public TrackCampaignDao getTrackCampaignDao() {
    return this.trackCampaignDao;
  }
  
  public void setTrackCampaignDao(TrackCampaignDao trackCampaignDao) {
    this.trackCampaignDao = trackCampaignDao;
  }
  
  public TrackMainCampaignDao getTrackMainCampaignDao() {
    return this.trackMainCampaignDao;
  }
  
  public void setTrackMainCampaignDao(TrackMainCampaignDao trackMainCampaignDao) {
    this.trackMainCampaignDao = trackMainCampaignDao;
  }
  
  public TrackCampaignPlacementSourceDao getTrackCampaignPlacementSourceDao() {
    return this.trackCampaignPlacementSourceDao;
  }
  
  public void setTrackCampaignPlacementSourceDao(TrackCampaignPlacementSourceDao trackCampaignPlacementSourceDao) {
    this.trackCampaignPlacementSourceDao = trackCampaignPlacementSourceDao;
  }
  
  public CustomerFeedback save(CustomerFeedback customerFeedback) {
    return (CustomerFeedback)this.customerFeedbackDao.save(customerFeedback.getPbFbId(), customerFeedback);
  }
  
  public Map<Integer, String> getBranches(Integer loanTypeId) throws NoResultException {
    List<MasterBank> banks = getAllBankForPL();
    Map<Integer, String> branches = new HashMap<>();
    if (banks != null)
      for (int i = 0; i < banks.size(); i++)
        branches.put(Integer.valueOf(i), ((MasterBank)banks.get(i)).getBankName());  
    return branches;
  }
  
  public Integer getApplicationCount(String isdCode, String appMobileNo, Integer loanTypeId) throws SQLException {
    return this.masterBranchDao.getApplicationCount(isdCode, appMobileNo, loanTypeId);
  }
  
  public Integer getCBSApplicationCount(Integer isdCode, String appMobileNo, Integer loanTypeId) throws SQLException {
    return this.masterBranchDao.getCBSApplicationCount(isdCode, appMobileNo, loanTypeId);
  }
  
  public CallBackLeadPushLog save(CallBackLeadPushLog contactCenterLeadResponse) throws SQLException {
    return (CallBackLeadPushLog)this.contactCenterLeadResponseDao.save(contactCenterLeadResponse.getLeadId(), contactCenterLeadResponse);
  }
  
  public CallBackLeadPushLog findById(Integer id) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (CallBackLeadPushLog)this.contactCenterLeadResponseDao.findById(id, CallBackLeadPushLog.class);
  }
  
  public CallBackLeadPushLog findByLeadId(Integer leadId) throws NoResultException {
    Map<String, Object> params = new HashMap<>(1);
    params.put("leadId", leadId);
    List<CallBackLeadPushLog> list = this.contactCenterLeadResponseDao.findByNamedQuery("CallBackLeadPushLog.findByLeadId", params);
    if (list == null || list.isEmpty())
      return null; 
    return list.get(0);
  }
  
  public ContactCenterLeadResponseDao getContactCenterLeadResponseDao() {
    return this.contactCenterLeadResponseDao;
  }
  
  public void setContactCenterLeadResponseDao(ContactCenterLeadResponseDao contactCenterLeadResponseDao) {
    this.contactCenterLeadResponseDao = contactCenterLeadResponseDao;
  }
  
  public List<MasterEmployeeOccupationType> getAllOccupationTypesByEmploymentType(Integer employeeTypeId) throws NoResultException {
    return this.employeeOccupationTypeDao.getAllOccupationTypesByEmploymentType(employeeTypeId);
  }
  
  public List<MasterEmployeeOccupationType> getAllOccupationTypesByLoanType(Integer loanTypeId) throws NoResultException {
    return this.employeeOccupationTypeDao.getAllOccupationTypesByLoanType(loanTypeId);
  }
  
  public MasterEmployeeOccupationType getOccupationTypesByEmploymentTypeId(Integer occupationTypeId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
    return (MasterEmployeeOccupationType)this.employeeOccupationTypeDao.findById(occupationTypeId, MasterEmployeeOccupationType.class);
  }
  
  public SbiUtil getSbiUtil() {
    return this.SbiUtil;
  }
  
  public void setSbiUtil(SbiUtil sbiUtil) {
    this.SbiUtil = sbiUtil;
  }
  
  public boolean getRequestACallBackByAppReferenceIdAndMobileNo(String appReferenceId, String trackMobile) throws NoResultException {
    boolean application = this.requestACallBackDao.getRequestACallBackByAppReferenceIdAndMobileNo(appReferenceId, trackMobile);
    if (application)
      return true; 
    return false;
  }
  
  public List<MasterEmployeeOccupationType> getOccupationCategoryByOccupationType(Integer occupationTypeId) throws NoResultException {
    return this.employeeOccupationTypeDao.getOccupationCategoryByOccupationType(occupationTypeId);
  }
  
   //Added by Pratima for CVE
	public ApplicationFormCveLoan save(ApplicationFormCveLoan cveForm)throws SQLException {
		return applicationFormCveLoanDao.save(cveForm.getAppSeqId(), cveForm);
	}
	
	public ApplicationFormCveLoan getApplicationFormCveLoanBySeqId(Integer appSeqId)throws NoResultException {

		logger.info("PersonalLoanService.java LNo : 952 : getApplicationFormCveLoanBySeqId:::::"+appSeqId);
		
		ApplicationFormCveLoan application = applicationFormCveLoanDao.getApplicationFormCveLoanBySeqId(appSeqId);
		logger.info("PersonalLoanService.java LNo : 955 : application.toString()"+application.toString());
		
		return application;
	    } 
	
	public ApplicationFormCveLoan getApplicationFormCveLoanByAppReferenceIdAndMobileNo(String appReferenceId, String cbsMobileNumber)throws NoResultException {
		
		ApplicationFormCveLoan application = applicationFormCveLoanDao.getApplicationFormCveLoanByAppReferenceIdAndMobileNo(appReferenceId, cbsMobileNumber);
 		if(application==null){
 			return null;
 		}
		return application;
	}
	
	public ApplicationFormCveLoan getApplicationFormCveLoanByAppReferenceId(String appReferenceId)throws NoResultException {
		
		ApplicationFormCveLoan application = applicationFormCveLoanDao.getApplicationFormCveLoanByAppReferenceId(appReferenceId);
 		if(application==null){
 			return null;
 		}
		return application;
	}
	
	
		public ApplicationFormCveLoanDao getApplicationFormCveLoanDao() {
			return applicationFormCveLoanDao;
		}
	
		public void setApplicationFormCveLoanDao(ApplicationFormCveLoanDao applicationFormCveLoanDao) {
			this.applicationFormCveLoanDao = applicationFormCveLoanDao;
		}
		
		//Added for CVE
		public String getCveReferenceIdBySeqId(Integer appSeqId) throws SQLException {
			logger.info("getCveReferenceIdBySeqId>>appSeqId::"+appSeqId);
			
			String referenceId="";
			
			referenceId = applicationFormCveLoanDao.getCveReferenceIdBySeqId(appSeqId);
			logger.info("getCveReferenceIdBySeqId>>referenceId"+referenceId);
			
			return referenceId;
		}
		//Ended for CVE
  
	  /*public String getCRMEmployerName(String employerName) {
	    try {
	      return this.masterEmployerDao.getCRMEmployerName(employerName);
	    } catch (Exception e) {
	      logger.info("CommonService.java LNo : 1684 : Exception Caught", e);
	      return null;
	    } 
	  }*/

		//Added for Centralized CVE consent
		public ApplicationFormCaseCve save(ApplicationFormCaseCve applicationFormCaseCve)throws SQLException {
			return applicationFormCaseCveDao.save(applicationFormCaseCve.getCrmLogId(), applicationFormCaseCve);
		}	

		public ApplicationFormCaseCveDao getApplicationFormCaseCveDao() {
			return applicationFormCaseCveDao;
		}

		public void setApplicationFormCaseCveDao(ApplicationFormCaseCveDao applicationFormCaseCveDao) {
			this.applicationFormCaseCveDao = applicationFormCaseCveDao;
		}
		
		public boolean updateCRMLeadCVE(String appRefNumberCve, String crmLeadIdCve, String cveAppEmail) throws SQLException {
//			logger.info("CommonService.java updateCRMLeadCVE >> appCrmLeadId---"+crmLeadIdCve+"--appReferenceId--"+appRefNumberCve+"--cveAppEmail--"+cveAppEmail);
			return applicationFormCveLoanDao.updateCRMLeadCVE(appRefNumberCve, crmLeadIdCve, cveAppEmail);
		}
	   //Ended for Centralized CVE consent

	//Added for CRM REST API
	public Integer getLeadSourceKey(Domain appFormData) throws SQLException,NoResultException {
		Integer leadSourceKey=7;
		
	//Auto loan
		if (appFormData instanceof ApplicationFormAutoLoan) {
			logger.info("getLeadSourceKey inside Auto Loan");
			ApplicationFormAutoLoan autoAppForm = (ApplicationFormAutoLoan) appFormData;
			
			logger.info("getCrmLeadSourceId :: " + autoAppForm.getCrmLeadSourceId());
			if (autoAppForm.getCrmLeadSourceId() != null) {
		    	leadSourceKey = autoAppForm.getCrmLeadSourceId();	//web drop off
		    } else if (autoAppForm.getAppDataSourceId() != null) {
		    	leadSourceKey = getLeadSourceKey(autoAppForm.getAppDataSourceId(), autoAppForm.getAppSeqId(), Constants.AUTO_LOAN_ID);
		    } else {
		    	leadSourceKey = 7; //OCAS
		    }
		} 
		
		//Home loan
		if (appFormData instanceof ApplicationFormHomeLoan) {
			logger.info("getLeadSourceKey inside Home Loan");
			ApplicationFormHomeLoan homeAppForm = (ApplicationFormHomeLoan) appFormData;
			
			logger.info("getCrmLeadSourceId :: " + homeAppForm.getCrmLeadSourceId());
			if (homeAppForm.getCrmLeadSourceId() != null) {
		    	leadSourceKey = homeAppForm.getCrmLeadSourceId();	//web drop off
		    } else if (homeAppForm.getAppDataSourceId() != null) {
		    	leadSourceKey = getLeadSourceKey(homeAppForm.getAppDataSourceId(), homeAppForm.getAppSeqId(), Constants.HOME_LOAN_ID);
		    } else {
		    	leadSourceKey = 7; //OCAS
		    }
		}
		
		//Scholar loan
			if (appFormData instanceof ApplicationFormEducationLoan) {
			logger.info("getLeadSourceKey inside Education Loan");
			ApplicationFormEducationLoan educationAppForm = (ApplicationFormEducationLoan) appFormData;
			
			logger.info("getCrmLeadSourceId :: " + educationAppForm.getCrmLeadSourceId());
			if (educationAppForm.getCrmLeadSourceId() != null) {
		    	leadSourceKey = educationAppForm.getCrmLeadSourceId();	//web drop off
		    } else if (educationAppForm.getAppDataSourceId() != null) {
		    	leadSourceKey = getLeadSourceKey(educationAppForm.getAppDataSourceId(), educationAppForm.getAppSeqId(), Constants.EDUCATION_LOAN_ID);
		    } else {
		    	leadSourceKey = 7; //OCAS
		    }
		} 
		
		//Personal loan
		if (appFormData instanceof ApplicationFormPersonalLoan) {
			logger.info("getLeadSourceKey inside Personal Loan");
			ApplicationFormPersonalLoan personalAppForm = (ApplicationFormPersonalLoan) appFormData;
			
			logger.info("getCrmLeadSourceId :: " + personalAppForm.getCrmLeadSourceId());
			if (personalAppForm.getCrmLeadSourceId() != null) {
		    	leadSourceKey = personalAppForm.getCrmLeadSourceId();	//web drop off
		    } else if (personalAppForm.getAppDataSourceId() != null) {
		    	leadSourceKey = getLeadSourceKey(personalAppForm.getAppDataSourceId(), personalAppForm.getAppSeqId(), Constants.PERSONAL_LOAN_ID);
		    } else {
		    	leadSourceKey = 7; //OCAS
		    }
		} 

    //Agri loan
	if (appFormData instanceof ApplicationFormAgriLoan) {
		logger.info("getLeadSourceKey inside Agri Loan");
		ApplicationFormAgriLoan agriAppForm = (ApplicationFormAgriLoan)appFormData;
		logger.info("getCrmLeadSourceId :: " + agriAppForm.getCrmLeadSourceId());
		if (agriAppForm.getCrmLeadSourceId() != null) {
			leadSourceKey = agriAppForm.getCrmLeadSourceId();
		} else if (agriAppForm.getAppDataSourceId() != null) {
			leadSourceKey = getLeadSourceKey(agriAppForm.getAppDataSourceId(), agriAppForm.getAppSeqId(), Constants.AGRI_LOAN_ID);
		} 
	}
			
		logger.info("final leadSourceKey :: " + leadSourceKey);
		
		return leadSourceKey;
	}

	private Integer getLeadSourceKey(Integer appDataSourceId, Integer appSeqId, Integer loanTypeId) throws SQLException,NoResultException {
		
		Integer leadSourceKey = 7;
		switch (appDataSourceId) {
			case 1 : leadSourceKey = 128; //OCAS web
			break;
			
			case 2 : leadSourceKey = getUploadLeadSource(appSeqId, loanTypeId);
			break;
			
			//case 3 : leadSourceKey = 10; //Branch walkin
			case 3 : leadSourceKey = 7; //changed as per CRM requirement for LMS created lead
			break;
			
			case 4 : leadSourceKey = 127; //Callback
			break;
			
			case 7 : leadSourceKey = 7; //OCAS
			break;
			
			case 8 : leadSourceKey = 127; //Callback
			break;
		}
		return leadSourceKey;
	}

	private Integer getUploadLeadSource(Integer appSeqId, Integer loanTypeId) throws SQLException,NoResultException {
		
		Integer leadSourceKey = 126; //upload
		logger.info("appSeqId :: " + appSeqId);
	  	ApplicationFormLead lead = this.applicationFormLeadDao.getLeadByAppSeqId(appSeqId, loanTypeId);
	  	
	  	Integer campaignId = null;
	  	if (lead != null) {
	  		campaignId = lead.getLeadCampaignId();
	  		if (campaignId != null) {
	  			switch (campaignId) {
	    			case 3 : leadSourceKey = 91; //SMS
	    			break;
	    			
	    			case 302 : leadSourceKey = 92; //missed call
	    			break;
	  			}
	    	}
	  	}
			return leadSourceKey;

	}	
	//Ended CRM REST API	

		
	public BureauLinkRequestConfig getCicConfigById(Integer id)  {
		if (id == null)
	    	return null;
	    return (BureauLinkRequestConfig)this.bureauLinkRequestResponseDao.getCicRequestConfig(id);
	}


	public ConsentDao getConsentDao() {
		return consentDao;
	}

	public void setConsentDao(ConsentDao consentDao) {
		this.consentDao = consentDao;
	}
	
	public CcmsConfigDao getCcmsConfigDao() {
		return ccmsConfigDao;
	}

	public void setCcmsConfigDao(CcmsConfigDao ccmsConfigDao) {
		this.ccmsConfigDao = ccmsConfigDao;
	}
	
	public Consent getConsentByLoanType(Integer loanTypeId) {
		if (loanTypeId == null)
	    	return null;
	    return consentDao.getConsentByLoanType(loanTypeId);
	}
	
	public Consent getConsentByLoanAndCustomerType(Integer loanTypeId, String customerType) {
		if (loanTypeId == null)
	    	return null;
	    return consentDao.getConsentByLoanAndCustomerType(loanTypeId, customerType);
	}

	public Integer getConsentIdByLoanType(Integer loanTypeId) throws NoResultException, SQLException {
		if (loanTypeId == null)
	    	return null;
	    return consentDao.getConsentIdByLoanType(loanTypeId);
	}
	
	public MasterAgriProductDao getAglProductDao() {
		return this.aglProductDao;
	}

	public void setAglProductDao(MasterAgriProductDao aglProductDao) {
		this.aglProductDao = aglProductDao;
	}
	
	  public boolean isTeleCallerUserRole(Integer bankLmsUserId) throws NullPointerException {
	    return this.masterBranchDao.isTeleCallerUserRole(bankLmsUserId);
	  }
	  
	  //read clob data separately to avoid privacy violation error [14, 2] after DB TCPS implementation
	  public String getClobdata(String table, String clobColumn, String idColumn, Integer idVal) {
		  return consentDao.getClobData(table, clobColumn, idColumn, idVal);
	  }

	  public CCMSConfig getCcmsConfigById(Integer id)  {
		  if (id == null)
			  return null;

		  return (CCMSConfig)this.ccmsConfigDao.getCcmsConfigById(id);
	  }
	
	
	 public PrivacyNoticeDao getPrivacyNoticeDao() {
		 return privacyNoticeDao;
	 }

	 public void setPrivacyNoticeDao(PrivacyNoticeDao privacyNoticeDao) {
		 this.privacyNoticeDao = privacyNoticeDao;
	 }
	 

	 public boolean getLanguageBylannguageCode(String lannguageCode) throws NoResultException, SQLException {
		   
		   Integer count = (Integer) this.privacyNoticeDao.getLanguageBylannguageCode(lannguageCode);
		    
		   logger.info("count "+count);
		   
		   if(count>0) {
			   return true;
		   }else {
			   return false;
		   }

		 
		 
	 }
	 
	 public PrivacyRequestResponse getPrivacyNoticeByLocale(String privacyLocale)throws NoResultException {
			
			PrivacyRequestResponse application = privacyNoticeDao.getPrivacyRequestResponseByPrivacyLocale(privacyLocale);
	 		if(application==null){
	 			return null;
	 		}
			return application;
	 }

	public PrivacyRequestResponseDao getPrivacyRequestResponseDao() {
		return privacyRequestResponseDao;
	}


	public void setPrivacyRequestResponseDao(PrivacyRequestResponseDao privacyRequestResponseDao) {
		this.privacyRequestResponseDao = privacyRequestResponseDao;
	}
	
	//
	public PrivacyRequestResponse getPrivacyByLocale(String locale) {
		return privacyRequestResponseDao.getPrivacyByLocale(locale);
	}
	
	// Language drop down 
//	public List<Object[]> getAllPrivacyLanguages() {
//		return privacyRequestResponseDao.getAllPrivacyLanguages();
//	}
	
	public MasterLanguageDao getMasterLanguageDao() {
		return masterLanguageDao;
	}
	public void setMasterLanguageDao(MasterLanguageDao masterLanguageDao) {
		this.masterLanguageDao = masterLanguageDao;
	}



	public List<MasterLanguage> getAllActiveLanguages() {
		return masterLanguageDao.getAllActiveLanguages();
	}

	
}
