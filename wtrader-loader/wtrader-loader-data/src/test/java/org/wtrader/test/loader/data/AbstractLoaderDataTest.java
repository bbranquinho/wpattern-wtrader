package org.wtrader.test.loader.data;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wtrader.test.loader.data.utils.LoaderDataTestContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LoaderDataTestContext.class })
public abstract class AbstractLoaderDataTest {

	protected void logAndFail(String errorMsg, Logger logger){
		if(logger != null){
			logger.fatal(errorMsg);
		}

		Assert.fail(errorMsg);
	}

	protected void logAndFail(String errorMsg, Exception e, Logger logger){
		if(logger != null){
			logger.fatal(errorMsg, e);
		}

		Assert.fail(errorMsg);
	}

}
