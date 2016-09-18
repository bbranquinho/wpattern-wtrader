package rddl.competition;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import rddl.EvalException;
import rddl.RDDL;
import rddl.RDDL.BOOL_EXPR;
import rddl.RDDL.DOMAIN;
import rddl.RDDL.INSTANCE;
import rddl.RDDL.LCONST;
import rddl.RDDL.LVAR;
import rddl.RDDL.NONFLUENTS;
import rddl.RDDL.OBJECTS_DEF;
import rddl.RDDL.PVARIABLE_DEF;
import rddl.RDDL.PVARIABLE_STATE_DEF;
import rddl.RDDL.PVAR_INST_DEF;
import rddl.RDDL.PVAR_NAME;
import rddl.RDDL.TYPE_NAME;
import rddl.State;
import rddl.parser.parser;
import util.Pair;

import mc.*;

public class Scheduler 
{
	
	public static final int DEFAULT_RANDOM_SEED = 0;
	private static RDDL rddl = new RDDL();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
			/** Define a host server */
			String host = Server.HOST_NAME;
			/** Define a port */
			int port = Server.PORT_NUMBER;
			String clientName = "random";
			int randomSeed = DEFAULT_RANDOM_SEED;
			
			String instance_list = "../pomdp_instance_list.txt";
			
			long app_start_time = System.currentTimeMillis();
			
			String resultFile = "result.txt";
			File rf = new File(resultFile);
			if(rf.exists())
				rf.delete();
			try
			{
				rf.createNewFile();
			}
			catch(Exception ex)
			{
				
			}
			
			ArrayList<String> instances = new ArrayList<String>();
			ArrayList<String> sarsopInstances = new ArrayList<String>();
			ArrayList<String> uctInstances = new ArrayList<String>();
			ArrayList<String> failedUcts = new ArrayList<String>();
			
			/*if ( args.length < 4 ) {
				System.out.println("usage: rddlfilename hostname clientname policyclassname " +
						"(optional) portnumber randomSeed instanceName/directory");
				System.exit(1);
			}*/
			if ( args.length < 5 ) {
				System.out.println("usage: rddlfilename instance_list hostname portnumber clientname " + "(optional) totaltime");
				System.exit(1);
			}
//			host = args[1];
//			clientName = args[2];
//			port = Integer.valueOf(args[3]);
			instance_list = args[1];
			host = args[2];
			port = Integer.valueOf(args[3]);
			clientName = args[4];
			
//			long TOTAL_TIME = (24 * 3600 - 60) * 1000;
			long TOTAL_TIME = (24 * 3600 - 2) * 1000;
			if(args.length > 5)
			{
				long read_time = Long.valueOf(args[5]).longValue();
				if(read_time != -1)
					TOTAL_TIME = read_time * 1000;
			}
				
			boolean SARSOP_LIST = true;
//			if(args.length >= 6)
//				SARSOP_LIST = Boolean.valueOf(args[5]).booleanValue();
			
			int SARSOP_NUM = 9;
//			if(args.length >= 7)
//				SARSOP_NUM = Integer.valueOf(args[6]).intValue();
			
