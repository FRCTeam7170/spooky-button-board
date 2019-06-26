EESchema Schematic File Version 4
LIBS:frcButtonBoardPro-cache
EELAYER 29 0
EELAYER END
$Descr USLetter 11000 8500
encoding utf-8
Sheet 3 5
Title "Button Board"
Date "2019-05-15"
Rev "1.0"
Comp "FRC Team 7170"
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
Text HLabel 5150 3650 0    50   Output ~ 0
BUTTON1
Text HLabel 5750 3650 2    50   Output ~ 0
BUTTON2
Text HLabel 5150 3750 0    50   Output ~ 0
BUTTON3
Text HLabel 5750 3750 2    50   Output ~ 0
BUTTON4
Wire Wire Line
	5150 3650 5200 3650
Wire Wire Line
	5150 3750 5200 3750
Wire Wire Line
	5200 3850 5150 3850
Wire Wire Line
	5150 3950 5200 3950
Wire Wire Line
	5150 4050 5200 4050
Wire Wire Line
	5150 4150 5200 4150
Wire Wire Line
	5150 4250 5200 4250
Wire Wire Line
	5150 4350 5200 4350
Wire Wire Line
	5700 3750 5750 3750
Wire Wire Line
	5700 3850 5750 3850
Wire Wire Line
	5700 3950 5750 3950
Wire Wire Line
	5700 4050 5750 4050
Wire Wire Line
	5700 4150 5750 4150
Wire Wire Line
	5700 4250 5750 4250
Wire Wire Line
	5700 4350 5750 4350
Text HLabel 5150 3850 0    50   Output ~ 0
BUTTON5
Text HLabel 5750 3850 2    50   Output ~ 0
BUTTON6
Text HLabel 5150 3950 0    50   Output ~ 0
BUTTON7
Text HLabel 5750 3950 2    50   Output ~ 0
BUTTON8
Text HLabel 5150 4050 0    50   Output ~ 0
BUTTON9
Text HLabel 5750 4050 2    50   Output ~ 0
BUTTON10
Text HLabel 5150 4150 0    50   Output ~ 0
BUTTON11
Text HLabel 5750 4150 2    50   Output ~ 0
BUTTON12
Text HLabel 5150 4250 0    50   Output ~ 0
BUTTON13
Text HLabel 5750 4250 2    50   Output ~ 0
BUTTON14
Text HLabel 5150 4350 0    50   Output ~ 0
BUTTON15
Text HLabel 5750 4350 2    50   Output ~ 0
BUTTON16
Wire Wire Line
	5700 3650 5750 3650
$Comp
L Connector_Generic:Conn_02x08_Odd_Even J?
U 1 1 5D2B2EB8
P 5400 3950
AR Path="/5CE0E572/5D2B2EB8" Ref="J?"  Part="1" 
AR Path="/5CDFEBD0/5D2B2EB8" Ref="J3"  Part="1" 
F 0 "J3" H 5450 4467 50  0000 C CNN
F 1 "Conn_02x08_Odd_Even" H 5450 4376 50  0000 C CNN
F 2 "frcButtonBoardPro:16PIN_RIBBON_CONN" H 5400 3950 50  0001 C CNN
F 3 "~" H 5400 3950 50  0001 C CNN
	1    5400 3950
	1    0    0    -1  
$EndComp
$EndSCHEMATC
