package docbuilder;

/**
 * Created by rames on 05-05-2019.
 */
public class AdditionalInfo {
    private AboutInfo aboutInfo;
    private HighlightsInfo highlightsInfo;
    private String miscellaneous;

    public AboutInfo getAboutInfo() {
        return aboutInfo;
    }

    public void setAboutInfo(AboutInfo aboutInfo) {
        this.aboutInfo = aboutInfo;
    }

    public HighlightsInfo getHighlightsInfo() {
        return highlightsInfo;
    }

    public void setHighlightsInfo(HighlightsInfo highlightsInfo) {
        this.highlightsInfo = highlightsInfo;
    }

    public String getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(String miscellaneous) {
        this.miscellaneous = miscellaneous;
    }
}
