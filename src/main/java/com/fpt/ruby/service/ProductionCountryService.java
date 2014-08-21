package com.fpt.ruby.service;

import com.fpt.ruby.model.ProductionCountry;

public interface ProductionCountryService {

	void persistProductionCountry(ProductionCountry productionCountry);

	ProductionCountry findProductionCountryByIso_3166_1(String iso_3166_1);

	void updateProductionCountry(ProductionCountry productionCountry);

	void deleteProductionCountry(ProductionCountry productionCountry);
}
