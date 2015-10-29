import oracle.jrockit.jfr.JFR;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.impl.ObjectPropertyImpl;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 20.10.2015.
 */
public class UserInterface extends JFrame {
    private JPanel rootPanel;
    private JTextField newTopic;
    private JComboBox dependsDrop;
    private JComboBox subtopicOf;
    private JButton createTopicButton;
    private JButton addSub;
    private JTextArea console;
    private JButton addDepend;
    private JButton saveOntButton;

    private JComboBox lType;
    private JButton addLectType;
    private JTextField courName;
    private JComboBox hasTopicD;
    private JTabbedPane tabs;
    private JButton addCourseTopic;
    private JComboBox topicBoxView;
    private JComboBox courseBoxView;
    private JButton removeTopic;
    private JButton removeCourse;
    private JTextArea sparqlQuery;
    private JComboBox isTheoreticalPartOf;

    // Properties
    ObjectProperty isSubstopicOf;
    ObjectProperty hasSubtopic;
    ObjectProperty hasRequirement;
    ObjectProperty isRequirement;
    ObjectProperty hasTopic;
    ObjectProperty isTopicIn;

    ObjectProperty isCourseSubclass;
    ObjectProperty hasCourseSubclas;
    // Our resource...
    Individual newResource;
    List<String> depends = new ArrayList<>();
    List<String> subtopics = new ArrayList<>();
    List<String> lectureType = new ArrayList<>();
    List<String> hasTopicList = new ArrayList<>();


    OntModel model;

