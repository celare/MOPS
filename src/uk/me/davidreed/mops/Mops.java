package uk.me.davidreed.mops;
import java.util.*;

public class Mops
	{
	private int[] aS, aL, aM, aK, aP;
	private double[] aA, aR, aC, aE, aI, aG, aQ, aT, aF;
	private String[] aY$, aP$, aS$;
	private double S, P, Q, F;
	private String N$;
	private int N, W, X;
	private Random B;
	private MopsState CurrentState = MopsState.Instantiate;
	//private IInputListener InputListener = null;
	private OnMopsOutputListener onMopsOutputListener;
	private OnMopsPrintListener onMopsPrintListener;

	/*public interface IInputListener {
		public void Execute(String input);
	}*/
	public interface OnMopsOutputListener {
		public abstract void OnMopsOutput(String output);
	}
	public interface OnMopsPrintListener {
		public abstract void OnMopsPrint(String output);
	}

	public void setOnMopsOutputListener(OnMopsOutputListener listener) {
		onMopsOutputListener = listener;
	}
	public void setOnMopsPrintListener(OnMopsPrintListener listener) {
		onMopsPrintListener = listener;
	}
	
	private enum MopsState {
		Instantiate,
		CheckPrinterAttached,
		NewGame,
		Command,
		CurrentValues,
		ProductionRequired,
		RawMaterialOrder,
		PrintWeekSummary
	}
		
	public void Mops()
	{
		// Don't start immediately because client must hook up event handlers before calling Start()
	}
	
	public void Start()
	{
		//MopsEngine(MopsState.Instantiate);
		Process("");
	}

/*	
	public void MopsEngine(MopsState state)
	{
		switch (state) {
			case Instantiate:
				Initialise();
				CheckPrinterAttached();
				break;
			case CheckPrinterAttached:
				NewGame();
				break;
			case NewGame:
				NewWeek();
				break;
		}
	}
*/

	private void Process(String input)
	{
		switch (CurrentState) {
			case Instantiate:
				Initialise();
				CheckPrinterAttached();
				break;
			case CheckPrinterAttached:
				CheckPrinterAttachedResponse(input);
				break;
			case NewGame:
				NewGameResponse(input);
				break;
			case Command:
				CommandResponse(input);
				break;
			case CurrentValues:
				CurrentValuesResponse(input);
				break;
			case ProductionRequired:
				ProductionRequiredResponse(input);
				break;
			case RawMaterialOrder:
				RawMaterialOrderResponse(input);
				break;
			case PrintWeekSummary:
				PrintWeekSummaryResponse(input);
				break;
		}
	}
	
	private void Print(String text)
	{
		onMopsOutputListener.OnMopsOutput(text);
	}
	
	public void Send(String input) {
		/*if (!(InputListener == null) && input != "") InputListener.Execute(input);*/
		if (input != "") Process(input);
	}
	
	private void Initialise()
	{
		// All arrays are one index larger than required to support 1-based access. Index 0 is blank or zero.
		aS = new int[34];
		aR = new double[37]; // aR[2] = Raw Materials
		aA = new double[34];
		aL = new int[22]; // aL[21] = Holiday flag
		aT = new double[34];
		aM = new int[] {0, 400, 60, 40, 60, 150, 0, 0};
		aY$ = new String[] {"", "     DAY", " EVENING", "SATURDAY", "  SUNDAY", "SUBCONTR"};
		aC = new double[] {0.0, 4.0, 4.3, 4.4, 5.0, 4.4, 0.0, 0.0, 0.0};
		aE = new double[] {0.0, 0.5, 1.0, 2.0, 3.0, 3.5, 0.0, 0.0};
		aR[1]=2.5;
		aI = new double[] {0.0, 0.02, 0.0, 0.0}; // aI[2] = Raw Materials
		aG = new double[] {0.0, 0.04, 0.0, 0.0, 0.0};
		aL[20] = 1;
		aF = new double[] {0.0, 0.5, 1.0, 1.5, 2.0, 3.0, 4.5, 6.0, 8.0, 0.0};
		aK = new int[] {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 320, 420, 400, 380, 420, 380, 520,
			500, 440, 400, 490, 500, 260, 530, 490, 540, 530,
			325, 340, 530
		};
		aA[1] = 0.2;
		aA[2] = 0.1;
		aA[3] = 1.0;
		aA[4] = 0.3;
		aA[5] = 0.05;
		aP$ = new String[] {"", "", "", "STRIKE:100% PROD. LOST"};
		S = 0.5;
		aQ = new double[] {0.0, 0.25, 0.15, 0.05};
		aS$ = new String[] {
			"",
			"50% RAW MATERIAL DELIVERY, THE REST NEXT WEEK",
			"RAW MATERIALS DELAYED 1 WEEK",
			"3 WEEK CYCLE FOR DELIVERY",
			"",
			"2 WEEK CYCLE FOR DELIVERY"
		};
		aP = new int[6]; //Production levels for the current week
	}
	/*
	private void CheckPrinterAttached()
	{
		Print("IS THE PRINTER ATTACHED AND SWITCHED ON?");
		InputListener = new IInputListener() {
			public void Execute(String response) {
				InputListener = null;
				if (response.toUpperCase().startsWith("Y")) {
					aL[20] = 1;
					MopsEngine(MopsState.CheckPrinterAttached);
					return;
				}
				if (response.toUpperCase().startsWith("N")) {
					aL[20] = 0;
					MopsEngine(MopsState.CheckPrinterAttached);
					return;
				}
				Print("PLEASE ANSWER YES OR NO");
				CheckPrinterAttached();
			}
		};
	}
*/	
	private void CheckPrinterAttached()
	{
		Print("IS THE PRINTER ATTACHED AND SWITCHED ON?");
		CurrentState = MopsState.CheckPrinterAttached;
	}
	private void CheckPrinterAttachedResponse(String response)
	{	
		if (response.toUpperCase().startsWith("Y")) {
			aL[20] = 1;
			NewGame();
		} else {
			if (response.toUpperCase().startsWith("N")) {
				aL[20] = 0;
				NewGame();
			} else {
				Print("PLEASE ANSWER YES OR NO");
			}
		}
	}
	
	private void NewGame()
	{
		Print("        *** M O P S ***");
		Print(" PRODUCTION TEAM'S NAME");
		aG[2] = 200.0; aI[2] = 550.0; aA[12] = 340.0; aS[12] = 340; aR[4] = 0.0; aR[2] = 0.0;
		aM[7] = 325; aM[6] = 325; aR[13] = 800.0; N = 2; W = 12;
		for (int K = 1; K <= 19; K++) aL[K] = 0;
		for (int K = 13; K <= 33; K++) aA[K] = 0.0;
		for (int K = 14; K <= 33; K++) aR[K] = 0.0;
		for (int K = 1; K <= 33; K++) aT[K] = 0.0;
		CurrentState = MopsState.NewGame;
	}
	private void NewGameResponse(String response)
	{
		N$ = response;
		NewWeek();
	}
	
	private void NewWeek()
	{
		W += 1; // Move to next week
		if (W > 32) {
			EndGame(); // Week 33 is final week
		} else {
			aR[4] = aR[2]; // Remember last week's ?
			aR[1] = 0.0; // Clear current week's ?
			B = new Random();
			aE[7] = 0.0; aC[7] = 0.0; aR[3] = 0.0; aI[3] = 0.0; aG[3] = 0.0; aF[9] = 0.0; aC[8] = 0.0; aM[6] = 0;
			for (int K = 1; K <= 5; K++) aP[K]= 0; // Clear production values for the current week
			if (W >= 16) SupplyAlterations();
			Command();
		}
	}
	
	private void SupplyAlterations()
	{
		int rnd;
		aS$[7] = "";
		rnd = B.nextInt(200);
		if (rnd > 7 || aL[1] > 1) {
			if (rnd <= 17 && aL[2] <= 1) {
				if (aR[W] != 0) {
					aS$[7] = aS$[2];
					aL[2] = aL[2] + 1;
					aR[W+1] = aR[W+1] + aR[W];
					aR[W] = 0.0;
				}
			}
		} else {
			if (aR[W] != 0) {
				aS$[7] = aS$[1];
				aL[1] = aL[1] + 1;
				aR[W] = aR[W] * S;
				aR[W+1] = aR[W+1] + (aR[W] * (1 - S));
			}
		}
		if (B.nextInt(300) < 70 && aL[3] <= 1) {
			aL[3] = aL[3] + 1;
			N = 3;
		}
		if (W > 25) N = 2;
	}
	
	private void Command()
	{
		Print("");
		Print("");
		Print("COMMAND (OR PRESS H FOR HELP)");
		CurrentState = MopsState.Command;
	}
	private void CommandResponse(String response)
	{
		switch(response.toUpperCase())
		{
			case "H":
				Help();
				break;
			case "R":
				RawMaterialOrder();
				break;
			case "D":
				X = 1; // Day shift
				ProductionRequired();
				break;
			case "E":
				X = 2; // Evening shift
				ProductionRequired();
				break;
			case "S":
				X = 5; // Subcontract
				ProductionRequired();
				break;
			case "A":
				X = 3; // Saturday shift
				ProductionRequired();
				break;
			case "U":
				X = 4; // Sunday shift
				ProductionRequired();
				break;
			case "X":
				//ToDo
				break;
			case "C":
				CurrentValues();
				break;
			case "Z":
				//ToDo
				break;
			default:
				Print("COMMAND NOT RECOGNISED");
				Command();
		}
	}
	
	private void Help()
	{
		Print("      *** HELP ***");
		Print("   THE FOLLOWING LIST OF COMMAND KEYS");
		Print("HAVE THE GIVEN MEANINGS. NO OTHER KEYS");
		Print("WILL WORK.");
		Print("H - DISPLAYS THE COMMAND KEYS");
		Print("R - SIGNALS A RAW MATERIALS ORDER");
		Print("D - SIGNALS PRODUCTION FOR DAY SHIFT");
		Print("E - SIGNALS PRODUCTION FOR EVE. SHIFT");
		Print("A - SIGNALS PRODUCTION FOR SAT. SHIFT");
		Print("U - SIGNALS PRODUCTION FOR SUN. SHIFT");
		Print("S - SIGNALS PRODUCTION FOR SUBCONTRACT");
		Print("C - SIGNALS A PRODUCTION SUBMISSION");
		Command();
	}
	
	private void CurrentValues()
	{
		for (int K = 1; K <= 5; K++) {
			Print("");
			Print(aY$[K] + " SHIFT " + aP[K]);
			Print("MATERIAL ORDER " + aR[2]);
		}
		Print("");
		Print("");
		Print("");
		Print("TO SUBMIT THE PLAN TYPE C");			
		Print("");
		Print("TO CHANGE THE PLAN TYPE H");			
		Print("");
		CurrentState = MopsState.CurrentValues;
	}
	
	private void CurrentValuesResponse(String response)
	{
		switch (response.toUpperCase())
		{
			case "H":
				Help();
				break;
			case "C":
				CalculateAvailabilityOfSubcontracting();
				ProductionAndQualityControl();
				PrintWeekSummary();
				break;
			default:
				
		}
	}
	
	private void CalculateAvailabilityOfSubcontracting()
	{
		if (aP[5] == 0 || (aL[13] == 1 && aL[14]== 1)) {
			aL[13]= 0;
		} else {
			if (aL[13] == 1) {
				if (B.nextFloat() < 0.2) {
					aL[14] = 1;
					aP[5] = 0;
				}
			} else {
				if (B.nextFloat() < 0.05) {
					aL[13] = 1;
					aP[5] = 0;
				}
			}
		}
	}
	
	private void ProductionAndQualityControl()
	{
		float rand = B.nextFloat();
		boolean blnQualityLossFactors = false;
		boolean blnProductionLossFactors = false;
		
		P = 0;
		Q = 0;
		if (W < 14) {
			MakeAdjustments();
		} else {
			// Only check loss factors outside of holidays
			if (W < 25 || W > 26)
			{
				if (rand >= 0.25) {
					blnQualityLossFactors = true;
				} else {
					// Strike Conditions
					if (aL[10] == 1) {
						if (aL[17] == 1) {
							blnProductionLossFactors = true;
						} else {
							aL[17] = 1;
							if (B.nextDouble() < (0.5 / 7.0)) P = aA[3];
						}
					} else {
						if (rand < 0.03) {
							P = aA[3];
							aL[10] = 1;
						} else {
							blnProductionLossFactors = true;
						}
					}
				}
			}
			if (blnQualityLossFactors) {
				// Quality Loss Factors
				if (rand < 0.3 && aL[5] < 4) {
					Q = aQ[1];
					aL[5]++;
				}
				if (rand < 0.35 && aL[6] < 4) {
					Q = aQ[2];
					aL[6]++;
				}
				if (rand < 0.445 && aL[7] < 4) {
					Q = aQ[3];
					aL[7]++;
				}
			}
			if (blnProductionLossFactors) {
				// Production Loss Factors
				if (rand <= 0.045 && aL[8] < 4) {
					P = aA[1];
					aL[8]++;
				}
				if (rand <= 0.12 && aL[9] < 4) {
					P = aA[2];
					aL[9]++;						
				}
				if (rand <= 0.15 && aL[11] < 4) {
					P = aA[4];
					aL[11]++;						
				}
				if (rand <= 0.245 && aL[15] < 4) {
					P = aA[5];
					aL[15]++;						
				}				
			}
			QuantityCalculations();
		}
	}
		
	private void QuantityCalculations() {
		if (P == 1) Q = 0;
		MakeAdjustments();
	}
	
	private void MakeAdjustments() {
		for (int K = 1; K <= 4; K++) {
			aP[K] = (int)(aP[K] * (1 - P - Q));
		}
		aM[7] = aP[1] + aP[2] + aP[3] + aP[4] + aP[5]; // Sum of all production values for the week.
		aR[W+N] += aR[2];
		if (aL[10] == 1 || aL[10] == 2 || W == 25 || W == 26) {
			aC[7] = 600;
		} else {
			for (int K = 2; K <= 5; K++) {
				aC[7] += aP[K] * aC[K];
			}
			aC[7] += 1600;
		}
		if (aP[5] != 0) { // if P[5] ?????
			aC[7] += 100;
		}
		if (Q != 0) { // if Q ?????
			aI[2] += aR[W] - aM[6];
		} else {
			aI[2] += aR[W] - aM[7];
		}
		aG[4] = aG[2];
		aG[2] += aM[7];
		if (aS[W] <= aG[2]) {
			aG[2] -= aS[W];
			aA[W] = aS[W];
			F = 0;
		} else {
			aA[W] = aG[2];
			aG[2]= 0;
			F = aS[W] - aA[W];
			// Shortage Penalties
			if (F < 11) {
				aF[9] = (int)(((F * aF[1]) + 0.9) * 4.45);
			} else {
				for (int K = 1; K <= 6; K++) {
					if (F <= K * 50) {
						aF[9] = (int)(((F * aF[K + 1]) + 0.9) * 4.45);
					} else {
						aF[9] = (int)(((F * aF[8]) + 0.9) * 4.45);
					}
				}
			}
		}
		// Under production penalties
		if (W < 25 && W > 26) {
			aM[8] = aM[7] - aP[5]; // Production values for the current week minus the production value for the 5th day
			if (aM[8] >= 349 && aM[8] < 396) {
				aE[7] = (400 - aM[8]) * aE[1];
			}
			if (aM[8] >= 299 && aM[8] < 349) {
				aE[7] = (400 - aM[8]) * aE[2];
			}
			if (aM[8] >= 249 && aM[8] < 299) {
				aE[7] = (400 - aM[8]) * aE[3];
			}
			if (aM[8] < 249) {
				aE[7] = (400 - aM[8]) * aE[4];
			}
		}
		// Calculation totals
		aG[3] = aG[2] * aG[1];
		aR[3] = aR[W] * aR[1];
		aI[3] = aI[2] * aI[1];
		aC[8] = (600.9 + (1.05 * aA[W]));
		aT[1] = aC[7] + aR[3] + aI[3] + aG[3] + aC[8] + aE[7];
		aT[W] = aA[W] * 13.5;
		if (aR[2] != 0) {
			aT[1] += 30;
		}
		aT[5] += aS[W];
		aT[6] += aA[W];
		aT[7] = 0.01 * (int)(100 * (aT[7] + aT[1]));
		aT[8] += aF[9];
		aT[9] += aT[W];
		aT[10] = 0.01 * (int)(100 * (aT[9] + aT[7]));
		aT[11] += aC[8];
	}
	
	private void PrintWeekSummary()
	{
		Print("      WEEKLY REPORT  WEEK " + Integer.toString(W));
		Print("IN THE WEEK YOU HAVE JUST SUBMITTED");
		Print("THE FOLLOWING OCCURRED");
		Print("");
		Print("");
		Print("");
		Print("PRESS THE SPACE BAR TO CONTINUE");
		CurrentState = MopsState.PrintWeekSummary;
	}
	private void PrintWeekSummaryResponse(String response){
		
	}
	
	private void EndGame()
	{
		if (aL[20] != 1) {
			// Printer enabled
		}
		Print("CUMULATIVE ANALYSIS WEEK " + Integer.toString(W));
		Print("TOTAL:");
		Print("       SALES MET       " + Double.toString(aT[5]));
		Print("       SALES DEMAND    " + Double.toString(aT[4]));
		Print("CUSTOMER SHORTAGES     " + Double.toString(aT[4] - aT[5]));
		Print("CUMULATIVE PROD COSTS  " + Double.toString(aT[6]));
		Print("CUMULATIVE IND. COSTS  " + Double.toString(aT[10]));
		Print("TOTAL SALES REVENUE    " + Double.toString(aT[8]));
		Print("       PROFIT          " + Double.toString(aT[9]));
	}
	
	private void ProductionRequired()
	{
		aP[X] = 0;
		HolidayCheck();
		if (aL[21] == 1)
		{
			Command();
		}
		else
		{
			Print(aY$[X] + " PRODUCTION REQUIRED?");
			CurrentState = MopsState.ProductionRequired;
		}
	}
	private void ProductionRequiredResponse(String Response)
	{
		int NewLevel;
		try {
			NewLevel = Integer.parseInt(Response);
			aP[X] = NewLevel; // Set current week production level
			if (aP[X] < 0 || aP[X] > aM[X]) {
				Print("NUMBER OUTSIDE LIMITS OF 0 AND " + Integer.toString(aM[X]));
				aP[X] = 0;
			} else {
				aM[6] += aP[X];
				if (aM[6] > aI[2]) {
					Print("TOTAL PRODUCTION OF " + Integer.toString(aM[6]) + " IS GREATER THAN THE RAW MATERIAL");
					Print("INVENTORY OF " + Double.toString(aI[2]));
					Print("CHANGE YOUR PRODUCTION REQUESTS");
					for (int K = 1; K <= 5; K++) {
						aP[K] = 0;
					}
					aM[6] = 0;
				} else {
					Command();					
				}
			}
		}
		catch(Exception ex){
			Print("INPUT A NUMBER");
		}
	}
	
	private void HolidayCheck() {
		aL[21] = 0; // turn off holiday flag
		if (W >= 25 && W <= 26 && X != 5) {
			Print("HOLIDAYS: ONLY SUBCONTRACTING ALLOWED");
			aL[21] = 1; // turn on holiday flag for week 25/26 (non-subcontracting only)
		}
	}
	
	private void RawMaterialOrder() {
		Print("RAW MATERIAL ORDER IS ");
		CurrentState = MopsState.RawMaterialOrder;
	}
	private void RawMaterialOrderResponse(String input) {
		int OrderValue;
		try {
			OrderValue = Integer.parseInt(input);
			if (OrderValue < 0 || OrderValue > 3000) {
				Print("NUMBER OUTSIDE LIMITS OF 0 AND 3000");
			} else {
				aR[2] = OrderValue;
				Command();
			}
		} catch(Exception ex) {
			Print("INPUT A NUMBER");
		}
	}
}

