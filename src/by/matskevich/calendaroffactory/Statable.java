package by.matskevich.calendaroffactory;

public interface Statable {
	public String getState();
	public Statable next();
	public Statable before();
	public String getStatSign();
	public int getColor();
}
