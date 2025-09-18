package net.iiteam.jmeasure;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.AnnotatedElement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ParametersReportExtension implements AfterAllCallback, BeforeAllCallback
{
	private static final String OUTPUT_DIRECTORY = "build/test-results/measurements/";
	private MeasurementProducer producer;
	private ParametersReport ann;

	@Override
	public void beforeAll(ExtensionContext context)
	{
		AnnotatedElement element = context.getElement().orElse(null);
		if(element==null) return;

		ann = element.getAnnotation(ParametersReport.class);
		Object testInstance = context.getTestInstance().orElse(null);

		if(ann!=null&&testInstance instanceof MeasurementProducer)
			producer = (MeasurementProducer)testInstance;
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception
	{
		if(producer==null||ann==null) return;

		List<MeasurementRow> rows = producer.produceMeasurements();

		for(MeasurementRow row : rows)
			System.out.println(row.toString());

		if("xml".equalsIgnoreCase(ann.format()))
			writeXml(rows, ann);
		else if("csv".equalsIgnoreCase(ann.format()))
			writeCsv(rows, ann);
	}

	private void writeXml(List<MeasurementRow> rows, ParametersReport ann) throws IOException
	{
		Path out = getPath(ann);
		try(PrintWriter w = new PrintWriter(Files.newBufferedWriter(out)))
		{
			w.println("<report name=\""+ann.name()+"\">");
			for(MeasurementRow row : rows)
			{
				w.print("  <row");
				for(Map.Entry<String, Object> kv : row.getValues().entrySet())
					w.printf(" %s=\"%s\"", kv.getKey(), kv.getValue());
				w.println(" />");
			}
			w.println("</report>");
		}
		System.out.println("Parameters report written to "+out.toAbsolutePath());
	}

	private void writeCsv(List<MeasurementRow> rows, ParametersReport ann) throws IOException
	{
		Path out = getPath(ann);
		Set<String> allCols = new LinkedHashSet<>();
		for(MeasurementRow row : rows)
			allCols.addAll(row.getValues().keySet());

		try(PrintWriter w = new PrintWriter(Files.newBufferedWriter(out)))
		{
			w.println(String.join(",", allCols));
			for(MeasurementRow row : rows)
			{
				List<String> vals = new ArrayList<>();
				for(String col : allCols)
					vals.add(String.valueOf(row.getValues().get(col)));
				w.println(String.join(",", vals));
			}
		}
		System.out.println("Parameters report written to "+out.toAbsolutePath());
	}

	@NotNull
	private static Path getPath(ParametersReport ann) throws IOException
	{
		Path out = Paths.get(OUTPUT_DIRECTORY+ann.file());
		Files.createDirectories(out.getParent());
		return out;
	}
}
