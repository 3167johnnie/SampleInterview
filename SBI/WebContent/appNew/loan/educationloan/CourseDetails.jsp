<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<s:set var="needToDisable" value="true"/>
	<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
		<s:set var="needToDisable" value="false"/>
	</s:if>
	<s:if test="%{#needToDisable==true}">
		<li id="437"><label>Country of study<b class="req">*</b></label>
			<div class="flat-field">
				<s:if test="%{isDsrPage.equalsIgnoreCase('false')}">
					<s:select list="#{'0':'Select country','1':'India','2':'Abroad'}"
						value="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_EDVANTAGE?2:1}"
						cssClass="form-select disabledFields" id="CountryofStudyId"
						name="quote.loanQuoteCountryOfStudyId" disabled="true"
						onchange="showPerspectiveRows4ResidentForEducation(this);" onfocus="customOnFocus(this);"/>
				</s:if>
				<s:else>
					<s:select list="#{'0':'Select country','1':'India','2':'Abroad'}"
						value="%{quote.loanQuoteCountryOfStudyId}" cssClass="form-select disabledFields"
						disabled="true" id="CountryofStudyId"
						name="quote.loanQuoteCountryOfStudyId"
						onchange="showPerspectiveRows4ResidentForEducation(this);" onfocus="customOnFocus(this);"/>
				</s:else>
			</div>
		</li>
	</s:if>
	
	<div id="EDUCATIONRESIDENTTYPE">
			<s:if test="%{isDsrPage.equalsIgnoreCase('false')}">
				<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_EDVANTAGE}">
					<li><label>Country opted for education<b class="req">*</b></label> 
						<div class="flat-field">
								<s:select list ="%{#countries}" cssClass="form-select" id="countryOfStudy" name="quote.loanQuoteCountryOfStudy"
								 autocomplete="off" aria-invalid="false" headerValue="Select country" headerKey="0" onfocus="customOnFocus(this);"/>
						</div>
					</li>
				</s:if>
			</s:if>	
			<s:else>
				<s:if test="%{quote.loanQuoteCountryOfStudyId!=null && quote.loanQuoteCountryOfStudyId==2}">
					<li><label>Country opted for education<b class="req">*</b></label> 
						<div class="flat-field">
								<s:select list ="%{#countries}" cssClass="form-select" id="countryOfStudy" name="quote.loanQuoteCountryOfStudy" 
								autocomplete="off" aria-invalid="false"  onfocus="customOnFocus(this);"/>
						</div>
					</li>
				</s:if>
			</s:else>
	</div>
	
	<li id=""><label>Course type<b class="req">*</b></label>
		<div class="flat-field">
			<s:select list="%{beanList.courseType==null?'':beanList.courseType}"
				onchange="changeCourse(this);" cssClass="form-select"
				id="courseType" name="quote.loanQuoteCourseTypeId" autocomplete="off"
				aria-invalid="false" value="%{quote.loanQuoteCourseTypeId}"
				headerKey="0" headerValue="Select course type" onfocus="customOnFocus(this);"/>
		</div>
	</li>
	
	<div id="COURSEINSTITUTEUNIVERSITY">
	 	<s:if test="%{quote!=null && quote.loanQuoteCourseTypeId>0}">
			<s:include value="/appNew/loan/educationloan/includes/CourseTypeDetails.jsp"></s:include>
		</s:if>
	</div>
	
	<s:if test="%{#needToDisable==true}">
		<li id="443"><label>Nature of course<b class="req">*</b></label>
			<div class="flat-field">
				<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_EDVANTAGE && isDsrPage=='false'}">
					<s:if test="%{isDsrPage=='false'}">
						<s:select list="#{'0':'Select course nature','1':'Full time'}"
							cssClass="form-select" id="natureOfCourse"
							name="quote.loanQuoteNatureOfCourseId" autocomplete="off"
							aria-invalid="false" value="%{quote.loanQuoteNatureOfCourseId}"
							headerKey="1" headerValue="Full time" disabled="true" onfocus="customOnFocus(this);"/>
					</s:if>
					<s:else>
						<s:select list="#{'0':'Select course nature','1':'Full time'}"
							cssClass="form-select" id="natureOfCourse"
							name="quote.loanQuoteNatureOfCourseId" autocomplete="off"
							aria-invalid="false" value="%{quote.loanQuoteNatureOfCourseId}"
							disabled="true" onfocus="customOnFocus(this);"/>
					</s:else>
				</s:if>
				<s:else>
					<s:select
						list="#{'0':'Select course nature','1':'Full time','2':'Part time'}"
						cssClass="form-select" id="natureOfCourse"
						name="quote.loanQuoteNatureOfCourseId" autocomplete="off"
						aria-invalid="false" value="%{quote.loanQuoteNatureOfCourseId}" onfocus="customOnFocus(this);"/>
				</s:else>
			</div>
		</li>
		<li id="447"><label>Course duration<b class="req">*</b></label>
			<div>
				<div class="flat-field1">
					<s:select
						list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6'}"
						id="courseDurationyears" name="quote.loanQuoteCourseDurationYears" cssClass="form-select"
						value="%{quote.loanQuoteCourseDurationYears}" headerKey="" headerValue="Select years" onfocus="customOnFocus(this);"/>
				</div>
				<div class="flat-field1 mr-r9"> 
					<s:select
						list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10','11':'11'}"
						id="courseDurationMonth" name="quote.loanQuoteCourseDurationMonth" cssClass="form-select "
						value="%{quote.loanQuoteCourseDurationMonth}" headerKey="" headerValue="Select months" onfocus="customOnFocus(this);"/>
				</div>
			</div>
		</li>
	</s:if>
	
