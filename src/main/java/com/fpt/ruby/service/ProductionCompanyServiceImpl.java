package com.fpt.ruby.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpt.ruby.dao.ProductionCompanyDAO;
import com.fpt.ruby.model.ProductionCompany;

@Service("productionCompanyService")
public class ProductionCompanyServiceImpl implements ProductionCompanyService{

	@Autowired
	ProductionCompanyDAO productionCompanyDAO;
	
	@Override
	@Transactional
	public void persistProductionCompany(ProductionCompany productionCompany) {
		productionCompanyDAO.persistProductionCompany(productionCompany);
		
	}

	@Override
	@Transactional
	public void updateProductionCompany(ProductionCompany productionCompany) {
		productionCompanyDAO.updateProductionCompany(productionCompany);
		
	}
	@Override
	@Transactional
	public ProductionCompany findProductionCompanyById(int id) {
		return productionCompanyDAO.findProductionCompanyById(id);
	}

	@Override
	@Transactional
	public void deleteProductionCompany(ProductionCompany productionCompany) {
		productionCompanyDAO.deleteProductionCompany(productionCompany);
		
	}

}
