<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
 <li id="divAppOtherIdNumber">
	<label>Other Identity No<span class="req">&nbsp;* </span></label>
	<s:textfield placeholder="Enter OtherId number" cssClass="form-control" name="appForm.appOtherIdNumber"  id="appOtherIdNumber" value="%{appForm.appOtherIdNumber}"   maxlength="%{(appForm.appOtherId != null && appForm.appOtherId >0 && appForm.appOtherId == 166)?'10':((appForm.appOtherId != null && appForm.appOtherId >0 && appForm.appOtherId == 165)?'20':'12' ) }" />
</li>
