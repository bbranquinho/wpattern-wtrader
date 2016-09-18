package mc;

import java.util.*;

public class McState {
	
	public static int STATE_FLUENT_NUM;
	public boolean[] _fValues;
	
	public McState()
	{
		_fValues = new boolean[STATE_FLUENT_NUM];
	}
	
	public static McState generateMcState()
	{
		Random rand = new Random(0);
		McState state = new McState();
		for(int i = 0; i < STATE_FLUENT_NUM; i++)
		{
			state._fValues[i] = rand.nextBoolean();
		}
		return state;
	}
	
	public void assign(McState s)
	{
		for(int i = 0; i < STATE_FLUENT_NUM; i++)
		{
			_fValues[i] = s._fValues[i];
		}
	}
	
	public boolean equals(Object obj)
	{
		McState ms = (McState)obj;
		for(int i = 0; i < STATE_FLUENT_NUM; i++)
		{
			if(_fValues[i] != ms._fValues[i])
				return false;
		}
		return true;
	}
	
	public String toString()
	{
		String result = new String();
		for(int i = 0; i < STATE_FLUENT_NUM; i++)
		{
			if(_fValues[i])
				result += 1;
			else
				result += 0;
		}
		return result;
	}
	
	public int hashCode()
	{
		int result = -1;
		for(int i = 0; i < STATE_FLUENT_NUM; i++)
		{
			if(_fValues[i])
				result = i;
		}
		return result;
	}
}
