import com.fasterxml.jackson.databind.deser.Deserializers;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.engine.main.OpExecutor;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
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


        // Create ontology model
        model = ModelFactory.createOntologyModel();

        // Intialize Vocabulary
        VOC cVocabulary = new VOC(model);
        model = cVocabulary.updateModel();

        // Create the neccesarry classes
        BaseClasses bClass = new BaseClasses(model);
        bClass.createDummyData();
        model = bClass.updateModel();



        populateDropDown(model, model.getOntClass(OProps.TOPIC), model.getOntClass(OProps.COURSE));


        createTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (tabs.getSelectedIndex() == 1) {

                    /*
                     CREATE A NEW TOPIC
                     */
                    String resName = newTopic.getText();

                    Topic newTopic = new Topic();
                    newTopic.setName(resName);

                    if (!depends.isEmpty()) {

                        for (String s : depends) {
                            newTopic.setRequirements(model.getIndividual(s));
                        }
                    }
                    if (!subtopics.isEmpty()) {
                        for (String s : subtopics) {
                            newTopic.setSubtopic(model.getIndividual(s));
                        }
                    }

                    if (!lectureType.isEmpty()) {
                        for (String s : lectureType) {
                            if (s.equals("presentation")) {
                                newTopic.setPracticalPart(model.getIndividual(OProps.presentation));
                            } else {
                                newTopic.setTeoreticalPart(model.getIndividual(OProps.written_exam));

                            }
                        }
                    }

                    Triple newTopicSave = new Triple(model);
                    newTopicSave.addTopic(newTopic);
                    model = newTopicSave.getModel();


                    console.append("Created topic " + resName + "\n");
                    populateDropDown(model, model.getOntClass(OProps.TOPIC), model.getOntClass(OProps.COURSE));


                } else if (tabs.getSelectedIndex() == 0) {

                    String name = courName.getText();
                    Course newCourse = new Course();
                    newCourse.setCourseName(name);


                    for (String item : hasTopicList) {
                        newCourse.setTopic(model.getIndividual(item));
                    }

                    Triple newCourseTriple = new Triple(model);
                    newCourseTriple.addCourse(newCourse);
                    model = newCourseTriple.getModel();

                    console.append("Created course " + name + "\n");

                } else if (tabs.getSelectedIndex() == 3) {
                    runSparQL("");
                }

                populateDropDown(model, model.getOntClass(OProps.TOPIC), model.getOntClass(OProps.COURSE));

                depends.clear();
                subtopics.clear();
                hasTopicList.clear();

            }

        });


        saveOntButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportModel saveTask = new ExportModel(model);
                if (saveTask.isStatus()) {
                    console.append(" Wrote ontology to file [" + saveTask.getFilename() + "]");
                }
            }
        });

        addDepend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = dependsDrop.getSelectedItem().toString();
                if (!selected.equals("None")) {
                    console.append(" Added dependency : " + selected + "\n");
                    depends.add(selected);
                }
            }
        });

        addLectType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!lType.getSelectedItem().toString().equals("None")) {
                    lectureType.add(lType.getSelectedItem().toString());
                    console.append(" Added lecture type : " + lType.getSelectedItem().toString() + "\n");

                }
            }
        });

        addCourseTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hasTopicD.getSelectedItem().toString().equals("None")) {
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
                if (!selected.equals("None")) {
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

                populateDropDown(model, model.getOntClass(OProps.TOPIC), model.getOntClass(OProps.COURSE));
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

                populateDropDown(model, model.getOntClass(OProps.TOPIC), model.getOntClass(OProps.COURSE));
            }

        });

        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabs.getSelectedIndex() == 3) {
                    createTopicButton.setText("Execute");
                } else {
                    createTopicButton.setText("Save");

                }
            }
        });

    }


    public void populateDropDown(OntModel model, Resource topics, Resource course) {

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



        List<String> resourceList = new ArrayList<>();
        List<String> courseList = new ArrayList<>();


        dependsDrop.addItem("None");
        subtopicOf.addItem("None");
        hasTopicD.addItem("None");

        courseList = internalSparQL(OProps.COURSE);
        resourceList = internalSparQL(OProps.TOPIC);

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

    public void runSparQL(String txt) {
        txt = sparqlQuery.getText();
        SQuery quer = new SQuery(txt, model);

        console.append(quer.getResult());
    }

    public List<String> internalSparQL(String classType) {

        String query = "SELECT ?x  WHERE { ?x rdf:type <"+classType+">}";

        String preQuery =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX chk: <http://chriskvik.uia.io/ontology#> ";

        Query q = QueryFactory.create(preQuery + query);
        QueryExecution qe = QueryExecutionFactory.create(q, model);
        ResultSet resultset = qe.execSelect();
        List<String> uriList = new ArrayList<String>();

        while(resultset.hasNext()) {
            QuerySolution row = resultset.nextSolution();
            uriList.add(row.get("x").toString());
        }

        return uriList;
    }
}

