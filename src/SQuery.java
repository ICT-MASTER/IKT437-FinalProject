import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Execute a query on a supplied model.
 */


public class SQuery {


    String result;
    ResultSet resultset;


    public SQuery(String query, OntModel model) {

        String preQuery =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX chk: <http://chriskvik.uia.io/ontology#> ";

        Query q = QueryFactory.create(preQuery + query);
        QueryExecution qe= QueryExecutionFactory.create(q, model);
        resultset = qe.execSelect();

        setResult(ResultSetFormatter.asText(resultset));
    }

    /**
     *
     * @return a single string list containing each uri from the query.
     */
    public List<String> getResultSingleLine() {
        List<String> uriList = new ArrayList<String>();

        while(resultset.hasNext()) {
            QuerySolution row = resultset.nextSolution();
            uriList.add(row.get("x").toString());
        }

        return uriList;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
