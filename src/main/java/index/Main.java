package index;

import org.springframework.stereotype.Component;

@Component
public class Main {

   public static void main(String[] args) {
        new IndexDoc().sendToSolr();
    }

}
