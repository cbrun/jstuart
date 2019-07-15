package fr.obeo.tools.stuart;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class Posts {

	public static Set<String> getThreadIds(List<Post> postsBefore) {
		Set<String> threadIDs = Sets.newLinkedHashSet();
		for (Post post : postsBefore) {
			if (post.getThreadID() != null) {
				threadIDs.add(post.getThreadID());
			}
		}
		return threadIDs;
	}

	public static Multimap<String, Post> groupByThread(Collection<Post> pPosts) {
		Multimap<String, Post> byThread = HashMultimap.create();
		for (Post post : pPosts) {
			byThread.put(post.getThreadID(), post);
		}
		return byThread;
	}

	public static void toXlsFile(List<Post> posts, String filename, Set<String> teamAuthors) throws IOException {

		String lastCellReference = "A1";

		try (Workbook wb = new HSSFWorkbook();) {
			CreationHelper createHelper = wb.getCreationHelper();

			Sheet sheet = wb.createSheet("hits");

			int nbRow = 0;
			Row header = sheet.createRow(nbRow);
			header.createCell(0).setCellValue("Author");
			header.createCell(1).setCellValue("InTeam?");
			header.createCell(2).setCellValue("Subject");
			header.createCell(3).setCellValue("Creation Date");
			header.createCell(4).setCellValue("URL");
			header.createCell(5).setCellValue("Thread ID");
			header.createCell(6).setCellValue("First post?");
			header.createCell(7).setCellValue("Content");

			// author; repo; last_update; first_update; nb_commits; url
			// CellStyle cs = wb.createCellStyle();
			// cs.setWrapText(true);

			CellStyle hlink_style = wb.createCellStyle();
			Font hlink_font = wb.createFont();
			hlink_font.setUnderline(Font.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);

			CellStyle dateStyle = wb.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

			Multimap<String, Post> postsByAuthors = HashMultimap.create();
			for (Post h : posts) {
				postsByAuthors.put(h.getAuthor(), h);
			}
			Set<Post> oldestPostsForItsAuthor = Sets.newLinkedHashSet();
			for (String author : postsByAuthors.keySet()) {
				if (postsByAuthors.get(author).iterator().hasNext()) {
					Post oldest = postsByAuthors.get(author).iterator().next();
					for (Post p : postsByAuthors.get(author)) {
						if (p.getCreatedAt().before(oldest.getCreatedAt())) {
							oldest = p;
						}
					}
					oldestPostsForItsAuthor.add(oldest);
				}

			}

			for (Post h : posts) {
				nbRow++;
				// Create a row and put some cells in it. Rows are 0 based.
				Row row = sheet.createRow(nbRow);

				// Create a cell and put a value in it.
				Cell authorCell = row.createCell(0);
				authorCell.setCellValue(h.getAuthor());

				row.createCell(1).setCellValue(teamAuthors.contains(h.getAuthor()));
				row.createCell(2).setCellValue(h.getSubject());

				Cell lastUpdateC = row.createCell(3);

				lastUpdateC.setCellStyle(dateStyle);
				lastUpdateC.setCellValue(h.getCreatedAt());

				Cell hrefCell = row.createCell(4);
				Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
				link.setAddress(h.getKey());
				hrefCell.setHyperlink(link);

				hrefCell.setCellStyle(hlink_style);
				hrefCell.setCellValue(h.getKey());

				row.createCell(5).setCellValue(h.getThreadID());
				if (oldestPostsForItsAuthor.contains(h)) {
					row.createCell(6).setCellValue("1");
				} else {
					row.createCell(6).setCellValue("0");
				}

				Cell firstUpdateC = row.createCell(7);
				if (h.getMarkdownBody().length() <= 3000) {
					firstUpdateC.setCellValue(h.getMarkdownBody());
				} else {
					firstUpdateC.setCellValue("...");
				}
				lastCellReference = new CellReference(row.getRowNum(), firstUpdateC.getColumnIndex()).formatAsString();

			}
			System.out.println("nb rows" + nbRow);

			sheet.autoSizeColumn(0); // adjust width of the first column
			sheet.autoSizeColumn(1); // adjust width of the second column
			sheet.autoSizeColumn(2); // adjust width of the second column
			sheet.autoSizeColumn(3); // adjust width of the second column
			sheet.autoSizeColumn(4); // adjust width of the second column
			sheet.autoSizeColumn(5); // adjust width of the second column
			sheet.autoSizeColumn(6); // adjust width of the second column

			sheet.setAutoFilter(CellRangeAddress.valueOf("A1:" + lastCellReference));

			FileOutputStream fileOut = new FileOutputStream(filename);
			wb.write(fileOut);
			wb.close();
			fileOut.close();
		}

	}

}
