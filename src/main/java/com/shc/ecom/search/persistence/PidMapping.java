package com.shc.ecom.search.persistence;


import java.util.ArrayList;
import java.util.List;

import com.shc.ecom.search.common.pidmapping.PidInfo;
import com.shc.ecom.search.pidmapping.RetailExtract;
import com.shc.ecom.search.util.JsonUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.pidmapping.PidInfo;
import com.shc.ecom.search.pidmapping.RetailExtract;
/** 
 * pidMapping.txt file read and list of old Pid, New Pid, Expire Date is stored for further usage. The file should be comma
 * seperated csv format. The structure of file is : <Old Pid>,<New Pid>,<Expire date example: 15-12-2016>
 * @author vsingh8 
 * */

@Component
public class PidMapping extends FileDataAccessor {

    private static final long serialVersionUID = 1L;
    private static List<PidInfo> pidinfolist = new ArrayList<>();
    @Autowired 
    private RetailExtract retailextract;
    @Override
    public void save (String value) {
        if (StringUtils.equalsIgnoreCase(value, "EOF")) {
            if (!CollectionUtils.isEmpty(pidinfolist)) {
                retailextract.retailextract(pidinfolist);
            }
            return;
        }
        addPidInfoIntoList(value);
    }
    /** 
     * Read comma separated single line from pidMapping file and add pids to main list.
     * @param singleLine 
     * */
    private static void addPidInfoIntoList(String singleLine) {
        // Skip lines which are empty or null
        if (StringUtils.isEmpty(singleLine)) {
            return;
        }
        // Skip commented lines starting with '#'
        if (StringUtils.startsWith(singleLine, "#")) {
            return;
        }
        String lineWithoutSpace = singleLine.replaceAll("\\s+", "");
        String[] parts = lineWithoutSpace.split(",");
        // Skip if Pidmapping is invalid
        if (isInvalidPidMapping(parts)) {
            return;
        }
        // Add values to pidInfoList
        PidInfo pidInfo = new PidInfo();
        pidInfo.setOldPid(parts[0]);
        pidInfo.setNewPid(parts[1]);
        pidInfo.setExpDate(parts[2]);
        pidinfolist.add(pidInfo);
    }
    /** 
     * Check if the pid being added is valid pid.
     * @param parts
     * @return true if line is invalid
     * */
    private static boolean isInvalidPidMapping(String[] parts) {
        String dateRegEx = "([0-9]{2})-([0-9]{2})-([0-9]{4})";
        // Skip lines without 3 comma separated values
        if (parts.length != 3) {
            return true;
        }
        // Skip line if old pid is invalid.
        if (StringUtils.isEmpty(parts[0])) {
            return true;
        }
        // Skip line if new pid is invalid.
        if (StringUtils.isEmpty(parts[1])) {
            return true;
        }
        // Skip line if expire date is invalid.
        if (StringUtils.isEmpty(parts[2]) || !parts[2].matches(dateRegEx)) {
            return true;
        }
        return false;
    }
}