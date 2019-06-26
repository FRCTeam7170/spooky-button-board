EESchema Schematic File Version 4
LIBS:frcButtonBoardPro-cache
EELAYER 29 0
EELAYER END
$Descr USLetter 11000 8500
encoding utf-8
Sheet 2 5
Title "Button Board"
Date "2019-05-15"
Rev "1.0"
Comp "FRC Team 7170"
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Device:Battery BT?
U 1 1 5CDF2C9F
P 3750 4200
AR Path="/5CDF2C9F" Ref="BT?"  Part="1" 
AR Path="/5CDF090F/5CDF2C9F" Ref="BT2"  Part="1" 
F 0 "BT2" H 3858 4246 50  0000 L CNN
F 1 "Battery" H 3858 4155 50  0000 L CNN
F 2 "Connector_Wire:SolderWirePad_1x02_P5.08mm_Drill1.5mm" V 3750 4260 50  0001 C CNN
F 3 "~" V 3750 4260 50  0001 C CNN
	1    3750 4200
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5CDF2CA5
P 4000 4500
AR Path="/5CDF2CA5" Ref="#PWR?"  Part="1" 
AR Path="/5CDF090F/5CDF2CA5" Ref="#PWR013"  Part="1" 
F 0 "#PWR013" H 4000 4250 50  0001 C CNN
F 1 "GND" H 4005 4327 50  0000 C CNN
F 2 "" H 4000 4500 50  0001 C CNN
F 3 "" H 4000 4500 50  0001 C CNN
	1    4000 4500
	1    0    0    -1  
$EndComp
$Comp
L Device:CP C3
U 1 1 5CDF3710
P 4250 4200
F 0 "C3" H 4368 4246 50  0000 L CNN
F 1 "10u" H 4368 4155 50  0000 L CNN
F 2 "Capacitor_THT:CP_Radial_D5.0mm_P2.00mm" H 4288 4050 50  0001 C CNN
F 3 "~" H 4250 4200 50  0001 C CNN
	1    4250 4200
	1    0    0    -1  
$EndComp
Wire Wire Line
	4250 4050 4250 3950
Connection ~ 4250 3950
Wire Wire Line
	4250 3950 4550 3950
Wire Wire Line
	3750 3950 3750 4000
Wire Wire Line
	3750 3950 4250 3950
Wire Wire Line
	3750 4400 3750 4450
Wire Wire Line
	3750 4450 4000 4450
Wire Wire Line
	4000 4450 4000 4500
Wire Wire Line
	4000 4450 4250 4450
Wire Wire Line
	4250 4450 4250 4350
Connection ~ 4000 4450
$Comp
L Device:CP C4
U 1 1 5D05A55E
P 4850 4550
F 0 "C4" H 4968 4596 50  0000 L CNN
F 1 "10u" H 4968 4505 50  0000 L CNN
F 2 "Capacitor_THT:CP_Radial_D5.0mm_P2.00mm" H 4888 4400 50  0001 C CNN
F 3 "~" H 4850 4550 50  0001 C CNN
	1    4850 4550
	1    0    0    -1  
$EndComp
Wire Wire Line
	4850 4700 4850 4750
Wire Wire Line
	4700 3550 4250 3550
Wire Wire Line
	4250 3550 4250 3950
$Comp
L Connector_Generic:Conn_01x01 J2
U 1 1 5D3DED07
P 2100 4100
F 0 "J2" V 2064 4012 50  0000 R CNN
F 1 "Conn_01x01" V 1973 4012 50  0000 R CNN
F 2 "Connector_Wire:SolderWirePad_1x01_Drill1.5mm" H 2100 4100 50  0001 C CNN
F 3 "~" H 2100 4100 50  0001 C CNN
	1    2100 4100
	0    -1   -1   0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5D3E0AD5
P 2100 4350
AR Path="/5D3E0AD5" Ref="#PWR?"  Part="1" 
AR Path="/5CDF090F/5D3E0AD5" Ref="#PWR012"  Part="1" 
F 0 "#PWR012" H 2100 4100 50  0001 C CNN
F 1 "GND" H 2105 4177 50  0000 C CNN
F 2 "" H 2100 4350 50  0001 C CNN
F 3 "" H 2100 4350 50  0001 C CNN
	1    2100 4350
	1    0    0    -1  
$EndComp
Wire Wire Line
	2100 4350 2100 4300
Text Notes 1700 4000 0    50   ~ 0
GND connection for\nthe buttons and LEDs.
$Comp
L Device:Battery BT?
U 1 1 5CEA47CE
P 3250 4200
AR Path="/5CEA47CE" Ref="BT?"  Part="1" 
AR Path="/5CDF090F/5CEA47CE" Ref="BT1"  Part="1" 
F 0 "BT1" H 3358 4246 50  0000 L CNN
F 1 "Battery" H 3358 4155 50  0000 L CNN
F 2 "Connector_Wire:SolderWirePad_1x02_P5.08mm_Drill1.5mm" V 3250 4260 50  0001 C CNN
F 3 "~" V 3250 4260 50  0001 C CNN
	1    3250 4200
	1    0    0    -1  
$EndComp
Wire Wire Line
	3250 4400 3250 4450
Wire Wire Line
	3250 4450 3750 4450
Connection ~ 3750 4450
Wire Wire Line
	3250 4000 3250 3950
Wire Wire Line
	3250 3950 3750 3950
Connection ~ 3750 3950
Connection ~ 4850 4350
Wire Wire Line
	4850 4400 4850 4350
