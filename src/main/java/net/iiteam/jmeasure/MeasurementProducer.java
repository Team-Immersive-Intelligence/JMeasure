package net.iiteam.jmeasure;

import org.junit.jupiter.api.Test;

import java.util.List;

public interface MeasurementProducer
{
	List<MeasurementRow> produceMeasurements();

	@Test
	default void measurements()
	{

	}
}
