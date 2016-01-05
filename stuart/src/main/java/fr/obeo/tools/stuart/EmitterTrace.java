package fr.obeo.tools.stuart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class EmitterTrace {

	private File traceFile;
	private Gson gson;

	public EmitterTrace(File traceFile) {
		this.traceFile = traceFile;
		gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").setPrettyPrinting().create();
	}

	public Map<String, Date> load() throws FileNotFoundException {
		Map<String, Date> events = Maps.newLinkedHashMap();
		try {
			JsonReader reader = new JsonReader(new FileReader(traceFile));
			events = gson.fromJson(reader, new TypeToken<Map<String, Date>>() {
			}.getType());
		} catch (FileNotFoundException e) {
			// file will be created on save.
		}
		if (events == null) {
			/*
			 * if the file exists but is empty then gson returns null.
			 */
			events = Maps.newLinkedHashMap();
		}
		return events;

	}

	public Map<String, Date> save(Map<String, Date> trace) throws IOException {
		Files.write(gson.toJson(trace), traceFile, Charsets.UTF_8);
		return trace;
	}

	public Map<String, Date> evictOldEvents(Map<String, Date> trace, int nbDays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -nbDays);
		Date daysAgo = cal.getTime();
		Iterator<Map.Entry<String, Date>> it = trace.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Date> entry = it.next();
			Date sentDate = entry.getValue();
			if (sentDate.before(daysAgo)) {
				it.remove();
			}
		}

		return trace;
	}

}
