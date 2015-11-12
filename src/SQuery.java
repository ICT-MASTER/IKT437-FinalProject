import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;

/**
 * Execute a query on a supplied model.
 */


public class SQuery {


    String result;

    public SQuery(String query, OntModel model) {

        String preQuery =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX chk: <http://chriskvik.uia.io/ontology#> ";

        Query q = QueryFactory.create(preQuery + query);
        QueryExecution qe= QueryExecutionFactory.create(q, model);
        ResultSet resultset = qe.execSelect();

        setResult(ResultSetFormatter.asText(resultset));
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
