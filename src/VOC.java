import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;

/**
 * Generate the custom vocabulary used in this ontology.
 */
public class VOC {

    public static ObjectProperty isSubtopic;
    public static ObjectProperty hasSubtopic;

    public static ObjectProperty hasCourseSubclass;
    public static ObjectProperty isCourseSubclass;

    public static ObjectProperty hasRequirement;
    public static ObjectProperty isRequirement;

    public static ObjectProperty hasTheoreticalPart;
    public static ObjectProperty isTheoreticalPartOf;

    public static ObjectProperty hasPracticalPart;
    public static ObjectProperty isPracticalPartOf;

    public static ObjectProperty hasTopic;
    public static ObjectProperty isTopicIn;

    private OntModel model;

    public VOC (OntModel model) {
        this.model = model;
        populateVocabulary();
    }

    /* Initialize the model with the correct vocabulary */
    private void populateVocabulary() {


        // Add ObjectProperties to the model...

        isSubtopic = model.createObjectProperty
                                (OProps.NS + "isSubtopic");
        hasSubtopic = model.createObjectProperty
                                (OProps.NS + "hasSubtopic");
        hasCourseSubclass = model.createObjectProperty
                                (OProps.NS + "hasCourseSubclass");
        isCourseSubclass = model.createObjectProperty
                                (OProps.NS + "isCourseSubclass");
        hasRequirement = model.createObjectProperty
                                (OProps.NS + "hasRequirement");
        isRequirement = model.createObjectProperty
                                (OProps.NS + "isRequirement");
        hasTheoreticalPart = model.createObjectProperty
                                (OProps.NS + "hasTheoreticalPart");
        isTheoreticalPartOf = model.createObjectProperty
                                (OProps.NS + "isTheoreticalPartOf");
        hasPracticalPart = model.createObjectProperty
                                (OProps.NS + "hasPracticalPart");
        isPracticalPartOf = model.createObjectProperty
                                (OProps.NS + "isPracticalPartOf");
        hasTopic = model.createObjectProperty
                                (OProps.NS + "hasTopic");
        isTopicIn = model.createObjectProperty
                                (OProps.NS + "isTopicIn");

        // Set the inverse properties of the objects...
        isSubtopic
                .setInverseOf(hasSubtopic);
        hasCourseSubclass
                .setInverseOf(isCourseSubclass);
        hasRequirement
                .setInverseOf(isRequirement);
        hasTheoreticalPart
                .setInverseOf(isTheoreticalPartOf);
        hasPracticalPart
                .setInverseOf(isPracticalPartOf);
        hasTopic
                .setInverseOf(isTopicIn);

    }


    /**
     @return an updates model including custom voabulary.
     **/

    public OntModel updateModel () {
        return this.model;
    }



}
