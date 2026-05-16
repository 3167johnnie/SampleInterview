package com.mintstreet.common.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.result.StreamResult;

import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.FileHelper;
import com.mintstreet.common.validation.ValidatorUtil;

public class FileUploadAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(FileUploadAction.class.getName());
	private static final long serialVersionUID = 1L;
	
	private File qqfile;
	private String qqfileContentType;
	private String qqfileFileName;
	private String qquuid;
	private String qqtotalfilesize;
	private String qqfilePath;
	private String responseJSON;
	private String tempdocUrl;
	private String docRank;
	private String uploadProdctName;
	private String docFolder;
	private byte uploadType;
	private String thumbdocId;
	private String thumbdocUrlId;
	private String imageDivId;

	private String localFileName;
 String extension;
	
	public StreamResult upload(){
		
		Set<String> invalidFileNames = new HashSet<>();
		//invalidFileNames.add("x00");
		invalidFileNames.add("../");
		invalidFileNames.add("\\");
		invalidFileNames.add("<!--");
		invalidFileNames.add("-->");
		invalidFileNames.add("%20");
		invalidFileNames.add("%22");
		invalidFileNames.add("%3c");
		invalidFileNames.add("%253c");
		invalidFileNames.add("%3e");
		invalidFileNames.add("%0e");
		invalidFileNames.add("%28");
		invalidFileNames.add("%29");
		invalidFileNames.add("%2528");		
		invalidFileNames.add("%26");
		invalidFileNames.add("%24");
		invalidFileNames.add("%3f");
		invalidFileNames.add("%3b");
		invalidFileNames.add("%3d");
		
		String regex="^[xX[0-9]{3}]*$";
		
		try {
			if(!isDsrPage.equalsIgnoreCase("true")){
				if(SessionUtil.getBankLMSUser()!=null){
					responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
					responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
			        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
				}
			}
			
			if(uploadType > 0) {
				if(ValidatorUtil.isValid(uploadProdctName) && qqfile != null){
					appSeqId = null;
					if(Constants.HL_PDF_GENRATION_LOCATION.equalsIgnoreCase(uploadProdctName)){
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						if(appSeqId==null){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						}
					}else if(Constants.AL_PDF_GENRATION_LOCATION.equalsIgnoreCase(uploadProdctName)){
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
					}else if(Constants.EL_PDF_GENRATION_LOCATION.equalsIgnoreCase(uploadProdctName)){
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId==null){
							appSeqId = SessionUtil.getEducationTakeOverLoanApplicationSequenceId();
						}
					}else if(Constants.PL_PDF_GENRATION_LOCATION.equalsIgnoreCase(uploadProdctName)){
						appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
					}else if(Constants.AGL_PDF_GENRATION_LOCATION.equalsIgnoreCase(uploadProdctName)){
						appSeqId = SessionUtil.getAgriLoanApplicationSequenceId();
					}else if(Constants.SCC_PDF_GENRATION_LOCATION.equalsIgnoreCase(uploadProdctName)){
						appSeqId = SessionUtil.getCreditCardApplicationSequenceId();
					}
					if(!ValidatorUtil.isValid(appSeqId) && uploadType!=6){
						responseMessage = Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
						responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
				        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
					}
					

					docFolder = Constants.UPLOAD_INITIAL+uploadProdctName;
					extension = FilenameUtils.getExtension(qqfileFileName);
					logger.info("FileUploadAction.java LN 80 file name ::" + qqfileFileName);
					logger.info("FileUploadAction.java LN 81 file extension ::" + extension);
					
					int length = qqfileFileName.split("\\.").length;
					String fileName = qqfileFileName.split("\\.")[0];
					logger.info("FileUploadAction.java LN 85 file name afte split :: "+ fileName);
					//write on 24 aug 2023
					logger.info("FileUploadAction.java LN 86 file getQqfileContentType afte split :: "+ getQqfileContentType());
					
					boolean isValidFile = true;
					if (getQqfileContentType().equals("application/pdf") && !extension.toLowerCase().contains("pdf") ) {
						isValidFile = false;
					} else if (getQqfileContentType().equals("image/jpeg") && !(extension.toLowerCase().contains("jpeg") || extension.toLowerCase().contains("jpg")) ) {
						isValidFile = false;
					}  else if (getQqfileContentType().equals("image/png") && !extension.toLowerCase().contains("png")) {
						isValidFile = false;
					} else if (getQqfileContentType().equals("image/gif") && !extension.toLowerCase().contains("gif")) {
						isValidFile = false;
					} else if (getQqfileContentType().equals("image/jpg")) {
						isValidFile = false;
					}
					
					if (!isValidFile) {
						responseMessage = "File contains invalid names. Please try again with valid file.";
						responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
				        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes()));
					}
					
					//write on 24 aug 2023
					if(fileName.matches(regex)){
						logger.info("FileUploadAction.java LN 116 text which is blocking :: "+ fileName);
        				responseMessage = "File contains invalid names. Please try again with valid file.";
						responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
				        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
					}
						
					for(String text : invalidFileNames) {
            			if (fileName.contains(text)) {//sCurrentLine.contains("<")
            				logger.info("FileUploadAction.java LN 117 text which is blocking :: "+ text);
            				responseMessage = "File contains invalid names. Please try again with valid file.";
    						responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
    				        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
		            	}/* else {
		            		output1.append(sCurrentLine);
		            	}*/
            		}
					
					if((length < 2) || (length > 2)) {
						responseMessage = "File contains invalid extensions. Please try again with valid extensions.";
						responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
				        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
					}
					
					if(!Character.isLetter(qqfileFileName.charAt(qqfileFileName.length()-1)) || !(Pattern.matches("^[a-zA-Z0-9_-]+$", fileName))) {
						responseMessage = "File name can contains only [@ , _ , -] variables. Please try again with valid file/file name.";
						responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
				        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
					}
					
					
					if(uploadType==6){
						appSeqId=0;
					}
					localFileName = appSeqId+DateUtil.getFormattedDateForUpload()+"."+extension;
					qqfile = FileHelper.handleFile(qqfile, localFileName, docFolder, false, extension);
					if(qqfile==null){
						responseMessage = "The uploaded file is incorrect format. Please try again.";
						responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
				        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes()));
					}

					tempdocUrl="";
					qqfileFileName="";
				}else{
					responseMessage = "Either the file size is more or incorrect file format. Please try again.";
					responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
			        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
				}
			}
			
		} catch (NullPointerException e) {
			logger.info("FileUploadAction.java LN 91 exception ::", e);
	        addActionError(e.getMessage());
	        responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
			responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
	        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
	    } catch (Exception e) {
			logger.info("FileUploadAction.java LN 91 exception ::", e);
	        addActionError(e.getMessage());
	        responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
			responseJSON = "{\"success\":false,\"thumbdocId\":\""+thumbdocId+"\",\"responseMessage\":\""+responseMessage+"\" }";
	        return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes())); 
	    }
		responseJSON = "{\"success\":true,  " +
				"\"thumbdocId\":\""+thumbdocId+"\", " +
				"\"extension\":\""+extension+"\"," +
				"\"thumbdocUrlId\":\""+thumbdocUrlId+"\"," +
				"\"localFileName\":\""+localFileName+"\"," +
				"\"qqfileFileName\":\""+qqfileFileName+"\"," +
				"\"uploadType\":\""+uploadType+"\"," +
				"\"docRank\":\""+docRank+"\"   }";
		return new StreamResult(new ByteArrayInputStream(responseJSON.getBytes()));  
	}
	
	public File getQqfile() {
		return qqfile;
	}
	public void setQqfile(File qqfile) {
		this.qqfile = qqfile;
	}
	public String getQqfileContentType() {
		return qqfileContentType;
	}
	public void setQqfileContentType(String qqfileContentType) {
		this.qqfileContentType = qqfileContentType;
	}
	public String getQqfileFileName() {
		return qqfileFileName;
	}
	public void setQqfileFileName(String qqfileFileName) {
		this.qqfileFileName = qqfileFileName;
	}
	public String getQquuid() {
		return qquuid;
	}
	public void setQquuid(String qquuid) {
		this.qquuid = qquuid;
	}
	public String getQqtotalfilesize() {
		return qqtotalfilesize;
	}
	public void setQqtotalfilesize(String qqtotalfilesize) {
		this.qqtotalfilesize = qqtotalfilesize;
	}
	public String getQqfilePath() {
		return qqfilePath;
	}
	public void setQqfilePath(String qqfilePath) {
		this.qqfilePath = qqfilePath;
	}
	public String getResponseJSON() {
		return responseJSON;
	}
	public void setResponseJSON(String responseJSON) {
		this.responseJSON = responseJSON;
	}
	public String getTempdocUrl() {
		return tempdocUrl;
	}
	public void setTempdocUrl(String tempdocUrl) {
		this.tempdocUrl = tempdocUrl;
	}
	public String getDocRank() {
		return docRank;
	}
	public void setDocRank(String docRank) {
		this.docRank = docRank;
	}
	public String getDocFolder() {
		return docFolder;
	}
	public void setDocFolder(String docFolder) {
		this.docFolder = docFolder;
	}
	public byte getUploadType() {
		return uploadType;
	}
	public void setUploadType(byte uploadType) {
		this.uploadType = uploadType;
	}
	public String getThumbdocId() {
		return thumbdocId;
	}
	public void setThumbdocId(String thumbdocId) {
		this.thumbdocId = thumbdocId;
	}
	public String getThumbdocUrlId() {
		return thumbdocUrlId;
	}
	public void setThumbdocUrlId(String thumbdocUrlId) {
		this.thumbdocUrlId = thumbdocUrlId;
	}
	public String getImageDivId() {
		return imageDivId;
	}
	public void setImageDivId(String imageDivId) {
		this.imageDivId = imageDivId;
	}
	public Integer getAppSeqId() {
		return appSeqId;
	}
	public void setAppSeqId(Integer appSeqId) {
		this.appSeqId = appSeqId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUploadProdctName() {
		return uploadProdctName;
	}

	public void setUploadProdctName(String uploadProdctName) {
		this.uploadProdctName = uploadProdctName;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}
	
}
