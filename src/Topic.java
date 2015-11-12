import org.apache.jena.ontology.Individual;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 11.11.2015.
 */
public class Topic {


    String name;
    List<Individual> subtopics;
    List<Individual> requirements;
    Individual practicalPart;
    Individual teoreticalPart;


    public Topic () {

        subtopics = new ArrayList<Individual>();
        requirements = new ArrayList<Individual>();

    }
    public String getName() {
        return name;
    }

    /**
     * @param name without NS
     */
    public void setName(String name) {
        this.name = OProps.NS+name;
    }

    public List<Individual> getSubtopics() {
        return subtopics;
    }

    public void setSubtopic(Individual subtopics) {
        this.subtopics.add(subtopics);
    }

    public Individual getPracticalPart() {
        return practicalPart;
    }

    public void setPracticalPart(Individual practicalPart) {
        this.practicalPart = practicalPart;
    }

    public List<Individual> getRequirements() {
        return requirements;
    }

    public void setRequirements(Individual requirements) {
        this.requirements.add(requirements);
    }

    public Individual getTeoreticalPart() {
        return teoreticalPart;
    }

    public void setTeoreticalPart(Individual teoreticalPart) {
        this.teoreticalPart = teoreticalPart;
    }
}
