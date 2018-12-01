package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.search.common.messages.ContextMessage;

/**
 * Checks the GB Content's response which is mapped under _blob -> content.  
 * This also validates if the domain itself is first valid by validating the primary response such as blob, as well as meta data such as id, ft and search.
 *  
 * @author rgopala
 *
 */
public class ContentExistenceRule implements IRule<DomainObject> {

	private static final long serialVersionUID = -6157473992184253844L;

	@Override
	public boolean evaluate(DomainObject domainObject, ContextMessage context) {

		if(domainObject==null || domainObject.get_blob()==null || domainObject.get_blob().getContent()==null || domainObject.get_id()==null || domainObject.get_ft()==null) {
			return false;
		}
		return true;
	}
}
