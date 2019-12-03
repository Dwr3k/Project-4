import java.util.Scanner;

public class cmsc401 
{
	int[] cutSpots;
	int minCost = 0;
	int rodSize = 0;
	int numCuts = 0;
	boolean doneWithLeft = false;

	public void inputTime()
	{
		Scanner input = new Scanner(System.in);

		rodSize = input.nextInt();
		numCuts = input.nextInt();

		cutSpots = new int[rodSize];

		for(int i = 0; i < numCuts; ++i)
		{
			cutSpots[input.nextInt()] = 1;
		}

		cutTime(0, rodSize, 0);
	}

	public int cutTime(int start, int end, int cutsMade)
	{
		int segmentSize = end - start;
		int middle = (end + start) / 2;
		int cuttingPosition = 0;

		if(cutSpots[middle] == 1)
		{
			cuttingPosition = middle;
		}
		else
		{
			int minDistance = 1000;

			for(int i = start; i < end; ++i)
			{
				if(cutSpots[i] == 1)
				{
					if(Math.abs(i - middle) < minDistance)
					{
						minDistance = Math.abs(i - middle);
						cuttingPosition = i;
					}
				}
			}
		}

		minCost += segmentSize;
		++numCuts;
		cutSpots[cuttingPosition] = 2;

		if(cutsMade < numCuts)
		{
			for(int i = start; i < middle; ++i)
			{
				if(cutSpots[i] == 1)
				{
					cutTime(start, middle, cutsMade);
					doneWithLeft = true;
				}
			}
			
			if(doneWithLeft == true)
			{
				for(int i = middle; i < end; ++i)
				{
					if(cutSpots[i] == 1)
					{
						cutTime(middle, end, cutsMade);
					}
				}
			}
		}

		return minCost;
	}

	public static void main(String[] args)
	{
		cmsc401 program = new cmsc401();
		program.inputTime();
		System.out.println(program.minCost);
	}
}

