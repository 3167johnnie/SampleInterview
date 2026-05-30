package com.mintstreet.loan.autoloan.dao;

import java.sql.SQLException;
import java.util.List;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.autoloan.entity.MasterCarCompany;

public class CarCompanyDao extends GenericDao<Integer,MasterCarCompany> {
	private static final long serialVersionUID = -3211938888147551360L;
	
	public List<MasterCarCompany> getAllCarcompany() throws SQLException {
		return findByNamedQuery("MasterCarCompany.getAllCarcompany");
	}
}
