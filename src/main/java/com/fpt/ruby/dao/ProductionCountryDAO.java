package com.fpt.ruby.dao;

import com.fpt.ruby.model.ProductionCountry;

public interface ProductionCountryDAO {
	
		  void persistProductionCountry(ProductionCountry productionCountry);
		  
		  ProductionCountry findProductionCountryByIso_3166_1(String iso_3166_1);
		  
		  void updateProductionCountry(ProductionCountry productionCountry);
		  
		  void deleteProductionCountry(ProductionCountry productionCountry);
		  
}
