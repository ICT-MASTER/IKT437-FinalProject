import org.apache.jena.ontology.OntModel;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by chris on 11.11.2015.
 */
public class ExportModel {

    boolean status;
    String filename = "mymodel";

    public ExportModel(OntModel model) {

        setStatus(false);

        FileWriter out = null;
        try {
            // XML format - long and verbose
            out = new FileWriter( filename+".xml" );
            model.write( out, "RDF/XML-ABBREV" );

            // OR Turtle format - compact and more readable
            // use this variant if you're not sure which to use!
            out = new FileWriter( filename+".ttl" );
            model.write( out, "Turtle" );
            System.out.println("Wrote ontology to file...");
        } catch (IOException w) {
            setStatus(false);
            w.printStackTrace();
        } finally {
            if (out != null) { //hei
                try {out.close();} catch (IOException ignore) {setStatus(false);}

            }
        }

        setStatus(true);
    }


    public String getFilename() {
        filename += ".ttl / .xml";
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
