import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;

import java.util.List;

/**
 * Created by chris on 11.11.2015.
 */
public class Triple {


    Individual resource;
    OntModel model;


    public Triple(OntModel model) {
        this.model = model;
    }


    public void addCourse(Course c) {

        // Create course
        Individual newCourse = model.createIndividual
                                (c.getCourseName(),model.getOntClass(OProps.COURSE));

        // Link course with topics, and topics with course.
        for(Individual topic : c.getTopicsOfCourse()) {
            if (!(topic == null)) {
                newCourse.addProperty(VOC.hasTopic, topic);
                topic.addProperty(VOC.isTopicIn, newCourse);
            }
        }


        // Set theoretical part (hasTheoreticalPart) / practical part (hasPracticalPart)
        try {
            newCourse.addProperty(VOC.hasTheoreticalPart, c.getTeorethicalPart());
        }
        catch (NullPointerException e){
            System.out.println("No theoretical part in this course.");
        }
        // Set link (isPracticalPartIn) / (isTheoreticalPartIn)
        try {
            newCourse.addProperty(VOC.hasPracticalPart, c.getPracticalPart());
        }
        catch (NullPointerException e){
            System.out.println("No practical part in this course.");
        }
    }

    public void addTopic(Topic t) {

        // Create course
        Individual newResource = model.createIndividual
                (t.getName(),model.getOntClass(OProps.TOPIC));

        // Link topic with req, and topics with course.
        for(Individual topic : t.getRequirements()) {
            if (!(topic == null)) {
                newResource.addProperty(VOC.hasRequirement, topic);
                topic.addProperty(VOC.isRequirement, newResource);
            }
        }

        for(Individual topic : t.getSubtopics()) {
            if (!(topic == null)) {
                newResource.addProperty(VOC.hasSubtopic, topic);
                topic.addProperty(VOC.isSubtopic, newResource);
            }
        }


        // Set theoretical part (hasTheoreticalPart) / practical part (hasPracticalPart)
        try {
            newResource.addProperty(VOC.hasTheoreticalPart,t.getTeoreticalPart());
            model.getIndividual(OProps.written_exam)
                                .addProperty(VOC.isRequirement, newResource);

        }
        catch (NullPointerException e){
            System.out.println("No theoretical part in this course.");
        }
        // Set link (isPracticalPartIn) / (isTheoreticalPartIn)
        try {
            newResource.addProperty(VOC.hasTheoreticalPart,t.getPracticalPart());
            model.getIndividual(OProps.presentation)
                    .addProperty(VOC.isRequirement, newResource);
        }
        catch (NullPointerException e){
            System.out.println("No practical part in this course.");
        }
    }

    public OntModel getModel () {
        return model;
    }
}
