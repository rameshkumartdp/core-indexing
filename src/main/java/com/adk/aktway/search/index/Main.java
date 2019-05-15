package com.adk.aktway.search.index;

import com.adk.aktway.search.config.GlobalConstants;
import com.adk.aktway.search.config.PropertiesLoader;
import org.springframework.stereotype.Component;

@Component
public class Main {

   public static void main(String[] args) {
       PropertiesLoader.load(GlobalConstants.BASE_CONFIG_FILE_NAME);
       new IndexDoc().sendToSolr();
   }

}
