package ru.nuykin.onlineEduRuCoursesParse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PrintsFun {

    static public void PrintCoursesInfo(ArrayList<Fiver> courseInfo) throws IOException {
        FileWriter writerCourse = new FileWriter("coursesInfo.txt", false);
        String startInsertCourseQuery = "INSERT INTO `allcourses` (`id`, `name`, `univer`, `platform`, `duration`, `link`)";

        for (int i = 0; i < courseInfo.size(); i++) {
            writerCourse.write(
                    startInsertCourseQuery + " VALUES (" +
                            "'" + courseInfo.get(i).id + "', " +
                            "'" + courseInfo.get(i).name + "', " +
                            "'" + courseInfo.get(i).university + "', " +
                            "'" + courseInfo.get(i).platform + "', " +
                            "'" + courseInfo.get(i).duration + "', " +
                            "'" + courseInfo.get(i).url + "'" +
                            ");\n"
            );
        }

        writerCourse.flush();
    }

    static public void PrintDirectionInfo(Map<String, Integer> directions) throws IOException {
        FileWriter writerDirection = new FileWriter("directionsInfo.txt", false);
        String startInsertDirectionsQuery = "INSERT INTO `direction` (`id`, `name`)";

        for (Map.Entry<String, Integer> entry : directions.entrySet()) {
            writerDirection.write(
                    startInsertDirectionsQuery + " VALUES (" +
                            "'" + entry.getValue() + "', " +
                            "'" + entry.getKey()  + "'" +
                            ");\n"
            );
        }

        writerDirection.flush();
    }

    static public void PrintCourseDirection(ArrayList<Map.Entry<Integer, Integer>> coursesDirections) throws IOException {
        FileWriter writerCourseDirection = new FileWriter("coursesDirectionsInfo.txt", false);
        String startInsertCourseDirectionsQuery = "INSERT INTO `direction_allcourses` (`course`, `direction`)";

        for (Map.Entry<Integer, Integer> entry : coursesDirections) {
            writerCourseDirection.write(
                    startInsertCourseDirectionsQuery + " VALUES (" +
                            "'" + entry.getKey() + "', " +
                            "'" + entry.getValue() + "'" +
                            ");\n"
            );
        }

        writerCourseDirection.flush();
    }
}