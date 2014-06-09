package uk.me.davidreed.mops;
import java.util.*;

public class Mops
{
	private int[] aS, aL, aT, aM, aK, aP;
	private double[] aA, aR, aC, aE, aI, aG, aF, aQ;
	private String[] aY$, aP$, aS$;
	private double S;
	private String N$;
	private int N, W, P, Q;
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
		CurrentValues
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
				CurrentState = MopsState.CheckPrinterAttached;
				break;
			case CheckPrinterAttached:
				if (CheckPrinterAttachedResponse(input)) {
					NewGame();
					CurrentState = MopsState.NewGame;
				}
				break;
			case NewGame:
				NewGameResponse(input);
				if (NewWeek()) {
					Command();
					CurrentState = MopsState.Command;
				} else {
					EndGame();
				}
				break;
			case Command:
				CommandResponse(input);
				break;
			case CurrentValues:
				CurrentValuesResponse(input);
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
		aS = new int[33];
		aR = new double[36];
		aA = new double[33];
		aL = new int[31];
		aT = new int[33];
		aM = new int[] {400, 60, 40, 60, 150, 0, 0};
		aY$ = new String[] {"     DAY", " EVENING", "SATURDAY", "  SUNDAY", "SUBCONTR"};
		aC = new double[] {4.0, 4.3, 4.4, 5.0, 4.4, 0.0, 0.0, 0.0};
		aE = new double[] {0.5, 1.0, 2.0, 3.0, 3.5, 0.0, 0.0};
		aR[0]=2.5;
		aI = new double[] {0.2, 0.0, 0.0};
		aG = new double[] {0.4, 0.0, 0.0};
		aL[19] = 2;
		aF = new double[] {0.5, 1.0, 1.5, 2.0, 3.0, 4.5, 6.0, 8.0, 0.0};
		aK = new int[] {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 320, 420, 400, 380, 420, 380, 520, 500, 440, 400,
			490, 500, 260, 530, 490, 540, 530, 325, 340, 530
		};
		aA[0] = 0.2;
		aA[1] = 0.1;
		aA[2] = 1.0;
		aA[3] = 0.3;
		aA[4] = 0.05;
		aP$ = new String[] {"", "", "STRIKE:100% PROD. LOST"};
		S = 0.5;
		aQ = new double[] {0.25, 0.15, 0.05};
		aS$ = new String[] {
			"50% RAW MATERIAL DELIVERY, THE REST NEXT WEEK",
			"RAW MATERIALS DELAYED 1 WEEK",
			"3 WEEK CYCLE FOR DELIVERY",
			"",
			"2 WEEK CYCLE FOR DELIVERY"
		};
		aP = new int[5];
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
	}
	private Boolean CheckPrinterAttachedResponse(String response)
	{	
		if (response.toUpperCase().startsWith("Y")) {
			aL[20] = 1;
			return true;
		}
		if (response.toUpperCase().startsWith("N")) {
			aL[20] = 0;
			return true;
		}
		Print("PLEASE ANSWER YES OR NO");
		CheckPrinterAttached();
		return false;
	}
	
	private void NewGame()
	{
		Print("        *** M O P S ***");
		Print(" PRODUCTION TEAM'S NAME");
		aG[1] = 200.0; aI[1] = 550.0; aA[11] = 340.0; aS[11] = 340; aR[3] = 0.0; aR[1] = 0.0;
		aM[6] = 325; aM[5] = 325; aR[12] = 800.0; N = 2; W = 12;
		for (int K = 0; K < 19; K++) aL[K] = 0;
		for (int K = 12; K < 33; K++) aA[K] = 0.0;
		for (int K = 13; K < 33; K++) aR[K] = 0.0;
		for (int K = 0; K < 33; K++) aT[K] = 0;
	}
	private void NewGameResponse(String response)
	{
		N$ = response;
	}
	
	private Boolean NewWeek()
	{
		W += 1;
		if (W > 32) {
			return false;
		} else {
			aR[3] = aR[1]; aR[1] = 0.0;
			B = new Random();
			aE[6] = 0.0; aC[6] = 0.0; aR[2] = 0.0; aI[2] = 0.0; aG[2] = 0.0; aF[8] = 0.0; aC[7] = 0.0; aM[5] = 0;
			for (int K = 0; K < 5; K++) aP[K]= 0;
			if (W >= 16) SupplyAlterations();
			return true;
		}
	}
	
	private void SupplyAlterations()
	{
		int rnd;
		aS$[6] = "";
		rnd = B.nextInt(200);
		if (rnd > 7 || aL[0] > 1) {
			if (rnd <= 17 && aL[1] <= 1) {
				if (aR[W-1] != 0) {
					aS$[6] = aS$[2];
					aL[1] = aL[1] + 1;
					aR[W] = aR[W] + aR[W-1];
					aR[W-1] = 0.0;
				}
			}
		} else {
			if (aR[W-1] != 0) {
				aS$[6] = aS$[0];
				aL[0] = aL[0] + 1;
				aR[W-1] = aR[W-1] * S;
				aR[W] = aR[W] + (aR[W-1] * (1 - S));
			}
		}
		if (B.nextFloat() < 0.0233 && aL[2] <= 1) {
			aL[2] = aL[2] + 1;
			N = 3;
		}
		if (W > 25) N = 2;
	}
	
	private void Command()
	{
		Print("");
		Print("");
		Print("COMMAND (OR PRESS H FOR HELP)");
	}
	
	private void CommandResponse(String response)
	{
		switch(response.toUpperCase())
		{
			case "H":
				Help();
				Command();
				break;
			case "C":
				CurrentValues();
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
	}
	
	private void CurrentValues()
	{
		for (int K = 0; K < 5; K++) {
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
				break;
			default:
				
		}
	}
	
	private void CalculateAvailabilityOfSubcontracting()
	{
		if (aP[4] == 0 || (aL[12] == 1 && aL[13]== 1)) {
			aL[12]= 0;
			ProductionAndQualityControl();
		} else {
			if (aL[12] == 1) {
				if (B.nextFloat() < 0.2) {
					aL[13] = 1;
					aP[4] = 0;
				}
			} else {
				if (B.nextFloat() < 0.05) {
					aL[12] = 1;
					aP[4] = 0;
				} else {
					aL[12] = 0;
				}
			}
		}
	}
	
	private void ProductionAndQualityControl()
	{
		P = 0;
		Q = 0;
		if (W < 25 || W > 26)
		{
			
		}
	}
	
	private void EndGame()
	{
		
	}
}

