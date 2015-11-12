import org.apache.jena.ontology.Individual;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 11.11.2015.
 */
public class Course {

    List<Individual> topicsOfCourse;
    Individual teorethicalPart;
    Individual practicalPart;
    String courseName;


    public Course () {
        topicsOfCourse = new ArrayList<Individual>();
    }


    public List<Individual> getTopicsOfCourse() {
        return topicsOfCourse;
    }

    public void setTopic(Individual item) {
        topicsOfCourse.add(item);
    }

    public Individual getPracticalPart() {
        return practicalPart;
    }

    public void setPracticalPart(Individual practicalPart) {
        this.practicalPart = practicalPart;
    }

    public Individual getTeorethicalPart() {
        return teorethicalPart;
    }

    public void setTeorethicalPart(Individual teorethicalPart) {
        this.teorethicalPart = teorethicalPart;
    }

    public String getCourseName() {
        return courseName;
    }

    /**
     *
     * @param courseName a name for the new course, without NS.
     */
    public void setCourseName(String courseName) {
        this.courseName = OProps.NS + courseName;
    }
}
