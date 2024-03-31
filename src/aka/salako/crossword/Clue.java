package aka.salako.crossword;

public class Clue {
	private String clue;
	private int row;
	private int column;
	private boolean horizontal;
	
	public Clue(String clue, int row, int column, boolean horizontal) {}
	
	public String getClue() {
		return clue;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
	
	@Override
	public String toString() {
		if (!horizontal) {
			return "Indice : " + clue + ", ligne : " + row + ", colonne : " + column;
		}
		return "Indice : " + clue + ", ligne : " + row + ", colonne : " + column + horizontal;
	}
}
