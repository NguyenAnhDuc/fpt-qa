package com.fpt.ruby.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fpt.ruby.model.ProductionCompany;

@Repository("ProductionCompanyDAO")
public class ProductionCompanyDAOImpl implements ProductionCompanyDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void persistProductionCompany(ProductionCompany productionCompany) {
		sessionFactory.getCurrentSession().persist(productionCompany);
	}

	
	@Override
	public void updateProductionCompany(ProductionCompany productionCompany) {
		sessionFactory.getCurrentSession().update(productionCompany);

	}
	@Override
	public void deleteProductionCompany(ProductionCompany productionCompany) {
		sessionFactory.getCurrentSession().delete(productionCompany);

	}

	@Override
	public ProductionCompany findProductionCompanyById(int id) {
		return (ProductionCompany) sessionFactory.getCurrentSession().get(ProductionCompany.class, id);
	}

}