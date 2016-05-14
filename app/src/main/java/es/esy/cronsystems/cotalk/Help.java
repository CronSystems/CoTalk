package es.esy.cronsystems.cotalk;

/**
 * Created by nikky on 07.05.16.
 */
public class Help {

    private String help_header = "Test header" ;
    private String help_text = "Test answer";

    public String getHelp_header() {
            return help_header;
    }

    public void setHelp_header(String help_header) {
        this.help_header = help_header;
    }

    public String getHelp_text() {
        return help_text;
    }

    public void setHelp_text(String help_text) {
        this.help_text = help_text;
    }
}
