package com.fpt.ruby.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpt.ruby.dao.ProductionCountryDAO;
import com.fpt.ruby.model.ProductionCountry;

@Service("productionCountryService")
public class ProductionCountryServiceImpl implements ProductionCountryService{

	@Autowired
	ProductionCountryDAO productionCountryDAO;
	
	@Override
	@Transactional
	public void persistProductionCountry(ProductionCountry productionCountry) {
		productionCountryDAO.persistProductionCountry(productionCountry);
		
	}

	@Override
	@Transactional
	public void updateProductionCountry(ProductionCountry productionCountry) {
		productionCountryDAO.updateProductionCountry(productionCountry);
		
	}
	@Override
	@Transactional
	public ProductionCountry findProductionCountryByIso_3166_1(String iso_3166_1) {
		return productionCountryDAO.findProductionCountryByIso_3166_1(iso_3166_1);
	}

	@Override
	@Transactional
	public void deleteProductionCountry(ProductionCountry productionCountry) {
		productionCountryDAO.deleteProductionCountry(productionCountry);
		
	}

}
