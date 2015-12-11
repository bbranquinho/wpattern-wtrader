package org.wtrader.loader.utils.properties;

import java.net.Proxy;

import org.wtrader.loader.utils.ApplicationType;

public interface IApplicationProperties {

	public int getGeneticAlgorithmMaxParallelPopulation();

	public String getApplicationHostname();

	public int getApplicationPort();

	public int getApplicationConnectionTimeout();

	public int getEvaluationProcessorBufferSize();

	public int getEvaluationProcessorQueueSize();

	public int getEvaluationProcessorThreadsNum();

	public ApplicationType getApplicationType();

	public int getEvaluationProcessorInitialPopulationSize(int populationSize);

	public Proxy getProxy();

	public Boolean isSolutionSynchronized();

}