			try {
				// Cannot assume always in rddl.policy
				//Class c = Class.forName(args[3]);
				
				// Load RDDL files
				File f = new File(args[0]);
				if (f.isDirectory()) {
					for (File f2 : f.listFiles())
						if (f2.getName().endsWith(".rddl")) {
							//System.out.println("Loading: " + f2);
							rddl.addOtherRDDL(parser.parse(f2));
						}
				} else
					rddl.addOtherRDDL(parser.parse(f));
				
				/*for(File instFile : f.getParentFile().listFiles())
				{
					if(instFile.getName().equals("pomdp_instance_list.txt"))
					{
						BufferedReader reader = new BufferedReader(new FileReader(instFile));
						String instanceName = null;
						while((instanceName = reader.readLine()) != null)
						{
							instances.add(instanceName);
						}
						
						reader.close();
					}
				}*/
				BufferedReader instance_reader = new BufferedReader(new FileReader(instance_list));
				String instance_name = null;
				while((instance_name = instance_reader.readLine()) != null)
				{
					instances.add(instance_name);
				}
				instance_reader.close();
				
				int instSize = instances.size();
				//int index = 0;
				//analyze instances
				if(!SARSOP_LIST)
				{
					for(int i = 0; i < instances.size(); i++)
					{
						String instanceName = instances.get(i);
						//index++;
						INSTANCE instance = rddl._tmInstanceNodes.get(instanceName);
						NONFLUENTS nonFluents = rddl._tmNonFluentNodes.get(instance._sNonFluents);
						DOMAIN domain = rddl._tmDomainNodes.get(instance._sDomain);
					
						HashMap<TYPE_NAME,ArrayList<LCONST>>_hmObject2Consts = new HashMap<TYPE_NAME,ArrayList<LCONST>>();
					
						for (OBJECTS_DEF obj_def : nonFluents._hmObjects.values())
							_hmObject2Consts.put(obj_def._sObjectClass, obj_def._alObjects);
					
						for (OBJECTS_DEF obj_def : instance._hmObjects.values())
							_hmObject2Consts.put(obj_def._sObjectClass, obj_def._alObjects);
					
						State state = new State();
						state.init(nonFluents != null ? nonFluents._hmObjects : null, instance._hmObjects,
								domain._hmTypes, domain._hmPVariables, domain._hmCPF,
								instance._alInitState, nonFluents == null ? null : nonFluents._alNonFluents,
								domain._alStateConstraints, domain._exprReward, instance._nNonDefActions);
						
						int spaceSize = 0;
					
						for (Map.Entry<PVAR_NAME,PVARIABLE_DEF> e : domain._hmPVariables.entrySet()) {
							PVAR_NAME pname   = e.getKey();
							PVARIABLE_DEF def = e.getValue();
							if (def instanceof PVARIABLE_STATE_DEF && !((PVARIABLE_STATE_DEF)def)._bNonFluent) 
							{	
								int size = 1;
								for(TYPE_NAME type: def._alParamTypes)
									size *= _hmObject2Consts.get(type).size();
								spaceSize += size;
							}
						}					
						
						try 
						{
							boolean ssConst = false;
							boolean aaConst = false;
							int ssCnt = 0;
							int aaCnt = 0;
							for(BOOL_EXPR expr: domain._alStateConstraints)
							{
								HashSet<Pair> relevant_vars = new HashSet<Pair>();
								HashMap<LVAR, LCONST> empty_sub = new HashMap<LVAR, LCONST>();

								expr.collectGFluents(empty_sub, state, relevant_vars);

								boolean aa = true;
								boolean ss = true;
								for (Pair p : relevant_vars) {
									if (state.getPVariableType((PVAR_NAME) p._o1) == State.ACTION)
										ss = false;
									else
										aa = false;
								}
								if(aa)
									aaCnt++;
								else if(ss)
									ssCnt++;
							}

							if(aaCnt > 0)
								aaConst = true;
							if(ssCnt > 0)
								ssConst = true;
							//if (spaceSize <= SARSOP_NUM && instance._nNonDefActions == 1 && domain._bStateConstraints == false && index <= instSize) 
							Process process = null;
							if (spaceSize <= SARSOP_NUM && !ssConst)
							{
								//System.out.println("before translation");
								process = Runtime.getRuntime().exec("java rddl.translate.RDDL2Pomdpx " + args[0] + " " + instanceName);
								StreamConsumer errorConsumer = new StreamConsumer(process.getErrorStream(), "error");
								StreamConsumer outputConsumer = new StreamConsumer(process.getInputStream(), "output");
								errorConsumer.start();
								outputConsumer.start();
								int exitVal = process.waitFor();
								if (exitVal == 0) 
									sarsopInstances.add(instanceName);
								else
									uctInstances.add(instanceName);
							}
							else
							{
								uctInstances.add(instanceName);
							}
						}
						catch(Exception e)
						{
							uctInstances.add(instanceName);
						}
					}
					try
					{
						FileWriter writer = new FileWriter("sarsop_instance_list.txt");
						for(String instance: sarsopInstances)
							writer.write(instance + "\r\n");
						writer.close();
						
						writer = new FileWriter("uct_instance_list.txt");
						for(String instance: uctInstances)
							writer.write(instance + "\r\n");
						writer.close();
					}
					catch(Exception e)
					{
						
					}
					return;
				}
				else
				{
					/*BufferedReader reader = new BufferedReader(new FileReader("uct_instance_list.txt"));
					String instanceName = null;
					while((instanceName = reader.readLine()) != null)
					{
						uctInstances.add(instanceName);
					}	
					reader.close();
					
					reader = new BufferedReader(new FileReader("sarsop_instance_list.txt"));
					while((instanceName = reader.readLine()) != null)
					{
						sarsopInstances.add(instanceName);
					}	
					reader.close();*/
					
					uctInstances.addAll(instances);
					
					/*for(String instance: instances)
					{
						boolean uctI = true;
						for(String sarsopI: sarsopInstances)
						{
							if(instance.compareTo(sarsopI) == 0)
								uctI = false;
						}
						if(uctI)
							uctInstances.add(instance);
					}*/
				}
				
				//Sarsop execution
				Process process1 = null;
				Process process2 = null;
				int sarsop_num = sarsopInstances.size();
				int running_sarsop = 0;
				boolean process1Running = false;
				boolean process2Running = false;
				String instanceName1 = null;
				String instanceName2 = null;
				long inst1_start_time = 0;
				long inst2_start_time = 0;
				long inst1_end_time = 0;
				long inst2_end_time = 0;
				FileWriter writer = new FileWriter(resultFile, true);
				if(sarsop_num > 0)
				{
					instanceName1 = sarsopInstances.get(0);
					inst1_start_time = System.currentTimeMillis();
					writer.write("instance:" + instanceName1 + " uses sarsop" + " time:" + String.valueOf((System.currentTimeMillis() - app_start_time) / 1000) + "\r\n");
					process1 = Runtime.getRuntime().exec("OfflineSolver.exe " + "pomdpx\\" + instanceName1 + ".pomdpx");
					StreamConsumer errorConsumer1 = new StreamConsumer(process1.getErrorStream(), "error");
					StreamConsumer outputConsumer1 = new StreamConsumer(process1.getInputStream(), "output");
					errorConsumer1.start();
					outputConsumer1.start();
					running_sarsop = 1;
					process1Running = true;
				}
				if(sarsop_num > 1)
				{
					instanceName2 = sarsopInstances.get(1);
					inst2_start_time = System.currentTimeMillis();
					writer.write("instance:" + instanceName2 + " uses sarsop" + " time:" + String.valueOf((System.currentTimeMillis() - app_start_time) / 1000) + "\r\n");
					process2 = Runtime.getRuntime().exec("OfflineSolver.exe " + "pomdpx\\" + instanceName2 + ".pomdpx");
					StreamConsumer errorConsumer2 = new StreamConsumer(process2.getErrorStream(), "error");
					StreamConsumer outputConsumer2 = new StreamConsumer(process2.getInputStream(), "output");
					errorConsumer2.start();
					outputConsumer2.start();
					running_sarsop = 2;
					process2Running = true;
				}
				writer.close();
				int runned_sarsop =  0;
				long DEFAULT_INTERVAL = 2 * 1000;
				//for(int i = 0; i < sarsopInstances.size(); )
				while(runned_sarsop < sarsop_num)
				{	
					boolean process1Finished = true;
					boolean process2Finished = true;
					try 
					{
						if(process1Running)
							process1.exitValue();
					} 
					catch (IllegalThreadStateException e) 
					{
						process1Finished = false;
					}
					if(process1Finished)
					{
						writer = new FileWriter(resultFile, true);
						if(process1Running)
						{
							int exitVal1 = process1.exitValue();
							if (exitVal1 == 0) 
							{
								writer.write("instance:" + instanceName1 + " ends successfully\r\n");
							//FileWriter ssWriter = new FileWriter("sarsop_instance_list.txt");
							//for(int k = 1; k < sarsopInstances.size(); k++)
							//{
								//ssWriter.write(sarsopInstances.get(k) + "\r\n");
							//}
							//ssWriter.close();
							} 
							else 									
							{
								writer.write("instance:" + instanceName1 + " failed in sarsop\r\n");
								uctInstances.add(0, instanceName1);
							//FileWriter ssWriter = new FileWriter("sarsop_instance_list.txt");
							//for(int k = 1; k < sarsopInstances.size(); k++)
								//{
								//ssWriter.write(sarsopInstances.get(k) + "\r\n");
								//}
								//ssWriter.close();
							}
							inst1_end_time = System.currentTimeMillis();
							writer.write("instance:" + instanceName1 + " time:" + String.valueOf((inst1_end_time - inst1_start_time) / 1000) + "\r\n");
							writer.write("time from beginning: " + String.valueOf(inst1_end_time - app_start_time) + "\r\n\r\n");
							runned_sarsop++;
							running_sarsop--;
							process1Running = false;
						}
						if(runned_sarsop + running_sarsop < sarsop_num)
						{
							instanceName1 = sarsopInstances.get(runned_sarsop + running_sarsop);
							inst1_start_time = System.currentTimeMillis();
							process1 = Runtime.getRuntime().exec("OfflineSolver.exe " + "pomdpx\\" + instanceName1 + ".pomdpx");
							StreamConsumer errorConsumer1 = new StreamConsumer(process1.getErrorStream(), "error");
							StreamConsumer outputConsumer1 = new StreamConsumer(process1.getInputStream(), "output");
							errorConsumer1.start();
							outputConsumer1.start();
							running_sarsop++;
							process1Running = true;
							
							writer.write("instance:" + instanceName1 + " uses sarsop" + " time:" + String.valueOf((System.currentTimeMillis() - app_start_time) / 1000) + "\r\n");
						}
						writer.close();
					}
					else
					{
						try 
						{
							if(process2Running)
								process2.exitValue();
						} 
						catch (IllegalThreadStateException e) 
						{
							process2Finished = false;
						}
						if(process2Finished)
						{
							writer = new FileWriter(resultFile, true);
							if(process2Running)
							{
								int exitVal2 = process2.exitValue();
								if (exitVal2 == 0) 
								{
									writer.write("instance:" + instanceName2 + " ends successfully\r\n");
								//FileWriter ssWriter = new FileWriter("sarsop_instance_list.txt");
								//for(int k = 1; k < sarsopInstances.size(); k++)
								//{
									//ssWriter.write(sarsopInstances.get(k) + "\r\n");
								//}
								//ssWriter.close();
								} 
								else 									
								{
									writer.write("instance:" + instanceName2 + " failed in sarsop\r\n");
									uctInstances.add(0, instanceName2);
								//FileWriter ssWriter = new FileWriter("sarsop_instance_list.txt");
								//for(int k = 1; k < sarsopInstances.size(); k++)
								//{
									//ssWriter.write(sarsopInstances.get(k) + "\r\n");
								//}
								//ssWriter.close();
								}
								inst2_end_time = System.currentTimeMillis();
								writer.write("instance:" + instanceName2 + " time:" + String.valueOf((inst2_end_time - inst2_start_time) / 1000) + "\r\n");
								writer.write("time from beginning: " + String.valueOf(inst2_end_time - app_start_time) + "\r\n\r\n");
								runned_sarsop++;
								running_sarsop--;
								process2Running = false;
							}
							if(runned_sarsop + running_sarsop < sarsop_num)
							{
								instanceName2 = sarsopInstances.get(runned_sarsop + running_sarsop);
								inst2_start_time = System.currentTimeMillis();
								process2 = Runtime.getRuntime().exec("OfflineSolver.exe " + "pomdpx\\" + instanceName2 + ".pomdpx");
								StreamConsumer errorConsumer2 = new StreamConsumer(process2.getErrorStream(), "error");
								StreamConsumer outputConsumer2 = new StreamConsumer(process2.getInputStream(), "output");
								errorConsumer2.start();
								outputConsumer2.start();
								running_sarsop++;
								process2Running = true;
								writer.write("instance:" + instanceName2 + " uses sarsop" + " time:" + String.valueOf((System.currentTimeMillis() - app_start_time) / 1000) + "\r\n");
							}
							writer.close();
						}
					}
					try 
					{
						Thread.sleep(DEFAULT_INTERVAL);
					} 
					catch (InterruptedException e1) 
					{
					}	
				}
					
					
//					String instanceName = sarsopInstances.get(i);
//					long inst_start_time = System.currentTimeMillis();
//					FileWriter writer = new FileWriter(resultFile, true);
//						//writer.write("instance:" + instanceName + " starts\r\n");
//						//Process process = null;
//					try
//					{
//						writer.write("instance:" + instanceName	+ " uses sarsop\r\n");
//						writer.close();
//						//process = Runtime.getRuntime().exec("java rddl.translate.RDDL2Pomdpx " + args[0] + " " + instanceName);
//						//StreamConsumer errorConsumer = new StreamConsumer(process.getErrorStream(), "error");
//						//StreamConsumer outputConsumer = new StreamConsumer(process.getInputStream(), "output");
//						//errorConsumer.start();
//						//outputConsumer.start();
//						//int exitVal = process.waitFor();
//						if (true) 
//						{
//							long sarsop_start_time = System.currentTimeMillis();
//							if(process1 == null)
//							{
//								String instanceName1 = sarsopInstances.get(i);
//								process1 = Runtime.getRuntime().exec("OfflineSolver.exe " + "pomdpx\\" + instanceName + ".pomdpx" + " --timeout 540");
//								StreamConsumer errorConsumer1 = new StreamConsumer(process1.getErrorStream(), "error");
//								StreamConsumer outputConsumer1 = new StreamConsumer(process1.getInputStream(), "output");
//								errorConsumer1.start();
//								outputConsumer1.start();
//								i++;
//								running_sarsop++;
//							}
//							if(process2 == null && i < 2)
//							{
//								String instanceName1 = sarsopInstances.get(i);
//								process2 = Runtime.getRuntime().exec("OfflineSolver.exe " + "pomdpx\\" + instanceName + ".pomdpx" + " --timeout 540");
//								StreamConsumer errorConsumer1 = new StreamConsumer(process2.getErrorStream(), "error");
//								StreamConsumer outputConsumer1 = new StreamConsumer(process2.getInputStream(), "output");
//								errorConsumer1.start();
//								outputConsumer1.start();
//								i++;
//								running_sarsop++;
//							}
//
//								long DEFAULT_TIMEOUT = 40 * 60 * 1000;
//								long DEFAULT_INTERVAL = 2 * 1000;
//								boolean processFinished = false;
//								while (System.currentTimeMillis() - sarsop_start_time < DEFAULT_TIMEOUT && !processFinished) 
//								{
//									processFinished = true;
//									try 
//									{
//										process2.exitValue();
//									} 
//									catch (IllegalThreadStateException e) 
//									{
//										processFinished = false;
//										try 
//										{
//											Thread.sleep(DEFAULT_INTERVAL);
//										} 
//										catch (InterruptedException e1) 
//										{
//										}
//									}	
//								}
//								if (!processFinished) 
//								{
//									process2.destroy();
//									writer = new FileWriter(resultFile, true);
//									writer.write("instance:" + instanceName + " timeout in sarsop\r\n");
//									uctInstances.add(0, instanceName);								} 
//								else 
//								{
//									writer = new FileWriter(resultFile, true);
//									int exitVal2 = process1.exitValue();
//									if (exitVal2 == 0) 
//									{
//										writer.write("instance:" + instanceName + " ends successfully\r\n");
//										FileWriter ssWriter = new FileWriter("sarsop_instance_list.txt");
//										for(int k = 1; k < sarsopInstances.size(); k++)
//										{
//											ssWriter.write(sarsopInstances.get(k) + "\r\n");
//										}
//										ssWriter.close();
//									} 
//									else 									
//									{
//										writer.write("instance:" + instanceName + " failed in sarsop\r\n");
//										instances.add(0, instanceName);
//										FileWriter ssWriter = new FileWriter("sarsop_instance_list.txt");
//										for(int k = 1; k < sarsopInstances.size(); k++)
//										{
//											ssWriter.write(sarsopInstances.get(k) + "\r\n");
//										}
//										ssWriter.close();
//									}
//								}
//							}
//						} 
//						catch(Exception e)
//						{
//							if(process1 != null)
//								process1.destroy();
//							writer = new FileWriter(resultFile, true);
//							writer.write("instance:" + instanceName	+ " ends with exception" + "\r\n");
//							uctInstances.add(instanceName);
//							FileWriter ssWriter = new FileWriter("sarsop_instance_list.txt");
//							for(int k = 1; k < sarsopInstances.size(); k++)
//							{
//								ssWriter.write(sarsopInstances.get(k) + "\r\n");
//							}
//							ssWriter.close();
//						}
//					}
				
				//uct execution
				for(int i = 0; i < uctInstances.size(); i++)
				{
					long inst_start_time = System.currentTimeMillis();
					writer = new FileWriter(resultFile, true);
					//writer.write("instance:" + instanceName + " starts\r\n");
					Process process = null;
					String instanceName = uctInstances.get(i);
					try
					{
						writer.write("instance:" + instanceName + " uses uct\r\n");
						writer.close();
						long timeToRun = (TOTAL_TIME - (System.currentTimeMillis() - app_start_time))/(uctInstances.size() + failedUcts.size() - i);
//						String[] cmd = { "java", "rddl.competition.Client", args[0], args[1], args[2], args[3], instanceName, String.valueOf(timeToRun) };
						String[] cmd = { "java", "-Xmx8g", "-cp", "bin:lib/*", "rddl.competition.Client", args[0], host, clientName, String.valueOf(port), instanceName, String.valueOf(timeToRun) };
						process = Runtime.getRuntime().exec(cmd);
						StreamConsumer errorConsumer = new StreamConsumer(process.getErrorStream(), "error");
						StreamConsumer outputConsumer = new StreamConsumer(process.getInputStream(), "output");
						errorConsumer.start();
						outputConsumer.start();
						int exitVal = process.waitFor();
						writer = new FileWriter(resultFile, true);
						if (exitVal == 0)
						{
							writer.write("instance:" + instanceName + " ends successfully" + "\r\n");
							/*FileWriter uctWriter = new FileWriter("uct_instance_list.txt");
							for(int k = 1; k < uctInstances.size(); k++)
								uctWriter.write(uctInstances.get(i) + "\r\n");
							uctWriter.close();*/
						}
						else 
						{
							writer.write("instance:" + instanceName	+ " ends with error" + "\r\n");
							failedUcts.add(instanceName);
							/*FileWriter uctWriter = new FileWriter("uct_instance_list.txt");
							for(int k = 1; k < uctInstances.size(); k++)
								uctWriter.write(uctInstances.get(i) + "\r\n");
							uctWriter.close();*/
							FileWriter failedWriter = new FileWriter("failed_ucts.txt");
							for(int k = 0; k < uctInstances.size(); k++)
								failedWriter.write(failedUcts.get(i) + "\r\n");
							failedWriter.close();
						}
					}
				
					catch (Exception e) 
					{
						if(process != null)
							process.destroy();
						writer = new FileWriter(resultFile, true);
						writer.write("instance:" + instanceName	+ " ends with exception" + "\r\n");
						failedUcts.add(instanceName);
					}
					long inst_end_time = System.currentTimeMillis();
					writer.write("instance:" + instanceName + " time:" + String.valueOf((inst_end_time - inst_start_time) / 1000) + "\r\n");
					writer.write("time from beginning: " + String.valueOf(inst_end_time - app_start_time) + "\r\n\r\n");
					writer.close();
					
				}
			}
			catch(Exception e)
			{
				
			}
	}

}

class StreamConsumer extends Thread 
{     
	InputStream is;
	String type;
	StreamConsumer (InputStream is, String type) 
	{         
		this.is = is;
		this.type = type;
	}
	
	public void run () 
	{         
		try 
		{             
			InputStreamReader isr = new InputStreamReader (is);
			BufferedReader br = new BufferedReader (isr);
			String line = null;
			while ((line = br.readLine()) != null)
				//System.out.println (type + ">" + line);
				System.out.println(line);
		} 
		catch (IOException ioe) 
		{             
			ioe.printStackTrace();           
		}     
	} 
} 
