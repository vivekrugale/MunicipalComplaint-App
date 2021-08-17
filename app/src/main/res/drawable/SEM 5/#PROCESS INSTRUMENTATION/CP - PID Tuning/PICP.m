clc;
clear all;
close all;

%PI CP Pressure TF
s = tf('s');
sys1 = (0.33*exp(-0.2197*s))/(1.055*s+1)
system = pade(sys1)

kp = 14;
ki = 10.5;
kd = 0.6;
cont = kp + ki/s + kd*s
cl_sys = feedback(cont*system,1)
%syss = (-0.33*s + 3.004)/ (1.055*s^2 + 10.6*s + 9.103)
step(cl_sys);