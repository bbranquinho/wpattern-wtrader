package org.wtrader.test.cep.processor.utils;

import org.apache.commons.logging.Log;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/ctx-wtrader-test-cep-processor.xml" })
public abstract class AbstractCEPProcessorTest {

	protected void logAndFail(String errorMsg, Log log){

		if(log != null){
			log.fatal(errorMsg);
		}

		Assert.fail(errorMsg);
	}

	protected void logAndFail(String errorMsg, Exception e, Log log){

		if(log != null){
			log.fatal(errorMsg, e);
		}

		Assert.fail(errorMsg);
	}

}
