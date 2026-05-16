<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html dir="ltr" lang="en" version="-//W3C//DTD XHTML 1.1//EN" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd">

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE,chrome=1">
  	<title><s:property value="%{(metaInfo.title!=null && metaInfo.title!='') ? metaInfo.title : @com.mintstreet.common.util.Constants@HOME_TITLE}"/></title>
    <meta name="keywords" content="<s:property value="%{(metaInfo.keywords!=null && metaInfo.keywords!='') ? metaInfo.keywords : @com.mintstreet.common.util.Constants@HOME_KEYWORDS}"/>" />
    <meta name="description" content="<s:property value="%{(metaInfo.description!=null && metaInfo.description!='') ? metaInfo.description : @com.mintstreet.common.util.Constants@HOME_DESCRIPTION}"/>" />
	<meta name="keywords" content="home loan, home loans, quick home loan, auto home India, home loans India, home loan interest rates, home loans calculator, home loan rates in India." />
    
	
	
	<link rel="shortcut icon" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/favicon.ico"/>
  	<!--Bottstrap CSS -->
    <link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/bootstrap.css" media="all">
    <!--Core CSS -->
    <link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/global.css" media="all">
		
    <s:include value="/app/common/version.jsp"></s:include>
    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.min.js"></script>
</head>

<body lang="en" dir="ltr">
<div class="whiteBg contnetCntr">
	<div class="container-fluid">
    	<div class="row">
        	<div class="col-lg-12">
            	<a  href="javascript:close();"  class="btn close-btn btn-primary pull-right">Close</a>
                <div class="text-center">
                	<div class="padding20px">
						<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/logo.png" alt="state bank of india" title="state bank of india" />
                    </div>
                </div>
            	<ul class="table-bank-info clearfix">                
                    <li class="tb-loan-amount">
                    	<div class="box">
                        	<p>
	                        	<i class="ico-rupee"></i>
                        		<em class="font-rupee">`</em><span class="Rs"><s:property value="getText('{0,number,##,##,###}',{appForm.appLoanAmount*100000})"/></span>
	                        	<br />Loan Amount
                        	</p>
                        </div>
                    </li>
                    <s:if test="%{appForm.appHomeLoanId==12}">
	                    <li class="tb-intersetrate">
	                    	<div class="box">
	                    		<div class="bhl-int-icon">
	                    			<i class="ico-inrate"></i>
	                    		</div>
	                        	<div class="bhl-int-cont">
		                        	<p>
		                        		 <s:property value="getText('{0,number,##.00}',{appForm.getAppBhlInterestRate})"/>%&nbsp;
		                        		<span>
			                    	  		(1 - 12 months)	
			                    		</span>
			                    	</p>
			                    	<p>	
			                    		<s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRate})"/>%&nbsp;
			                    	  	<span>
			                    	  	(13 - 24 months)
			                    	  	</span> 
			                    	 </p>
		                    		Interest Rate
		                    	</div>                        	
	                        </div>
	                    </li>
                    </s:if>
                    <s:else>
	                    <li class="tb-intersetrate">
	                    	<div class="box">
	                        	<p><i class="ico-inrate"></i><span>
	                        	<s:if test="%{appForm.appLoanInterestRateDiscount>0}">
		                              <s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRateDiscount})"/>%&nbsp;
		                    	</s:if>
		                    	<s:else>
		                    	     <s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRate})"/>%&nbsp;
		                    	</s:else>
		                    	</span><br />Interest Rate</p>                        	
	                        </div>
	                    </li>
                    </s:else>
                    <li class="tb-tenure">
                    	<div class="box">
                        	<p><i class="ico-tenure"></i>
                        	    <span>
                        	    <s:if test="%{appForm.appProductTenureFlag==1}"><s:property value="%{appForm.appLoanTenure}"/> months</s:if>
	                    		<s:else><s:property value="%{appForm.appLoanTenure}"/> years</s:else></span>
	                    	<br />Tenure</p>                        	
                        </div>
                    </li>
                    <s:if test="%{appForm.appHomeLoanId==12}">
	                    <li class="tb-emi">
		                    <div class="box">
								<div class="bhl-int-icon">
									<i class="ico-emi"></i>
								</div>
								<div class="bhl-int-cont">	
									<p>
										<em class="font-rupee">`</em> <s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.getAppBhlFirstInterestEmi())})"/>
										<span>(1 - 12 months)</span>
									</p>
									<p>
									<em class="font-rupee">`</em>  <s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})"/>
										<span>(13 - 24 months)</span> 
									</p>	
									EMI
								</div>
								</div>
						</li>
                    </s:if>
                     <s:elseif test="%{appForm.productId!=null && appForm.productId==13 && appForm.appLoanTenure<=12}">
	                    <li class="tb-emi">
		                    <div class="box">
								<p>
									<i class="ico-emi"></i>
									<em class="font-rupee">`</em>
									<span class="Rs"><s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})"/></span>
									<br />Interest
								</p>
							</div>
						</li>
                    </s:elseif>
                    <s:else>
                    	<li class="tb-emi">
                       	<s:if test="%{appForm.appLoanEmiDiscount>0}">
                   				<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
									<s:if test="%{appForm.appHomeLoanId == @com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
										<div class="multi-emis">
											<ul class="fr-emi">
				                    			<li>
				                    				<span>Only Interest</span></br>
				                    				<s:property value="%{tenure1Duration}"/>  mth
				                    			</li>
				                    			<li>
				                    				<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/></span></br>
				                    				<s:property value="%{tenure2Duration}"/>  mth
				                    			</li>
				                    			<li>
				                    				<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/></span></br>
				                    				<s:property value="%{tenure3Duration}"/>  mth
				                    				
				                    			</li>
				                    			<li>
				                    				<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure4Emi})"/></span></br>
				                    				<s:property value="%{tenure4Duration}"/>  mth
				                    				
				                    			</li>
				                    		</ul>
				                    		<p>EMI</p>
				                    	</div>
									</s:if>
									<s:else>
										<div class="box">
											<p>
												<i class="ico-emi"></i>
												<em class="font-rupee">`</em>
												<span class="Rs"><s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmiDiscount)})"/></span>
												<br />EMI
											</p>
										</div>
									</s:else>
								</s:if>
								<s:else>
									<div class="box">
										<p>
											<i class="ico-emi"></i>
		                    				<em class="font-rupee">`</em>
		                    				<span class="Rs"><s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})"/></span>
											<br />EMI
										</p>
	                    			</div>
								</s:else>
                    	    </s:if>
							<s:else>
                  				<s:if test="%{(appForm.appHomeLoanId == @com.mintstreet.common.util.Constants@SBI_PRIVILEGE_PRODUCT_ID || appForm.appHomeLoanId == @com.mintstreet.common.util.Constants@SBI_SHAURYA_PRODUCT_ID)}">
	                  				<s:if test="%{(appForm.appLoanEmiDiscount1>0 && appForm.appLoanEmiDiscount2>0) && appForm.appLoanInterestRate>0 && appForm.appLoanInterestRateDiscount>0}">
		                   					<div class="box">
		                   						<ul class="tw-emi">
		                    						<li>
			                    						<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(appForm.appLoanEmiDiscount1)})"/></span></br>
			                    						0-<s:property value="%{repaymentMonth1}"/>  mth
		                    						</li>
		                    						<li>
		                    							<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(appForm.appLoanEmiDiscount2)})"/></span></br>
		                    							<s:property value="%{repaymentMonth1+1}"/>-<s:property value="%{repaymentMonth2}"/>  mth
		                    						</li>
		                   						</ul>
		                   						<p class="txt-center">EMI</p>
		                   					</div>
	                  					</s:if>
	                  					<s:else>
	                  						<s:if test="%{(appForm.appLoanEmi1>0 && appForm.appLoanEmi2>0)}">
	                  							<div class="box">
		                    						<ul class="tw-emi">
			                    						<li>
				                    						<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(appForm.appLoanEmi1)})"/></span></br>
				                    						0-<s:property value="%{repaymentMonth1}"/>  mth
			                    						</li>
			                    						<li>
			                    							<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(appForm.appLoanEmi2)})"/></span></br>
			                    							<s:property value="%{repaymentMonth1+1}"/>-<s:property value="%{repaymentMonth2}"/>  mth
			                    						</li>
		                    						</ul>
	                    							<p class="txt-center">EMI</p>
	                    						</div>
	                  						</s:if>
		                  					<s:else>
		                  						<s:if test="{appForm.appLoanEmi1==null && appForm.appLoanEmi2==null}">
		                  							<div class="box">
		                    						<p>
		                    							<i class="ico-emi"></i>
			                    						<em class="font-rupee">`</em>
			                    						<span class="Rs"><s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})"/></span>
		                    							<br />EMI</p>
		                    					</div>
		                  						</s:if>
		                  					</s:else>
	                  					</s:else>
                  				</s:if>
                  			 <s:else>
								<s:if test="%{appForm.appHomeLoanId == @com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
									<div class="multi-emis">
										<ul class="fr-emi">
				             			<li>
				             				<span>Only Interest</span></br>
											<s:property value="%{tenure1Duration}"/>  mth
										</li>
										<li>
											<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/></span></br>
											<s:property value="%{tenure2Duration}"/>  mth
											
										</li>
										<li>
											<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/></span></br>
											<s:property value="%{tenure3Duration}"/>  mth
											
										</li>
										<li>
											<span class="wr">Rs</span>&nbsp;<span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure4Emi})"/></span></br>
											<s:property value="%{tenure4Duration}"/>  mth
											</li>
										</ul>
										<p>EMI</p>
									</div>
								 </s:if>
								 <s:else>
									<div class="box">
										<p>
											<i class="ico-emi"></i>
											<em class="font-rupee">`</em>
											<span class="Rs"><s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})"/></span>
											<br />EMI
										</p>
									</div>
								 </s:else>
							</s:else>
						</s:else>         	
                       </li>   
                    </s:else>
					<li class="tb-emi">
                   		<div class="box">
							<p>
								<i class="ico-pr"></i>
								<em class="font-rupee">`</em>
								<s:if test="%{appForm.appLoanProcessingFeedDiscount>0}">
                   					<em class="font-rupee">`</em> <span class="Rs"><s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFeeDiscount)})"/></span>
                  				</s:if>
                    			<s:else>
									<s:if test ="%{appForm.appLoanTenure<=20 && appForm.appSchemePmay>0}">
										<span class="Rs">0</span>
									</s:if>
                    				<s:else>
	                    				<span class="Rs"><s:property value="getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFee)})"/></span>
                    				</s:else>
                    			</s:else>
                    	        <br />Processing fee
                    	    </p>                        	
						</div>
					</li>
                </ul>
            	<div class="loan-table-wrapper">
            	<s:if test="%{homeLoanPage>0}">
	            	<s:if test="%{appForm.appHomeLoanId==12}">
	            		<div class="bhl-loan th clearfix">
	                    	<div class="col col1">
	                        	Month #
	                        </div>
	                        <div class="col col2">
	                        	Month
	                        </div>
	                        <div class="col col3">
	                        	Beginning loan balance
	                        </div>
	                      <div class="col col4">
	                			Interest 
	                		</div>
	                        <div class="col col5">
	                        	Outstanding loan amount
	                        </div>
	                    </div>
	            	</s:if>
	            	<s:else>
	            		<div class="th clearfix">
	                    	<div class="col col1">
	                        	Month #
	                        </div>
	                        <div class="col col2">
	                        	Month
	                        </div>
	                        <div class="col col3">
	                        	Beginning loan balance
	                        </div>
		                	<div class="col col4">
	                       		<s:if test="%{homeLoanPage >0 && appForm.appHomeLoanId == 3}">EMI<!--  / Interest payable --></s:if> 
	                           <s:elseif test="%{educationLoanPage >0 && appForm.appLoanAccountType == 1}">EMI<!--  / Interest payable  --></s:elseif>
	                           <s:else>EMI</s:else>
	                       	</div>
	                		<div class="col col5">
	                        		Monthly interest
	                       	</div>
	                		<div class="col col6">
	                			Principal repaid
	                		</div>
	                        <div class="col col7">
	                        	Outstanding loan amount
	                        </div>
	                    </div>
            	</s:else>
	            </s:if>
	            <s:elseif test="%{personalLoanPage>0}">
		            <s:if test="%{appForm.productId!=null && appForm.productId==13 && appForm.appLoanTenure<=12}">
		            		<div class="th clearfix">
		                    	<div class="col col11">
		                        	Month #
		                        </div>
		                        <div class="col col22">
		                        	Month
		                        </div>
		                        <div class="col col33">
		                        	Beginning loan balance
		                        </div>
		                		
		              			<div class="col col44">
		                        		Monthly interest
		                       	</div>
		               			
		                        <div class="col col55">
		                        	Outstanding loan amount
		                        </div>
	                    	</div>
		            </s:if>
		            <s:else>
		            	<div class="th clearfix">
	                    	<div class="col col1">
	                        	Month #
	                        </div>
	                        <div class="col col2">
	                        	Month
	                        </div>
	                        <div class="col col3">
	                        	Beginning loan balance
	                        </div>
	                		<div class="col col4">
	                        		<s:if test="%{homeLoanPage >0 && appForm.appHomeLoanId == 3}">EMI<!--  / Interest payable --></s:if> 
		                           <s:elseif test="%{educationLoanPage >0 && appForm.appLoanAccountType == 1}">EMI<!--  / Interest payable  --></s:elseif>
		                           <s:else>EMI</s:else>
	                        </div>
	              			<div class="col col5">
	                        		Monthly interest
	                       	</div>
	               			<div class="col col6">
	               				Principal repaid 
	               			</div>
	                        <div class="col col7">
	                        	Outstanding loan amount
	                        </div>
	                    </div>
		            </s:else>
	            </s:elseif>
            	<s:else>
            		<div class="th clearfix">
                    	<div class="col col1">
                        	Month #
                        </div>
                        <div class="col col2">
                        	Month
                        </div>
                        <div class="col col3">
                        	Beginning loan balance
                        </div>
                		<div class="col col4">
                        		<s:if test="%{homeLoanPage >0 && appForm.appHomeLoanId == 3}">EMI<!--  / Interest payable --></s:if> 
	                           <s:elseif test="%{educationLoanPage >0 && appForm.appLoanAccountType == 1}">EMI<!--  / Interest payable  --></s:elseif>
	                           <s:else>EMI</s:else>
                        </div>
              			<div class="col col5">
                        		Monthly interest
                       	</div>
               			<div class="col col6">
               				Principal repaid 
               			</div>
                        <div class="col col7">
                        	Outstanding loan amount
                        </div>
                    </div>
            	</s:else>
				<s:iterator value="%{repayments}"  var="repayment" status="repaymentIndex" >             
                    <div class="tr clearfix <s:property value="%{#repaymentIndex.index%2!=0?'alternate':''}"/>">
                    	<s:if test="%{homeLoanPage>0}">
	                    	<s:if test="%{appForm.appHomeLoanId!=12}">
	                    		<div class="col col1"><s:property value="%{#repaymentIndex.index+1}"/></div>
	                        	<div class="col col2"><s:property value="%{#repayment.month}"/></div>
	                        	<div class="col col3"><em class="font-rupee">`</em> <s:property value="getText('{0,number,##,##,###}',{#repayment.begninngLoanAmount})"/></div>
								<div class="col col4"><em class="font-rupee">`</em>
		                            <s:if test="%{#repayment.EMI>0}">
		                   				<s:property value="getText('{0,number,##,##,###}',{#repayment.EMI})"/>
		                   			</s:if>
		                   			<s:else>
		                   			   <span>--</span>
		                   			</s:else>
                 				</div>
                 				<div class="col col5"><em class="font-rupee">`</em>
							    	<s:property value="getText('{0,number,##,##,###}',{#repayment.interest})"/>
							    </div>
		                        <div class="col col6"><em class="font-rupee">`</em>
		                            <s:if test="%{#repayment.principal>0}">
		                   				<s:property value="getText('{0,number,##,##,###}',{#repayment.principal})"/>
		                   			</s:if>
		                   			<s:else>
		                   			    <span>--</span>
		                   			</s:else>
	                   			</div>
	                        	<div class="col col7"><em class="font-rupee">`</em>
	                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.remainingLoanAmount})"/>
	                        	</div>
	                    	</s:if>
	                    	<s:else>
	                    		<div class="col bhl-col1"><s:property value="%{#repaymentIndex.index+1}"/></div>
	                        	<div class="col bhl-col2"><s:property value="%{#repayment.month}"/></div>
	                        	<div class="col bhl-col3"><em class="font-rupee">`</em>
	                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.begninngLoanAmount})"/>
	                        	</div>
		                        <div class="col bhl-col4"><em class="font-rupee">`</em>
		                            <s:if test="%{#repayment.principal>0}">
		                   				 <s:property value="getText('{0,number,##,##,###}',{#repayment.principal})"/>
		                   			</s:if>
		                   			<s:else>
		                   			    <span>--</span>
		                   			</s:else>
	                   			</div>
	                        	<div class="col bhl-col5"><em class="font-rupee">`</em>
	                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.remainingLoanAmount})"/>
	                        	</div>
	                    	</s:else>
                    	</s:if>
                    	<s:elseif test="%{personalLoanPage>0}">
                    		<s:if test="%{appForm.productId!=null && appForm.productId==13 && appForm.appLoanTenure<=12}">
	            					<div class="col col11"><s:property value="%{#repaymentIndex.index+1}"/></div>
		                       		<div class="col col22"><s:property value="%{#repayment.month}"/></div>
		                        	<div class="col col33"><em class="font-rupee">`</em>
		                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.begninngLoanAmount})"/>
		                        	</div>
								   
		                 			<div class="col col44"><em class="font-rupee">`</em>
		                 				<s:property value="getText('{0,number,##,##,###}',{#repayment.interest})"/>
		                 			</div>
			                       
		                        	<div class="col col55"><em class="font-rupee">`</em>
		                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.remainingLoanAmount})"/>
		                        	</div>
	            			</s:if>
	            			<s:else>
		            			<div class="col col1"><s:property value="%{#repaymentIndex.index+1}"/></div>
	                       		<div class="col col2"><s:property value="%{#repayment.month}"/></div>
	                        	<div class="col col3"><em class="font-rupee">`</em>
	                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.begninngLoanAmount})"/>
	                        	</div>
							    <s:if test="%{#repayment.EMI>0}">
							    	<div class="col col4"><em class="font-rupee">`</em>
	                   				 	<s:property value="getText('{0,number,##,##,###}',{#repayment.EMI})"/>
	                   				</div>
	                 			</s:if>
	                 			<s:else>
	                 			   <span>--</span>
	                 			</s:else>
	                 			<div class="col col5"><em class="font-rupee">`</em>
	                 				<s:property value="getText('{0,number,##,##,###}',{#repayment.interest})"/>
	                 			</div>
		                        <div class="col col6"><em class="font-rupee">`</em>
		                            <s:if test="%{#repayment.principal>0}">
		                   				<s:property value="getText('{0,number,##,##,###}',{#repayment.principal})"/>
		                   			</s:if>
		                   			<s:else>
		                   			    <span>--</span>
		                   			</s:else>
	                   			</div>
	                        	<div class="col col7"><em class="font-rupee">`</em>
	                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.remainingLoanAmount})"/>
	                        	</div>
	            			</s:else>
                    	</s:elseif>
                    	<s:else>
                    		<div class="col col1"><s:property value="%{#repaymentIndex.index+1}"/></div>
                       		<div class="col col2"><s:property value="%{#repayment.month}"/></div>
                        	<div class="col col3"><em class="font-rupee">`</em>
                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.begninngLoanAmount})"/>
                        	</div>
                 			<div class="col col4"><em class="font-rupee">`</em>
							    <s:if test="%{#repayment.EMI>0}">
                   				 	<s:property value="getText('{0,number,##,##,###}',{#repayment.EMI})"/>
	                 			</s:if>
    	             			<s:else>
	                 			   <span>--</span>
	                 			</s:else>
                 			</div>
                 			<div class="col col5"><em class="font-rupee">`</em>
                 				<s:property value="getText('{0,number,##,##,###}',{#repayment.interest})"/>
                 			</div>
	                        <div class="col col6"><em class="font-rupee">`</em>
	                            <s:if test="%{#repayment.principal>0}">
	                   				<s:property value="getText('{0,number,##,##,###}',{#repayment.principal})"/>
	                   			</s:if>
	                   			<s:else>
	                   			    <span>--</span>
	                   			</s:else>
                   			</div>
                        	<div class="col col7"><em class="font-rupee">`</em>
                        		<s:property value="getText('{0,number,##,##,###}',{#repayment.remainingLoanAmount})"/>
                        	</div>
                    	</s:else>
                    </div>
                   </s:iterator>
                 </div>
                <div class="text-center">
                	<a href="javascript:close();" class="btn close-btn btn-primary">Close</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
	jQuery("span.Rs").each(function(){
		var placeholder = jQuery(this).text();
		var strip_commas = placeholder.replace(/,/g, "");
		if(strip_commas>0){
			strip_commas = Number(strip_commas).toPrecision();
			strip_commas = strip_commas.replace(".0","");
			jQuery(this).text(moneyFormat(strip_commas));
		}
	});
	function moneyFormat(str) {
        str+='';
        if(!str||!str.length)return null;
        str=str.split('.');
        var dP='';
        if(str.length>1&&str[1].match(/^[0-9]+$/)){
            dP='.' + str[1];
        }
        str=str[0];
        str=str.replace(/^[0]{1,20}/,'');
        str=str.replace(/[,\s+]/g,'');
        var tmp="";
        var tmpcount=0;
        var hsep=true;
        var prev=0;

        for(prev=str.length-1;prev>=0;prev--) {
            tmp+=str.substr(prev,1);
            tmpcount++;
            if(hsep&&tmpcount==3&&prev) {
                tmp+=",";
                hsep=false;
                tmpcount=0;
            } else if(!hsep&&tmpcount==2&&prev) {
                tmp+=",";
                tmpcount=0;
            }
        }
        str="";                
        for(prev=tmp.length-1;prev>=0;prev--)
            str+=tmp.substr(prev,1);
        return str + dP;
    }
</script>

</body>
</html>
