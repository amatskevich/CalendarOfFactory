package by.matskevich.calendaroffactory;

public interface Statable {
	String getState();
	Statable next();
	Statable before();
	String getStatSign();
	int getColor();
	double getWorkHours();
	int getNormalHours();
	String getDescription();
}
