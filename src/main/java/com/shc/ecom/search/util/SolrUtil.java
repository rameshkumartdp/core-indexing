package com.shc.ecom.search.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shc.ecom.search.annotation.Child;

/**
 * All Solr utilities go here
 * Created by hdargah on 6/9/2016.
 */

public class SolrUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrUtil.class);

    private SolrUtil() {
    }

    /**
     * Takes any object and converts it to a SolrInputDocument
     * Also, handles parent-child relationship in the Object, use @Child annotation for child docs
     *
     * @param obj Any POJO to be converted to SolrInputDocument
     * @return
     */
    public static SolrInputDocument convertToSolrInputDocument(Object obj) {

        SolrInputDocument solrInputDocument = new SolrInputDocument();

        //Iterate through all the fields
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true); //Mandatory
            try {
                //Check if it is a child
                Child childAnnotation = f.getAnnotation(Child.class);
                if (childAnnotation != null) {
                    List<Object> childDocs = (List<Object>) f.get(obj);
                    for (Object child : childDocs) {
                        //The recursive call takes care of multiple nested docs
                        SolrInputDocument childDocument = convertToSolrInputDocument(child);
                        solrInputDocument.addChildDocument(childDocument);
                    }
                    continue;
                }
                //If not a child, add the field
                solrInputDocument.addField(getFieldName(f), f.get(obj));
            } catch (IllegalAccessException iaE) {
                LOGGER.error("Error in converting SearchDoc to SolrInputDoc", iaE);
            }
        }
        return solrInputDocument;
    }

    /**
     * A wrapper for the  convertToSolrInputDocument(Object obj) method.
     * Handles a Collection of POJO's
     *
     * @param docs
     * @return
     */
    public static List<SolrInputDocument> convertToSolrInputDocuments(Collection<?> docs) {

        List<SolrInputDocument> solrInputDocuments = new ArrayList<>();
        for (Object doc : docs) {
            SolrInputDocument solrInputDocument = convertToSolrInputDocument(doc);
            solrInputDocuments.add(solrInputDocument);
        }

        return solrInputDocuments;
    }

    /**
     * Get the name from the @Field annotation, if not present, return the field name from POJO.
     * <p>
     * For fields that require special names in the SolrInput Document, use the @Field("FieldName") annotation
     *
     * @param f Field object from reflection
     * @return
     */
    private static String getFieldName(Field f) {

        //Get the Field Annotation
        org.apache.solr.client.solrj.beans.Field field = f.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);

        //Make sure the value is not the default value
        if (field != null && !"#default".equals(field.value())) {
            return field.value();
        } else {
            //If the member does not have any annotation, retain the field name
            return f.getName();
        }

    }
}
