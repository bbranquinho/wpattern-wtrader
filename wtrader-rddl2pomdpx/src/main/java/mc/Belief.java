package mc;

import java.util.*;
import rddl.*;

public class Belief {
	
	public int _size;
	public int _wSize;
	public ArrayList<McState> _particles;
	public ArrayList<McState> _wParticles;
	public ArrayList<Double> _weights;
	public Random _rand = new Random(0);
	
	public Belief()
	{
		_size = 0;
		_wSize = 0;
	}
	
	public void clear()
	{
		if(_particles != null)
		{
			_particles.clear();
			_size = 0;
		}
	}
	
	public McState generateState()
	{
		if(_size == 0)
			return McState.generateMcState();
		else
			 return _particles.get(_rand.nextInt(_size));
	}
	
	public void addSample(McState state)
	{
		if(_particles == null)
		{
			_particles = new ArrayList<McState>();
		}
		
		_particles.add(state);
		
		_size++;
	}
	
	public void addSampleWithWeight(McState state, double weight)
	{
		if(_wParticles == null)
		{
			_wParticles = new ArrayList<McState>();
		}
		if(_weights == null)
		{
			_weights = new ArrayList<Double>();
		}
		_wParticles.add(state);
		_weights.add(new Double(weight));
		_wSize++;
	}
	
	public boolean resample()
	{
		if(_particles == null)
			_particles = new ArrayList<McState>();
		else
		{
			_particles.clear();
			_size = 0;
		}
		double sum = 0;
		for(int i = 0; i < _wSize; i++)
			sum += _weights.get(i).doubleValue();
		if(sum != 0)
		{
			for (int j = 0; j < SearchTree.PARTICLE_NUM; j++) {
				double prob = _rand.nextDouble() * sum;
				for (int i = 0; i < _wSize; i++) {
					prob = prob - _weights.get(i).doubleValue();
					if (prob < 0) {
						_particles.add(_wParticles.get(i));
						_size++;
						break;
					}
				}
			}
		}
		_wParticles.clear();
		_weights.clear();
		_wSize = 0;
		if(_size == 0)
			return false;
		return true;
	}
}
