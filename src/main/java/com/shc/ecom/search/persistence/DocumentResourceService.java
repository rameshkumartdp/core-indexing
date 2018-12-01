/**
 *
 */
package com.shc.ecom.search.persistence;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.Serializable;

/**
 * @author rgopala
 */
public class DocumentResourceService implements ResourceLoaderAware,
        Serializable {

    private static final long serialVersionUID = 3738453791502989219L;
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getResource(String location) {
        return resourceLoader.getResource(location);
    }

}
