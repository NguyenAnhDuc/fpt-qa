package com.fpt.ruby.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fpt.ruby.model.ProductionCountry;

@Repository("ProductionCountryDAO")
public class ProductionCountryDAOImpl implements ProductionCountryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void persistProductionCountry(ProductionCountry productionCountry) {
		sessionFactory.getCurrentSession().persist(productionCountry);
	}

	@Override
	public ProductionCountry findProductionCountryByIso_3166_1(String iso_3166_1) {
		return (ProductionCountry) sessionFactory.getCurrentSession().get(ProductionCountry.class, iso_3166_1);
	}

	@Override
	public void updateProductionCountry(ProductionCountry productionCountry) {
		sessionFactory.getCurrentSession().update(productionCountry);

	}
	@Override
	public void deleteProductionCountry(ProductionCountry productionCountry) {
		sessionFactory.getCurrentSession().delete(productionCountry);

	}

}