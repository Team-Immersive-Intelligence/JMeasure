package net.iiteam.jmeasure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ParametersReport(name = "Artillery Report", file = "distances.xml")
public class ArtillerySimulationTest implements MeasurementProducer
{
	@Override
	public List<MeasurementRow> produceMeasurements()
	{
		List<MeasurementRow> rows = new ArrayList<>();
		double[] calibers = {75.0, 88.0, 105.0};
		double[] angles = {10, 20, 30, 45, 60};

		for(double c : calibers)
		{
			for(double a : angles)
			{
				double dist = runShotSimulation(c, a);
				HashMap<String, Object> map = new HashMap<>();
				map.put("caliber_mm", c);
				map.put("angle_deg", a);
				map.put("distance_m", dist);
				rows.add(new MeasurementRow(map));
			}
		}
		return rows;
	}

	private double runShotSimulation(double caliber, double angleDeg)
	{
		double v = 800.0*(caliber/100.0);
		double g = 9.81;
		double rad = Math.toRadians(angleDeg);
		return (v*v*Math.sin(2*rad))/g;
	}
}
