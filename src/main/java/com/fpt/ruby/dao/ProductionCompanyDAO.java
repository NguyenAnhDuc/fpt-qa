package com.fpt.ruby.dao;

import com.fpt.ruby.model.ProductionCompany;

public interface ProductionCompanyDAO {
	
		  void persistProductionCompany(ProductionCompany productionCompany);
		  
		  ProductionCompany findProductionCompanyById(int id);
		  
		  void updateProductionCompany(ProductionCompany productionCompany);
		  
		  void deleteProductionCompany(ProductionCompany productionCompany);
		  
}
