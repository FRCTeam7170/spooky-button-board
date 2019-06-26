EESchema Schematic File Version 4
LIBS:frcButtonBoardPro-cache
EELAYER 29 0
EELAYER END
$Descr USLetter 11000 8500
encoding utf-8
Sheet 5 5
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Device:Buzzer BZ?
U 1 1 5CE15E09
P 5800 3850
AR Path="/5CE15E09" Ref="BZ?"  Part="1" 
AR Path="/5CE12EE7/5CE15E09" Ref="BZ1"  Part="1" 
F 0 "BZ1" H 5952 3879 50  0000 L CNN
F 1 "Buzzer" H 5952 3788 50  0000 L CNN
F 2 "Buzzer_Beeper:Buzzer_12x9.5RM7.6" V 5775 3950 50  0001 C CNN
F 3 "~" V 5775 3950 50  0001 C CNN
	1    5800 3850
	1    0    0    -1  
$EndComp
$Comp
L Transistor_BJT:2N3904 Q17
U 1 1 5CE15F47
P 5550 4250
F 0 "Q17" H 5740 4296 50  0000 L CNN
F 1 "2N3904" H 5740 4205 50  0000 L CNN
F 2 "Package_TO_SOT_THT:TO-92_Inline" H 5750 4175 50  0001 L CIN
F 3 "https://www.fairchildsemi.com/datasheets/2N/2N3904.pdf" H 5550 4250 50  0001 L CNN
	1    5550 4250
	1    0    0    -1  
$EndComp
$Comp
L Device:R_US R?
U 1 1 5CE24C22
P 5500 3850
AR Path="/5CE0E572/5CE24C22" Ref="R?"  Part="1" 
AR Path="/5CE12EE7/5CE24C22" Ref="R34"  Part="1" 
F 0 "R34" V 5295 3850 50  0000 C CNN
F 1 "1K" V 5386 3850 50  0000 C CNN
F 2 "Resistor_THT:R_Axial_DIN0207_L6.3mm_D2.5mm_P10.16mm_Horizontal" V 5540 3840 50  0001 C CNN
F 3 "~" H 5500 3850 50  0001 C CNN
	1    5500 3850
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR018
U 1 1 5CE258BD
P 5650 3650
F 0 "#PWR018" H 5650 3500 50  0001 C CNN
F 1 "+5V" H 5665 3823 50  0000 C CNN
F 2 "" H 5650 3650 50  0001 C CNN
F 3 "" H 5650 3650 50  0001 C CNN
	1    5650 3650
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR019
U 1 1 5CE26036
P 5650 4500
F 0 "#PWR019" H 5650 4250 50  0001 C CNN
F 1 "GND" H 5655 4327 50  0000 C CNN
F 2 "" H 5650 4500 50  0001 C CNN
F 3 "" H 5650 4500 50  0001 C CNN
	1    5650 4500
	1    0    0    -1  
$EndComp
$Comp
L Device:R_US R?
U 1 1 5CE2650B
P 5150 4250
AR Path="/5CE0E572/5CE2650B" Ref="R?"  Part="1" 
AR Path="/5CE12EE7/5CE2650B" Ref="R33"  Part="1" 
F 0 "R33" V 4945 4250 50  0000 C CNN
F 1 "1K" V 5036 4250 50  0000 C CNN
F 2 "Resistor_THT:R_Axial_DIN0207_L6.3mm_D2.5mm_P10.16mm_Horizontal" V 5190 4240 50  0001 C CNN
F 3 "~" H 5150 4250 50  0001 C CNN
	1    5150 4250
	0    1    1    0   
$EndComp
Wire Wire Line
	5300 4250 5350 4250
Text HLabel 4950 4250 0    50   Input ~ 0
BUZZER_PWM
Wire Wire Line
	4950 4250 5000 4250
Wire Wire Line
	5650 4450 5650 4500
Wire Wire Line
	5650 4050 5650 4000
Wire Wire Line
	5500 4000 5650 4000
Wire Wire Line
	5650 4000 5650 3950
Wire Wire Line
	5650 3950 5700 3950
Connection ~ 5650 4000
Wire Wire Line
	5700 3750 5650 3750
Wire Wire Line
	5650 3750 5650 3700
Wire Wire Line
	5650 3700 5500 3700
Connection ~ 5650 3700
Wire Wire Line
	5650 3700 5650 3650
$EndSCHEMATC
