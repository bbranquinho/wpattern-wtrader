package org.wtrader.cep.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TechnicalAnalysisMethod {

	public String name() default "";

	public String[] paramsName() default {};

	public DefaultValue[] defaultValues() default {};

}
