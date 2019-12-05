//Derrick McGlone
import java.util.Scanner;

public class cmsc401 
{
	int[] cutSpots;
	int minCost = 0;
	int rodSize = 0;
	int numCuts = 0;
	int[] tempArray = null;



	public void inputTime()//Takes the input and builds the array
	{
		Scanner input = new Scanner(System.in);

		rodSize = input.nextInt();
		numCuts = input.nextInt();

		cutSpots = new int[rodSize + 1];
		

		for(int i = 0; i < numCuts; ++i)
		{
			cutSpots[input.nextInt()] = 1;
		}

		input.close();

		reduxTime(0, rodSize, 0);
		System.out.println(minCost);
	}

	public void reduxTime(int start, int end, int cutsMade)
	{
		int bestCut = -10000;
		int cuttingPosition = 0;
		int closestLeft = 0;
		int closestRight = 0;
		int segmentSize = end - start;
		boolean clusterCut = false;
		
		int[] bestCuts = new int[end - start];

		for(int i = start; i < end; ++i)
		{
			if(cutSpots[i] == 1 && clusterTime(i, start, end)) 
			{
				int clusterLength = 1;
				int index = i;

				while(clusterTime(index, start, end))
				{
					++clusterLength;
					++index;
				}

				if(clusterLength == segmentSize - 1) 
				{
					clusterCut = true;
					cuttingPosition = (end + start) / 2;
				}
			}

			if(cutSpots[i] == 1 && clusterCut == false)
			{	
				for(int j = i - 1; j >= start; --j)
				{
					if(cutSpots[j] == 1 || j == start)
					{
						closestLeft = i -j;
						break;
					}
				}

				for(int k = i + 1; k <= end; ++k)
				{
					if(cutSpots[k] == 1 || k == end)
					{
						closestRight = k - i;
						break;
					}
				}
				
				bestCuts[i] = closestRight + closestLeft;

				if(closestRight + closestLeft > bestCut)
				{
					bestCut = closestRight + closestLeft;
					cuttingPosition = i;
					
				}

			}
		}
		
		for(int i = 0; i < bestCuts.length; ++i)
		{
			if(bestCuts[i] != bestCut)
			{
				
			}
		}

		minCost += segmentSize;
		cutSpots[cuttingPosition] = 2;
		++cutsMade;

		System.out.println(cuttingPosition);

		if(cutsMade < numCuts)
		{
			for(int i = start; i < cuttingPosition; ++i)
			{
				if(cutSpots[i] == 1)
				{
					reduxTime(start, cuttingPosition, cutsMade);
				}
			}

			for(int i = cuttingPosition; i < end; ++i)
			{
				if(cutSpots[i] == 1)
				{
					reduxTime(cuttingPosition, end, cutsMade);
				}
			}
		}
	}

	public boolean clusterTime(int clusterFound, int start, int end)
	{
		int clusterBefore = cutSpots[clusterFound - 1];
		int clusterAfter = cutSpots[clusterFound];
		
		if(clusterBefore == start && clusterAfter == 1)
		{
			return true;
		}
		else if(clusterBefore == 1 && clusterAfter == 1)
		{
			return true;
		}
		else if(clusterBefore == 1 && clusterAfter == end)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public static void main(String[] args)
	{
		cmsc401 program = new cmsc401();
		program.inputTime();
	}
}