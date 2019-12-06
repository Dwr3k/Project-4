//Derrick McGlone
import java.util.Scanner;
public class cmsc401 
{
	int[] cutSpots; //Array that keeps track of what lines you can cut in the rod 
	int minCost = 0; //Minimum cut cost
	
	int rodSize = 0; // Size of rod
	int numCuts = 0; //Number of cuts that are possible

	int[] secondCuts; //Copy of original array for second method attempt
	int secondMin = 0; //Min Cost for the second method attempt

	public void inputTime()//Takes the input and builds the arrays
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

		reduxTime(0, rodSize, 0); //First rod cutting attempt
		cutTime(0, rodSize, 0); //Second rod cutting attempt


		if(minCost < secondMin) //If first attempt was smaller than second
		{
			System.out.println(minCost);
		}
		else if(minCost == secondMin) //If both methods returned same answer (dont really need this one ig)
		{
			System.out.println(minCost);
		}
		else if(secondMin < minCost) //If second attempt was smaller than the first
		{
			System.out.println(secondMin);
		}
	}

	public void reduxTime(int start, int end, int cutsMade)
	{
		int cuttingPosition = 0; //What line the rod will be cut at
		int closestLeft = 0; //How close current cut position is to another cut line on the left (or end of rod if first cut line)
		int closestRight = 0; //How close current cut position is to another cut line on the right (or end of rod if last cut line)
		int segmentSize = end - start; //How big segment of rod we are going to cut is 
		boolean clusterCut = false; //If all lines in segment are able to cut, just cut segment in half
		int middle = (start + end) /2; //Where the middle of the segment is

		int[] bestCuts = new int[rodSize + 1]; //Array that keeps the sum of closestLeft and closestRight for each cut line

		for(int i = start; i < end; ++i)//For loop that goes through all lines in segment
		{
			if(cutSpots[i] == 1 && clusterTime(i, start, end)) //If statement to see if a cut line is in a cluster
			{
				int clusterLength = 1;
				int index = i;

				while(clusterTime(index, start, end))//Sees how many clusters there are 
				{
					++clusterLength;
					++index;
				}

				if(clusterLength == segmentSize - 1)//If rod segment is only cut lines do the special cut
				{
					clusterCut = true;
					cuttingPosition = (end + start) / 2;
				}
			}

			if(cutSpots[i] == 1 && clusterCut == false)//If segment is not made of only clusters, find every cut lines bestCuts
			{	
				for(int j = i - 1; j >= start; --j)//For loop to see how far away cut line (or wall if end cut line) to the left 
				{
					if(cutSpots[j] == 1 || j == start)//If line is cut line or if you are at wall
					{
						closestLeft = i - j;
						break;
					}
				}

				for(int k = i + 1; k <= end; ++k)//For loop to find closest cut line or wall to the right
				{
					if(cutSpots[k] == 1 || k == end)//If line is a cut line or if you are at wall
					{
						closestRight = k - i;
						break;
					}
				}

				bestCuts[i] = closestLeft + closestRight;//Updates bestCuts array to have sum of closestLeft and closestRight at line position
			}
		}

		if(clusterCut == false)//If not doing special cluster cut
		{
			int maxCut = -100000;//Keeps track of what the biggest bestCut value is

			for(int i = 0; i < bestCuts.length; ++i)//Finds biggest bestCut out of all lines in segment
			{
				if(bestCuts[i] > maxCut)
				{
					maxCut = bestCuts[i];
				}
			}

			for(int i = 0; i < bestCuts.length; ++i)//Goes through array, if cutLine bestCut is not maxCut, set it to 0
			{
				if(bestCuts[i] != maxCut)
				{
					bestCuts[i] = 0;
				}
			}

			int distanceFromMid = 1000000;//Keeps track of how far the current cutting Position is from the middle
			
			for(int i = 0; i < bestCuts.length; ++i)//If there are multiple cutLines with same bestCut, finds the one closest to segment middle
			{
				if(bestCuts[i] != 0 && Math.abs(i - middle) < distanceFromMid)//If cut line is closer to the middle, update cut position
				{
					//System.out.println("Position: " + i + " has cut " + bestCuts[i]);
					cuttingPosition = i;
					distanceFromMid = Math.abs(i - middle);
				}
			}
		}

		minCost += segmentSize;//Add segmentSize to minCost and do a "cut"
		cutSpots[cuttingPosition] = 2;//Updates cut line to 2 to signify cuts been made
		++cutsMade;//Update cuts made

		//System.out.println(cuttingPosition);

		if(cutsMade < numCuts)
		{
			for(int i = start; i < cuttingPosition; ++i)//Sees if there are any cut Lines to the left and if so, cut new bar segment
			{
				if(cutSpots[i] == 1)
				{
					reduxTime(start, cuttingPosition, cutsMade);
				}
			}

			for(int i = cuttingPosition; i < end; ++i)//Sees if there are any cut Lines to the right and if so, cut new bar segment
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

		if(clusterBefore == start && clusterAfter == 1)// |start|CutLine|CutLine|
		{
			return true;
		}
		else if(clusterBefore == 1 && clusterAfter == 1)// |CutLine|CutLine|CutLine|
		{
			return true;
		}
		else if(clusterBefore == 1 && clusterAfter == end)// |CutLine|CutLine|End|
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