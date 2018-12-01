package com.shc.ecom.search.persistence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class QuickAdd extends FileDataAccessor {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Set<String> ssins = new HashSet<>();

    public Set<String> getSsins() {
        return ssins;
    }

    public void setSsins(Set<String> ssins) {
        this.ssins = ssins;
    }

    @Override
    public void save(String value) {
        if (!value.startsWith("#") && !StringUtils.equalsIgnoreCase(value, "EOF")) {
            ssins.add(value);
        }
    }

}
