package com.fpt.ruby.service;

import com.fpt.ruby.model.ProductionCompany;

public interface ProductionCompanyService {

	void persistProductionCompany(ProductionCompany productionCompany);

	ProductionCompany findProductionCompanyById(int id);

	void updateProductionCompany(ProductionCompany productionCompany);

	void deleteProductionCompany(ProductionCompany productionCompany);
}
