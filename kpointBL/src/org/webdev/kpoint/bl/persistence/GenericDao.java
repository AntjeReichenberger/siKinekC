package org.webdev.kpoint.bl.persistence;

import java.io.Serializable;
import java.util.List;

import org.webdev.kpoint.bl.logging.ApplicationException;

public interface GenericDao <T, PK extends Serializable> {

    /**
     * Persist a new object to the database.
     * @param newInstance The object to create
     * @return The primary key of the object created
     * @throws ApplicationException 
     * @throws Exception 
     */
    PK create(T newInstance) throws Exception;
    
    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key
     * @param id The id of the object to retrieve
     * @return The object matching the supplied id; null if none exist
     */
    T read(PK id) throws Exception;;


    /**
     * Save changes made to a persistent object.
     * @param transientObject The object to save
     * @throws ApplicationException 
     * @throws Exception 
     */
    void update(T transientObject) throws Exception;

    
    /**
     * Remove an object from persistent storage in the database
     * @param persistentObject The object to remove
     */
    void delete(T persistentObject) throws Exception;;
    
    /**
     * Retrieves a list of all persisted objects stored in the database
     * @return A list of all persisted objects stored in the database
     */
    List<T> fetch() throws Exception;;
}


