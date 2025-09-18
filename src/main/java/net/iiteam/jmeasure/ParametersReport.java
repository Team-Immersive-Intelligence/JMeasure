package net.iiteam.jmeasure;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtendWith(ParametersReportExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public @interface ParametersReport
{
	String name();

	String file();

	String format() default "xml";

	String[] columns() default {};
}
