package net.iiteam.jmeasure;

import java.util.Map;

public class MeasurementRow
{
	private final Map<String, Object> values;

	public MeasurementRow(Map<String, Object> values)
	{
		this.values = values;
	}

	public Map<String, Object> getValues()
	{
		return values;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for(Map.Entry<String, Object> kv : values.entrySet())
		{
			if(!first) sb.append(", ");
			sb.append(kv.getKey()).append("=").append(kv.getValue());
			first = false;
		}
		sb.append("}");
		return sb.toString();
	}
}
