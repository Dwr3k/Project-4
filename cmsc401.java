//Derrick McGlone
import java.util.Scanner;

public class cmsc401 
{
	int[] cutSpots;
	int minCost = 0;
	int rodSize = 0;
	int numCuts = 0;

	int[] secondCuts;
	int secondMin = 0;

	public void inputTime()//Takes the input and builds the array
	{
		Scanner input = new Scanner(System.in);

		rodSize = input.nextInt();
		numCuts = input.nextInt();

		cutSpots = new int[rodSize + 1];
		secondCuts = new int[rodSize + 1];

		for(int i = 0; i < numCuts; ++i)
		{
			cutSpots[input.nextInt()] = 1;	
		}

		for(int i = 0; i < cutSpots.length; ++i)
		{
			secondCuts[i] = cutSpots[i];
		}

		input.close();

		reduxTime(0, rodSize, 0);
		cutTime(0, rodSize, 0);


		if(minCost < secondMin)
		{
			System.out.println(minCost);
		}
		else if(minCost == secondMin)
		{
			System.out.println(minCost);
		}
		else if(secondMin < minCost)
		{
			System.out.println(secondMin);
		}
	}

	public void reduxTime(int start, int end, int cutsMade)
	{
		int cuttingPosition = 0;
		int closestLeft = 0;
		int closestRight = 0;
		int segmentSize = end - start;
		boolean clusterCut = false;
		int middle = (start + end) /2;

		int[] bestCuts = new int[rodSize + 1];

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

				bestCuts[i] = closestLeft + closestRight;
			}
		}

		if(clusterCut == false)
		{
			int maxCut = -100000;

			for(int i = 0; i < bestCuts.length; ++i)
			{
				if(bestCuts[i] > maxCut)
				{
					maxCut = bestCuts[i];
				}
			}

			for(int i = 0; i < bestCuts.length; ++i)
			{
				if(bestCuts[i] != maxCut)
				{
					bestCuts[i] = 0;
				}
			}

			int distanceFromMid = 1000000;
			for(int i = 0; i < bestCuts.length; ++i)
			{
				if(bestCuts[i] != 0 && Math.abs(i - middle) < distanceFromMid)
				{
					//System.out.println("Position: " + i + " has cut " + bestCuts[i]);
					cuttingPosition = i;
					distanceFromMid = Math.abs(i - middle);
				}
			}
		}

		minCost += segmentSize;
		cutSpots[cuttingPosition] = 2;
		++cutsMade;

		//System.out.println(cuttingPosition);

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

	public void cutTime(int start, int end, int cutsMade)
	{
		int segmentSize = end - start; //Gets segment size
		int middle = (end + start) / 2; //Finds the middle point of the segment (in terms of array position)
		int cuttingPosition = 0; //Where the cut will happen 

		if(secondCuts[middle] == 1)//Is there a cut spot in the middle?
		{
			cuttingPosition = middle;
		}
		else//If not, find the closest cut spot to middle
		{
			int minDistance = 1000;

			for(int i = start; i < end; ++i)
			{
				if(secondCuts[i] == 1)
				{
					if(Math.abs(i - middle) < minDistance)
					{
						minDistance = Math.abs(i - middle);
						cuttingPosition = i;
					}
				}
			}
		}

		secondMin += segmentSize; 		//Adds cost of cutting segment
		++cutsMade;						//Increases number of cuts made
		secondCuts[cuttingPosition] = 2;	//Makes a cut at the determined cutting

		if(cutsMade < numCuts)
		{
			for(int i = start; i < cuttingPosition; ++i)//Recursively cuts the left side of new bar until no more cut spots
			{
				if(secondCuts[i] == 1)
				{
					cutTime(start, cuttingPosition, cutsMade);
					i = 0;
				}
			}

			for(int i = cuttingPosition; i < end; ++i)//Cuts right side recursively until no more cut spots on right
			{
				if(secondCuts[i] == 1)//If cut spot found, cut bar again
				{
					cutTime(cuttingPosition, end, cutsMade);
					i = 0;
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