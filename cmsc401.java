//Derrick McGlone
import java.util.Scanner;

public class cmsc401 
{
	int[] cutSpots;
	int minCost = 0;
	int rodSize = 0;
	int numCuts = 0;

	public void inputTime()//Takes the input and builds the array
	{
		Scanner input = new Scanner(System.in);

		rodSize = input.nextInt();
		numCuts = input.nextInt();

		cutSpots = new int[rodSize];

		for(int i = 0; i < numCuts; ++i)
		{
			cutSpots[input.nextInt()] = 1;
		}

		input.close();

		cutTime(0, rodSize, 0); //Initial call to cut with full bar size being passsed
	}

	public void cutTime(int start, int end, int cutsMade)
	{
		int segmentSize = end - start; //Gets segment size
		int middle = (end + start) / 2; //Finds the middle point of the segment (in terms of array position)
		int cuttingPosition = 0; //Where the cut will happen 

		if(cutSpots[middle] == 1)//Is there a cut spot in the middle?
		{
			cuttingPosition = middle;
		}
		else//If not, find the closest cut spot to middle
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

		minCost += segmentSize; 		//Adds cost of cutting segment
		++cutsMade;						//Increases number of cuts made
		cutSpots[cuttingPosition] = 2;	//Makes a cut at the determined cutting

		if(cutsMade < numCuts)
		{
			for(int i = start; i < cuttingPosition; ++i)//Recursively cuts the left side of new bar until no more cut spots
			{
				if(cutSpots[i] == 1)
				{
					cutTime(start, cuttingPosition, cutsMade);
					i = 0;
				}
			}

			for(int i = cuttingPosition; i < end; ++i)//Cuts right side recursively until no more cut spots on right
			{
				if(cutSpots[i] == 1)//If cut spot found, cut bar again
				{
					cutTime(cuttingPosition, end, cutsMade);
					i = 0;
				}
			}
		}
	}

	public static void main(String[] args)
	{
		cmsc401 program = new cmsc401();
		program.inputTime();
		System.out.println(program.minCost);
	}
}
