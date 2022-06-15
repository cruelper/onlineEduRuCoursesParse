package ru.nuykin.onlineEduRuCoursesParse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OnlineEduRuCoursesParseApplication {
	static String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.115 Safari/537.36 OPR/88.0.4412.40";

	// эти данные, возможно, надо поменять, если нужно бует запускать все через длительное время
	static String baseURL = "https://online.edu.ru";
	static String startCatalogURL = baseURL + "/public/courses.xhtml?";
	static String endCatalogURL = "rating=desc&disp_mode=cs";
	static int countOfPages = 78;

	public static void main(String[] args) throws IOException, InterruptedException {

		FileWriter writer = new FileWriter("coursesInfo.txt", false);
		String startQuery = "INSERT INTO `allcourses` (`id`, `name`, `univer`, `platform`, `duration`, `link`)";
		int id = 1132;

		for (int pageNum = 0; pageNum < countOfPages; pageNum++) {
			ArrayList<String> courseUrls = getCoursesOnPage(pageNum);
			for (int i = 0; i < courseUrls.size(); i++) {
//				System.out.println(i + "  " + baseURL + courseUrls.get(i));
				Fiver courseData = getCourseData(baseURL + courseUrls.get(i));
				Thread.sleep(500);

				writer.write(
						startQuery + " VALUES (" +
								"'" + id + "', " +
								"'" + courseData.name + "', " +
								"'" + courseData.university + "', " +
								"'" + courseData.platform + "', " +
								"'" + courseData.duration + "', " +
								"'" + courseData.url + "'" +
								");\n"
				);
				id++;
			}
			System.out.println("Страница " + pageNum + " закончена");
		}

		writer.flush();
	}

	static ArrayList<String> getCoursesOnPage(int pageNum) throws IOException {
		String pageUrl = startCatalogURL;
		if (pageNum == 0) pageUrl = pageUrl + endCatalogURL;
		else pageUrl = pageUrl + "page=" + (pageNum - 1) + "&" + endCatalogURL;
		Document coursePage = Jsoup.connect(pageUrl).userAgent(userAgent).get();

		ArrayList<String> coursesUrlsOnPage = new ArrayList<>();
		Elements courseCards = coursePage.getElementsByClass("rc-card-link");
		for (int i = 0; i < courseCards.size(); i++){
			coursesUrlsOnPage.add(courseCards.get(i).attr("href"));
		}

		return coursesUrlsOnPage;
	}

	static Fiver getCourseData(String courseUrl) throws IOException {
		Document coursePage = Jsoup.connect(courseUrl).userAgent(userAgent).get();
		Fiver courseData = new Fiver();

		courseData.name = coursePage.getElementsByClass("course-name").get(0).text();

		courseData.university = coursePage.getElementById("j_idt281:j_idt302")
				.select("span")
				.text();

		Element table = coursePage
				.getElementsByClass("ui-panelgrid ui-widget course-view-header-table").get(0);
		Elements rows = table.select("tr");
		Element row = rows.get(1);
		Elements cols = row.select("td");
		courseData.duration = cols.get(1).text();
		courseData.platform = cols.get(2).text();

		Element buttonGoToCourse = coursePage
				.getElementsByClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only rc-action-button")
				.get(0);
		String badUrl = buttonGoToCourse.attr("onclick");
		String b = badUrl.substring(badUrl.indexOf("'") + 1);


		badUrl = badUrl.substring(badUrl.indexOf("'") + 1);
		badUrl = badUrl.substring(0, badUrl.indexOf("'"));

		courseData.url = badUrl.replaceAll("\\\\", "");

		return courseData;
	}

}
