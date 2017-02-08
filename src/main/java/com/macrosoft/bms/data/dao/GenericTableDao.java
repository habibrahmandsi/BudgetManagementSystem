package com.macrosoft.bms.data.dao;

import java.io.Serializable;

/** A generic table DAO for CRUD methods
 * 
 * @author habib
 * 
 * @param E - The entity/table/data object
 * @param P - The primary key object of the entity/table/data
 */
public interface GenericTableDao<E, P extends Serializable> {
	public void saveOrUpdate(E record);
	public void save(E record);
	public E get(P pk);
	public void update(E record);
	public void  delete(E record);
}