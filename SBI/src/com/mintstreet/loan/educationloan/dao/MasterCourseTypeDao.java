package com.mintstreet.loan.educationloan.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.educationloan.entity.MasterCourseType;

public class MasterCourseTypeDao  extends GenericDao<Integer, MasterCourseType>{
	private static final long serialVersionUID = -1024830983112261589L;

	public List<BigDecimal> getCourseTypeList(Integer isAbroad) throws NoResultException, SQLException{
		String query = "SELECT COURSE_TYPE_ID, COURSE_TYPE_NAME, COURSE_TYPE_ORDER_BY FROM RUPEEPOWER_OCAS_T_05040 "
				+ " WHERE  COURSE_TYPE_ACTIVE ='Y' AND COURSE_TYPE_DELETED='N' AND COURSE_TYPE_IS_CHECKER=0 ";
				if(isAbroad.intValue()==2){
					query += "AND COURSE_TYPE_ABROAD = 2 ";
				}		
				query += " ORDER BY COURSE_TYPE_ORDER_BY ASC ";
		@SuppressWarnings("unchecked")
		List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, null);
		return Objectlist;
	}
	public Map<Integer, String> getCourseType(Integer isAbroad) throws NoResultException, SQLException{
		List<BigDecimal> Objectlist = (List<BigDecimal>) getCourseTypeList(isAbroad);
		if(Objectlist!=null && !Objectlist.isEmpty()){
			Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
			Object[] courseTypeList = Objectlist.toArray();
			if(courseTypeList!=null && courseTypeList.length>0){
				for (int index = 0; index < courseTypeList.length;index++) {
					Object[] courseTypeObj = (Object[])courseTypeList[index];
					maps.put(Integer.parseInt(courseTypeObj[0].toString()), courseTypeObj[1].toString());
				}
			}
			return maps;
		}
		return null;
	}
}
