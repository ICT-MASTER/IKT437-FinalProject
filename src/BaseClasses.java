import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

/**
 * A Class responsible for creating the base-classes used in the ontology...
 * topics, courses ...
 *
 * We can then specify ranges and domains for the vocabulary..
 */
public class BaseClasses {

    OntModel model;
    public BaseClasses (OntModel model) {
        this.model = model;

        /*
         *  CLASS CREATION
         */

        OntClass topics
                = model.createClass
                        (OProps.NS + "topics");

        OntClass course
                = model.createClass
                        (OProps.NS + "course");

        OntClass learningType
                = model.createClass
                        (OProps.NS + "learningtype");


        /*
         *  CONSTRAINTS AND OBJECT PROPERTIES / RELATIONS...
         */

        VOC.hasCourseSubclass.setRange(course);
        VOC.isCourseSubclass.setRange(course);
        VOC.isTopicIn.setRange(course);
        VOC.hasTopic.setRange(topics);

        VOC.hasSubtopic.setRange(topics);
        VOC.isSubtopic.setRange(topics);
        VOC.hasRequirement.setRange(topics);
        VOC.isRequirement.setRange(topics);

    }

    /**
     @return an updates model including custom voabulary.
     **/

    public void createDummyData () {




        // Learning Types
        Individual presentation = model.createIndividual
                (OProps.NS + "presentation", model.getResource(OProps.LEARN_TYPE));
        Individual written_exam = model.createIndividual
                (OProps.NS + "written_exam", model.getResource(OProps.LEARN_TYPE));


        Topic exT = new Topic();
        exT.setName("Base-Subject");
        exT.setPracticalPart(model.getIndividual(OProps.written_exam));

        Triple newTopic = new Triple(model);
        newTopic.addTopic(exT);
        model = newTopic.getModel();

        Topic exT2 = new Topic();
        exT2.setName("Advanced-Subject");
        exT2.setPracticalPart(model.getIndividual(OProps.written_exam));
        exT2.setRequirements(model.getIndividual(OProps.NS+"Base-Subject"));
        exT2.setSubtopic(model.getIndividual(OProps.NS+"Base-Subject"));


        Triple newTopic2 = new Triple(model);
        newTopic2.addTopic(exT2);
        model = newTopic2.getModel();


        Course exC = new Course();
        exC.setCourseName("Example-Course");
        exC.setPracticalPart(model.getIndividual(OProps.presentation));
        exC.setTeorethicalPart(model.getIndividual(OProps.written_exam));
        exC.setTopic(model.getIndividual(OProps.NS+"Base-Subject"));
        exC.setTopic(model.getIndividual(OProps.NS+"Advanced-Subject"));


        Triple newCourse = new Triple(model);
        newCourse.addCourse(exC);
        model = newCourse.getModel();
    }

    public OntModel updateModel () {
        return this.model;
    }
}