Wire Wire Line
	4850 4250 4850 4350
Wire Wire Line
	6250 3950 7600 3950
Text Notes 6500 4650 0    30   ~ 0
Must be set up to draw 10mA to\nmaintain minimum load current.\nThe ratio between the bottom\nand top resistors in the divider\nmust be approx. 3 to get 5V out.
Wire Wire Line
	5000 3550 6250 3550
Wire Wire Line
	4850 4750 6250 4750
Connection ~ 5400 3950
Wire Wire Line
	5400 3950 6250 3950
Text Notes 5500 4300 0    39   ~ 0
Protection for cap\non adjust pin\ndischarging into\nLM317 output.
Text Notes 5050 3450 0    39   ~ 0
Protection for output cap\ndischarging into LM317 output.
Wire Wire Line
	5400 4350 5400 4300
Wire Wire Line
	4850 4350 5400 4350
Wire Wire Line
	5400 3950 5400 4000
Wire Wire Line
	5150 3950 5400 3950
$Comp
L Diode:1N4002 D2
U 1 1 5D05F7C4
P 5400 4150
F 0 "D2" V 5354 4229 50  0000 L CNN
F 1 "1N4002" V 5445 4229 50  0000 L CNN
F 2 "Diode_THT:D_DO-41_SOD81_P10.16mm_Horizontal" H 5400 3975 50  0001 C CNN
F 3 "http://www.vishay.com/docs/88503/1n4001.pdf" H 5400 4150 50  0001 C CNN
	1    5400 4150
	0    -1   1    0   
$EndComp
Wire Wire Line
	6250 3550 6250 3950
Connection ~ 7600 3950
Wire Wire Line
	7600 3950 7600 3900
$Comp
L Diode:1N4002 D1
U 1 1 5D05C490
P 4850 3550
F 0 "D1" H 4850 3766 50  0000 C CNN
F 1 "1N4002" H 4850 3675 50  0000 C CNN
F 2 "Diode_THT:D_DO-41_SOD81_P10.16mm_Horizontal" H 4850 3375 50  0001 C CNN
F 3 "http://www.vishay.com/docs/88503/1n4001.pdf" H 4850 3550 50  0001 C CNN
	1    4850 3550
	1    0    0    -1  
$EndComp
Wire Wire Line
	6250 4800 6250 4750
Connection ~ 6250 4750
Wire Wire Line
	7600 4750 7600 4500
Wire Wire Line
	7600 3950 7600 4200
$Comp
L Device:CP C5
U 1 1 5D058222
P 7600 4350
F 0 "C5" H 7718 4396 50  0000 L CNN
F 1 "10u" H 7718 4305 50  0000 L CNN
F 2 "Capacitor_THT:CP_Radial_D5.0mm_P2.00mm" H 7638 4200 50  0001 C CNN
F 3 "~" H 7600 4350 50  0001 C CNN
	1    7600 4350
	1    0    0    -1  
$EndComp
Text Notes 7700 3850 0    50   ~ 0
<- This is the regulated output.
Connection ~ 6250 3950
$Comp
L power:+5V #PWR015
U 1 1 5CE141D6
P 7600 3900
F 0 "#PWR015" H 7600 3750 50  0001 C CNN
F 1 "+5V" H 7615 4073 50  0000 C CNN
F 2 "" H 7600 3900 50  0001 C CNN
F 3 "" H 7600 3900 50  0001 C CNN
	1    7600 3900
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5CDF8800
P 6250 4800
AR Path="/5CDF8800" Ref="#PWR?"  Part="1" 
AR Path="/5CDF090F/5CDF8800" Ref="#PWR014"  Part="1" 
F 0 "#PWR014" H 6250 4550 50  0001 C CNN
F 1 "GND" H 6255 4627 50  0000 C CNN
F 2 "" H 6250 4800 50  0001 C CNN
F 3 "" H 6250 4800 50  0001 C CNN
	1    6250 4800
	1    0    0    -1  
$EndComp
$Comp
L Regulator_Linear:LM317_3PinPackage U?
U 1 1 5CDF2C99
P 4850 3950
AR Path="/5CDF2C99" Ref="U?"  Part="1" 
AR Path="/5CDF090F/5CDF2C99" Ref="U3"  Part="1" 
F 0 "U3" H 4850 4192 50  0000 C CNN
F 1 "LM317_3PinPackage" H 4850 4101 50  0000 C CNN
F 2 "Package_TO_SOT_THT:TO-220-3_Vertical" H 4850 4200 50  0001 C CIN
F 3 "http://www.ti.com/lit/ds/symlink/lm317.pdf" H 4850 3950 50  0001 C CNN
	1    4850 3950
	1    0    0    -1  
$EndComp
$Comp
L Device:R_POT_TRIM_US RV1
U 1 1 5CE8CEBA
P 6250 4350
F 0 "RV1" H 6182 4396 50  0000 R CNN
F 1 "R_POT_TRIM_US" H 6182 4305 50  0000 R CNN
F 2 "Potentiometer_THT:Potentiometer_Bourns_3296W_Vertical" H 6250 4350 50  0001 C CNN
F 3 "~" H 6250 4350 50  0001 C CNN
	1    6250 4350
	-1   0    0    1   
$EndComp
Wire Wire Line
	6250 4750 7600 4750
Wire Wire Line
	6100 4350 5400 4350
Connection ~ 5400 4350
Wire Wire Line
	6250 3950 6250 4200
Wire Wire Line
	6250 4500 6250 4750
$EndSCHEMATC