    public UserInterface() {
        setContentPane(rootPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // NS
        String uri = "http://chriskvik.uia.io/ontology#";

        // Create ontology model
        model = ModelFactory.createOntologyModel();

        /******************************************************************************
         *
         *   CLASSES
         *
         ******************************************************************************/

        // Topic related modelling
        OntClass topics
                = model.createClass(uri + "topics");

        // Course class
        OntClass course
                = model.createClass(uri + "course");

        // Course class
        OntClass learningType
                = model.createClass(uri + "learningtype");


        /******************************************************************************
         *
         *   INDIVIDUALS
         *
         ******************************************************************************/

        Individual advancedSubject
                = model.createIndividual(uri + "Android", topics);

        Individual baseSubject
                = model.createIndividual(uri + "Java", topics);

        Individual exampleCourse
                = model.createIndividual(uri + "IKT001", course);

        Individual presentation
                = model.createIndividual(uri + "presentation", learningType);

        Individual lecture
                = model.createIndividual(uri + "lecture", learningType);


        /******************************************************************************
         *
         *   TOPICS
         *
         ******************************************************************************/

        // Properties
        ObjectProperty isSubstopicOf = model.createObjectProperty(uri + "isSubtopicOf");
        hasSubtopic = model.createObjectProperty(uri + "hasSubtopic");
        hasCourseSubclas = model.createObjectProperty(uri + "hasCourseSubclass");
        isCourseSubclass = model.createObjectProperty(uri + "isCourseSubclass");
        ObjectProperty hasRequirement = model.createObjectProperty(uri + "hasRequirement");
        ObjectProperty isRequirement = model.createObjectProperty(uri + "isRequirement");

        ObjectProperty isTheoreticalPartOf = model.createObjectProperty(uri + "isTheoreticalPartOf");
        ObjectProperty hasTheoreticalPart = model.createObjectProperty(uri + "hasTheoreticalPart");
        ObjectProperty isPracticalPartOf = model.createObjectProperty(uri + "isPracticalPartOf");
        ObjectProperty hasPracticalPart = model.createObjectProperty(uri + "hasPracticalPart");

        hasTopic = model.createObjectProperty(uri + "hasTopic");
        isTopicIn = model.createObjectProperty(uri + "isTopicIn");


        /**
         *  PROPERTIES -> PROPERTIES
         */

        // Inverse of properties
        isSubstopicOf.addInverseOf(hasSubtopic);
        isCourseSubclass.addInverseOf(hasCourseSubclas);
        hasRequirement.addInverseOf(isRequirement);
        isTheoreticalPartOf.addInverseOf(hasTheoreticalPart);
        isPracticalPartOf.addInverseOf(hasPracticalPart);
        hasTopic.addInverseOf(isTopicIn);


        // The domain of the properties. For example a topic HAS a subtopic AND a course has a PRACTICAL PART.
        hasSubtopic.addDomain(topics);
        hasCourseSubclas.addDomain(course);
        hasRequirement.addDomain(topics);
        hasTopic.addDomain(course);
        hasTheoreticalPart.addDomain(topics);
        hasPracticalPart.addDomain(topics);


        // Add all topics as subopics of the main topic.
        model.add(topics, hasSubtopic, advancedSubject);
        model.add(topics, hasSubtopic, baseSubject);


        // Relations between topics
        advancedSubject.setPropertyValue(hasRequirement, baseSubject);
        advancedSubject.setPropertyValue(hasSubtopic, baseSubject);
        baseSubject.setPropertyValue(isSubstopicOf, advancedSubject);
        baseSubject.setPropertyValue(isRequirement, advancedSubject);

        // Relations between courses


        /******************************************************************************
         *
         *   COURSES
         *
         * ******************************************************************************/

        // Example course with a topic with a defined lecture type.
        exampleCourse.setPropertyValue(hasTopic, advancedSubject);
        advancedSubject.setPropertyValue(hasTheoreticalPart, presentation);
        model.add(course, hasCourseSubclas, exampleCourse);


        populateDropDown(model, topics, course);




        createTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(tabs.getSelectedIndex());

                if(tabs.getSelectedIndex() == 1) {
                    // Create resouce..
                    String resName = newTopic.getText();
                    newResource = model.createIndividual(uri + resName, topics);
                    model.add(topics, hasSubtopic, newResource);

                    if (!depends.isEmpty()) {
                        for (String item : depends) {

                            newResource.setPropertyValue(hasRequirement, model.getIndividual(item));
                            model.getIndividual(item).setPropertyValue(isRequirement, newResource);


                        }
                    }

                    if (!subtopics.isEmpty()) {
                        for (String item : subtopics) {
                            newResource.setPropertyValue(isSubstopicOf, model.getIndividual(item));
                            model.getIndividual(item).setPropertyValue(isSubstopicOf, newResource);
                        }
                    }

                    if (!lectureType.isEmpty()) {
                        for (String item : lectureType) {

                            if (item.equals("presentation")) {
                                newResource.setPropertyValue(hasPracticalPart, model.getIndividual(uri + item));
                                model.getIndividual(uri + item).setPropertyValue(hasPracticalPart, newResource);

                            } else if (item.equals("lecture")) {
                                newResource.setPropertyValue(hasTheoreticalPart, model.getIndividual(uri + item));
                                model.getIndividual(uri + item).setPropertyValue(hasTheoreticalPart, newResource);
                            }
                        }
                    }

                    console.append("Created topic " + resName + "\n");

                }

                else if (tabs.getSelectedIndex() == 0) {

                    String name = courName.getText();

                    Individual newCourse
                            = model.createIndividual(uri + name, course);

                    model.add(course, hasCourseSubclas, newCourse);
                    for(String item : hasTopicList) {
                        newCourse.addProperty(hasTopic,model.getIndividual(item));
                    }

                    console.append("Created course " + name + "\n");

                }

                else if (tabs.getSelectedIndex() == 3) {
                    runSparQL();
                }

                populateDropDown(model, topics,course);

                depends.clear();
                subtopics.clear();


                NodeIterator allTopics = model.listObjectsOfProperty(topics, hasSubtopic);

                while (allTopics.hasNext()) {
                    System.out.println(allTopics.nextNode());
                }

            }




        });


        saveOntButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileWriter out = null;
                try {
                    // XML format - long and verbose
                    out = new FileWriter( "mymodel.xml" );
                    model.write( out, "RDF/XML-ABBREV" );

                    // OR Turtle format - compact and more readable
                    // use this variant if you're not sure which to use!
                    out = new FileWriter( "mymodel.ttl" );
                    model.write( out, "Turtle" );
                    System.out.println("Wrote ontology to file...");
                } catch (IOException w) {
                    w.printStackTrace();
                } finally {
                    if (out != null) {
                        try {out.close();} catch (IOException ignore) {}
                        console.append("WROTE ONTOLOGY TO FILE\n");

                    }
                }
            }
        });

        // Listeners
        addDepend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = dependsDrop.getSelectedItem().toString();
                if(!selected.equals("None")) {
                    console.append(" Added dependency : " + selected + "\n");
                    depends.add(selected);
                }
            }
        });

        addLectType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!lType.getSelectedItem().toString().equals("None")) {
                    lectureType.add(lType.getSelectedItem().toString());
                    console.append(" Added lecture type : " + lType.getSelectedItem().toString() + "\n");

                }
            }
        });

        addCourseTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!hasTopicD.getSelectedItem().toString().equals("None")) {
                    String depend = hasTopicD.getSelectedItem().toString();
                    hasTopicList.add(depend);

                    console.append("Added topic " + depend + " too course \n");

                }
            }
        });



        addSub.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = subtopicOf.getSelectedItem().toString();
                if(!selected.equals("None")) {
                    console.append(" Added subtopic : " + selected + "\n");
                    subtopics.add(selected);

                }
            }
        });

        removeCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String co = courseBoxView.getSelectedItem().toString();
                // remove statements where resource is object
                Individual ind = model.getIndividual(co);
                ind.remove();
                console.append("Removed " + co + "\n");

                populateDropDown(model,topics,course);
            }
        });

        removeTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topic = topicBoxView.getSelectedItem().toString();
                // remove statements where resource is object
                Individual ind = model.getIndividual(topic);
                ind.remove();
                console.append("Removed " + topic + "\n");

                populateDropDown(model,topics,course);
            }

        });



        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(tabs.getSelectedIndex() == 3) {
                    createTopicButton.setText("Execute");
                }
                else {
                    createTopicButton.setText("Save");

                }
            }
        });
    }





    public void populateDropDown(Model model, Resource topics, Resource course) {

        dependsDrop.removeAllItems();
        subtopicOf.removeAllItems();
        dependsDrop.validate();
        subtopicOf.validate();
        hasTopicD.removeAllItems();
        hasTopicD.validate();
        lType.validate();
        lType.removeAllItems();
        topicBoxView.removeAllItems();
        topicBoxView.validate();
        courseBoxView.removeAllItems();
        courseBoxView.validate();
        // Theoretical..
        lType.addItem("None");
        lType.addItem("presentation");
        lType.addItem("lecture");

        ObjectProperty test = null;
        NodeIterator allTopics = model.listObjectsOfProperty(topics, hasSubtopic);
        NodeIterator allCourses = model.listObjectsOfProperty(course,hasCourseSubclas);


        List<String> resourceList = new ArrayList<>();
        List<String> courseList = new ArrayList<>();

        while (allTopics.hasNext()) {
            resourceList.add(allTopics.nextNode().toString());
        }

        while (allCourses.hasNext()) {
            courseList.add(allCourses.nextNode().toString());
        }

        dependsDrop.addItem("None");
        subtopicOf.addItem("None");
        hasTopicD.addItem("None");

        for (String item : resourceList) {
            dependsDrop.addItem(item);
            subtopicOf.addItem(item);
            hasTopicD.addItem(item);
            topicBoxView.addItem(item);
        }

        for (String item : courseList) {
            courseBoxView.addItem(item);
        }


    }

    public void runSparQL() {


        // Retrieve all individuals of type class with SPARQL
        String preQuery =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX chk: <http://chriskvik.uia.io/ontology#> ";

        String queryEnd = sparqlQuery.getText();
        System.out.println(preQuery+queryEnd);

        Query query = QueryFactory.create(preQuery+queryEnd);
        QueryExecution qe= QueryExecutionFactory.create(query, model);
        ResultSet resultset = qe.execSelect();


       // ResultSetFormatter.out(System.out, resultset, query);
        String resultsAsString = ResultSetFormatter.asText(resultset);
        console.append(resultsAsString.replace("-",""));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
