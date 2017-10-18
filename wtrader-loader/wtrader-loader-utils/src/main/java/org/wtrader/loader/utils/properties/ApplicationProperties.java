package org.wtrader.loader.utils.properties;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.InvalidParameterException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.wtrader.loader.utils.ApplicationType;
import org.wtrader.loader.utils.ErrorMessages;
import org.wtrader.loader.utils.beans.BaseBean;
import org.wtrader.loader.utils.exceptions.PropertyNotFoundedException;

@Configuration
@PropertySource({ "classpath:conf/data_loader.properties",  })
public class ApplicationProperties extends BaseBean implements IApplicationProperties {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                   VARIABLES                                                        //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = 201401201040L;

	private static IApplicationProperties instance;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                              APPLICATION PROPERTIES                                                //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final String APPLICATION_HOSTNAME_PROPERTY = "application.hostname";

	private static final String APPLICATION_PORT_PROPERTY = "application.port";

	private static final String EVALUATION_PROCESSOR_QUEUE_SIZE_PROPERTY = "evaluation.processor.queue.size";

	@Value("${" + APPLICATION_HOSTNAME_PROPERTY + ":#{null}}")
	private String applicationHostname;

	@Value("${" + APPLICATION_PORT_PROPERTY + ":#{null}}")
	private Integer applicationPort;

	@Value("${application.connection.timeout:1000}")
	private int applicationConnectionTimeout;

	@Value("${evaluation.processor.buffer.size:3}")
	private int evaluationProcessorBufferSize;

	@Value("${" + EVALUATION_PROCESSOR_QUEUE_SIZE_PROPERTY + ":#{null}}")
	private String evaluationProcessorQueueSize;

	@Value("${evaluation.processor.initial.population.size:#{\"100.0%\"}}")
	private String evaluationProcessorInitialPopulationSize;

	@Value("${evaluation.processor.threads.num:auto}")
	private String evaluationProcessorThreadsNum;

	@Value("${application.type:standalone}")
	private String applicationType;

	@Value("${system.proxy.hostname:#{null}}")
	private String proxyHostname;

	@Value("${system.proxy.port:#{null}}")
	private Integer proxyPort;

	@Override
	public String getApplicationHostname() {
		return this.applicationHostname;
	}

	@Override
	public int getApplicationPort() {
		return this.applicationPort;
	}

	@Override
	public int getApplicationConnectionTimeout() {
		return this.applicationConnectionTimeout;
	}

	@Override
	public int getEvaluationProcessorBufferSize() {
		return this.evaluationProcessorBufferSize;
	}

	@Override
	public int getEvaluationProcessorQueueSize() {
		return Integer.parseInt(this.evaluationProcessorQueueSize);
	}

	@Override
	public int getEvaluationProcessorInitialPopulationSize(int populationSize) {
		int size;

		if (this.evaluationProcessorInitialPopulationSize.contains("%")) {
			double percentual = Double.parseDouble(this.evaluationProcessorInitialPopulationSize.replace("%", ""));
			size = (int) (populationSize * (percentual / 100.0));
		} else {
			size = (int) Double.parseDouble(this.evaluationProcessorInitialPopulationSize);
		}

		if (size > populationSize) {
			size = populationSize;
		}

		return size;
	}

	@Override
	public int getEvaluationProcessorThreadsNum() {
		if (this.evaluationProcessorThreadsNum.trim().toLowerCase().equals("auto")) {
			return Runtime.getRuntime().availableProcessors();
		}

		return Integer.parseInt(this.evaluationProcessorThreadsNum);
	}

	@Override
	public ApplicationType getApplicationType() {
		return ApplicationType.parse(this.applicationType);
	}

	@Override
	public Proxy getProxy() {
		if ((this.proxyHostname != null) && !this.proxyHostname.trim().isEmpty()) {
			return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyHostname, this.proxyPort));
		} else {
			return null;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                            MULTIPLE GENETIC ALGORITHM                                              //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Genetic Algorithm Max Parallel Population
	private static final String GENETIC_ALGORITHM_MAX_PARALLEL_POPULATION_PROPERTY = "genetic.algorithm.max.parallel.population";

	@Value("${" + GENETIC_ALGORITHM_MAX_PARALLEL_POPULATION_PROPERTY + ":#{1}}")
	private int geneticAlgorithmMaxParallelPopulation;

	@Override
	public int getGeneticAlgorithmMaxParallelPopulation() {
		return this.geneticAlgorithmMaxParallelPopulation;
	}

	// Synchronize solution
	private static final String GENETIC_ALGORITHM_SYNCHRONIZE_SOLUTION_PROPERTY = "genetic.algorithm.synchronize.solution";

	@Value("${" + GENETIC_ALGORITHM_SYNCHRONIZE_SOLUTION_PROPERTY + ":#{false}}")
	private Boolean geneticAlgorithmSynchronizeSolution;

	@Override
	public Boolean isSolutionSynchronized() {
		return this.geneticAlgorithmSynchronizeSolution;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                 PUBLIC METHODS                                                     //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@PostConstruct
	public void init() {
		instance = this;

		switch (this.getApplicationType()) {
		case CLIENT:
			if (this.applicationHostname == null) {
				throw new PropertyNotFoundedException(String.format(ErrorMessages.PROPERTY_NOT_FOUNDED, APPLICATION_HOSTNAME_PROPERTY));
			}

			if (this.applicationPort == null) {
				throw new PropertyNotFoundedException(String.format(ErrorMessages.PROPERTY_NOT_FOUNDED, APPLICATION_PORT_PROPERTY));
			}
			break;

		case SERVER:
			if (this.applicationPort == null) {
				throw new PropertyNotFoundedException(String.format(ErrorMessages.PROPERTY_NOT_FOUNDED, APPLICATION_PORT_PROPERTY));
			}

			if (this.evaluationProcessorQueueSize == null) {
				throw new PropertyNotFoundedException(String.format(ErrorMessages.PROPERTY_NOT_FOUNDED, EVALUATION_PROCESSOR_QUEUE_SIZE_PROPERTY));
			}

			if (this.getGeneticAlgorithmMaxParallelPopulation() <= 0) {
				throw new InvalidParameterException(String.format(ErrorMessages.INVALID_PARAMETER_VALUE, this.getGeneticAlgorithmMaxParallelPopulation(),
						GENETIC_ALGORITHM_MAX_PARALLEL_POPULATION_PROPERTY));
			}
			break;

		case STANDALONE:
			break;
		}
	}

	public static IApplicationProperties getApplicationPropertiesInstance() {
		return instance;
	}

}
